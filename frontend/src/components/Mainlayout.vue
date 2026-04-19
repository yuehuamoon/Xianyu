<!-- src/layouts/MainLayout.vue -->
<script setup>
import { ref, reactive, computed, onMounted, onUnmounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';

const search = ref('');
const isCategoryOpen = ref(false);
const router = useRouter();
const route = useRoute();

const glowPosition = reactive({
  x: 0,
  y: 0,
  visible: false,
});

const glowStyle = computed(() => ({
  left: `${glowPosition.x}px`,
  top: `${glowPosition.y}px`,
  opacity: glowPosition.visible ? 1 : 0,
}));

function handleGlobalMouseMove(e) {
  glowPosition.x = e.clientX;
  glowPosition.y = e.clientY;
  glowPosition.visible = true;
}

function handleGlobalMouseLeave() {
  glowPosition.visible = false;
}

const handleSearch = () => {
  const keyword = search.value.trim();
  if (!keyword) return;
  router.push({ path: '/search', query: { keyword } });
};
</script>

<template>
  <div class="bg" @mousemove="handleGlobalMouseMove" @mouseleave="handleGlobalMouseLeave">
    <!-- 全局鼠标光晕 -->
    <div class="mouse-glow" :style="glowStyle"></div>

    <!-- 导航栏 - 原 App.vue 中的导航结构 -->
    <nav class="header">
      <ul class="header-left">
        <li class="logo-item">
          <router-link to="/" class="nav-link">首页</router-link>
        </li>
        <li class="header-item">
          <router-link to="/send" class="nav-link">发布商品</router-link>
        </li>
        <li class="header-item">
          <router-link to="/profile" class="nav-link">个人主页</router-link>
        </li>
        <li class="header-item">
          <router-link to="/category" class="nav-link">分类</router-link>
        </li>
        
        <li class="header-item">
          <router-link to="/community" class="nav-link">社区</router-link>
        </li>
      </ul>
      
      <!-- 搜索框 -->
      <ul class="header-right">
        <li class="search">
          <input 
            v-model="search" 
            type="text" 
            placeholder="输入商品名称搜索" 
            class="search-input"
            @keydown.enter="handleSearch"
          />
          <button class="search-btn" @click="handleSearch">搜索</button>
        </li>
      </ul>
    </nav>

    <!-- 嵌套路由出口：渲染首页、发布、个人主页等页面 -->
    <router-view></router-view>
  </div>
</template>

<style scoped>
/* Liquid Glass Style - Based on Admin Dashboard Design */
.bg {
  width: 100%;
  min-height: 100vh;
  background: linear-gradient(135deg,
    rgba(255, 240, 245, 1) 0%,
    rgba(252, 231, 243, 1) 25%,
    rgba(254, 242, 242, 1) 50%,
    rgba(255, 237, 241, 1) 75%,
    rgba(253, 242, 248, 1) 100%
  );
  background-size: 200% 200%;
  animation: subtleGradient 20s ease infinite;
  position: relative;
  overflow-x: hidden;
}

/* 全局鼠标光晕 */
.mouse-glow {
  position: fixed;
  width: 350px;
  height: 350px;
  border-radius: 50%;
  background: radial-gradient(
    circle at center,
    rgba(236, 72, 153, 0.18) 0%,
    rgba(244, 114, 182, 0.12) 25%,
    rgba(243, 63, 94, 0.06) 45%,
    transparent 65%
  );
  transform: translate(-50%, -50%);
  pointer-events: none;
  z-index: 9999;
  transition: opacity 0.3s ease;
  filter: blur(3px);
  mix-blend-mode: screen;
}

@keyframes subtleGradient {
  0%, 100% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
}

/* Floating particles background */
.bg::before,
.bg::after {
  content: '';
  position: fixed;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.15;
  animation: gentleFloat 25s ease-in-out infinite;
  pointer-events: none;
}

.bg::before {
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, rgba(236, 72, 153, 0.25) 0%, transparent 70%);
  top: 10%;
  left: 5%;
}

.bg::after {
  width: 500px;
  height: 500px;
  background: radial-gradient(circle, rgba(244, 114, 182, 0.2) 0%, transparent 70%);
  bottom: 10%;
  right: 5%;
  animation-delay: 12s;
}

@keyframes gentleFloat {
  0%, 100% { transform: translate(0, 0) scale(1); opacity: 0.15; }
  33% { transform: translate(30px, -30px) scale(1.05); opacity: 0.2; }
  66% { transform: translate(-30px, 30px) scale(0.95); opacity: 0.1; }
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 80%;
  max-width: 1200px;
  margin: 16px auto;
  padding: 16px 24px;
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(30px) saturate(180%);
  -webkit-backdrop-filter: blur(30px) saturate(180%);
  border: 1px solid rgba(255, 255, 255, 0.9);
  border-radius: 24px;
  box-shadow:
    0 8px 32px rgba(236, 72, 153, 0.06),
    inset 0 1px 1px rgba(255, 255, 255, 0.8);
  position: sticky;
  top: 16px;
  z-index: 999;
}

.header ul {
  display: flex;
  align-items: center;
  gap: 8px;
  list-style: none;
  padding: 0;
  margin: 0;
}

.nav-link {
  display: inline-block;
  padding: 12px 20px;
  margin-left: 4px;
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #6b7280;
  text-decoration: none;
  border-radius: 16px;
  background: transparent;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.nav-link:hover {
  color: #ec4899;
  background: rgba(255, 255, 255, 0.5);
}

.nav-link.router-link-exact-active {
  color: #ec4899;
  background: rgba(255, 255, 255, 0.7);
  border: 1px solid rgba(236, 72, 153, 0.2);
  box-shadow: 0 8px 40px rgba(236, 72, 153, 0.12);
}

.search {
  display: flex;
  margin-right: 8px;
  gap: 8px;
}

.search-input {
  width: 220px;
  padding: 12px 16px;
  font-size: 14px;
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.4);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  outline: none;
  color: #374151;
  transition: all 0.3s ease;
}

.search-input::placeholder {
  color: rgba(107, 114, 128, 0.6);
}

.search-input:focus {
  background: rgba(255, 255, 255, 0.7);
  border-color: rgba(236, 72, 153, 0.3);
  box-shadow:
    0 0 0 3px rgba(236, 72, 153, 0.06),
    0 4px 16px rgba(236, 72, 153, 0.08);
}

.search-btn {
  padding: 12px 20px;
  font-size: 14px;
  font-weight: 500;
  background: linear-gradient(135deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
  color: #fff;
  border: none;
  border-radius: 16px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 16px rgba(236, 72, 153, 0.25);
}

.search-btn:hover {
  transform: translateY(-2px);
  box-shadow:
    0 12px 40px rgba(236, 72, 153, 0.25),
    0 4px 12px rgba(236, 72, 153, 0.15);
}

.category-dropdown {
  position: relative;
}

.category-dropdown-menu {
  position: absolute;
  top: calc(100% + 8px);
  left: 0;
  min-width: 180px;
  padding: 8px;
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(30px) saturate(200%);
  -webkit-backdrop-filter: blur(30px) saturate(200%);
  border: 1px solid rgba(236, 72, 153, 0.2);
  border-radius: 20px;
  display: none;
  flex-direction: column;
  gap: 4px;
  z-index: 9999;
  box-shadow: 0 12px 40px rgba(236, 72, 153, 0.12);
}

.category-dropdown:hover .category-dropdown-menu {
  display: flex;
}

.dropdown-link {
  padding: 12px 16px;
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  text-decoration: none;
  color: #6b7280;
  border-radius: 12px;
  background: transparent;
  transition: all 0.3s ease;
}

.dropdown-link:hover {
  background: rgba(236, 72, 153, 0.1);
  color: #ec4899;
}
</style>