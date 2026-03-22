<template>
  <div class="dashboard-container">
    <el-container>
      <el-header class="header">
        <div class="logo">闲鱼后台管理系统</div>
        <div>
          <el-button type="danger" @click="handleLogout" size="small">退出登录</el-button>
        </div>
      </el-header>
      <el-container class="content-wrap">
        <el-aside width="210px" class="aside">
          <el-menu :default-active="activeMenu" router>
            <el-menu-item index="/dashboard/user">用户管理</el-menu-item>
            <el-menu-item index="/dashboard/item">商品展示</el-menu-item>
          </el-menu>
        </el-aside>
        <el-main class="main-content">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const activeMenu = computed(() => route.path)

const handleLogout = () => {
  localStorage.removeItem('token')
  router.push('/login')
  ElMessage.success('已退出登录')
}
</script>

<style scoped>
.dashboard-container {
  height: 100vh;
  background-color: #f3f4f6;
}

.header {
  background-color: #ffffff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 0 20px;
}

.logo {
  font-size: 20px;
  font-weight: bold;
  color: #409eff;
}

.content-wrap {
  height: calc(100vh - 60px);
}

.aside {
  background-color: #ffffff;
  border-right: 1px solid #ebeef5;
}

.main-content {
  padding: 16px;
}
</style>
