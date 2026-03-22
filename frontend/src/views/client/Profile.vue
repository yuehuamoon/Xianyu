<template>
  <div class="profile-container">
    <el-row :gutter="20">
      <!-- 左侧个人信息 -->
      <el-col :span="8">
        <el-card class="user-card">
          <div class="avatar-wrap">
            <el-avatar :size="100" :src="userInfo.avatarUrl || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" />
          </div>
          <h2 class="nickname">{{ userInfo.nickName || '未命名用户' }}</h2>
          <p class="role-tag">
            <el-tag size="small" :type="userInfo.role === 1 ? 'danger' : 'success'">
              {{ userInfo.role === 1 ? '管理员' : '普通用户' }}
            </el-tag>
          </p>
          <div class="info-list">
            <div class="info-item">
              <span class="label">手机号：</span>
              <span class="value">{{ userInfo.phone }}</span>
            </div>
            <div class="info-item">
              <span class="label">真实姓名：</span>
              <span class="value">{{ userInfo.realName || '未填写' }}</span>
            </div>
            <div class="info-item">
              <span class="label">钱包余额：</span>
              <span class="value price">¥{{ userInfo.wallet || 0 }}</span>
            </div>
            <div class="info-item">
              <span class="label">地址：</span>
              <span class="value">{{ userInfo.address || '未填写' }}</span>
            </div>
          </div>
        </el-card>
      </el-col>

      <!-- 右侧交易记录 -->
      <el-col :span="16">
        <el-card class="deals-card">
          <template #header>
            <div class="card-header">
              <span>我的交易记录</span>
              <el-button type="primary" link @click="loadDeals">刷新</el-button>
            </div>
          </template>
          
          <el-table :data="deals" v-loading="loadingDeals" style="width: 100%">
            <el-table-column prop="id" label="订单号" width="80" />
            <el-table-column prop="goodId" label="商品ID" width="80" />
            <el-table-column label="交易金额" width="120">
              <template #default="{ row }">
                <span class="price">¥{{ typeof row.unitPrice === 'number' ? row.unitPrice.toFixed(2) : row.unitPrice }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="count" label="数量" width="80" align="center" />
            <el-table-column label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="dealStatusType(row.status)">{{ dealStatusText(row.status) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
              <template #default="{ row }">
                <el-button 
                  v-if="row.status === 0" 
                  type="success" 
                  size="small" 
                  @click="payDeal(row)">支付</el-button>
                <el-button 
                  type="danger" 
                  size="small" 
                  @click="cancelDeal(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getUserInfo } from '../../api/user'
import { getDealList, updateDeal, deleteDeal } from '../../api/deal'
import { ElMessage, ElMessageBox } from 'element-plus'

const userInfo = ref({})
const deals = ref([])
const loadingDeals = ref(false)

// 获取当前登录用户手机号
const currentUserPhone = localStorage.getItem('phone') || ''

const loadUserInfo = async () => {
  if (!currentUserPhone) return
  
  try {
    const res = await getUserInfo({ phone: currentUserPhone })
    if (res.code === 200 && res.data) {
      userInfo.value = res.data
    }
  } catch (error) {
    console.error('Failed to load user info', error)
  }
}

const loadDeals = async () => {
  loadingDeals.value = true
  try {
    // 后端 /deal/info 接口逻辑可能需要根据用户ID过滤，当前使用默认分页
    const res = await getDealList({ page: 1 })
    if (res.code === 200) {
      deals.value = res.data || []
    }
  } catch (error) {
    ElMessage.error('获取订单记录失败')
  } finally {
    loadingDeals.value = false
  }
}

const payDeal = async (row) => {
  try {
    const res = await updateDeal({ id: row.id, status: 1 })
    if (res.code === 200) {
      ElMessage.success('支付成功')
      loadDeals()
    } else {
      const msg = res.msg || '支付失败'
      ElMessage.error(msg === 'wei deng lu' ? '请先登录' : msg)
    }
  } catch (error) {
    if (error.response && error.response.data && error.response.data.msg) {
      ElMessage.error(error.response.data.msg === 'wei deng lu' ? '请先登录' : error.response.data.msg)
    } else {
      ElMessage.error('支付请求异常')
    }
  }
}

const cancelDeal = async (row) => {
  ElMessageBox.confirm('确定要删除这条订单记录吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const res = await deleteDeal({ id: row.id })
      if (res.code === 200) {
        ElMessage.success('删除成功')
        loadDeals()
      } else {
        ElMessage.error(res.msg || '删除失败')
      }
    } catch (error) {
      ElMessage.error('请求异常')
    }
  }).catch(() => {})
}

const dealStatusText = (status) => {
  const map = {
    0: '待支付',
    1: '已支付',
    2: '已发货',
    3: '已完成',
    4: '已取消'
  }
  return map[status] || '未知'
}

const dealStatusType = (status) => {
  const map = {
    0: 'warning',
    1: 'success',
    2: 'primary',
    3: 'info',
    4: 'danger'
  }
  return map[status] || 'info'
}

onMounted(() => {
  loadUserInfo()
  loadDeals()
})
</script>

<style scoped>
.profile-container {
  margin-top: 20px;
}

.user-card {
  text-align: center;
}

.avatar-wrap {
  margin: 20px 0;
}

.nickname {
  margin: 0 0 10px;
  color: #303133;
}

.role-tag {
  margin-bottom: 30px;
}

.info-list {
  text-align: left;
  padding: 0 20px;
}

.info-item {
  margin-bottom: 15px;
  font-size: 14px;
  display: flex;
}

.info-item .label {
  color: #909399;
  width: 80px;
}

.info-item .value {
  color: #303133;
  flex: 1;
}

.price {
  color: #f56c6c !important;
  font-weight: bold;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
