<template>
  <div class="home-container">
    <div class="search-bar">
      <el-input 
        v-model="searchKeyword" 
        placeholder="输入标签搜索商品 (如: 教材教辅)" 
        class="search-input"
        clearable
        @keyup.enter="handleSearch"
      >
        <template #append>
          <el-button icon="Search" @click="handleSearch" />
        </template>
      </el-input>
      <el-button @click="resetSearch" style="margin-left: 10px;">重置</el-button>
    </div>

    <div class="goods-grid" v-loading="loading">
      <el-empty v-if="!loading && goods.length === 0" description="暂无商品数据" />
      
      <el-card v-for="item in goods" :key="item.id" class="good-card" shadow="hover">
        <div class="good-img-placeholder">
          <img v-if="item.imgUrl" :src="item.imgUrl" alt="商品图片" />
          <el-icon v-else size="40" color="#c0c4cc"><Picture /></el-icon>
        </div>
        <div class="good-info">
          <h3 class="good-name">{{ item.name }}</h3>
          <p class="good-desc">{{ item.content }}</p>
          <div class="good-tags">
            <el-tag size="small" type="info">{{ item.label || '无标签' }}</el-tag>
            <span class="view-count">浏览: {{ item.viewCount || 0 }}</span>
          </div>
          <div class="good-footer">
            <span class="price">¥{{ item.unitPrice != null ? (item.unitPrice / 100).toFixed(2) : '0.00' }}</span>
            <el-button type="primary" size="small" @click="buyGood(item)">购买</el-button>
          </div>
        </div>
      </el-card>
    </div>

    <el-dialog v-model="buyDialogVisible" title="确认购买" width="400px">
      <div v-if="currentGood" class="buy-confirm-content">
        <p><strong>商品：</strong>{{ currentGood.name }}</p>
        <p><strong>单价：</strong>¥{{ currentGood.unitPrice != null ? (currentGood.unitPrice / 100).toFixed(2) : '0.00' }}</p>
        <p><strong>卖家：</strong>{{ currentGood.sallerId || currentGood.sellerId }}</p>
        <div style="margin-top: 15px;">
          购买数量：<el-input-number v-model="buyCount" :min="1" :max="currentGood.count" size="small" />
        </div>
        <p style="margin-top: 15px; font-size: 18px; color: #f56c6c; font-weight: bold;">
          总价：¥{{ currentGood.unitPrice != null ? ((currentGood.unitPrice * buyCount) / 100).toFixed(2) : '0.00' }}
        </p>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="buyDialogVisible = false">取消</el-button>
          <el-button type="danger" @click="submitOrder" :loading="submitLoading">确认下单</el-button>
        </span>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getGoodList, searchGood } from '../../api/good'
import { createDeal } from '../../api/deal'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'

const router = useRouter()
const goods = ref([])
const loading = ref(false)
const searchKeyword = ref('')

const buyDialogVisible = ref(false)
const currentGood = ref(null)
const buyCount = ref(1)
const submitLoading = ref(false)

const loadGoods = async () => {
  loading.value = true
  try {
    const res = await getGoodList({ page: 1 })
    if (res.code === 200) {
      // 过滤出上架状态(status=1)的商品
      goods.value = (res.data || []).filter(item => item.status === 1)
    }
  } catch (error) {
    ElMessage.error('加载商品失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = async () => {
  if (!searchKeyword.value.trim()) {
    return loadGoods()
  }
  loading.value = true
  try {
    const res = await searchGood({ label: searchKeyword.value.trim() })
    if (res.code === 200) {
      goods.value = (res.data || []).filter(item => item.status === 1)
    } else {
      ElMessage.warning(res.msg || '未搜索到结果')
      goods.value = []
    }
  } catch (error) {
    ElMessage.error('搜索异常')
  } finally {
    loading.value = false
  }
}

const resetSearch = () => {
  searchKeyword.value = ''
  loadGoods()
}

const buyGood = (item) => {
  const token = localStorage.getItem('token')
  if (!token) {
    ElMessage.warning('请先登录后再购买')
    router.push('/login')
    return
  }
  currentGood.value = item
  buyCount.value = 1
  buyDialogVisible.value = true
}

const submitOrder = async () => {
  submitLoading.value = true
  try {
    const payload = {
      goodId: currentGood.value.id,
      sellerId: currentGood.value.sallerId || currentGood.value.sellerId,
      buyerId: 1001, // Mock buyer ID, ideally parsed from token
      unitPrice: currentGood.value.unitPrice,
      count: buyCount.value,
      status: 0 // 0-待支付/已创建
    }
    const res = await createDeal(payload)
    if (res.code === 200) {
      ElMessage.success('下单成功！可以在个人主页查看订单。')
      buyDialogVisible.value = false
      // 真实场景应该跳支付或者刷新库存
    } else {
      ElMessage.error(res.msg || '下单失败')
    }
  } catch (error) {
    ElMessage.error('订单提交异常')
  } finally {
    submitLoading.value = false
  }
}

onMounted(() => {
  loadGoods()
})
</script>

<style scoped>
.home-container {
  padding: 20px 0;
}

.search-bar {
  display: flex;
  justify-content: center;
  margin-bottom: 30px;
}

.search-input {
  width: 500px;
}

.goods-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.good-card {
  border-radius: 8px;
  overflow: hidden;
  transition: transform 0.2s;
}

.good-card:hover {
  transform: translateY(-5px);
}

.good-img-placeholder {
  height: 200px;
  background-color: #f0f2f5;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
}

.good-img-placeholder img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.good-info {
  padding: 15px;
}

.good-name {
  margin: 0 0 10px 0;
  font-size: 16px;
  color: #303133;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.good-desc {
  font-size: 13px;
  color: #909399;
  margin: 0 0 15px 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.good-tags {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.view-count {
  font-size: 12px;
  color: #c0c4cc;
}

.good-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-top: 1px solid #ebeef5;
  padding-top: 15px;
}

.price {
  color: #f56c6c;
  font-size: 20px;
  font-weight: bold;
}
</style>
