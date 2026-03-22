<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <div class="title-wrap">
          <span>商品信息展示</span>
          <el-tag size="small" :type="dataSource === 'api' ? 'success' : 'warning'">
            {{ dataSource === 'api' ? '后端数据' : '演示数据' }}
          </el-tag>
        </div>
        <div class="actions">
          <el-input v-model="keyword" placeholder="按标签搜索" clearable size="small" class="search-input" />
          <el-button type="primary" size="small" @click="searchItems">搜索</el-button>
          <el-button size="small" @click="fetchItems">刷新</el-button>
        </div>
      </div>
    </template>
    <el-table :data="items" v-loading="loading" style="width: 100%" border>
      <el-table-column prop="id" label="ID" width="80" align="center" />
      <el-table-column prop="name" label="商品名" min-width="130" show-overflow-tooltip />
      <el-table-column prop="label" label="标签" width="130" align="center" />
      <el-table-column label="单价" width="100" align="center">
        <template #default="{ row }">
          ¥{{ row.unitPrice != null ? (row.unitPrice / 100).toFixed(2) : '0.00' }}
        </template>
      </el-table-column>
      <el-table-column prop="count" label="数量" width="90" align="center" />
      <el-table-column prop="status" label="状态" width="120" align="center">
        <template #default="{ row }">
          <el-tag :type="statusType(row.status)">{{ statusText(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="address" label="交易地址" min-width="150" show-overflow-tooltip />
      <el-table-column prop="viewCount" label="浏览量" width="100" align="center" />
      <el-table-column prop="favoriteCount" label="收藏量" width="100" align="center" />
    </el-table>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getGoodList, searchGood } from '../api/good'

const items = ref([])
const loading = ref(false)
const keyword = ref('')
const dataSource = ref('api')

const mockItems = [
  { id: 1, name: '二手显示器', label: '数码', unitPrice: 45000, count: 1, status: 1, address: '东区宿舍楼下', viewCount: 28, favoriteCount: 6 },
  { id: 2, name: '算法导论', label: '教材', unitPrice: 3500, count: 1, status: 1, address: '图书馆门口', viewCount: 15, favoriteCount: 3 },
  { id: 3, name: '机械键盘', label: '数码', unitPrice: 12000, count: 1, status: 0, address: '南门快递点', viewCount: 42, favoriteCount: 12 }
]

const fetchItems = async () => {
  loading.value = true
  try {
    const res = await getGoodList({ page: 1 })
    if (res.code === 200) {
      items.value = res.data || []
      dataSource.value = 'api'
    } else {
      ElMessage.error(res.msg || '获取商品数据失败')
      items.value = mockItems
      dataSource.value = 'mock'
    }
  } catch (error) {
    ElMessage.error('商品服务不可用，已切换为演示数据')
    items.value = mockItems
    dataSource.value = 'mock'
  } finally {
    loading.value = false
  }
}

const searchItems = async () => {
  if (!keyword.value.trim()) {
    fetchItems()
    return
  }
  loading.value = true
  try {
    if (dataSource.value === 'mock') {
      items.value = mockItems.filter(item => item.label.includes(keyword.value.trim()))
      loading.value = false
      return
    }
    const res = await searchGood({ label: keyword.value.trim() })
    if (res.code === 200) {
      items.value = res.data || []
    } else {
      ElMessage.error(res.msg || '搜索商品失败')
    }
  } catch (error) {
    ElMessage.error('网络错误或服务器异常')
  } finally {
    loading.value = false
  }
}

const statusText = (status) => {
  if (status === 0) return '审核中'
  if (status === 1) return '上架'
  if (status === 2) return '售出'
  if (status === 3) return '失效'
  return '未知'
}

const statusType = (status) => {
  if (status === 1) return 'success'
  if (status === 2) return 'warning'
  if (status === 3) return 'info'
  return 'danger'
}

onMounted(() => {
  fetchItems()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
}

.title-wrap {
  display: flex;
  align-items: center;
  gap: 8px;
}

.actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

.search-input {
  width: 200px;
}
</style>
