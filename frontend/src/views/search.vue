<template>
  <div class="search-container">
    <div class="container">
      <!-- 搜索标题 -->
      <div class="search-header">
        <h2 class="search-title">
          搜索结果: <span class="keyword">"{{ searchKeyword }}"</span>
        </h2>
        <p class="result-count" v-if="!loading && data.length > 0">
          找到 {{ data.length }} 个商品
        </p>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading && data.length === 0" class="loading-state">
        <p>搜索中...</p>
      </div>

      <!-- 无结果状态 -->
      <div v-else-if="!loading && data.length === 0" class="empty-state">
        <p>未找到相关商品</p>
        <router-link to="/" class="back-link">返回首页</router-link>
      </div>

      <!-- 搜索结果 - 完全模拟index.vue的卡片效果 -->
      <div v-else>
        <div class="card-grid">
          <router-link
            v-for="(item, idx) in data"
            :key="idx"
            class="card"
            :to="{
              path: '/detail',
              state: { page: item.id }
            }"
          >
            <div class="card-img">
              <img
                :src="item.imgUrl"
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

        <!-- 触底加载 -->
        <div ref="loadMoreRef" class="load-more">
          <p v-if="loading">加载中...</p>
          <p v-if="finished">已加载全部</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'

const route = useRoute()

// 完全模拟index.vue的数据结构
const data = ref([])
const page = ref(1)
const loading = ref(false)
const finished = ref(false)
const searchKeyword = ref('')
const loadMoreRef = ref(null)
let observer = null

// 搜索函数 - 在search.vue中直接发送请求
async function loadPage() {
  if (loading.value || finished.value) return

  const keyword = route.query.keyword || ''
  if (!keyword) return

  loading.value = true
  const token = localStorage.getItem('token')

  try {
    console.log(`发送搜索请求: keyword="${keyword}", page=${page.value}`)

    const res = await axios.post(
      `/search/good`,
      {
        page: page.value,
        type: 'good',
        keyword: keyword
      },
      {
        headers: {
          'Content-Type': 'application/json',
          'token': token,
        }
      }
    )

    console.log('收到响应:', res)

    const list = res.data || []

    console.log('处理后的数据:', list)
    console.log('数据长度:', list.length)

    // 追加数据 - 完全模拟index.vue的逻辑
    data.value.push(...list)

    if (list.length === 0) {
      finished.value = true
    } else {
      page.value++
    }

  } catch (e) {
    console.error('搜索请求失败:', e)
  } finally {
    loading.value = false
  }
}

// 初始化搜索
onMounted(() => {
  console.log('search.vue 组件已挂载')
  console.log('当前路由参数:', route.query)

  const keyword = route.query.keyword || ''
  searchKeyword.value = keyword

  if (keyword) {
    loadPage()
  }

  // 设置IntersectionObserver - 完全模拟index.vue
  observer = new IntersectionObserver(
    ([entry]) => {
      if (entry.isIntersecting) {
        loadPage()
      }
    },
    {
      root: null,
      threshold: 0.1,
    }
  )

  if (loadMoreRef.value) {
    observer.observe(loadMoreRef.value)
    console.log('已设置滚动监听')
  }
})

// 监听路由参数变化
watch(
  () => route.query.keyword,
  (newKeyword, oldKeyword) => {
    console.log('关键词变化:', oldKeyword, '->', newKeyword)
    if (newKeyword && newKeyword !== oldKeyword) {
      // 重置数据并重新搜索
      data.value = []
      page.value = 1
      finished.value = false
      searchKeyword.value = newKeyword
      loadPage()
    }
  }
)

onUnmounted(() => {
  if (observer) {
    observer.disconnect()
  }
})
</script>

<style scoped>
/* 完全复制index.vue的样式 */
.search-container {
  width: 100%;
  min-height: calc(100vh - 100px);
}

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

.search-header {
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid rgba(236, 72, 153, 0.2);
}

.search-title {
  font-size: 24px;
  font-weight: 600;
  color: #374151;
  margin: 0 0 8px;
}

.keyword {
  background: linear-gradient(135deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.result-count {
  font-size: 14px;
  color: #6b7280;
  margin: 0;
}

.loading-state,
.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #6b7280;
  font-size: 16px;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.back-link {
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

/* 卡片网格 - 完全复制index.vue */
.card-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  align-items: start;
}

/* 单个卡片 - 完全复制index.vue */
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

/* 图片区域 - 完全复制index.vue */
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

/* 内容 - 完全复制index.vue */
.card-body {
  padding: 16px;
  background: transparent;
  flex: 1;
  display: flex;
  flex-direction: column;
}

/* 标题 - 完全复制index.vue */
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

/* 价格 - 完全复制index.vue */
.price {
  font-size: 18px;
  font-weight: 600;
  background: linear-gradient(135deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin: 0 0 8px;
}

/* 描述 - 完全复制index.vue */
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

/* 触底加载区域 - 完全复制index.vue */
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

/* 响应式 - 完全复制index.vue */
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
