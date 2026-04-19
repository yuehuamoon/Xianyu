<template>
  <div class="dashboard">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2 class="page-title">数据看板</h2>
      <p class="page-desc">基于 /user /good /deal 实时接口汇总</p>
    </div>

    <!-- 错误提示 -->
    <div v-if="error" class="error-tip">{{ error }}</div>

    <!-- 统计卡片 -->
    <div class="stats-grid">
      <div class="stat-card shine-effect">
        <div class="stat-icon">
          <ShoppingCart class="icon" :size="24" :stroke-width="1.5" />
        </div>
        <h3 class="stat-value">{{ stats.orders }}</h3>
        <div class="stat-footer">
          <p class="stat-label">订单总数</p>
          <span class="stat-change">实时</span>
        </div>
      </div>
      <div class="stat-card shine-effect">
        <div class="stat-icon">
          <DollarSign class="icon" :size="24" :stroke-width="1.5" />
        </div>
        <h3 class="stat-value">¥{{ formatAmount(stats.totalAmount) }}</h3>
        <div class="stat-footer">
          <p class="stat-label">成交金额</p>
          <span class="stat-change">实时</span>
        </div>
      </div>
      <div class="stat-card shine-effect">
        <div class="stat-icon">
          <Users class="icon" :size="24" :stroke-width="1.5" />
        </div>
        <h3 class="stat-value">{{ stats.activeUsers }}</h3>
        <div class="stat-footer">
          <p class="stat-label">活跃用户</p>
          <span class="stat-change">实时</span>
        </div>
      </div>
      <div class="stat-card shine-effect">
        <div class="stat-icon">
          <TrendingUp class="icon" :size="24" :stroke-width="1.5" />
        </div>
        <h3 class="stat-value">{{ stats.activeGoods }}</h3>
        <div class="stat-footer">
          <p class="stat-label">在售商品</p>
          <span class="stat-change">实时</span>
        </div>
      </div>
    </div>

    <!-- 图表区域 -->
    <div class="charts-grid">
      <!-- 订单状态分布趋势 - 折线图 -->
      <div class="chart-card wide">
        <h3 class="chart-title">订单状态分布趋势</h3>
        <div class="chart-container">
          <v-chart :option="lineChartOption" autoresize />
        </div>
      </div>

      <!-- 商品分类分布 - 饼图 -->
      <div class="chart-card">
        <h3 class="chart-title">商品分类分布</h3>
        <div class="chart-container">
          <v-chart :option="pieChartOption" autoresize />
        </div>
      </div>
    </div>

    <!-- 最近订单表格 -->
    <div class="table-card">
      <h3 class="table-title">最近订单</h3>
      <div class="table-wrapper">
        <table class="orders-table">
          <thead>
            <tr>
              <th>订单号</th>
              <th>买家</th>
              <th>商品</th>
              <th>金额</th>
              <th>状态</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="recentOrders.length === 0">
              <td colspan="5" class="empty-tip">暂无订单数据</td>
            </tr>
            <tr v-for="order in recentOrders" :key="order.id" class="table-row">
              <td class="order-id">#ORD-{{ order.id }}</td>
              <td>买家-{{ order.buyerId }}</td>
              <td>商品-{{ order.goodId }}</td>
              <td class="order-amount">¥{{ formatAmount(order.unitPrice * order.count) }}</td>
              <td>
                <span :class="['status-badge', getStatusClass(order.status)]">
                  {{ getStatusText(order.status) }}
                </span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { use } from 'echarts/core';
import { CanvasRenderer } from 'echarts/renderers';
import { LineChart, PieChart } from 'echarts/charts';
import {
  TitleComponent,
  TooltipComponent,
  GridComponent,
  LegendComponent
} from 'echarts/components';
import VChart from 'vue-echarts';
import axios from 'axios';
import { onMounted, ref, computed } from 'vue';
import { ShoppingCart, DollarSign, Users, TrendingUp } from 'lucide-vue-next';

// 注册 ECharts 组件
use([
  CanvasRenderer,
  LineChart,
  PieChart,
  TitleComponent,
  TooltipComponent,
  GridComponent,
  LegendComponent
]);

// 统计数据
const userStats = ref({ total: 0, active: 0, banned: 0, admin: 0, normal: 0 });
const goodStats = ref({ total: 0, active: 0, sold: 0, offline: 0, category: [] });
const dealStats = ref({ total: 0, pending: 0, paid: 0, cancelled: 0, completed: 0, refunded: 0, totalAmount: 0 });
const recentOrders = ref([]);
const error = ref('');

// 统计卡片数据
const stats = computed(() => ({
  orders: dealStats.value.total,
  totalAmount: dealStats.value.totalAmount || 0,
  activeUsers: userStats.value.active,
  activeGoods: goodStats.value.active
}));

// 订单状态统计
const orderStatus = computed(() => ({
  pending: dealStats.value.pending,
  paid: dealStats.value.paid,
  cancel: dealStats.value.cancelled,
  complete: dealStats.value.completed,
  refund: dealStats.value.refunded
}));

// 商品分类统计
const categoryData = computed(() => {
  const colors = ['#ec4899', '#f472b6', '#fbcfe8', '#fce7f3', '#e11d48'];
  return (goodStats.value.category || []).map((item, index) => ({
    name: item.label || '其他',
    value: item.count || 0,
    color: colors[index % colors.length]
  })).slice(0, 5);
});

// 折线图配置
const lineChartOption = computed(() => ({
  tooltip: {
    trigger: 'axis',
    backgroundColor: 'rgba(255, 255, 255, 0.95)',
    borderColor: 'rgba(255, 255, 255, 0.9)',
    borderRadius: 16,
    padding: [12, 16],
    boxShadow: '0 8px 32px rgba(236, 72, 153, 0.1)',
    textStyle: {
      color: '#374151',
      fontSize: 14,
      fontWeight: 400
    }
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    top: '10%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    data: ['待付款', '已付款', '已取消', '已完成', '已退款', '总计'],
    axisLine: {
      lineStyle: {
        color: '#d1d5db'
      }
    },
    axisLabel: {
      color: '#9ca3af',
      fontSize: 12
    }
  },
  yAxis: {
    type: 'value',
    axisLine: {
      lineStyle: {
        color: '#d1d5db'
      }
    },
    axisLabel: {
      color: '#9ca3af',
      fontSize: 12
    },
    splitLine: {
      lineStyle: {
        color: 'rgba(236, 72, 153, 0.05)',
        type: 'dashed'
      }
    }
  },
  series: [{
    data: [
      orderStatus.value.pending,
      orderStatus.value.paid,
      orderStatus.value.cancel,
      orderStatus.value.complete,
      orderStatus.value.refund,
      dealStats.value.total
    ],
    type: 'line',
    smooth: true,
    symbol: 'circle',
    symbolSize: 8,
    lineStyle: {
      width: 3,
      color: {
        type: 'linear',
        x: 0, y: 0, x2: 1, y2: 0,
        colorStops: [
          { offset: 0, color: '#ec4899' },
          { offset: 1, color: '#f472b6' }
        ]
      }
    },
    itemStyle: {
      color: '#ec4899',
      borderWidth: 0
    },
    areaStyle: {
      color: {
        type: 'linear',
        x: 0, y: 0, x2: 0, y2: 1,
        colorStops: [
          { offset: 0, color: 'rgba(236, 72, 153, 0.15)' },
          { offset: 1, color: 'rgba(236, 72, 153, 0.02)' }
        ]
      }
    }
  }]
}));

// 饼图配置
const pieChartOption = computed(() => ({
  tooltip: {
    trigger: 'item',
    backgroundColor: 'rgba(255, 255, 255, 0.95)',
    borderColor: 'rgba(255, 255, 255, 0.9)',
    borderRadius: 16,
    padding: [12, 16],
    boxShadow: '0 8px 32px rgba(236, 72, 153, 0.1)',
    textStyle: {
      color: '#374151',
      fontSize: 14,
      fontWeight: 400
    },
    formatter: '{b}: {c} ({d}%)'
  },
  legend: {
    orient: 'vertical',
    right: '5%',
    top: 'center',
    textStyle: {
      color: '#6b7280',
      fontSize: 12
    }
  },
  series: [{
    type: 'pie',
    radius: ['35%', '55%'],
    center: ['35%', '50%'],
    avoidLabelOverlap: true,
    itemStyle: {
      borderRadius: 6,
      borderColor: '#fff',
      borderWidth: 2
    },
    label: {
      show: false
    },
    labelLine: {
      show: false
    },
    emphasis: {
      scale: true,
      scaleSize: 5
    },
    data: categoryData.value.map(item => ({
      name: item.name,
      value: item.value,
      itemStyle: { color: item.color }
    }))
  }]
}));

// 格式化金额（分转元）
const formatAmount = (amount) => {
  if (!amount) return '0.00';
  return (Number(amount) / 100).toFixed(2);
};

// 获取状态文本
const getStatusText = (status) => {
  const statusMap = {
    0: '待付款',
    1: '已付款',
    2: '已取消',
    3: '已完成',
    4: '已退款'
  };
  return statusMap[status] || '未知';
};

// 获取状态样式类
const getStatusClass = (status) => {
  const classMap = {
    0: 'pending',
    1: 'paid',
    2: 'cancel',
    3: 'complete',
    4: 'refund'
  };
  return classMap[status] || '';
};

// 加载统计数据
async function loadStats() {
  try {
    const [userRes, goodRes, dealRes, orderRes] = await Promise.all([
      axios.get('/user/count'),
      axios.get('/good/count'),
      axios.get('/deal/count'),
      axios.post('/deal/info', { page: 1 })
    ]);

    if (userRes.data?.code === 200) userStats.value = userRes.data.data || {};
    if (goodRes.data?.code === 200) goodStats.value = goodRes.data.data || {};
    if (dealRes.data?.code === 200) dealStats.value = dealRes.data.data || {};
    if (orderRes.data?.code === 200) recentOrders.value = (orderRes.data.data || []).slice(0, 5);

    if (userRes.data?.code !== 200 || goodRes.data?.code !== 200 || dealRes.data?.code !== 200) {
      error.value = '部分接口返回异常，已显示可用数据';
    }
  } catch (err) {
    error.value = '接口请求失败，请确认后端服务已启动';
    console.error('数据加载失败：', err);
  }
}

onMounted(() => {
  loadStats();
});
</script>

<style scoped>
/* ==================== Dashboard Container ==================== */
.dashboard {
  display: flex;
  flex-direction: column;
  gap: 32px;
  padding: 0;
  min-height: 100%;
  background: transparent;
}

/* ==================== Page Header ==================== */
.page-header {
  margin-bottom: 8px;
}

.page-title {
  font-size: 36px;
  font-weight: 300;
  letter-spacing: 0.02em;
  color: #374151;
  margin: 0 0 8px 0;
}

.page-desc {
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #9ca3af;
  margin: 0;
}

/* ==================== Error Tip ==================== */
.error-tip {
  padding: 12px 16px;
  background: rgba(236, 72, 153, 0.1);
  border-radius: 12px;
  color: #ec4899;
  font-size: 14px;
  font-weight: 400;
}

/* ==================== Stats Grid ==================== */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
}

.stat-card {
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(30px) saturate(180%);
  -webkit-backdrop-filter: blur(30px) saturate(180%);
  border: 1px solid rgba(255, 255, 255, 0.9);
  border-radius: 24px;
  padding: 24px;
  box-shadow: 0 8px 32px rgba(236, 72, 153, 0.06);
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.stat-card:hover {
  background: rgba(255, 255, 255, 0.65);
  transform: translateY(-4px);
  box-shadow: 0 16px 48px rgba(236, 72, 153, 0.1);
}

/* Shine Effect */
.stat-card::after {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(
    45deg,
    transparent 30%,
    rgba(255, 255, 255, 0.3) 50%,
    transparent 70%
  );
  transform: translateX(-100%);
  transition: transform 0.6s ease;
}

.stat-card:hover::after {
  transform: translateX(100%);
}

.stat-icon {
  margin-bottom: 16px;
}

.stat-icon .icon {
  color: #ec4899;
}

.stat-value {
  font-size: 36px;
  font-weight: 300;
  letter-spacing: 0.02em;
  color: #374151;
  margin: 0 0 12px 0;
}

.stat-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.stat-label {
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #9ca3af;
  margin: 0;
}

.stat-change {
  font-size: 12px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #4caf50;
}

/* ==================== Charts Grid ==================== */
.charts-grid {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 24px;
}

.chart-card {
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(30px) saturate(180%);
  -webkit-backdrop-filter: blur(30px) saturate(180%);
  border: 1px solid rgba(255, 255, 255, 0.9);
  border-radius: 24px;
  padding: 24px;
  box-shadow: 0 8px 32px rgba(236, 72, 153, 0.06);
}

.chart-card.wide {
  /* 2倍宽度 */
}

.chart-title {
  font-size: 18px;
  font-weight: 300;
  letter-spacing: 0.02em;
  color: #374151;
  margin: 0 0 24px 0;
}

.chart-container {
  height: 300px;
}

/* ==================== Table Card ==================== */
.table-card {
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(30px) saturate(180%);
  -webkit-backdrop-filter: blur(30px) saturate(180%);
  border: 1px solid rgba(255, 255, 255, 0.9);
  border-radius: 24px;
  padding: 24px;
  box-shadow: 0 8px 32px rgba(236, 72, 153, 0.06);
}

.table-title {
  font-size: 18px;
  font-weight: 300;
  letter-spacing: 0.02em;
  color: #374151;
  margin: 0 0 24px 0;
}

.table-wrapper {
  overflow-x: auto;
}

.orders-table {
  width: 100%;
  border-collapse: collapse;
}

.orders-table th {
  font-size: 12px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #9ca3af;
  text-transform: uppercase;
  padding: 16px 24px;
  text-align: left;
  border-bottom: 1px solid rgba(236, 72, 153, 0.1);
}

.orders-table td {
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #374151;
  padding: 20px 24px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.3);
}

.table-row {
  transition: background 0.3s ease;
}

.table-row:hover {
  background: rgba(255, 255, 255, 0.4);
}

.order-id {
  font-weight: 500;
  color: #6b7280;
}

.order-amount {
  font-weight: 500;
  color: #374151;
}

.empty-tip {
  text-align: center;
  color: #ec4899;
  padding: 32px 0;
}

/* ==================== Status Badge ==================== */
.status-badge {
  display: inline-block;
  padding: 6px 16px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 400;
  letter-spacing: 0.02em;
}

.status-badge.pending {
  background: rgba(255, 152, 0, 0.1);
  color: #ff9800;
}

.status-badge.paid {
  background: rgba(33, 150, 243, 0.1);
  color: #2196f3;
}

.status-badge.cancel {
  background: rgba(158, 158, 158, 0.1);
  color: #9e9e9e;
}

.status-badge.complete {
  background: rgba(76, 175, 80, 0.1);
  color: #4caf50;
}

.status-badge.refund {
  background: rgba(244, 67, 54, 0.1);
  color: #f44336;
}

/* ==================== Responsive ==================== */
@media (max-width: 1200px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .charts-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
}
</style>