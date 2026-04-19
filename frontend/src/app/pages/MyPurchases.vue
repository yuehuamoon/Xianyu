<template>
  <div class="space-y-8">
    <!-- Header -->
    <div>
      <h1 class="text-foreground mb-2" style="font-size: 2rem; font-weight: 300; letter-spacing: -0.01em">My Purchases</h1>
      <p class="text-muted-foreground text-sm" style="font-weight: 300">Track your orders and purchase history</p>
    </div>

    <!-- Stats Overview -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-5">
      <div class="bg-card rounded-3xl p-6 border-2 border-[#D4AF7A]/15 shadow-[0_4px_20px_rgba(232,180,168,0.08)] hover:shadow-[0_6px_28px_rgba(232,180,168,0.15)] transition-all">
        <div class="flex items-center gap-3 mb-2">
          <div class="w-11 h-11 rounded-2xl bg-gradient-to-br from-primary/15 to-accent/10 flex items-center justify-center shadow-[0_2px_8px_rgba(232,180,168,0.1)]">
            <Package class="w-5 h-5 text-primary" :stroke-width="1.5" />
          </div>
          <div>
            <p class="text-muted-foreground text-xs" style="font-weight: 300">Total Orders</p>
            <p class="text-foreground" style="font-size: 1.5rem; font-weight: 300; letter-spacing: -0.02em">{{ stats.total }}</p>
          </div>
        </div>
      </div>

      <div class="bg-card rounded-3xl p-6 border-2 border-[#D4AF7A]/15 shadow-[0_4px_20px_rgba(232,180,168,0.08)] hover:shadow-[0_6px_28px_rgba(232,180,168,0.15)] transition-all">
        <div class="flex items-center gap-3 mb-2">
          <div class="w-11 h-11 rounded-2xl bg-green-50 flex items-center justify-center shadow-[0_2px_8px_rgba(134,239,172,0.15)]">
            <CheckCircle class="w-5 h-5 text-green-600" :stroke-width="1.5" />
          </div>
          <div>
            <p class="text-muted-foreground text-xs" style="font-weight: 300">Delivered</p>
            <p class="text-foreground" style="font-size: 1.5rem; font-weight: 300; letter-spacing: -0.02em">{{ stats.delivered }}</p>
          </div>
        </div>
      </div>

      <div class="bg-card rounded-3xl p-6 border-2 border-[#D4AF7A]/15 shadow-[0_4px_20px_rgba(232,180,168,0.08)] hover:shadow-[0_6px_28px_rgba(232,180,168,0.15)] transition-all">
        <div class="flex items-center gap-3 mb-2">
          <div class="w-11 h-11 rounded-2xl bg-blue-50 flex items-center justify-center shadow-[0_2px_8px_rgba(147,197,253,0.15)]">
            <Clock class="w-5 h-5 text-blue-600" :stroke-width="1.5" />
          </div>
          <div>
            <p class="text-muted-foreground text-xs" style="font-weight: 300">In Transit</p>
            <p class="text-foreground" style="font-size: 1.5rem; font-weight: 300; letter-spacing: -0.02em">{{ stats.in_transit }}</p>
          </div>
        </div>
      </div>

      <div class="bg-card rounded-3xl p-6 border-2 border-[#D4AF7A]/15 shadow-[0_4px_20px_rgba(232,180,168,0.08)] hover:shadow-[0_6px_28px_rgba(232,180,168,0.15)] transition-all">
        <div class="flex items-center gap-3 mb-2">
          <div class="w-11 h-11 rounded-2xl bg-gradient-to-br from-primary/15 to-accent/10 flex items-center justify-center shadow-[0_2px_8px_rgba(232,180,168,0.1)]">
            <TrendingUp class="w-5 h-5 text-primary" :stroke-width="1.5" />
          </div>
          <div>
            <p class="text-muted-foreground text-xs" style="font-weight: 300">Total Spent</p>
            <p class="text-foreground" style="font-size: 1.5rem; font-weight: 300; letter-spacing: -0.02em">${{ stats.totalSpent }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- Filter Tabs -->
    <div class="bg-card rounded-3xl p-2 border-2 border-[#D4AF7A]/15 shadow-[0_4px_20px_rgba(232,180,168,0.08)] inline-flex gap-2">
      <button class="px-5 py-2.5 bg-gradient-to-r from-primary/15 to-accent/10 text-primary rounded-2xl text-sm border border-[#D4AF7A]/20 shadow-[0_2px_8px_rgba(232,180,168,0.1)]" style="font-weight: 400">
        All Orders
      </button>
      <button class="px-5 py-2.5 hover:bg-muted/30 text-foreground rounded-2xl text-sm transition-all hover:shadow-[0_2px_8px_rgba(232,180,168,0.08)]" style="font-weight: 300">
        In Transit
      </button>
      <button class="px-5 py-2.5 hover:bg-muted/30 text-foreground rounded-2xl text-sm transition-all hover:shadow-[0_2px_8px_rgba(232,180,168,0.08)]" style="font-weight: 300">
        Delivered
      </button>
      <button class="px-5 py-2.5 hover:bg-muted/30 text-foreground rounded-2xl text-sm transition-all hover:shadow-[0_2px_8px_rgba(232,180,168,0.08)]" style="font-weight: 300">
        Cancelled
      </button>
    </div>

    <!-- Purchases List -->
    <div class="space-y-4">
      <ItemCard v-for="item in purchases" :key="item.id" :item="item" type="purchase" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { Package, TrendingUp, Clock, CheckCircle } from 'lucide-vue-next'
import ItemCard from '../components/ItemCard.vue'

const purchases = [
  { id: 1, image: "desk lamp", title: "Modern Desk Lamp", price: 38, status: "delivered", date: "Mar 20, 2026" },
  { id: 2, image: "vintage book", title: "Classic Literature Set", price: 65, status: "in_transit", date: "Mar 24, 2026" },
  { id: 3, image: "coffee maker", title: "Vintage Coffee Maker", price: 82, status: "delivered", date: "Mar 15, 2026" },
  { id: 4, image: "throw pillow", title: "Handwoven Throw Pillows", price: 45, status: "delivered", date: "Mar 10, 2026" },
  { id: 5, image: "wall art", title: "Vintage Wall Art", price: 95, status: "in_transit", date: "Mar 26, 2026" },
  { id: 6, image: "plant pot", title: "Ceramic Plant Pot Set", price: 38, status: "delivered", date: "Mar 8, 2026" },
]

const stats = computed(() => ({
  total: purchases.length,
  delivered: purchases.filter((p) => p.status === "delivered").length,
  in_transit: purchases.filter((p) => p.status === "in_transit").length,
  totalSpent: purchases.reduce((sum, p) => sum + p.price, 0),
}))
</script>
