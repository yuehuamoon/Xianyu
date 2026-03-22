<template>
  <div class="client-layout">
    <el-menu mode="horizontal" router :default-active="route.path" class="nav-menu">
      <div class="logo">校园闲鱼</div>
      <el-menu-item index="/home">首页大厅</el-menu-item>
      <el-menu-item index="/profile">个人主页</el-menu-item>
      
      <div class="nav-right">
        <template v-if="hasToken">
          <el-button type="text" @click="handleLogout">退出登录</el-button>
          <el-button type="primary" link @click="goAdmin">进入后台</el-button>
        </template>
        <template v-else>
          <el-button type="primary" @click="goLogin">登录 / 注册</el-button>
        </template>
      </div>
    </el-menu>
    
    <div class="main-content">
      <router-view></router-view>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()

const hasToken = computed(() => !!localStorage.getItem('token'))

const goLogin = () => router.push('/login')
const goAdmin = () => router.push('/dashboard')

const handleLogout = () => {
  localStorage.removeItem('token')
  ElMessage.success('已退出登录')
  router.push('/login')
}
</script>

<style scoped>
.client-layout {
  min-height: 100vh;
  background-color: #f5f7fa;
}

.nav-menu {
  display: flex;
  align-items: center;
  padding: 0 40px;
  position: sticky;
  top: 0;
  z-index: 100;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.logo {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
  margin-right: 40px;
}

.nav-right {
  margin-left: auto;
  display: flex;
  gap: 15px;
  align-items: center;
}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}
</style>
