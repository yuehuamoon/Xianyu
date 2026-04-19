<template>
  <div class="otheruser-bg" @mousemove="handleGlobalMouseMove" @mouseleave="handleGlobalMouseLeave">
    <!-- 全局鼠标光晕 -->
    <div class="mouse-glow" :style="glowStyle"></div>

    <div class="body">
      <!-- 用户信息卡片 -->
    <div 
      class="user-card"
      @mousemove="handleMouseMove"
      @mouseleave="handleMouseLeave"
      :style="cardStyle"
    >
      <!-- 头像区域 -->
      <div class="avatar-section">
        <div class="avatar-wrapper">
          <img 
            :src="toUserInfo?.avatarUrl || defaultAvatar" 
            alt="用户头像"
            class="avatar"
          />
          <div class="avatar-glow"></div>
        </div>
        <div class="status-badge" :class="statusClass">
          {{ statusText }}
        </div>
      </div>

      <!-- 基本信息 -->
      <div class="info-section">
        <h2 class="username">{{ toUserInfo?.nickName || toUserInfo?.realName || '用户' }}</h2>
        <p class="user-id">ID: {{ toUserInfo?.id }}</p>
        
        <div class="info-grid">
          <div class="info-item">
            <span class="info-label">性别</span>
            <span class="info-value">{{ genderText }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">角色</span>
            <span class="info-value">{{ roleText }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">钱包余额</span>
            <span class="info-value price">¥{{ toUserInfo?.wallet || '0.00' }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">电话</span>
            <span class="info-value">{{ toUserInfo?.phone || '未设置' }}</span>
          </div>
          <div class="info-item full-width">
            <span class="info-label">地址</span>
            <span class="info-value">{{ toUserInfo?.address || '未设置' }}</span>
          </div>
          <div class="info-item full-width">
            <span class="info-label">注册时间</span>
            <span class="info-value">{{ formatTime(toUserInfo?.createTime) }}</span>
          </div>
        </div>
      </div>

      <!-- 装饰元素 -->
      <div class="card-decoration">
        <div class="deco-circle"></div>
        <div class="deco-circle small"></div>
      </div>
    </div>

    <!-- 评论区域 -->
    <div class="comment-section">
      <CommentLayout :data="commentData" />
    </div>
    </div>
  </div>
</template>

<script setup>
import CommentLayout from '@/components/commentLayout.vue';
import axios from 'axios';
import { onMounted, reactive, ref, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter();
const userId = route.params.userId;

// 默认头像
const defaultAvatar = 'https://api.dicebear.com/7.x/avataaars/svg?seed=' + userId;

// 用户信息
let toUserInfo = ref(null);

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

// 评论数据
let commentData = computed(() => ({
  id: userId,
  type: 'user',
  data: toUserInfo.value || {},
}));

// 卡片交互状态
const cardTransform = reactive({
  rotateX: 0,
  rotateY: 0,
  scale: 1,
});

// 计算卡片样式
const cardStyle = computed(() => ({
  transform: `perspective(1000px) rotateX(${cardTransform.rotateX}deg) rotateY(${cardTransform.rotateY}deg) scale(${cardTransform.scale})`,
  transition: cardTransform.scale === 1 ? 'transform 0.6s ease-out' : 'transform 0.1s ease-out',
}));

// 鼠标移动处理 - 产生相对于中心的上扬效果
function handleMouseMove(e) {
  const card = e.currentTarget;
  const rect = card.getBoundingClientRect();
  
  // 计算鼠标相对于卡片中心的位置
  const centerX = rect.left + rect.width / 2;
  const centerY = rect.top + rect.height / 2;
  
  // 鼠标位置相对于中心的偏移
  const mouseX = e.clientX - centerX;
  const mouseY = e.clientY - centerY;
  
  // 计算旋转角度（最大15度）
  const maxRotate = 15;
  const rotateY = (mouseX / (rect.width / 2)) * maxRotate;
  const rotateX = -(mouseY / (rect.height / 2)) * maxRotate;
  
  cardTransform.rotateX = rotateX;
  cardTransform.rotateY = rotateY;
  cardTransform.scale = 1.02;
}

// 鼠标离开处理
function handleMouseLeave() {
  cardTransform.rotateX = 0;
  cardTransform.rotateY = 0;
  cardTransform.scale = 1;
}

// 获取用户信息
async function getInfo() {
  try {
    let res = await axios.post('/user/selectById', { id: userId });
    toUserInfo.value = res.data.data;
    console.log('用户信息：', toUserInfo.value);
    if (res.data.data == null){
            router.push("/")
            alert("user is null")
    }
  } catch (err) {
    console.error('获取用户信息失败：', err);
    router.push("/")
    alert("error")
  }
}

// 格式化时间
function formatTime(time) {
  if (!time) return '未知';
  const date = new Date(time);
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  });
}

// 计算属性：性别文本
const genderText = computed(() => {
  if (toUserInfo.value?.gender === 0) return '男';
  if (toUserInfo.value?.gender === 1) return '女';
  return '未知';
});

// 计算属性：角色文本
const roleText = computed(() => {
  const roles = {
    0: '普通用户',
    1: '管理员',
    2: '特殊角色',
  };
  return roles[toUserInfo.value?.role] || '普通用户';
});

// 计算属性：状态文本
const statusText = computed(() => {
  const statuses = {
    0: '已封禁',
    1: '正常',
    2: '审核中',
  };
  return statuses[toUserInfo.value?.status] || '正常';
});

// 计算属性：状态样式类
const statusClass = computed(() => {
  const status = toUserInfo.value?.status;
  if (status === 1) return 'status-normal';
  if (status === 0) return 'status-banned';
  if (status === 2) return 'status-pending';
  return 'status-normal';
});

onMounted(getInfo);
</script>

<style scoped>
/* Liquid Glass Style - 用户页面 */
.otheruser-bg {
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

.body {
  width: 80%;
  max-width: 1200px;
  margin: 24px auto;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* 用户信息卡片 */
.user-card {
  position: relative;
  padding: 32px;
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(30px) saturate(180%);
  -webkit-backdrop-filter: blur(30px) saturate(180%);
  border: 1px solid rgba(255, 255, 255, 0.9);
  border-radius: 24px;
  box-shadow:
    0 8px 32px rgba(236, 72, 153, 0.06),
    inset 0 1px 1px rgba(255, 255, 255, 0.8);
  display: flex;
  gap: 32px;
  align-items: flex-start;
  transform-style: preserve-3d;
  cursor: pointer;
}

/* 头像区域 */
.avatar-section {
  position: relative;
  flex-shrink: 0;
}

.avatar-wrapper {
  position: relative;
  width: 120px;
  height: 120px;
}

.avatar {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid rgba(236, 72, 153, 0.3);
  transition: all 0.4s ease;
}

.avatar-glow {
  position: absolute;
  inset: -8px;
  border-radius: 50%;
  background: linear-gradient(135deg, rgba(236, 72, 153, 0.2) 0%, rgba(244, 114, 182, 0.1) 100%);
  filter: blur(12px);
  z-index: -1;
  transition: all 0.4s ease;
}

.user-card:hover .avatar {
  border-color: rgba(236, 72, 153, 0.6);
}

.user-card:hover .avatar-glow {
  inset: -12px;
  filter: blur(16px);
}

/* 状态徽章 */
.status-badge {
  position: absolute;
  bottom: -8px;
  left: 50%;
  transform: translateX(-50%);
  padding: 4px 12px;
  font-size: 12px;
  font-weight: 500;
  border-radius: 12px;
  white-space: nowrap;
}

.status-normal {
  background: linear-gradient(135deg, #10b981 0%, #34d399 100%);
  color: #fff;
}

.status-banned {
  background: linear-gradient(135deg, #ef4444 0%, #f87171 100%);
  color: #fff;
}

.status-pending {
  background: linear-gradient(135deg, #f59e0b 0%, #fbbf24 100%);
  color: #fff;
}

/* 信息区域 */
.info-section {
  flex: 1;
}

.username {
  font-size: 28px;
  font-weight: 600;
  margin: 0 0 8px;
  background: linear-gradient(135deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.user-id {
  font-size: 14px;
  color: #6b7280;
  margin: 0 0 20px;
}

/* 信息网格 */
.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.info-item {
  position: relative;
  display: flex;
  flex-direction: column;
  gap: 6px;
  padding: 14px 18px;
  background: rgba(255, 255, 255, 0.6);
  border-radius: 16px;
  border: 2px solid rgba(236, 72, 153, 0.2);
  box-shadow:
    0 4px 12px rgba(236, 72, 153, 0.08),
    inset 0 1px 2px rgba(255, 255, 255, 0.9);
  transition: all 0.3s ease;
  overflow: hidden;
}

/* 左侧装饰条 */
.info-item::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 4px;
  background: linear-gradient(180deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
  border-radius: 4px 0 0 4px;
}

/* hover 效果 */
.info-item:hover {
  border-color: rgba(236, 72, 153, 0.4);
  background: rgba(255, 255, 255, 0.75);
  box-shadow:
    0 6px 20px rgba(236, 72, 153, 0.12),
    inset 0 1px 2px rgba(255, 255, 255, 0.9);
  transform: translateX(4px);
}

.info-item.full-width {
  grid-column: span 2;
}

.info-label {
  font-size: 12px;
  color: #9ca3af;
  font-weight: 400;
  letter-spacing: 0.02em;
  padding-left: 8px;
}

.info-value {
  font-size: 15px;
  color: #374151;
  font-weight: 600;
  padding-left: 8px;
}

.info-value.price {
  background: linear-gradient(135deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  font-size: 18px;
  font-weight: 700;
}

/* 装饰元素 */
.card-decoration {
  position: absolute;
  top: 0;
  right: 0;
  width: 150px;
  height: 150px;
  pointer-events: none;
  overflow: hidden;
}

.deco-circle {
  position: absolute;
  top: -30px;
  right: -30px;
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background: linear-gradient(135deg, rgba(236, 72, 153, 0.15) 0%, rgba(244, 114, 182, 0.1) 100%);
  filter: blur(20px);
}

.deco-circle.small {
  top: 60px;
  right: 40px;
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, rgba(236, 72, 153, 0.2) 0%, rgba(244, 114, 182, 0.15) 100%);
}

/* 评论区域 */
.comment-section {
  padding: 24px;
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(30px) saturate(180%);
  -webkit-backdrop-filter: blur(30px) saturate(180%);
  border: 1px solid rgba(255, 255, 255, 0.9);
  border-radius: 24px;
  box-shadow:
    0 8px 32px rgba(236, 72, 153, 0.06),
    inset 0 1px 1px rgba(255, 255, 255, 0.8);
}

/* 响应式 */
@media (max-width: 768px) {
  .user-card {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }
  
  .info-grid {
    grid-template-columns: 1fr;
  }
  
  .info-item.full-width {
    grid-column: span 1;
  }
}
</style>