<template>
  <div class="space-y-8">
    <!-- Header -->
    <div class="flex items-center justify-between">
      <div>
        <h1 class="text-foreground mb-2" style="font-size: 2rem; font-weight: 300; letter-spacing: -0.01em">My Favorites</h1>
        <p class="text-muted-foreground text-sm" style="font-weight: 300">Items you've saved for later</p>
      </div>
      <div class="flex items-center gap-2 text-sm text-muted-foreground">
        <Heart class="w-4 h-4 fill-primary text-primary" :stroke-width="1.5" />
        <span style="font-weight: 300">{{ favorites.length }} items saved</span>
      </div>
    </div>

    <!-- Favorites Grid -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <div
        v-for="item in favorites"
        :key="item.id"
        class="bg-card rounded-3xl border-2 border-[#D4AF7A]/15 shadow-[0_4px_20px_rgba(232,180,168,0.08)] overflow-hidden group hover:shadow-[0_8px_32px_rgba(232,180,168,0.2)] hover:border-[#D4AF7A]/30 transition-all duration-500"
      >
        <!-- Image -->
        <div class="relative aspect-square bg-gradient-to-br from-muted/20 to-secondary/10 overflow-hidden">
          <ImageWithFallback
            :src="`https://source.unsplash.com/400x400/?${encodeURIComponent(item.image)}`"
            :alt="item.title"
            className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-700"
          />
          
          <!-- Remove from Favorites Button -->
          <button class="absolute top-4 right-4 w-10 h-10 bg-white/95 backdrop-blur-sm rounded-2xl flex items-center justify-center hover:bg-destructive hover:text-destructive-foreground transition-all shadow-[0_4px_16px_rgba(0,0,0,0.15)]">
            <X class="w-4 h-4" :stroke-width="1.5" />
          </button>

          <!-- Hover Overlay -->
          <div class="absolute inset-0 bg-gradient-to-t from-black/40 via-transparent to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-500 flex items-end p-5">
            <button class="w-full py-3 bg-white/95 backdrop-blur-sm text-foreground rounded-2xl hover:bg-primary hover:text-primary-foreground transition-all text-sm shadow-[0_4px_16px_rgba(0,0,0,0.15)]" style="font-weight: 400">
              View Item
            </button>
          </div>
        </div>

        <!-- Content -->
        <div class="p-5">
          <h3 class="text-foreground mb-1 text-sm line-clamp-1" style="font-weight: 400; letter-spacing: 0.01em">{{ item.title }}</h3>
          <p class="text-primary mb-3" style="font-size: 1.25rem; font-weight: 400">${{ item.price }}</p>
          
          <div class="flex items-center justify-between pt-3 border-t border-[#D4AF7A]/10">
            <p class="text-xs text-muted-foreground" style="font-weight: 300">by {{ item.seller }}</p>
            <p class="text-xs text-muted-foreground" style="font-weight: 300">{{ item.addedDate }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- Actions -->
    <div v-if="favorites.length > 0" class="flex justify-center gap-4">
      <button class="px-7 py-3 bg-card border-2 border-[#D4AF7A]/20 text-foreground rounded-3xl hover:shadow-[0_4px_20px_rgba(232,180,168,0.15)] transition-all text-sm" style="font-weight: 400">
        Clear All
      </button>
      <button class="px-7 py-3 bg-gradient-to-r from-primary to-accent text-primary-foreground rounded-3xl hover:shadow-[0_8px_28px_rgba(232,180,168,0.35)] transition-all text-sm" style="font-weight: 400">
        Add All to Cart
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Heart, X } from 'lucide-vue-next'
import ImageWithFallback from '../components/figma/ImageWithFallback.vue'

const favorites = [
  { id: 1, image: "vintage record player", title: "Vintage Record Player", price: 245, seller: "RetroVibes", addedDate: "2 days ago" },
  { id: 2, image: "silk scarf", title: "Designer Silk Scarf", price: 68, seller: "LuxuryFinds", addedDate: "1 week ago" },
  { id: 3, image: "brass candlestick", title: "Antique Brass Candlesticks", price: 85, seller: "VintageHome", addedDate: "3 days ago" },
  { id: 4, image: "leather bag", title: "Leather Messenger Bag", price: 128, seller: "UrbanStyle", addedDate: "5 days ago" },
  { id: 5, image: "ceramic bowl", title: "Handmade Ceramic Bowl Set", price: 92, seller: "ArtisanCraft", addedDate: "1 day ago" },
  { id: 6, image: "vintage mirror", title: "Ornate Gold Mirror", price: 165, seller: "DecorTreasures", addedDate: "4 days ago" },
]
</script>
