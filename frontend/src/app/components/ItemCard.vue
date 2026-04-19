<template>
  <!-- Listing card -->
  <div v-if="type === 'listing'"
    class="rounded-[2rem] backdrop-blur-2xl border border-white/60 shadow-[0_8px_32px_rgba(255,179,217,0.12)] overflow-hidden group hover:shadow-[0_16px_64px_rgba(255,179,217,0.25)] hover:border-white/80 transition-all duration-500"
    style="background: linear-gradient(135deg, rgba(255, 255, 255, 0.7) 0%, rgba(255, 230, 242, 0.5) 100%)"
  >
    <!-- Image -->
    <div class="relative aspect-square bg-gradient-to-br from-white/40 to-secondary/30 overflow-hidden">
      <!-- Glossy overlay on image -->
      <div 
        class="absolute inset-0 z-10 opacity-30 pointer-events-none"
        style="background: linear-gradient(135deg, rgba(255, 255, 255, 0.6) 0%, transparent 50%, rgba(255, 255, 255, 0.3) 100%)"
      ></div>
      <ImageWithFallback
        :src="`https://source.unsplash.com/400x400/?${encodeURIComponent(item.image)}`"
        :alt="item.goodname"
        className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-700"
      />
      <div class="absolute top-4 right-4 z-20">
        <span 
          class="text-xs px-3 py-1.5 rounded-full backdrop-blur-xl border border-white/60"
          :style="{ background: statusConfig[item.status]?.bg || statusConfig.active.bg, color: statusConfig[item.status]?.text || statusConfig.active.text }"
        >
          {{ statusConfig[item.status]?.label || statusConfig.active.label }}
        </span>
      </div>
      <div v-if="item.status === 'active'" class="absolute inset-0 z-20 bg-gradient-to-t from-white/60 via-transparent to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-500 flex items-center justify-center backdrop-blur-sm">
        <div class="flex gap-3">
          <button 
            class="w-11 h-11 backdrop-blur-xl rounded-3xl flex items-center justify-center hover:scale-110 transition-all shadow-[0_8px_24px_rgba(255,179,217,0.3)] border border-white/70"
            style="background: linear-gradient(135deg, rgba(255, 255, 255, 0.9) 0%, rgba(255, 255, 255, 0.7) 100%)"
          >
            <Edit2 class="w-4 h-4 text-primary" :stroke-width="2" />
          </button>
          <button 
            class="w-11 h-11 backdrop-blur-xl rounded-3xl flex items-center justify-center hover:scale-110 transition-all shadow-[0_8px_24px_rgba(255,179,217,0.3)] border border-white/70"
            style="background: linear-gradient(135deg, rgba(255, 255, 255, 0.9) 0%, rgba(255, 255, 255, 0.7) 100%)"
          >
            <Trash2 class="w-4 h-4 text-destructive" :stroke-width="2" />
          </button>
        </div>
      </div>
    </div>

    <!-- Content -->
    <div class="p-5 relative">
      <!-- Glossy highlight -->
      <div 
        class="absolute top-0 left-0 right-0 h-8 opacity-40 pointer-events-none"
        style="background: linear-gradient(180deg, rgba(255, 255, 255, 0.5) 0%, transparent 100%)"
      ></div>
      <h3 class="text-foreground mb-2 text-sm line-clamp-1">{{ item.goodname }}</h3>
      <p class="text-primary text-lg mb-4">${{ item.unitprice }}</p>

      <!-- Stats -->
      <div class="flex items-center justify-between text-xs text-muted-foreground pt-4 border-t border-white/40">
        <div class="flex items-center gap-1.5">
          <Eye class="w-3.5 h-3.5" :stroke-width="2" />
          <span>{{ item.views }}</span>
        </div>
        <div class="flex items-center gap-1.5">
          <Heart class="w-3.5 h-3.5" :stroke-width="2" />
          <span>{{ item.likes }}</span>
        </div>
        <button class="text-primary hover:underline text-xs">Manage →</button>
      </div>
    </div>
  </div>

  <!-- Purchase card -->
  <div v-else
    class="rounded-[2rem] backdrop-blur-2xl border border-white/60 shadow-[0_8px_32px_rgba(255,179,217,0.12)] p-5 hover:shadow-[0_16px_64px_rgba(255,179,217,0.25)] hover:border-white/80 transition-all duration-500"
    style="background: linear-gradient(135deg, rgba(255, 255, 255, 0.7) 0%, rgba(255, 230, 242, 0.5) 100%)"
  >
    <div class="flex gap-5">
      <!-- Image -->
      <div class="w-28 h-28 flex-shrink-0 rounded-3xl bg-gradient-to-br from-white/40 to-secondary/30 overflow-hidden shadow-[0_4px_24px_rgba(255,179,217,0.15)] relative">
        <!-- Glossy overlay -->
        <div 
          class="absolute inset-0 z-10 opacity-30 pointer-events-none"
          style="background: linear-gradient(135deg, rgba(255, 255, 255, 0.6) 0%, transparent 50%, rgba(255, 255, 255, 0.3) 100%)"
        ></div>
        <ImageWithFallback
          :src="`https://source.unsplash.com/200x200/?${encodeURIComponent(item.image)}`"
          :alt="item.goodname"
          className="w-full h-full object-cover"
        />
      </div>

      <!-- Content -->
      <div class="flex-1 min-w-0">
        <div class="flex items-start justify-between mb-2">
          <h3 class="text-foreground text-sm line-clamp-1">{{ item.goodname }}</h3>
          <span 
            class="text-xs px-3 py-1.5 rounded-full backdrop-blur-xl border border-white/60"
            :style="{ background: statusConfig[item.status]?.bg || statusConfig.active.bg, color: statusConfig[item.status]?.text || statusConfig.active.text }"
          >
            {{ statusConfig[item.status]?.label || statusConfig.active.label }}
          </span>
        </div>
        <p class="text-primary mb-2">${{ item.unitprice }}</p>
        <p class="text-xs text-muted-foreground mb-4">Ordered on {{ item.date }}</p>
        
        <div class="flex gap-2">
          <button 
            class="flex-1 px-4 py-2.5 backdrop-blur-xl rounded-3xl hover:shadow-[0_4px_20px_rgba(255,179,217,0.2)] transition-all text-xs border border-white/60"
            style="background: linear-gradient(135deg, rgba(255, 179, 217, 0.3) 0%, rgba(255, 179, 217, 0.2) 100%)"
          >
            View Details
          </button>
          <button v-if="item.status === 'delivered'"
            class="flex-1 px-4 py-2.5 backdrop-blur-xl rounded-3xl hover:shadow-[0_4px_20px_rgba(255,179,217,0.15)] transition-all text-xs border border-white/60"
            style="background: linear-gradient(135deg, rgba(255, 255, 255, 0.5) 0%, rgba(255, 255, 255, 0.3) 100%)"
          >
            Leave Review
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Eye, Heart, Edit2, Trash2 } from 'lucide-vue-next'
import ImageWithFallback from './figma/ImageWithFallback.vue'

const props = defineProps<{
  item: {
    id: number
    image: string
    goodname: string
    unitprice: number
    status: string
    views?: number
    likes?: number
    date?: string
  }
  type: 'listing' | 'purchase'
}>()

const statusConfig: Record<string, { label: string; bg: string; text: string }> = {
  active: { label: "Active", bg: "rgba(134, 239, 172, 0.3)", text: "#16a34a" },
  sold: { label: "Sold", bg: "rgba(209, 213, 219, 0.3)", text: "#6b7280" },
  pending: { label: "Pending", bg: "rgba(253, 224, 71, 0.3)", text: "#ca8a04" },
  delivered: { label: "Delivered", bg: "rgba(134, 239, 172, 0.3)", text: "#16a34a" },
  in_transit: { label: "In Transit", bg: "rgba(147, 197, 253, 0.3)", text: "#2563eb" },
  cancelled: { label: "Cancelled", bg: "rgba(252, 165, 165, 0.3)", text: "#dc2626" },
}
</script>
