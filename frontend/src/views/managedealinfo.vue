<template>
  <div class="deal-body">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2 class="page-title">订单请求</h2>
      <p class="page-desc">基于 /deal/info 的订单请求视图</p>
    </div>

    <!-- 搜索区域 -->
    <div class="search-bar glass-card">
      <div class="search-title">订单搜索</div>
      <input
        type="text"
        v-model="searchKey"
        placeholder="交易ID/卖家ID/买家ID/商品ID"
        class="search-input"
      >
      <button class="search-btn" @click="search">
        <Search class="icon" :size="16" :stroke-width="1.5" />
        <span>搜索</span>
      </button>
    </div>

    <!-- 错误/加载提示 -->
    <div v-if="error" class="error-tip">{{ error }}</div>
    <div v-if="loading" class="loading-tip">加载中...</div>

    <!-- 订单卡片列表 -->
    <div class="requests-list">
      <div v-if="rawTarget.length === 0 && !loading" class="empty-tip">
        暂无订单数据
      </div>

      <div
        v-for="request in rawTarget"
        :key="request.id"
        class="request-card glass-card shine-effect"
      >
        <!-- 订单头部 -->
        <div class="request-header">
          <div class="buyer-avatar">
            <img
              src="https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=100&h=100&fit=crop"
              :alt="`buyer-${request.buyerId}`"
              class="avatar-img"
            >
          </div>

          <div class="request-info">
            <div class="request-title-row">
              <h3 class="request-title">订单 #{{ request.id }} / 商品 #{{ request.goodId }}</h3>
              <span class="status-tag glass-card">
                <Tag class="icon" :size="14" :stroke-width="1.5" />
                {{ getStatusText(request.status) }}
              </span>
            </div>
            <div class="request-meta">
              <span class="buyer-name">买家-{{ request.buyerId }}</span>
              <span class="create-time">
                <Clock class="icon" :size="14" :stroke-width="1.5" />
                {{ formatTime(request.createTime) }}
              </span>
            </div>
          </div>
        </div>

        <!-- 订单描述 -->
        <p class="request-desc">
          卖家-{{ request.sellerId }} 发起交易，数量 {{ request.count }}
        </p>

        <!-- 订单金额和操作 -->
        <div class="request-footer">
          <div class="amount-info">
            <div class="amount-item">
              <DollarSign class="icon money" :size="18" :stroke-width="1.5" />
              <span class="amount-value">¥{{ formatAmount(request.unitPrice * request.count) }}</span>
            </div>
            <div class="count-item">
              <MessageCircle class="icon" :size="14" :stroke-width="1.5" />
              <span class="count-value">{{ request.count }} 件</span>
            </div>
          </div>

          <div class="action-buttons">
            <button class="action-btn edit" @click="openEditModal(request)">
              <Edit2 class="icon" :size="14" :stroke-width="1.5" />
              <span>编辑</span>
            </button>
            <button class="action-btn delete" @click="deleteDeal(request)">
              <Trash2 class="icon" :size="14" :stroke-width="1.5" />
              <span>删除</span>
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 分页控件 -->
    <div class="pagination">
      <button @click="getDealList(currentPage - 1)" :disabled="currentPage <= 1">上一页</button>
      <span>第 {{ currentPage }} 页</span>
      <button @click="getDealList(currentPage + 1)">下一页</button>
    </div>

    <!-- 编辑弹窗 -->
    <Teleport to="body">
      <div v-if="showEditModal" class="modal-overlay" @click.self="closeEditModal">
        <div class="modal-card glass-card">
          <div class="modal-header">
            <h3 class="modal-title">编辑订单 #{{ editingDeal?.id }}</h3>
            <button class="modal-close" @click="closeEditModal">
              <X class="icon" :size="20" :stroke-width="1.5" />
            </button>
          </div>
          <div class="modal-body">
            <div class="form-grid">
              <div class="form-item">
                <label>商品ID <span class="required">*</span></label>
                <input type="number" v-model="editingDeal.goodId" class="form-input" min="1">
              </div>
              <div class="form-item">
                <label>卖家ID <span class="required">*</span></label>
                <input type="number" v-model="editingDeal.sellerId" class="form-input" min="1">
              </div>
              <div class="form-item">
                <label>买家ID <span class="required">*</span></label>
                <input type="number" v-model="editingDeal.buyerId" class="form-input" min="1">
              </div>
              <div class="form-item">
                <label>单价(分) <span class="required">*</span></label>
                <input type="number" v-model="editingDeal.unitPrice" class="form-input" min="1">
              </div>
              <div class="form-item">
                <label>数量</label>
                <input type="number" v-model="editingDeal.count" class="form-input" min="1">
              </div>
              <div class="form-item">
                <label>状态</label>
                <select v-model="editingDeal.status" class="form-select">
                  <option value="0">待付款</option>
                  <option value="1">已付款</option>
                  <option value="2">已取消</option>
                  <option value="3">已完成</option>
                  <option value="4">已退款</option>
                </select>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button class="btn-cancel" @click="closeEditModal">取消</button>
            <button class="btn-confirm" @click="confirmUpdate">确认保存</button>
          </div>
        </div>
      </div>
    </Teleport>
  </div>
</template>

<script setup>
import axios from "axios";
import { onMounted, ref, toRaw } from "vue";
import { Search, Tag, Clock, DollarSign, MessageCircle, Edit2, Trash2, X } from 'lucide-vue-next';

// 搜索关键词
const searchKey = ref("");
// 交易列表数据
const rawTarget = ref([]);
const currentPage = ref(1);
const loading = ref(true);
const error = ref('');

// 弹窗相关
const showEditModal = ref(false);
const editingDeal = ref(null);

// 格式化金额（分转元）
const formatAmount = (amount) => {
  if (!amount) return "0.00";
  return (Number(amount) / 100).toFixed(2);
};

// 格式化时间
const formatTime = (timeStr) => {
  if (!timeStr) return "未填写";
  return timeStr.replace("T", " ").substring(0, 19);
};

// 获取交易状态文本
const getStatusText = (status) => {
  const statusMap = {
    0: "待付款",
    1: "已付款",
    2: "已取消",
    3: "已完成",
    4: "已退款",
  };
  return statusMap[status] || "未知状态";
};

// 获取交易列表
async function getDealList(page = 1) {
  loading.value = true;
  error.value = '';
  try {
    currentPage.value = page;
    const res = await axios.post("/deal/info", { page: page });
    const data = res.data?.data || [];
    rawTarget.value = JSON.parse(JSON.stringify(data)).map((item) => ({
      ...item,
      originData: { ...item },
    }));
  } catch (err) {
    console.error("获取交易列表失败：", err);
    error.value = `获取数据失败：${err.response?.data?.msg || err.message}`;
    rawTarget.value = [];
  } finally {
    loading.value = false;
  }
}

// 打开编辑弹窗
const openEditModal = (deal) => {
  editingDeal.value = JSON.parse(JSON.stringify(toRaw(deal)));
  showEditModal.value = true;
  document.body.style.overflow = 'hidden';
};

// 关闭编辑弹窗
const closeEditModal = () => {
  showEditModal.value = false;
  editingDeal.value = null;
  document.body.style.overflow = '';
};

// 确认更新交易信息
const confirmUpdate = async () => {
  try {
    if (!editingDeal.value || !editingDeal.value.id) {
      alert("交易数据异常，请重新打开编辑窗口！");
      return;
    }
    if (!editingDeal.value.goodId || editingDeal.value.goodId < 1) return alert("商品ID必须为正整数！");
    if (!editingDeal.value.sellerId || editingDeal.value.sellerId < 1) return alert("卖家ID必须为正整数！");
    if (!editingDeal.value.buyerId || editingDeal.value.buyerId < 1) return alert("买家ID必须为正整数！");
    if (!editingDeal.value.unitPrice || editingDeal.value.unitPrice < 1) return alert("单价必须≥1分！");
    if (!editingDeal.value.count || editingDeal.value.count < 1) return alert("购买数量必须≥1！");

    const dealDTO = {
      id: editingDeal.value.id,
      goodId: Number(editingDeal.value.goodId),
      sellerId: Number(editingDeal.value.sellerId),
      buyerId: Number(editingDeal.value.buyerId),
      unitPrice: Number(editingDeal.value.unitPrice),
      count: Number(editingDeal.value.count),
      status: Number(editingDeal.value.status),
      createTime: editingDeal.value.createTime,
      updateTime: new Date().toISOString().replace(/\.\d+Z$/, "").replace("Z", ""),
    };

    const response = await axios.post("/deal/update", dealDTO);

    if (response.data.code === 200) {
      // 更新列表中的数据
      const dealId = editingDeal.value.id;
      const index = rawTarget.value.findIndex(item => item.id === dealId);
      if (index !== -1) {
        rawTarget.value[index] = { ...editingDeal.value, originData: { ...editingDeal.value } };
      }
      closeEditModal();
      alert(`交易ID: ${dealId} 修改成功！`);
    } else {
      alert(`修改失败：${response.data.msg}`);
    }
  } catch (err) {
    console.error("更新交易失败：", err);
    alert(`更新失败：${err.response?.data?.msg || err.message}`);
  }
};

// 删除交易
const deleteDeal = async (item) => {
  if (!confirm(`确定删除交易ID: ${item.id} 吗？`)) return;

  try {
    const response = await axios.post("/deal/deleteByID", { id: item.id, status: item.status });
    console.log(response.value)
    if (response.data.code === 200) {
      alert("删除成功！");
      getDealList(currentPage.value);
    } else {
      alert(`删除失败：${response.data.msg}`);
    }
  } catch (err) {
    console.error("删除交易失败：", err);
    alert(`删除失败：${err.response?.data?.msg || err.message}`);
  }
};

// 搜索功能
async function search() {
  if (!searchKey.value.trim()) {
    getDealList(1);
    return;
  }
  const keyword = searchKey.value.trim();
  const filtered = rawTarget.value.filter(
    (item) =>
      item.id.toString().includes(keyword) ||
      item.goodId.toString().includes(keyword) ||
      item.sellerId.toString().includes(keyword) ||
      item.buyerId.toString().includes(keyword)
  );
  rawTarget.value = filtered;
}

onMounted(() => {
  getDealList(1);
});
</script>

<style scoped>
/* ==================== Deal Body Container ==================== */
.deal-body {
  flex: 1;
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 24px;
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

/* ==================== Glass Card ==================== */
.glass-card {
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(30px) saturate(180%);
  -webkit-backdrop-filter: blur(30px) saturate(180%);
  border: 1px solid rgba(255, 255, 255, 0.9);
  box-shadow: 0 8px 32px rgba(236, 72, 153, 0.06);
}

/* ==================== Search Bar ==================== */
.search-bar {
  border-radius: 16px;
  padding: 16px 20px;
  display: flex;
  align-items: center;
  gap: 16px;
}

.search-title {
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #374151;
}

.search-input {
  flex: 1;
  padding: 10px 14px;
  background: rgba(255, 255, 255, 0.4);
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: 12px;
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #374151;
  transition: all 0.3s ease;
}

.search-input:focus {
  border-color: rgba(236, 72, 153, 0.3);
  box-shadow: 0 0 8px rgba(236, 72, 153, 0.08);
  background: rgba(255, 255, 255, 0.6);
}

.search-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 18px;
  background: linear-gradient(135deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
  color: #fff;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  letter-spacing: 0.02em;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(236, 72, 153, 0.2);
}

.search-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(236, 72, 153, 0.3);
}

.search-btn .icon {
  color: #fff;
}

/* ==================== Error/Loading Tip ==================== */
.error-tip {
  padding: 12px 16px;
  background: rgba(236, 72, 153, 0.1);
  border-radius: 12px;
  color: #ec4899;
  font-size: 14px;
}

.loading-tip {
  padding: 12px 16px;
  background: rgba(255, 255, 255, 0.5);
  border-radius: 12px;
  color: #9ca3af;
  font-size: 14px;
}

/* ==================== Requests List ==================== */
.requests-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.empty-tip {
  text-align: center;
  padding: 48px 0;
  color: #ec4899;
  font-size: 16px;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 24px;
}

/* ==================== Request Card ==================== */
.request-card {
  border-radius: 24px;
  padding: 24px;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.request-card:hover {
  background: rgba(255, 255, 255, 0.65);
  transform: translateY(-4px);
  box-shadow: 0 16px 48px rgba(236, 72, 153, 0.1);
}

/* Shine Effect */
.request-card::after {
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

.request-card:hover::after {
  transform: translateX(100%);
}

/* ==================== Request Header ==================== */
.request-header {
  display: flex;
  gap: 20px;
  margin-bottom: 16px;
}

.buyer-avatar {
  flex-shrink: 0;
}

.avatar-img {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid rgba(236, 72, 153, 0.1);
  transition: all 0.3s ease;
}

.request-card:hover .avatar-img {
  border-color: rgba(236, 72, 153, 0.3);
}

.request-info {
  flex: 1;
}

.request-title-row {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  margin-bottom: 8px;
}

.request-title {
  font-size: 20px;
  font-weight: 300;
  letter-spacing: 0.02em;
  color: #374151;
  margin: 0;
}

.status-tag {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 14px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #ec4899;
}

.status-tag .icon {
  color: #ec4899;
}

.request-meta {
  display: flex;
  align-items: center;
  gap: 16px;
}

.buyer-name {
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #374151;
}

.create-time {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #9ca3af;
}

.create-time .icon {
  color: #9ca3af;
}

/* ==================== Request Description ==================== */
.request-desc {
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #374151;
  line-height: 1.6;
  margin: 0 0 20px 0;
}

/* ==================== Request Footer ==================== */
.request-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.amount-info {
  display: flex;
  align-items: center;
  gap: 24px;
}

.amount-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.amount-item .icon.money {
  color: #4caf50;
}

.amount-value {
  font-size: 16px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #374151;
}

.count-item {
  display: flex;
  align-items: center;
  gap: 6px;
}

.count-item .icon {
  color: #9ca3af;
}

.count-value {
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #9ca3af;
}

.action-buttons {
  display: flex;
  gap: 12px;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px 16px;
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: 12px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #374151;
  transition: all 0.3s ease;
}

.action-btn .icon {
  color: #6b7280;
}

.action-btn.edit:hover {
  background: #ec4899;
  border-color: #ec4899;
  color: #fff;
}

.action-btn.edit:hover .icon {
  color: #fff;
}

.action-btn.delete:hover {
  background: #ef4444;
  border-color: #ef4444;
  color: #fff;
}

.action-btn.delete:hover .icon {
  color: #fff;
}

/* ==================== Edit Overlay ==================== */
.edit-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(30px);
  border-radius: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10;
}

.edit-form {
  border-radius: 20px;
  padding: 24px;
  width: 90%;
  max-width: 400px;
}

.edit-title {
  font-size: 16px;
  font-weight: 300;
  letter-spacing: 0.02em;
  color: #374151;
  margin: 0 0 20px 0;
  text-align: center;
}

.edit-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.edit-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.edit-item label {
  width: 60px;
  font-size: 12px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #374151;
}

.edit-input,
.edit-select {
  flex: 1;
  padding: 8px 12px;
  background: rgba(255, 255, 255, 0.5);
  border: 1px solid rgba(236, 72, 153, 0.2);
  border-radius: 8px;
  font-size: 12px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #374151;
}

.edit-input:focus,
.edit-select:focus {
  border-color: rgba(236, 72, 153, 0.4);
  box-shadow: 0 0 6px rgba(236, 72, 153, 0.1);
}

.edit-actions {
  display: flex;
  gap: 12px;
  margin-top: 20px;
  justify-content: center;
}

.btn-confirm {
  padding: 10px 20px;
  background: linear-gradient(135deg, #4caf50 0%, #388e3c 100%);
  color: #fff;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(76, 175, 80, 0.2);
}

.btn-confirm:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(76, 175, 80, 0.3);
}

.btn-cancel {
  padding: 10px 20px;
  background: linear-gradient(135deg, #ef4444 0%, #d32f2f 100%);
  color: #fff;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(239, 68, 68, 0.2);
}

.btn-cancel:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(239, 68, 68, 0.3);
}

/* ==================== Pagination ==================== */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  padding: 16px 0;
}

.pagination button {
  padding: 10px 20px;
  background: linear-gradient(135deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
  color: #fff;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  letter-spacing: 0.02em;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(236, 72, 153, 0.2);
}

.pagination button:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(236, 72, 153, 0.3);
}

.pagination button:disabled {
  background: rgba(236, 72, 153, 0.3);
  cursor: not-allowed;
  box-shadow: none;
}

.pagination span {
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #374151;
}

/* ==================== Responsive ==================== */
@media (max-width: 768px) {
  .request-header {
    flex-direction: column;
    gap: 12px;
  }

  .request-title-row {
    flex-direction: column;
    gap: 8px;
  }

  .request-footer {
    flex-direction: column;
    gap: 16px;
    align-items: flex-start;
  }

  .edit-grid {
    grid-template-columns: 1fr;
  }
}

/* ==================== Modal Styles ==================== */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(8px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  animation: fadeIn 0.2s ease;
  padding: 20px;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

.modal-card {
  width: 100%;
  max-width: 520px;
  max-height: 90vh;
  overflow-y: auto;
  border-radius: 20px;
  padding: 0;
  animation: slideUp 0.3s ease;
  background: #ffffff;
  border: 2px solid #3b82f6;
  box-shadow:
    0 20px 60px rgba(59, 130, 246, 0.15),
    0 8px 24px rgba(0, 0, 0, 0.1);
}

@keyframes slideUp {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

.modal-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 24px;
  border-bottom: 1px solid #e5e7eb;
  background: #f8fafc;
  position: sticky;
  top: 0;
  z-index: 1;
}

.modal-title {
  font-size: 18px;
  font-weight: 600;
  letter-spacing: 0.02em;
  color: #1f2937;
  margin: 0;
}

.modal-close {
  padding: 8px;
  background: rgba(59, 130, 246, 0.1);
  border: none;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.modal-close:hover { background: #3b82f6; }
.modal-close:hover .icon { color: #fff; }
.modal-close .icon { color: #3b82f6; }

.modal-body {
  padding: 24px;
  background: #ffffff;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 16px 24px;
  border-top: 1px solid #e5e7eb;
  background: #f8fafc;
  position: sticky;
  bottom: 0;
}

.modal-body .form-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.modal-body .form-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.modal-body .form-item label {
  font-size: 14px;
  font-weight: 500;
  letter-spacing: 0.02em;
  color: #374151;
}

.modal-body .form-item label .required {
  color: #3b82f6;
}

.modal-body .form-input,
.modal-body .form-select {
  padding: 12px 16px;
  background: #f9fafb;
  border: 2px solid #e5e7eb;
  border-radius: 12px;
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #374151;
  transition: all 0.3s ease;
}

.modal-body .form-input:focus,
.modal-body .form-select:focus {
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
  background: #ffffff;
  outline: none;
}

.modal-footer .btn-confirm {
  padding: 12px 24px;
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  color: #fff;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  letter-spacing: 0.02em;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
}

.modal-footer .btn-confirm:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(59, 130, 246, 0.4);
}

.modal-footer .btn-cancel {
  padding: 12px 24px;
  background: #f3f4f6;
  color: #6b7280;
  border: 2px solid #e5e7eb;
  border-radius: 12px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  letter-spacing: 0.02em;
  transition: all 0.3s ease;
}

.modal-footer .btn-cancel:hover {
  background: #e5e7eb;
  border-color: #d1d5db;
}
</style>