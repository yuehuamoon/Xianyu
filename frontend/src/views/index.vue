<template>
  <div>
    <div class="container">
      <!-- 筛选按钮区域 -->
      <div class="filter-bar">
        <span class="filter-label">排序：</span>
        <button
          :class="['filter-btn', { active: sortType === 'id' }]"
          @click="setSort('id')"
        >
          按序号
        </button>
        <button
          :class="['filter-btn', { active: sortType === 'priceAsc' }]"
          @click="setSort('priceAsc')"
        >
          价格升序
        </button>
        <button
          :class="['filter-btn', { active: sortType === 'priceDesc' }]"
          @click="setSort('priceDesc')"
        >
          价格降序
        </button>
      </div>

      <div class="card-grid">
        <router-link
          v-for="(item, idx) in sortedData"
          :key="idx"
          class="card"
          :to="{
            path: '/detail',
            state: { page: item.id }
          }"
        >
          <div class="card-img">
            <img
            :src= "item.imgUrl"
            alt="商品图"
          />
          </div>


          <div class="card-body">
            <h3 class="title">{{ item.name }}</h3>
            <p class="price">￥{{ item.unitPrice }}</p>
            <p class="desc">{{ item.content }}</p>
          </div>
        </router-link>
      </div>

          <!-- 👇 触底哨兵 -->
      <div ref="loadMoreRef" class="load-more">
        <p v-if="loading">加载中...</p>
        <p v-if="finished">已加载全部</p>
      </div>
    </div>


    <footer class="footer">

    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue';
import axios from 'axios';
import defaultImg from '@/assets/img/good.png'


const apiUrl = import.meta.env.VITE_API_URL;
const data      = ref([]);     // 列表数据
const page      = ref(1);      // 当前页码
const loading   = ref(false);  // 加载中
const finished  = ref(false);  // 全部加载完
const loadMoreRef = ref(null)
const sortType  = ref('id');   // 排序类型：id, priceAsc, priceDesc
let observer

/* 排序后的数据 */
const sortedData = computed(() => {
  const list = [...data.value];
  switch (sortType.value) {
    case 'priceAsc':
      return list.sort((a, b) => a.unitPrice - b.unitPrice);
    case 'priceDesc':
      return list.sort((a, b) => b.unitPrice - a.unitPrice);
    case 'id':
    default:
      return list.sort((a, b) => a.id - b.id);
  }
})

/* 设置排序类型 */
function setSort(type) {
  sortType.value = type;
}
/* 加载一页数据 */
async function loadPage() {
  if (loading.value || finished.value) return;
  loading.value = true;
  const token = localStorage.getItem('token')
  try {
    const res = await axios.post(
      `/good/info`,
      { page: page.value },              // ← JSON 体只放 page
      {
        headers: {
          'Content-Type': 'application/json',
          'token': token,
        }
      }
    );
    const list = res.data.data;          // 假设返回数组
    if (list.length === 0) finished.value = true;
    else {
      data.value.push(...list);     // 追加
      page.value++;                 // 下一页
    }
  } catch (e) {
    console.error(e);
  } finally {
    loading.value = false;
  }
}


const getLocalImg = (imgUrl) => {
  return new URL(imgUrl.value, import.meta.url).href
}


/* 滚动到底部触发 */
function onWindowScroll() {
  const scrollTop =
    window.pageYOffset || document.documentElement.scrollTop
  const clientHeight = document.documentElement.clientHeight
  const scrollHeight = document.documentElement.scrollHeight

  if (scrollTop + clientHeight >= scrollHeight - 20) {
    loadPage()
  }

  
}

onMounted(() => {
  loadPage()
  window.addEventListener('scroll', onWindowScroll)

  observer = new IntersectionObserver(
    ([entry]) => {
      if (entry.isIntersecting) {
        loadPage()
      }
    },
    {
      root: null,      // window
      threshold: 0.1,
    }
  )

  if (loadMoreRef.value) {
    observer.observe(loadMoreRef.value)
  }
})

onUnmounted(() => {
  window.removeEventListener('scroll', onWindowScroll)
  observer && observer.disconnect()
})
</script>

<style scoped>
/* Liquid Glass Style - Based on Admin Dashboard Design */
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

/* 筛选按钮区域 */
.filter-bar {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
  padding: 12px 16px;
  background: rgba(255, 255, 255, 0.4);
  border-radius: 12px;
  flex-wrap: wrap;
}

.filter-label {
  font-size: 14px;
  font-weight: 500;
  color: #374151;
}

.filter-btn {
  padding: 8px 16px;
  font-size: 13px;
  font-weight: 500;
  color: #6b7280;
  background: rgba(255, 255, 255, 0.6);
  border: 1px solid rgba(236, 72, 153, 0.2);
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.filter-btn:hover {
  background: rgba(236, 72, 153, 0.1);
  border-color: rgba(236, 72, 153, 0.4);
  color: #ec4899;
}

.filter-btn.active {
  background: linear-gradient(135deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
  color: #fff;
  border-color: transparent;
  box-shadow: 0 4px 12px rgba(236, 72, 153, 0.3);
}

/* 卡片网格 */
.card-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  align-items: start;
}

/* 单个卡片 */
.card {
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

/* 图片区域 */
.card-img {
  width: 100%;
  height: 160px;
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
  padding: 16px;
  background: transparent;
  flex: 1;
  display: flex;
  flex-direction: column;
}

/* 标题 */
.title {
  font-size: 14px;
  font-weight: 500;
  margin: 0 0 10px;
  line-height: 1.4;
  min-height: 40px;
  color: #374151;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

/* 价格 */
.price {
  font-size: 18px;
  font-weight: 600;
  background: linear-gradient(135deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin: 0 0 8px;
}

/* 描述 */
.desc {
  font-size: 12px;
  line-height: 1.5;
  color: #6b7280;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  margin-top: auto;
}

/* 触底加载区域 */
.load-more {
  height: 80px;
  margin-top: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #6b7280;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 16px;
}

/* 响应式 */
@media (max-width: 1024px) {
  .card-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 768px) {
  .card-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 480px) {
  .card-grid {
    grid-template-columns: 1fr;
  }
}
</style>