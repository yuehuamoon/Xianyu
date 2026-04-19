<template>
  <div id="managelayout" class="liquid-glass-bg" @mousemove="handleGlobalMouseMove" @mouseleave="handleGlobalMouseLeave">
    <!-- 全局鼠标光晕 -->
    <div class="mouse-glow" :style="glowStyle"></div>

    <!-- 动态光效背景粒子 -->
    <div class="floating-particles"></div>

    <!-- 左侧固定侧边栏 -->
    <aside class="sidebar">
      <div class="logo-area">
        <h1 class="logo-text">Admin Dashboard</h1>
      </div>

      <nav class="sidebar-nav">
        <div
          :class="['nav-item', { active: currentRoute === 'mindex' }]"
          @click="toPage('mindex')"
        >
          <LayoutDashboard class="nav-icon" :size="20" :stroke-width="1.5" />
          <span class="nav-label">数据看板</span>
        </div>
        <div
          :class="['nav-item', { active: currentRoute === 'userinfo' }]"
          @click="toPage('userinfo')"
        >
          <Users class="nav-icon" :size="20" :stroke-width="1.5" />
          <span class="nav-label">用户管理</span>
        </div>
        <div
          :class="['nav-item', { active: currentRoute === 'goodinfo' }]"
          @click="toPage('goodinfo')"
        >
          <ShoppingBag class="nav-icon" :size="20" :stroke-width="1.5" />
          <span class="nav-label">商品管理</span>
        </div>
        <div
          :class="['nav-item', { active: currentRoute === 'dealinfo' }]"
          @click="toPage('dealinfo')"
        >
          <FileText class="nav-icon" :size="20" :stroke-width="1.5" />
          <span class="nav-label">订单管理</span>
        </div>
      </nav>
    </aside>

    <!-- 主内容区 -->
    <main class="main-area">
      <!-- 顶部导航栏 -->
      <header class="top-header">
        <div class="header-title">{{ pageTitle }}</div>
        <div class="user-info">
          <div class="avatar">
            <span>管</span>
          </div>
          <div class="user-name">{{ userName }}</div>
          <button class="logout-btn" @click="layout">
            <span>退出</span>
          </button>
        </div>
      </header>

      <!-- 内容区域 -->
      <div class="content-area">
        <router-view></router-view>
      </div>
    </main>
  </div>
</template>

<script setup>
import { onMounted, ref, computed, watch, reactive } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { LayoutDashboard, Users, ShoppingBag, FileText } from 'lucide-vue-next';

const router = useRouter();
const route = useRoute();

// 全局鼠标光晕位置
const glowPosition = reactive({
  x: 0,
  y: 0,
  visible: false,
});

// 计算光晕样式
const glowStyle = computed(() => ({
  left: `${glowPosition.x}px`,
  top: `${glowPosition.y}px`,
  opacity: glowPosition.visible ? 1 : 0,
}));

// 全局鼠标移动处理
function handleGlobalMouseMove(e) {
  glowPosition.x = e.clientX;
  glowPosition.y = e.clientY;
  glowPosition.visible = true;
}

// 全局鼠标离开处理
function handleGlobalMouseLeave() {
  glowPosition.visible = false;
}

// 当前路由名称
const currentRoute = computed(() => route.name || 'mindex');

// 页面标题映射
const pageTitle = computed(() => {
  const titleMap = {
    'mindex': '数据看板',
    'userinfo': '用户管理',
    'goodinfo': '商品管理',
    'dealinfo': '订单管理'
  };
  return titleMap[currentRoute.value] || '管理后台';
});

// 用户名
const userName = ref('管理员');

// JWT解析函数
function parseJwt(token) {
  if (!token) return null;
  try {
    const base64Payload = token.split('.')[1];
    const paddedBase64 = base64Payload.padEnd(base64Payload.length + (4 - base64Payload.length % 4) % 4, '=');
    const payload = atob(paddedBase64);
    return JSON.parse(payload);
  } catch (e) {
    console.error('JWT解析失败：', e);
    return null;
  }
}

// 登出
function layout() {
  localStorage.clear();
  router.push('/mlogin');
}

// 页面跳转
const toPage = (routeName) => {
  router.push({ name: routeName });
};

// 权限校验
onMounted(() => {
  const token = localStorage.getItem('token');
  const userInfo = parseJwt(token);
  console.log('用户信息：', userInfo);

  if (userInfo) {
    userName.value = userInfo.realName || userInfo.nickName || '管理员';
  }

  try {
    if (userInfo && userInfo.role === '1') {
      // 已登录管理员
    } else {
      router.push({ name: 'mlogin' });
    }
  } catch (err) {
    console.error('权限校验失败：', err);
    router.push({ name: 'mlogin' });
  }
});
</script>

<style scoped>
/* ==================== Liquid Glass Background ==================== */
.liquid-glass-bg {
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

@keyframes subtleGradient {
  0%, 100% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
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

/* ==================== Floating Particles ==================== */
.floating-particles {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 0;
}

.floating-particles::before,
.floating-particles::after {
  content: '';
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.15;
  animation: gentleFloat 25s ease-in-out infinite;
}

.floating-particles::before {
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, rgba(236, 72, 153, 0.25) 0%, transparent 70%);
  top: 10%;
  left: 5%;
  animation-delay: 0s;
}

.floating-particles::after {
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

/* ==================== Fixed Sidebar ==================== */
.sidebar {
  position: fixed;
  left: 0;
  top: 0;
  height: 100vh;
  width: 210px;
  background: rgba(255, 255, 255, 0.4);
  backdrop-filter: blur(40px) saturate(180%);
  -webkit-backdrop-filter: blur(40px) saturate(180%);
  border-right: 1px solid rgba(255, 255, 255, 0.6);
  box-shadow: 4px 0 24px rgba(236, 72, 153, 0.04);
  padding: 32px 24px;
  z-index: 10;
  display: flex;
  flex-direction: column;
}

.logo-area {
  margin-bottom: 40px;
}

.logo-text {
  font-size: 20px;
  font-weight: 300;
  letter-spacing: 0.02em;
  background: linear-gradient(135deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.sidebar-nav {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px 20px;
  border-radius: 16px;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  color: #6b7280;
  font-weight: 400;
  letter-spacing: 0.02em;
}

.nav-item:hover {
  background: rgba(255, 255, 255, 0.3);
  color: #ec4899;
  transform: translateX(4px);
}

.nav-item.active {
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(30px) saturate(200%);
  -webkit-backdrop-filter: blur(30px) saturate(200%);
  border: 1px solid rgba(236, 72, 153, 0.2);
  box-shadow: 0 8px 40px rgba(236, 72, 153, 0.12);
  color: #ec4899;
}

.nav-icon {
  width: 20px;
  height: 20px;
  color: inherit;
}

.nav-label {
  font-size: 14px;
}

/* ==================== Main Area ==================== */
.main-area {
  margin-left: 210px;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  position: relative;
  z-index: 1;
}

/* ==================== Top Header ==================== */
.top-header {
  height: 65px;
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(30px) saturate(180%);
  -webkit-backdrop-filter: blur(30px) saturate(180%);
  border-bottom: 1px solid rgba(255, 255, 255, 0.9);
  box-shadow: 0 4px 20px rgba(236, 72, 153, 0.04);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 32px;
  position: sticky;
  top: 0;
  z-index: 5;
}

.header-title {
  font-size: 24px;
  font-weight: 300;
  letter-spacing: 0.02em;
  color: #374151;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  color: #fff;
  font-weight: 500;
  box-shadow: 0 8px 32px rgba(236, 72, 153, 0.2);
}

.user-name {
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #374151;
}

.logout-btn {
  padding: 8px 16px;
  background: rgba(236, 72, 153, 0.1);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: 12px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  transition: all 0.3s ease;
  color: #374151;
}

.logout-btn:hover {
  background: linear-gradient(135deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
  color: #fff;
  box-shadow: 0 4px 16px rgba(236, 72, 153, 0.2);
  transform: translateY(-2px);
}

/* ==================== Content Area ==================== */
.content-area {
  flex: 1;
  padding: 32px;
  min-height: calc(100vh - 65px);
}

/* 子页面玻璃卡片风格 */
.content-area > * {
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(30px) saturate(180%);
  -webkit-backdrop-filter: blur(30px) saturate(180%);
  border: 1px solid rgba(255, 255, 255, 0.9);
  border-radius: 24px;
  box-shadow: 0 8px 32px rgba(236, 72, 153, 0.06);
  padding: 24px;
  min-height: 100%;
  color: #374151;
}

/* ==================== Scrollbar ==================== */
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-track {
  background: transparent;
}

::-webkit-scrollbar-thumb {
  background: rgba(236, 72, 153, 0.2);
  border-radius: 10px;
  transition: all 0.3s ease;
}

::-webkit-scrollbar-thumb:hover {
  background: rgba(236, 72, 153, 0.4);
}

/* ==================== Responsive ==================== */
@media (max-width: 768px) {
  .sidebar {
    width: 180px;
    padding: 24px 16px;
  }

  .main-area {
    margin-left: 180px;
  }

  .nav-item {
    padding: 12px 16px;
  }

  .content-area {
    padding: 20px;
  }
}
</style>