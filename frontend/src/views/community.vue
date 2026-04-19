<template>
  <div>
    <div class="container">
      <h2 class="page-title">社区帖子</h2>
      <div class="post-grid">
          <Postitem
            v-for="item in posts"
            :key="item.id"
            :data="item"
            @click="toPost(item.id)"
          />
      </div>

      <!-- 触底哨兵 -->
      <div ref="loadMoreRef" class="load-more">
        <p v-if="loading">加载中...</p>
        <p v-if="finished">已加载全部</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import axios from 'axios';
import { onMounted, ref } from 'vue';
import Postitem from '@/item/postitem.vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const posts = ref([]);
const loading = ref(false);
const finished = ref(false);
const loadMoreRef = ref(null);

async function getPost() {
  if (loading.value || finished.value) return;
  loading.value = true;

  try {
    const res = await axios.post('/community/selectPost');
    const list = res.data.data;

    if (!list || list.length === 0) {
      finished.value = true;
    } else {
      posts.value = list;
    }
  } catch (e) {
    console.error(e);
  } finally {
    loading.value = false;
  }
}

function toPost(num){
  router.push(`/post/${num}`)
}
onMounted(getPost);
</script>

<style scoped>
/* Liquid Glass Style - Based on Admin Dashboard Design */
.container {
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

.page-title {
  margin: 0 0 24px;
  font-size: 22px;
  font-weight: 300;
  letter-spacing: 0.02em;
  color: #374151;
  padding: 16px;
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(20px) saturate(180%);
  -webkit-backdrop-filter: blur(20px) saturate(180%);
  border: 1px solid rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  text-align: center;
}

.post-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

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
@media (max-width: 768px) {
  .post-grid {
    grid-template-columns: 1fr;
  }
}
</style>