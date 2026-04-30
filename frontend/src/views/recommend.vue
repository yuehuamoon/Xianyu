<template>
  <div>
    <div class="container">
      <!-- 页面标题 -->
      <div class="page-header">
        <h2 class="page-title">为你推荐</h2>
        <p class="page-subtitle">根据你的偏好精选商品</p>
        <button class="refresh-btn" @click="loadRecommendations" :disabled="loading">
          <span :class="{ 'spin': loading }">&#x21bb;</span>
          换一批
        </button>
      </div>

      <!-- 加载中骨架屏 -->
      <div v-if="loading && data.length === 0" class="card-grid">
        <div v-for="i in 5" :key="i" class="card skeleton">
          <div class="card-img skeleton-img"></div>
          <div class="card-body">
            <div class="skeleton-text long"></div>
            <div class="skeleton-text short"></div>
            <div class="skeleton-text medium"></div>
          </div>
        </div>
      </div>

      <!-- 推荐商品卡片 -->
      <div v-else-if="data.length > 0" class="card-grid">
        <router-link
          v-for="(item, idx) in data"
          :key="idx"
          class="card"
          :to="{ path: '/detail', state: { page: item.id } }"
        >
          <div class="card-badge">推荐 #{{ idx + 1 }}</div>
          <div class="card-img">
            <img :src="item.imgUrl" alt="商品图" />
          </div>
          <div class="card-body">
            <h3 class="title">{{ item.name }}</h3>
            <p class="price">￥{{ item.unitPrice }}</p>
            <p class="desc">{{ item.content }}</p>
          </div>
        </router-link>
      </div>

      <!-- 无推荐 -->
      <div v-else class="empty-state">
        <p>暂无推荐商品，请先浏览一些商品让我们了解你的偏好</p>
        <router-link to="/" class="empty-link">去逛逛</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const data = ref([]);
const loading = ref(false);

/* 解析 JWT 获取 userId */
function parseUserId(token) {
  try {
    const base64 = token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/');
    const payload = JSON.parse(decodeURIComponent(
      atob(base64).split('').map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)).join('')
    ));
    return payload.userId ?? payload.id;
  } catch {
    return null;
  }
}

/* 加载推荐商品 */
async function loadRecommendations() {
  const token = localStorage.getItem('token');
  if (!token) return;

  loading.value = true;
  try {
    // 1. 获取推荐商品 ID 列表（数量由后端决定）
    const userId = parseUserId(token);
    const recRes = await axios.get('/recommend', {
      params: { userId },
      headers: { 'token': token }
    });
    if (recRes.data.code !== 200) return;
    const recIds = (recRes.data.data || []).map(item => item.id);
    if (recIds.length === 0) return;

    // 2. 用 for 循环逐页加载商品数据，直到找齐所有推荐项
    const result = [];
    for (let page = 1; result.length < recIds.length; page++) {
      const res = await axios.post('/good/info', { page }, {
        headers: { 'Content-Type': 'application/json', 'token': token }
      });
      const list = res.data.data || [];
      if (list.length === 0) break; // 没有更多数据
      for (let i = 0; i < list.length; i++) {
        if (recIds.includes(list[i].id)) {
          result.push(list[i]);
        }
      }
    }

    // 3. 按推荐顺序排列
    data.value = recIds
      .map(id => result.find(item => item.id === id))
      .filter(Boolean);
  } catch (e) {
    console.error('获取推荐失败', e);
  } finally {
    loading.value = false;
  }
}

onMounted(() => {
  loadRecommendations();
});
</script>

<style scoped>
.container {
  width: 80%;
  max-width: 1200px;
  margin: 24px auto 0;
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

/* 页面标题 */
.page-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 24px;
  padding-bottom: 20px;
  border-bottom: 1px solid rgba(236, 72, 153, 0.1);
}

.page-title {
  margin: 0;
  font-size: 22px;
  font-weight: 600;
  background: linear-gradient(135deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.page-subtitle {
  margin: 0;
  font-size: 14px;
  color: #9ca3af;
}

.refresh-btn {
  margin-left: auto;
  padding: 8px 20px;
  font-size: 13px;
  font-weight: 500;
  color: #ec4899;
  background: rgba(255, 255, 255, 0.6);
  border: 1px solid rgba(236, 72, 153, 0.3);
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.refresh-btn:hover:not(:disabled) {
  background: rgba(236, 72, 153, 0.1);
  border-color: rgba(236, 72, 153, 0.5);
}

.refresh-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.spin {
  display: inline-block;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

/* 卡片网格 */
.card-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 20px;
  align-items: start;
}

/* 单个卡片 */
.card {
  position: relative;
  display: flex;
  flex-direction: column;
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(30px) saturate(180%);
  -webkit-backdrop-filter: blur(30px) saturate(180%);
  border: 1px solid rgba(255, 255, 255, 0.9);
  border-radius: 20px;
  text-decoration: none;
  color: #374151;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  height: 100%;
  overflow: hidden;
  box-shadow:
    0 8px 32px rgba(236, 72, 153, 0.06),
    inset 0 1px 1px rgba(255, 255, 255, 0.8);
}

.card:hover {
  transform: translateY(-4px);
  background: rgba(255, 255, 255, 0.65);
  box-shadow:
    0 16px 48px rgba(236, 72, 153, 0.1),
    inset 0 1px 1px rgba(255, 255, 255, 0.9);
}

.card-badge {
  position: absolute;
  top: 10px;
  left: 10px;
  padding: 4px 10px;
  font-size: 11px;
  font-weight: 600;
  color: #fff;
  background: linear-gradient(135deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
  border-radius: 8px;
  z-index: 1;
  box-shadow: 0 4px 12px rgba(236, 72, 153, 0.3);
}

/* 图片区域 */
.card-img {
  width: 100%;
  height: 150px;
  background: linear-gradient(135deg, rgba(236, 72, 153, 0.05) 0%, rgba(244, 114, 182, 0.05) 100%);
  flex-shrink: 0;
  overflow: hidden;
}

.card-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.4s ease;
}

.card:hover .card-img img {
  transform: scale(1.05);
}

/* 内容 */
.card-body {
  padding: 14px;
  background: transparent;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.title {
  font-size: 13px;
  font-weight: 500;
  margin: 0 0 8px;
  line-height: 1.4;
  min-height: 36px;
  color: #374151;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.price {
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin: 0 0 6px;
}

.desc {
  font-size: 12px;
  line-height: 1.5;
  color: #9ca3af;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  margin-top: auto;
}

/* 骨架屏 */
.skeleton {
  pointer-events: none;
}

.skeleton-img {
  background: linear-gradient(90deg,
    rgba(236, 72, 153, 0.05) 25%,
    rgba(236, 72, 153, 0.1) 50%,
    rgba(236, 72, 153, 0.05) 75%
  );
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
}

.skeleton-text {
  height: 12px;
  border-radius: 6px;
  margin-bottom: 8px;
  background: linear-gradient(90deg,
    rgba(236, 72, 153, 0.05) 25%,
    rgba(236, 72, 153, 0.1) 50%,
    rgba(236, 72, 153, 0.05) 75%
  );
  background-size: 200% 100%;
  animation: shimmer 1.5s infinite;
}

.skeleton-text.long { width: 80%; }
.skeleton-text.short { width: 40%; }
.skeleton-text.medium { width: 60%; }

@keyframes shimmer {
  0% { background-position: 200% 0; }
  100% { background-position: -200% 0; }
}

/* 空状态 */
.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #9ca3af;
}

.empty-state p {
  font-size: 15px;
  margin-bottom: 16px;
}

.empty-link {
  display: inline-block;
  padding: 10px 24px;
  font-size: 14px;
  font-weight: 500;
  color: #fff;
  background: linear-gradient(135deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
  border-radius: 12px;
  text-decoration: none;
  box-shadow: 0 4px 16px rgba(236, 72, 153, 0.25);
  transition: all 0.3s ease;
}

.empty-link:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(236, 72, 153, 0.3);
}

/* 响应式 */
@media (max-width: 1200px) {
  .card-grid { grid-template-columns: repeat(4, 1fr); }
}

@media (max-width: 1024px) {
  .card-grid { grid-template-columns: repeat(3, 1fr); }
}

@media (max-width: 768px) {
  .card-grid { grid-template-columns: repeat(2, 1fr); }
}

@media (max-width: 480px) {
  .card-grid { grid-template-columns: 1fr; }
}
</style>
