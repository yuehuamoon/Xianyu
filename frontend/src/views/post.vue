<template>
  <div>
    <div class="container">
      <!-- 帖子内容 -->
      <div class="post-card">
        <h1 class="post-title">{{ data.title }}</h1>
        <div class="post-meta">
          <span class="author">{{ data.userName }}</span>
          <span class="time">{{ formatTime(data.createTime) }}</span>
        </div>
        <div class="post-content">
          {{ data.content }}
        </div>
        <div class="post-images" v-if="parsedImages.length > 0">
          <img
            v-for="(img, idx) in parsedImages"
            :key="idx"
            :src="img"
            alt="帖子图片"
          />
        </div>
        <div class="post-stats">
          <span>👍 {{ data.likes }}</span>
          <span>⭐ {{ data.favorite }}</span>
        </div>
      </div>

      <!-- 评论区 -->
      <div class="comment-section">
        <h3 class="comment-title">评论区</h3>
        <CommentLayout :data="commentData" />
      </div>
    </div>
  </div>
</template>

<script setup>
import CommentLayout from '@/components/commentLayout.vue';
import axios from 'axios';
import { onMounted, ref, computed, reactive } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();



const data = ref({});
let data_to_children = reactive({})
const commentData = {
  id: route.params.id,
  type: 'post',
  data: data_to_children,
};

// 解析图片URL数组
const parsedImages = computed(() => {
  if (!data.value.imgUrls) return [];
  try {
    return JSON.parse(data.value.imgUrls);
  } catch {
    return [];
  }
});

// 格式化时间
function formatTime(time) {
  if (!time) return '';
  const date = new Date(time);
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
  });
}

async function getPost() {
  const res = await axios.post('/community/selectPostById', {
    page: route.params.id,
  });
  console.log(route.params.id);
  console.log(res.data);
  data.value = res.data.data;
  data_to_children = res.data.data;
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

.post-card {
  padding: 24px;
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(20px) saturate(180%);
  -webkit-backdrop-filter: blur(20px) saturate(180%);
  border: 1px solid rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  margin-bottom: 24px;
}

.post-title {
  font-size: 24px;
  font-weight: 500;
  color: #374151;
  margin: 0 0 16px;
  line-height: 1.4;
}

.post-meta {
  display: flex;
  gap: 16px;
  font-size: 14px;
  color: #6b7280;
  margin-bottom: 20px;
}

.author {
  font-weight: 500;
  color: #ec4899;
}

.time {
  color: #9ca3af;
}

.post-content {
  font-size: 16px;
  line-height: 1.8;
  color: #374151;
  margin-bottom: 20px;
  white-space: pre-wrap;
}

.post-images {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 12px;
  margin-bottom: 20px;
}

.post-images img {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 12px;
  background: rgba(236, 72, 153, 0.05);
}

.post-stats {
  display: flex;
  gap: 24px;
  font-size: 14px;
  color: #6b7280;
  padding-top: 16px;
  border-top: 1px solid rgba(236, 72, 153, 0.1);
}

.comment-section {
  padding: 24px;
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(20px) saturate(180%);
  -webkit-backdrop-filter: blur(20px) saturate(180%);
  border: 1px solid rgba(255, 255, 255, 0.9);
  border-radius: 16px;
}

.comment-title {
  font-size: 18px;
  font-weight: 500;
  color: #374151;
  margin: 0 0 16px;
}

/* 响应式 */
@media (max-width: 768px) {
  .container {
    width: 95%;
    padding: 16px;
  }

  .post-card,
  .comment-section {
    padding: 16px;
  }

  .post-title {
    font-size: 20px;
  }

  .post-images {
    grid-template-columns: 1fr;
  }
}
</style>