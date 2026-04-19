<template>
  <div class="category-container">
    <div class="container">
      <!-- 分类标签栏 -->
      <div class="category-bar">
        <button
          v-for="cat in categoryList"
          :key="cat.key"
          :class="['category-btn', { active: currentType === cat.key }]"
          @click="switchCategory(cat.key)"
        >
          {{ cat.name }}
        </button>
      </div>

      <!-- 标题 -->
      <div class="category-header">
        <h2 class="category-title">
          {{ currentName }}
        </h2>
        <p class="result-count" v-if="!loading && data.length > 0">
          找到 {{ data.length }} 个商品
        </p>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading && data.length === 0" class="loading-state">
        <p>加载中...</p>
      </div>

      <!-- 无结果状态 -->
      <div v-else-if="!loading && data.length === 0" class="empty-state">
        <p>该分类暂无商品</p>
        <router-link to="/" class="back-link">返回首页</router-link>
      </div>

      <!-- 商品卡片 -->
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
              <img :src="item.imgUrl" alt="商品图" />
            </div>
            <div class="card-body">
              <h3 class="title">{{ item.name }}</h3>
              <p class="price">￥{{ item.unitPrice }}</p>
              <p class="desc">{{ item.content }}</p>
            </div>
          </router-link>
        </div>

        <div ref="loadMoreRef" class="load-more">
          <p v-if="loading">加载中...</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const router = useRouter()

const categoryList = [
  { key: '', name: '全部' },
  { key: 'daily', name: '生活用品' },
  { key: 'electronic', name: '电子产品' },
  { key: 'clothing', name: '服饰鞋帽' },
  { key: 'book', name: '图书文具' },
  { key: 'other', name: '其他物品' },
  { key: 'graduation', name: '毕业季闲置' },
]

const categoryNameMap = {
  daily: '生活用品',
  electronic: '电子产品',
  clothing: '服饰鞋帽',
  book: '图书文具',
  other: '其他物品',
  graduation: '毕业季闲置',
}

const currentType = computed(() => route.params.type || '')
const currentName = computed(() => categoryNameMap[currentType.value] || '全部分类')

const allProducts = ref([])
const data = ref([])
const loading = ref(false)

async function fetchAll() {
  if (loading.value) return
  loading.value = true
  const token = localStorage.getItem('token')

  try {
    const res = await axios.post(
      `/good/selectAll`,
      {},
      {
        headers: {
          'Content-Type': 'application/json',
          'token': token,
        }
      }
    )

    const list = res.data?.data || []
    allProducts.value = list

    // 按分类筛选
    filterByCategory()
  } catch (e) {
    console.error('加载分类商品失败:', e)
  } finally {
    loading.value = false
  }
}

function switchCategory(key) {
  if (key === '') {
    router.push('/category')
  } else {
    router.push(`/category/${key}`)
  }
}

onMounted(() => {
  fetchAll()
})

function filterByCategory() {
  data.value = currentType.value
    ? allProducts.value.filter(item => item.label === currentName.value)
    : allProducts.value
}

watch(currentType, () => {
  filterByCategory()
})
</script>

<style scoped>
.category-container {
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

.category-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  padding: 12px 16px;
  background: rgba(255, 255, 255, 0.4);
  border-radius: 12px;
  flex-wrap: wrap;
}

.category-btn {
  padding: 8px 18px;
  font-size: 13px;
  font-weight: 500;
  color: #6b7280;
  background: rgba(255, 255, 255, 0.6);
  border: 1px solid rgba(236, 72, 153, 0.2);
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.category-btn:hover {
  background: rgba(236, 72, 153, 0.1);
  border-color: rgba(236, 72, 153, 0.4);
  color: #ec4899;
}

.category-btn.active {
  background: linear-gradient(135deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
  color: #fff;
  border-color: transparent;
  box-shadow: 0 4px 12px rgba(236, 72, 153, 0.3);
}

.category-header {
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid rgba(236, 72, 153, 0.2);
}

.category-title {
  font-size: 24px;
  font-weight: 600;
  background: linear-gradient(135deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin: 0 0 8px;
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

.card-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  align-items: start;
}

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

.card-body {
  padding: 16px;
  background: transparent;
  flex: 1;
  display: flex;
  flex-direction: column;
}

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

.price {
  font-size: 18px;
  font-weight: 600;
  background: linear-gradient(135deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin: 0 0 8px;
}

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

.load-more {
  height: 80px;
  margin-top: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  color: #6b7280;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 16px;
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
