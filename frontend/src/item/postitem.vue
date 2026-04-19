<template>
    <div class="post-item">
        <div class="post-header">
            <span class="user-name">👤 {{ data.userName }}</span>
            <span class="post-time">{{ formatTime(data.createTime) }}</span>
        </div>
        <h3 class="post-title">{{ data.title }}</h3>
        <p class="post-content">{{ data.content }}</p>
        <div class="post-images" v-if="parsedImgUrls.length">
            <img
                v-for="(url, index) in parsedImgUrls"
                :key="index"
                :src="url"
                :alt="data.title"
                class="post-image"
            />
        </div>
        <div class="post-footer">
            <span class="stat">
                <span class="icon">♥</span>
                {{ data.likes }}
            </span>
            <span class="stat">
                <span class="icon">★</span>
                {{ data.favorite }}
            </span>
            <span class="status-badge" :class="statusClass">
                {{ statusText }}
            </span>
        </div>
    </div>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  data: {
    type: Object,
    required: true,
    default: () => ({})
  }
})




const parsedImgUrls = computed(() => {
    try {
        return JSON.parse(props.data.imgUrls || '[]');
    } catch {
        return [];
    }
});

const statusClass = computed(() => {
    return props.data.reviewStatu === 1 ? 'approved' : 'pending';
});

const statusText = computed(() => {
    return props.data.reviewStatu === 1 ? '已审核' : '待审核';
});

function formatTime(time) {
    if (!time) return '';
    const date = new Date(time);
    return date.toLocaleString('zh-CN', {
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
    });
}
</script>

<style scoped>
/* Liquid Glass Style - Based on Admin Dashboard Design */
.post-item {
    background: rgba(255, 255, 255, 0.5);
    backdrop-filter: blur(30px) saturate(180%);
    -webkit-backdrop-filter: blur(30px) saturate(180%);
    border: 1px solid rgba(255, 255, 255, 0.9);
    border-radius: 20px;
    padding: 0;
    transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
    box-shadow:
        0 8px 32px rgba(236, 72, 153, 0.06),
        inset 0 1px 1px rgba(255, 255, 255, 0.8);
    overflow: hidden;
}

.post-item:hover {
    transform: translateY(-4px);
    background: rgba(255, 255, 255, 0.65);
    box-shadow:
        0 16px 48px rgba(236, 72, 153, 0.1),
        inset 0 1px 1px rgba(255, 255, 255, 0.9);
}

.post-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 14px 18px;
    border-bottom: 1px solid rgba(255, 255, 255, 0.5);
    background: rgba(255, 255, 255, 0.3);
}

.user-name {
    font-size: 13px;
    font-weight: 400;
    letter-spacing: 0.02em;
    color: #374151;
}

.post-time {
    font-size: 12px;
    font-weight: 400;
    letter-spacing: 0.02em;
    color: #6b7280;
}

.post-title {
    margin: 0;
    padding: 16px 18px 8px;
    font-size: 16px;
    font-weight: 500;
    letter-spacing: 0.02em;
    color: #374151;
    line-height: 1.4;
}

.post-content {
    margin: 0;
    padding: 0 18px 16px;
    font-size: 14px;
    font-weight: 400;
    letter-spacing: 0.02em;
    color: #6b7280;
    line-height: 1.6;
    display: -webkit-box;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
    overflow: hidden;
}

.post-images {
    display: flex;
    gap: 10px;
    flex-wrap: wrap;
    padding: 0 18px 16px;
}

.post-image {
    width: 80px;
    height: 80px;
    object-fit: cover;
    border-radius: 12px;
    border: 1px solid rgba(255, 255, 255, 0.8);
    transition: transform 0.3s ease;
}

.post-image:hover {
    transform: scale(1.05);
}

.post-footer {
    display: flex;
    align-items: center;
    gap: 16px;
    padding: 12px 18px;
    border-top: 1px solid rgba(255, 255, 255, 0.5);
    background: rgba(255, 255, 255, 0.3);
}

.stat {
    display: flex;
    align-items: center;
    gap: 6px;
    font-size: 13px;
    font-weight: 400;
    letter-spacing: 0.02em;
    color: #6b7280;
}

.icon {
    font-size: 14px;
    color: #ec4899;
}

.status-badge {
    padding: 4px 12px;
    border-radius: 10px;
    font-size: 12px;
    font-weight: 500;
    letter-spacing: 0.02em;
}

.status-badge.approved {
    background: linear-gradient(135deg, rgba(236, 72, 153, 0.2) 0%, rgba(244, 114, 182, 0.2) 100%);
    color: #ec4899;
    border: 1px solid rgba(236, 72, 153, 0.3);
}

.status-badge.pending {
    background: rgba(107, 114, 128, 0.1);
    color: #6b7280;
    border: 1px solid rgba(107, 114, 128, 0.2);
}
</style>
    
