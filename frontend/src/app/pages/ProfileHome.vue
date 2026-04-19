<template>
  <div class="space-y-10">
    <!-- Personal Info Card - Liquid Glass -->
    <div 
      class="rounded-[2.5rem] p-10 backdrop-blur-2xl border border-white/60 shadow-[0_16px_64px_rgba(255,179,217,0.15)] relative overflow-hidden"
      style="background: linear-gradient(135deg, rgba(255, 255, 255, 0.7) 0%, rgba(255, 230, 242, 0.5) 100%)"
    >
      <!-- Glossy highlight -->
      <div 
        class="absolute top-0 left-0 right-0 h-40 opacity-50 pointer-events-none"
        style="background: linear-gradient(180deg, rgba(255, 255, 255, 0.6) 0%, transparent 100%)"
      ></div>
      
      <div class="flex items-start justify-between mb-8 relative z-10">
        <div>
          <h2 class="text-foreground mb-2 text-2xl">个人信息</h2>
          <p class="text-muted-foreground text-sm">管理你的个人主页和账号信息</p>
        </div>
        <button 
          class="flex items-center gap-2 px-5 py-2.5 backdrop-blur-xl rounded-3xl hover:shadow-[0_8px_32px_rgba(255,179,217,0.25)] transition-all text-sm border border-white/60"
          style="background: linear-gradient(135deg, rgba(255, 179, 217, 0.3) 0%, rgba(255, 179, 217, 0.2) 100%)"
        >
          <Edit2 class="w-4 h-4" :stroke-width="2" />
          编辑
        </button>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-2 gap-6 relative z-10">
        <!-- Left Column -->
        <div class="space-y-4">
          <div 
            class="flex items-center gap-3 p-5 rounded-3xl backdrop-blur-xl shadow-[0_4px_24px_rgba(255,179,217,0.1)] border border-white/60"
            style="background: linear-gradient(135deg, rgba(255, 255, 255, 0.6) 0%, rgba(255, 255, 255, 0.3) 100%)"
          >
            <div 
              class="w-11 h-11 rounded-3xl backdrop-blur-xl flex items-center justify-center shadow-[0_4px_16px_rgba(255,179,217,0.15)] border border-white/60"
              style="background: linear-gradient(135deg, rgba(255, 179, 217, 0.3) 0%, rgba(255, 179, 217, 0.2) 100%)"
            >
              <Mail class="w-5 h-5 text-primary" :stroke-width="2" />
            </div>
            <div>
              <p class="text-xs text-muted-foreground mb-0.5">邮箱地址</p>
              <p class="text-sm text-foreground">emma.thompson@email.com</p>
            </div>
          </div>

          <div 
            class="flex items-center gap-3 p-5 rounded-3xl backdrop-blur-xl shadow-[0_4px_24px_rgba(255,179,217,0.1)] border border-white/60"
            style="background: linear-gradient(135deg, rgba(255, 255, 255, 0.6) 0%, rgba(255, 255, 255, 0.3) 100%)"
          >
            <div 
              class="w-11 h-11 rounded-3xl backdrop-blur-xl flex items-center justify-center shadow-[0_4px_16px_rgba(255,179,217,0.15)] border border-white/60"
              style="background: linear-gradient(135deg, rgba(255, 179, 217, 0.3) 0%, rgba(255, 179, 217, 0.2) 100%)"
            >
              <MapPin class="w-5 h-5 text-primary" :stroke-width="2" />
            </div>
            <div>
              <p class="text-xs text-muted-foreground mb-0.5">地址</p>
              <p class="text-sm text-foreground">南极</p>
            </div>
          </div>

          <div 
            class="flex items-center gap-3 p-5 rounded-3xl backdrop-blur-xl shadow-[0_4px_24px_rgba(255,179,217,0.1)] border border-white/60"
            style="background: linear-gradient(135deg, rgba(255, 255, 255, 0.6) 0%, rgba(255, 255, 255, 0.3) 100%)"
          >
            <div 
              class="w-11 h-11 rounded-3xl backdrop-blur-xl flex items-center justify-center shadow-[0_4px_16px_rgba(255,179,217,0.15)] border border-white/60"
              style="background: linear-gradient(135deg, rgba(255, 179, 217, 0.3) 0%, rgba(255, 179, 217, 0.2) 100%)"
            >
              <Calendar class="w-5 h-5 text-primary" :stroke-width="2" />
            </div>
            <div>
              <p class="text-xs text-muted-foreground mb-0.5">加入于</p>
              <p class="text-sm text-foreground">January 2024</p>
            </div>
          </div>
        </div>

        <!-- Right Column -->
        <div class="space-y-4">
          <div 
            class="flex items-center gap-3 p-5 rounded-3xl backdrop-blur-xl shadow-[0_4px_24px_rgba(255,179,217,0.1)] border border-white/60"
            style="background: linear-gradient(135deg, rgba(255, 255, 255, 0.6) 0%, rgba(255, 255, 255, 0.3) 100%)"
          >
            <div 
              class="w-11 h-11 rounded-3xl backdrop-blur-xl flex items-center justify-center shadow-[0_4px_16px_rgba(255,179,217,0.15)] border border-white/60"
              style="background: linear-gradient(135deg, rgba(255, 179, 217, 0.3) 0%, rgba(255, 179, 217, 0.2) 100%)"
            >
              <Shield class="w-5 h-5 text-primary" :stroke-width="2" />
            </div>
            <div class="flex-1">
              <p class="text-xs text-muted-foreground mb-0.5">账号安全</p>
              <p class="text-sm text-foreground">Verified</p>
            </div>
            <span 
              class="text-xs px-3 py-1.5 rounded-full backdrop-blur-xl border border-white/60"
              style="background: rgba(134, 239, 172, 0.3); color: #16a34a"
            >
              Active
            </span>
          </div>

          <div 
            class="flex items-center gap-3 p-5 rounded-3xl backdrop-blur-xl shadow-[0_4px_24px_rgba(255,179,217,0.1)] border border-white/60"
            style="background: linear-gradient(135deg, rgba(255, 255, 255, 0.6) 0%, rgba(255, 255, 255, 0.3) 100%)"
          >
            <div 
              class="w-11 h-11 rounded-3xl backdrop-blur-xl flex items-center justify-center shadow-[0_4px_16px_rgba(255,179,217,0.15)] border border-white/60"
              style="background: linear-gradient(135deg, rgba(255, 179, 217, 0.3) 0%, rgba(255, 179, 217, 0.2) 100%)"
            >
              <Award class="w-5 h-5 text-primary" :stroke-width="2" />
            </div>
            <div>
              <p class="text-xs text-muted-foreground mb-0.5">销售评分</p>
              <p class="text-sm text-foreground">4.8 / 5.0 (42 reviews)</p>
            </div>
          </div>

          <div 
            class="flex items-center gap-3 p-5 rounded-3xl backdrop-blur-xl shadow-[0_4px_24px_rgba(255,179,217,0.1)] border border-white/60"
            style="background: linear-gradient(135deg, rgba(255, 255, 255, 0.6) 0%, rgba(255, 255, 255, 0.3) 100%)"
          >
            <div 
              class="w-11 h-11 rounded-3xl backdrop-blur-xl flex items-center justify-center shadow-[0_4px_16px_rgba(255,179,217,0.15)] border border-white/60"
              style="background: linear-gradient(135deg, rgba(255, 179, 217, 0.3) 0%, rgba(255, 179, 217, 0.2) 100%)"
            >
              <TrendingUp class="w-5 h-5 text-primary" :stroke-width="2" />
            </div>
            <div>
              <p class="text-xs text-muted-foreground mb-0.5">全部售出</p>
              <p class="text-sm text-foreground">2,345 元(28 件)</p>
            </div>
          </div>
        </div>
      </div>

      <!-- Bio Section -->
      <div class="mt-8 pt-8 border-t border-white/40 relative z-10">
        <h3 class="text-foreground mb-3 text-sm">关于我</h3>
        <p class="text-sm text-foreground/80 leading-relaxed">
          Hi! I'm Emma, a vintage enthusiast and sustainable fashion advocate. I love finding unique pieces and giving them a second life. All my items are carefully curated and well-maintained. Feel free to reach out if you have any questions!
        </p>
      </div>
    </div>

    <!-- Stats Overview - Glass cards -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-6">
      <div 
        v-for="(stat, idx) in stats" 
        :key="idx"
        class="rounded-[2rem] p-6 backdrop-blur-2xl border border-white/60 shadow-[0_8px_32px_rgba(255,179,217,0.12)] text-center hover:shadow-[0_16px_64px_rgba(255,179,217,0.2)] hover:scale-105 transition-all duration-300"
        style="background: linear-gradient(135deg, rgba(255, 255, 255, 0.7) 0%, rgba(255, 230, 242, 0.5) 100%)"
      >
        <p class="text-muted-foreground text-xs mb-2">{{ stat.label }}</p>
        <p class="text-foreground text-3xl font-light">{{ stat.value }}</p>
      </div>
    </div>

    <!-- Recent Listings -->
    <div 
      class="rounded-[2.5rem] p-10 backdrop-blur-2xl border border-white/60 shadow-[0_16px_64px_rgba(255,179,217,0.15)] relative overflow-hidden"
      style="background: linear-gradient(135deg, rgba(255, 255, 255, 0.7) 0%, rgba(255, 230, 242, 0.5) 100%)"
    >
      <div class="flex items-center justify-between mb-8">
        <div>
          <h2 class="text-foreground mb-1 text-xl">最近上架</h2>
          <p class="text-muted-foreground text-sm">你的最新上架物品</p>
        </div>
        <button class="text-primary text-sm hover:underline">查看全部 →</button>
      </div>
      <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
        <ItemCard v-for="item in recentListings" :key="item.id" :item="item" type="listing" />
      </div>
    </div>

    <!-- Recent Purchases -->
    <div 
      class="rounded-[2.5rem] p-10 backdrop-blur-2xl border border-white/60 shadow-[0_16px_64px_rgba(255,179,217,0.15)] relative overflow-hidden"
      style="background: linear-gradient(135deg, rgba(255, 255, 255, 0.7) 0%, rgba(255, 230, 242, 0.5) 100%)"
    >
      <div class="flex items-center justify-between mb-8">
        <div>
          <h2 class="text-foreground mb-1 text-xl">最近购买</h2>
          <p class="text-muted-foreground text-sm">你最近购买的物品</p>
        </div>
        <button class="text-primary text-sm hover:underline">查看全部 →</button>
      </div>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <ItemCard v-for="item in recentPurchases" :key="item.id" :item="item" type="purchase" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { Mail, MapPin, Calendar, Shield, Award, TrendingUp, Edit2 } from 'lucide-vue-next'
import ItemCard from '@/app/components/ItemCard.vue'
import axios from 'axios'

const token = localStorage.getItem('token');

const myGoods = ref([]);
const myRecords = ref([]);
// 新增：存储用户信息，用于一行展示
const userInfo = ref(null);

const stats = [
  { label: "正在售出", value: "12" },
  { label: "已售出", value: "28" },
  { label: "全部浏览", value: "3.2K" },
  { label: "喜欢", value: "45" },
]

const recentListings = [
  {
    goodId: 1,
    image: "vintage camera",
    goodname: "Vintage Film Camera",
    unitprice: 145,
    status: "active",
    views: 234,
    likes: 18,
  },
]

const recentPurchases = []

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
  userInfo.value = userinfo.userId; // 赋值用户信息

  // 我购买的（交易记录）
  try {
    const res = await axios.post(
      `/deal/selectByBuyerID`,
      { 'buyerId': userinfo.userId },
      { headers: { Authorization: `Bearer ${token}`, 'token': token } }
    );
    
    for (let i of res.data.data){
      recentPurchases.push(i)
      console.log(recentPurchases)
    }

  } catch (err) {
    console.error('获取购买记录失败：', err);
  }
}

onMounted(loadAll)
</script>
