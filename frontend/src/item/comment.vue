<template>
  <div class="comment-card">

    <!-- 头部：头像 + 用户名 + 时间（一行） -->
    <div class="header">
      <div class="avatar">
        <img src="" alt="avatar" />
      </div>
      <span class="username">{{ data.userName }}</span>
      <span class="time">{{ data.updateTime }}</span>
    </div>

    <!-- 内容 -->
    <div class="content">
      {{ data.content }}
    </div>

    <!-- 底部 -->
    <div class="footer">
      <span>{{ data.ip }}</span>
      <span><a @click="like">👍 {{ data.likes }}</a></span>
      <span><a @click="favorite">⭐ {{ data.favorite }}</a></span>
    </div>

  </div>
</template>

<script setup>
const props = defineProps({
  data: {
    type: Object,
    required: true,
    default: () => ({})
  }
})


function like(){
  console.log("1")
}

function favorite(){

}
</script>

<style scoped>
/* Liquid Glass Style - Based on Admin Dashboard Design */
.comment-card {
  width: 100%;
  padding: 16px 18px;
  box-sizing: border-box;
  background: rgba(255, 255, 255, 0.55);
  border-radius: 16px;
  border: 2px solid rgba(236, 72, 153, 0.12);
  box-shadow:
    0 4px 16px rgba(236, 72, 153, 0.06),
    0 1px 4px rgba(236, 72, 153, 0.04),
    inset 0 1px 2px rgba(255, 255, 255, 0.8);
  transition: all 0.3s ease;
  margin-bottom: 12px;
}

.comment-card:hover {
  background: rgba(255, 255, 255, 0.7);
  border-color: rgba(236, 72, 153, 0.25);
  box-shadow:
    0 8px 24px rgba(236, 72, 153, 0.1),
    0 2px 8px rgba(236, 72, 153, 0.06),
    inset 0 1px 2px rgba(255, 255, 255, 0.9);
  transform: translateY(-2px);
}

.comment-card:last-child {
  margin-bottom: 0;
}

/* 头部一行布局 */
.header {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 10px;
  margin-bottom: 8px;
  font-size: 14px;
}

/* 时间强制靠右 */
.time {
  margin-left: auto;
  color: #6b7280;
  font-size: 12px;
  font-weight: 400;
  letter-spacing: 0.02em;
}

/* 头像 */
.avatar img {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  object-fit: cover;
  background: rgba(255, 255, 255, 0.5);
  border: 2px solid rgba(236, 72, 153, 0.2);
}

/* 用户名 */
.username {
  font-weight: 500;
  letter-spacing: 0.02em;
  color: #374151;
}

/* 内容 */
.content {
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #374151;
  line-height: 1.6;
  margin-bottom: 10px;
}

/* 底部 */
.footer {
  display: flex;
  gap: 16px;
  font-size: 12px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #6b7280;
}

.footer a {
  cursor: pointer;
  transition: color 0.3s ease;
}

.footer a:hover {
  color: #ec4899;
}
</style>