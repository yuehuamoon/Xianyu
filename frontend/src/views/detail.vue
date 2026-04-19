<template>
  <div class="product-page">
    <!-- 商品主体区域 -->
    <div class="product-card">
      <!-- 左侧：图片幻灯片 -->
      <div class="product-images">
        <div v-if="detail.imgUrl && detail.imgUrl.length" class="slider"> 
          <img :src="detail.imgUrl" alt="商品图片" />
        </div>
        <div v-else class="placeholder">
          <span>暂无图片</span>
        </div>
      </div>

      <!-- 右侧：商品信息 + 购买按钮 -->
      <div class="product-info" v-if="detail.name">
        <h2 class="title">{{ detail.name }}</h2>
        <p><strong>类型：</strong>{{ detail.type || '暂无' }}</p>
        <p><strong>价格：</strong>{{ detail.unitPrice }} 元</p>
        <p><strong>描述：</strong>{{ detail.content }}</p>
        <p> <router-link :to="`/otheruser/${detail.sellerId}`"> <strong>卖家：</strong>{{ detail.sellerId }} </router-link> </p>
        <div>
          <button class="buy-btn" @click="buyNow">立即购买</button>
          <button class="buy-btn" @click="toChat(detail.sellerId)">联系商家</button>
        </div>

        <p v-if="msg" class="msg">{{ msg }}</p>
      </div>

      <!-- 加载状态 -->
      <div class="product-info" v-else-if="msg">
        <p class="msg">{{ msg }}</p>
        <router-link to="/" class="back-link">返回首页</router-link>
      </div>

      <!-- 加载中 -->
      <div class="product-info" v-else>
        <p>加载中...</p>
      </div>
    </div>

    <!-- 评论区 -->
    <div class="comments">
      <h3>评论区</h3>

      <CommentLayout
      v-if="detail.name"
      :data="commentData"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import CommentLayout from '@/components/commentLayout.vue';


const route = useRoute();
const router = useRouter();
const token = localStorage.getItem('token');

const detail = reactive({});
const msg = ref('');
const page = ref(history.state.page || null);

const commentData = ref({
  id: history.state.page,
  type: 'good',
  data: detail,
});


function parseJwt(token) {
  try {
    const base64Payload = token.split('.')[1]; // JWT 中间段
    const payload = atob(base64Payload);      // Base64 解码
    return JSON.parse(payload);                // 转成对象
  } catch (e) {
    return null;
  }
}


async function buyNow() {
  if (!token) {
    msg.value = '请先登录';
    return;
  }
  
  try {
    const userInfo = parseJwt(token);
    console.log(userInfo)
    const res = await axios.post(
      `/deal/insert`,
      {
        'goodId': JSON.parse(JSON.stringify(detail)).id,
        'buyerId': userInfo.userId,
        'sellerId': JSON.parse(JSON.stringify(detail)).sellerId,
        'unitPrice': JSON.parse(JSON.stringify(detail)).unitPrice,
        'count': JSON.parse(JSON.stringify(detail)).count,
       },
      { headers: { Authorization: `Bearer ${token}`, 'token': token } }
    );
    console.log(res.data.msg);
    msg.value = res.data.msg;
  } catch (e) {
    msg.value = '购买失败';
  }
}

function toChat(userId){
  console.log(userId)
  router.push(`/chat/${userId}`)

}

onMounted(async () => {
  const token = localStorage.getItem('token');
  const goodId = history.state.page;

  console.log('Detail页面加载，商品ID:', goodId);

  if (!goodId) {
    msg.value = '商品ID不存在，请从商品列表进入';
    console.error('商品ID为空');
    return;
  }

  try {
    const res = await axios.post(
      `/good/goodID`,
      { page: goodId },
      { headers: { 'token': token } },
    );

    console.log('商品详情API响应:', res.data);

    if (res.data && res.data.data) {
      Object.assign(detail, res.data.data);
      console.log('商品详情加载成功:', detail);

      // 更新commentData的ID
      commentData.value.id = goodId;
    } else {
      msg.value = '商品不存在或已被删除';
      console.error('API响应数据格式错误:', res.data);
    }
  } catch (e) {
    console.error('获取商品详情失败:', e);
    msg.value = '获取商品详情失败，请稍后重试';
  }
});



</script>

<style scoped>
/* Liquid Glass Style - Based on Admin Dashboard Design */
.product-page {
  width: 80%;
  max-width: 1200px;
  margin: 24px auto;
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

/* 主卡片 */
.product-card {
  display: flex;
  gap: 24px;
  padding: 24px;
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(30px) saturate(180%);
  -webkit-backdrop-filter: blur(30px) saturate(180%);
  border: 1px solid rgba(255, 255, 255, 0.9);
  border-radius: 20px;
  box-shadow:
    0 8px 32px rgba(236, 72, 153, 0.06),
    inset 0 1px 1px rgba(255, 255, 255, 0.8);
}

/* 图片区域 */
.product-images {
  flex: 1;
  min-width: 280px;
  background: rgba(255, 255, 255, 0.4);
  border: 1px solid rgba(255, 255, 255, 0.8);
  border-radius: 16px;
  overflow: hidden;
}

.product-images img {
  width: 100%;
  display: block;
  object-fit: cover;
}

.placeholder {
  height: 280px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #6b7280;
  background: linear-gradient(135deg, rgba(236, 72, 153, 0.05) 0%, rgba(244, 114, 182, 0.05) 100%);
}

/* 商品信息 */
.product-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.product-info .title {
  font-size: 24px;
  font-weight: 300;
  letter-spacing: 0.02em;
  background: linear-gradient(135deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  padding-bottom: 8px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.5);
}

/* 信息文字 */
.product-info p {
  font-size: 15px;
  line-height: 1.6;
  color: #374151;
}

.product-info p strong {
  color: #ec4899;
}

/* 按钮区域 */
.product-info > div {
  margin-top: 16px;
  display: flex;
  gap: 16px;
}

/* 玻璃按钮 */
.buy-btn {
  background: linear-gradient(135deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
  color: #fff;
  border: none;
  padding: 14px 24px;
  font-size: 14px;
  font-weight: 500;
  letter-spacing: 0.02em;
  border-radius: 16px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 16px rgba(236, 72, 153, 0.25);
}

.buy-btn:hover {
  transform: translateY(-2px);
  box-shadow:
    0 12px 40px rgba(236, 72, 153, 0.25),
    0 4px 12px rgba(236, 72, 153, 0.15);
}

.buy-btn:active {
  transform: translateY(0);
}

/* 第二个按钮（联系商家） */
.buy-btn:nth-child(2) {
  background: rgba(255, 255, 255, 0.5);
  color: #374151;
  border: 1px solid rgba(255, 255, 255, 0.8);
  box-shadow: none;
}

.buy-btn:nth-child(2):hover {
  background: rgba(255, 255, 255, 0.7);
}

/* 提示信息 */
.msg {
  margin-top: 16px;
  padding: 12px 16px;
  background: rgba(239, 68, 68, 0.1);
  border: 1px solid rgba(239, 68, 68, 0.2);
  border-radius: 12px;
  color: #ef4444;
  font-size: 14px;
}

/* 评论区 */
.comments {
  margin-top: 32px;
  padding: 20px;
  background: rgba(255, 255, 255, 0.4);
  backdrop-filter: blur(20px) saturate(180%);
  -webkit-backdrop-filter: blur(20px) saturate(180%);
  border: 1px solid rgba(255, 255, 255, 0.8);
  border-radius: 20px;
}

.comments h3 {
  font-size: 18px;
  font-weight: 300;
  letter-spacing: 0.02em;
  margin-bottom: 16px;
  color: #374151;
  padding-bottom: 8px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.5);
}

.comment {
  padding: 12px 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.3);
  font-size: 14px;
  color: #374151;
}

.comment:last-child {
  border-bottom: none;
}

/* 返回链接样式 */
.back-link {
  display: inline-block;
  margin-top: 16px;
  padding: 12px 24px;
  background: linear-gradient(135deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
  color: #fff;
  text-decoration: none;
  border-radius: 16px;
  font-weight: 500;
  transition: all 0.3s ease;
  box-shadow: 0 4px 16px rgba(236, 72, 153, 0.25);
}

.back-link:hover {
  transform: translateY(-2px);
  box-shadow:
    0 12px 40px rgba(236, 72, 153, 0.25),
    0 4px 12px rgba(236, 72, 153, 0.15);
}

/* 响应式 */
@media (max-width: 768px) {
  .product-card {
    flex-direction: column;
  }
}
</style>
