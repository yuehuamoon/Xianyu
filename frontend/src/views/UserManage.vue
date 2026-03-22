<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span>用户信息展示</span>
        <el-button type="primary" size="small" @click="fetchUsers">刷新数据</el-button>
      </div>
    </template>
    <el-table :data="users" v-loading="loading" style="width: 100%" border>
      <el-table-column prop="id" label="ID" width="80" align="center" />
      <el-table-column prop="phone" label="手机号" width="120" align="center" />
      <el-table-column prop="nickName" label="昵称" width="120" align="center" />
      <el-table-column prop="realName" label="真实姓名" width="100" align="center" />
      <el-table-column prop="role" label="角色" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="row.role === 1 ? 'danger' : 'success'">
            {{ row.role === 1 ? '管理员' : '普通用户' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="wallet" label="钱包余额" width="100" align="center" />
      <el-table-column prop="gender" label="性别" width="80" align="center">
        <template #default="{ row }">
          {{ row.gender === 0 ? '男' : row.gender === 1 ? '女' : '未知' }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : row.status === 0 ? 'danger' : 'warning'">
            {{ row.status === 1 ? '正常' : row.status === 0 ? '封禁' : '审核中' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="address" label="地址" min-width="150" show-overflow-tooltip />
      <el-table-column prop="createTime" label="创建时间" width="160" align="center">
        <template #default="{ row }">
          {{ formatDate(row.createTime) }}
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getUserList } from '../api/user'
import { ElMessage } from 'element-plus'

const users = ref([])
const loading = ref(false)

const fetchUsers = async () => {
  loading.value = true
  try {
    const res = await getUserList()
    if (res.code === 200) {
      users.value = res.data || []
    } else {
      ElMessage.error(res.msg || '获取用户数据失败')
    }
  } catch (error) {
    ElMessage.error('网络错误或服务器异常')
  } finally {
    loading.value = false
  }
}

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}:${String(date.getSeconds()).padStart(2, '0')}`
}

onMounted(() => {
  fetchUsers()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
