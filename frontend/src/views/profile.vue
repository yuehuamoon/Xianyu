<template>
  <div class="card">
    <!-- 优化：个人信息一行展示 -->
    <div class="profile-header">
      <h2>个人主页</h2>
      <!-- 可补充具体个人信息（如账号、头像），保持一行布局 -->
      <div class="profile-info">
        <span class="account">{{ userInfo?.nickName || '当前用户' }}</span>
        <span class="divider">|</span>
        <span class="update-time">最后更新：{{ new Date().toLocaleString() }}</span>
      </div>
    </div>


    <!-- 核心：已购/卖出物品两列布局，各自带滚动条小框 -->
    <div class="goods-container">
      <!-- 左侧：我发布的（卖出物品） -->
      <div class="goods-column">
        <div class="column-title">我发布的</div>
        <div class="goods-list scroll-box">
          <router-link
            v-for="p in myGoods"
            :key="p.Id || p.id"
            :to="{ path: 'detail', state: { page: p.id } }"
            class="goods-item"
          >
            <span class="goods-title">{{ p.name }}</span>
            <span class="goods-price">¥{{ p.unitPrice }}</span>
            <span class="goods-type">{{ p.label }}</span>
          </router-link>
        </div>
      </div>

      <!-- 右侧：我购买的（已购物品） -->
      <div class="goods-column">
        <div class="column-title">我购买的</div>
        <div class="goods-list scroll-box">
          <router-link
            v-for="r in myRecords"
            :key="r.goodId || r.id"
            :to="{ path: 'detail', state: { page: r.goodId } }"
            class="goods-item"
          >          
            <span class="goods-title">{{ r.goodName }}</span>
            <span class="goods-price">¥{{ r.unitPrice }}</span>
            <span class="goods-type">{{ r.label }}</span>
          </router-link>
        </div>
      </div>
    </div>

    <!-- 刷新按钮 -->
    <button @click="loadAll" class="refresh-btn">刷新</button>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';
import CommentLayout from '@/components/commentLayout.vue';

const apiUrl = import.meta.env.VITE_API_URL;
const token = localStorage.getItem('token');


const myGoods = ref([]);
const myRecords = ref([]);
// 新增：存储用户信息，用于一行展示
let userInfo = ref(null);

function parseJwt(token) {
  try {
    const base64Payload = token.split('.')[1]; // JWT 中间段
    const payload = atob(base64Payload);      // Base64 解码
    return JSON.parse(payload);                // 转成对象
  } catch (e) {
    return null;
  }
}

async function loadAll() {
  if (!token) return;

  const userinfo = parseJwt(token);
  userInfo = userinfo; // 赋值用户信息

  // 我发布的
  try {
    const goodsRes = await axios.post(
      `/good/selectByUserId`,
      { 'id': userinfo.userId },
      { headers: { Authorization: `Bearer ${token}`, 'token': token } }
    );
    myGoods.value = goodsRes.data.data;
  } catch (err) {
    console.error('获取发布商品失败：', err);
  }

  // 我购买的（交易记录）
  try {
    const goodsRes = await axios.post(
      `/deal/selectByBuyerID`,
      { 'buyerId': userinfo.userId, },
      { headers: { Authorization: `Bearer ${token}`, 'token': token } }
    );

    myRecords.value = goodsRes.data.data;
  } catch (err) {
    console.error('获取购买记录失败：', err);
  }
}

onMounted(loadAll);
</script>

<style scoped>
/* Liquid Glass Style - Based on Admin Dashboard Design */
.card {
  width: 80%;
  max-width: 1200px;
  margin: 24px auto;
  padding: 28px;
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(30px) saturate(180%);
  -webkit-backdrop-filter: blur(30px) saturate(180%);
  border: 1px solid rgba(255, 255, 255, 0.9);
  border-radius: 24px;
  box-shadow:
    0 8px 32px rgba(236, 72, 153, 0.06),
    inset 0 1px 1px rgba(255, 255, 255, 0.8);
}

/* 顶部个人信息 */
.profile-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  margin-bottom: 24px;
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(20px) saturate(180%);
  -webkit-backdrop-filter: blur(20px) saturate(180%);
  border: 1px solid rgba(255, 255, 255, 0.9);
  border-radius: 16px;
}

.profile-header h2 {
  margin: 0;
  font-size: 20px;
  font-weight: 300;
  letter-spacing: 0.02em;
  background: linear-gradient(135deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.profile-info {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #6b7280;
}

.divider {
  opacity: 0.4;
}

/* 两列容器 */
.goods-container {
  display: flex;
  gap: 24px;
  margin-bottom: 24px;
}

/* 单列 */
.goods-column {
  flex: 1;
  background: rgba(255, 255, 255, 0.4);
  backdrop-filter: blur(20px) saturate(180%);
  -webkit-backdrop-filter: blur(20px) saturate(180%);
  border: 1px solid rgba(255, 255, 255, 0.8);
  border-radius: 20px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

/* 列标题 */
.column-title {
  padding: 14px;
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  text-align: center;
  background: linear-gradient(135deg, rgba(236, 72, 153, 0.1) 0%, rgba(244, 114, 182, 0.1) 100%);
  color: #374151;
  border-bottom: 1px solid rgba(255, 255, 255, 0.5);
}

/* 滚动区域 */
.scroll-box {
  height: 400px;
  padding: 16px;
  overflow-y: auto;
  overflow-x: hidden;
  scrollbar-width: thin;
  scrollbar-color: rgba(236, 72, 153, 0.2) transparent;
}

.scroll-box::-webkit-scrollbar {
  width: 6px;
}

.scroll-box::-webkit-scrollbar-track {
  background: transparent;
}

.scroll-box::-webkit-scrollbar-thumb {
  background: rgba(236, 72, 153, 0.2);
  border-radius: 3px;
}

/* 商品列表 */
.goods-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

/* 商品项 */
.goods-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 14px 16px;
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  background: rgba(255, 255, 255, 0.5);
  color: #374151;
  text-decoration: none;
  border: 1px solid rgba(255, 255, 255, 0.8);
  border-radius: 12px;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.goods-item:hover {
  background: rgba(255, 255, 255, 0.65);
  transform: translateX(4px);
}

/* 商品字段 */
.goods-title {
  flex: 2;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  font-weight: 500;
}

.goods-price {
  flex: 1;
  text-align: center;
  font-weight: 500;
  background: linear-gradient(135deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.goods-type {
  flex: 1;
  text-align: right;
  opacity: 0.6;
  font-size: 12px;
}

/* 刷新按钮 */
.refresh-btn {
  display: block;
  margin: 0 auto;
  padding: 14px 32px;
  background: linear-gradient(135deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
  color: #fff;
  font-size: 14px;
  font-weight: 500;
  letter-spacing: 0.02em;
  border: none;
  border-radius: 16px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 16px rgba(236, 72, 153, 0.25);
}

.refresh-btn:hover {
  transform: translateY(-2px);
  box-shadow:
    0 12px 40px rgba(236, 72, 153, 0.25),
    0 4px 12px rgba(236, 72, 153, 0.15);
}

.refresh-btn:active {
  transform: translateY(0);
}

/* 响应式 */
@media (max-width: 768px) {
  .goods-container {
    flex-direction: column;
  }
}
</style>
