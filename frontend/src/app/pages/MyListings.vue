<template>
  <div class="space-y-8">
    <!-- Header -->
    <div class="flex items-center justify-between">
      <div>
        <h1 class="text-foreground mb-2" style="font-size: 2rem; font-weight: 300; letter-spacing: -0.01em">My Listings</h1>
        <p class="text-muted-foreground text-sm" style="font-weight: 300">Manage your items for sale</p>
      </div>
      <button class="flex items-center gap-2 px-6 py-3 bg-gradient-to-r from-primary to-accent text-primary-foreground rounded-3xl hover:shadow-[0_8px_28px_rgba(232,180,168,0.3)] transition-all text-sm" style="font-weight: 400">
        <Plus class="w-4 h-4" :stroke-width="1.5" />
        New Listing
      </button>
    </div>

    <!-- Stats Cards -->
    <div class="grid grid-cols-2 md:grid-cols-4 gap-5">
      <button
        @click="filter = 'all'"
        :class="[
          'p-5 rounded-3xl border-2 transition-all duration-300 text-left',
          filter === 'all'
            ? 'bg-gradient-to-br from-primary/15 to-accent/5 border-[#D4AF7A]/40 shadow-[0_6px_24px_rgba(232,180,168,0.2)]'
            : 'bg-card border-[#D4AF7A]/15 shadow-[0_4px_20px_rgba(232,180,168,0.08)] hover:shadow-[0_6px_24px_rgba(232,180,168,0.15)]'
        ]"
      >
        <p class="text-muted-foreground text-xs mb-1.5" style="font-weight: 300">All Items</p>
        <p class="text-foreground" style="font-size: 1.75rem; font-weight: 300; letter-spacing: -0.02em">{{ stats.all }}</p>
      </button>

      <button
        @click="filter = 'active'"
        :class="[
          'p-5 rounded-3xl border-2 transition-all duration-300 text-left',
          filter === 'active'
            ? 'bg-gradient-to-br from-primary/15 to-accent/5 border-[#D4AF7A]/40 shadow-[0_6px_24px_rgba(232,180,168,0.2)]'
            : 'bg-card border-[#D4AF7A]/15 shadow-[0_4px_20px_rgba(232,180,168,0.08)] hover:shadow-[0_6px_24px_rgba(232,180,168,0.15)]'
        ]"
      >
        <p class="text-muted-foreground text-xs mb-1.5" style="font-weight: 300">Active</p>
        <p class="text-foreground" style="font-size: 1.75rem; font-weight: 300; letter-spacing: -0.02em">{{ stats.active }}</p>
      </button>

      <button
        @click="filter = 'sold'"
        :class="[
          'p-5 rounded-3xl border-2 transition-all duration-300 text-left',
          filter === 'sold'
            ? 'bg-gradient-to-br from-primary/15 to-accent/5 border-[#D4AF7A]/40 shadow-[0_6px_24px_rgba(232,180,168,0.2)]'
            : 'bg-card border-[#D4AF7A]/15 shadow-[0_4px_20px_rgba(232,180,168,0.08)] hover:shadow-[0_6px_24px_rgba(232,180,168,0.15)]'
        ]"
      >
        <p class="text-muted-foreground text-xs mb-1.5" style="font-weight: 300">Sold</p>
        <p class="text-foreground" style="font-size: 1.75rem; font-weight: 300; letter-spacing: -0.02em">{{ stats.sold }}</p>
      </button>

      <button
        @click="filter = 'pending'"
        :class="[
          'p-5 rounded-3xl border-2 transition-all duration-300 text-left',
          filter === 'pending'
            ? 'bg-gradient-to-br from-primary/15 to-accent/5 border-[#D4AF7A]/40 shadow-[0_6px_24px_rgba(232,180,168,0.2)]'
            : 'bg-card border-[#D4AF7A]/15 shadow-[0_4px_20px_rgba(232,180,168,0.08)] hover:shadow-[0_6px_24px_rgba(232,180,168,0.15)]'
        ]"
      >
        <p class="text-muted-foreground text-xs mb-1.5" style="font-weight: 300">Pending</p>
        <p class="text-foreground" style="font-size: 1.75rem; font-weight: 300; letter-spacing: -0.02em">{{ stats.pending }}</p>
      </button>
    </div>

    <!-- Search and Filters -->
    <div class="bg-card rounded-3xl p-5 border-2 border-[#D4AF7A]/20 shadow-[0_4px_20px_rgba(232,180,168,0.08)]">
      <div class="flex flex-col sm:flex-row gap-4">
        <div class="flex-1 relative">
          <Search class="absolute left-4 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground" :stroke-width="1.5" />
          <input
            type="text"
            placeholder="Search your listings..."
            v-model="searchTerm"
            class="w-full pl-11 pr-4 py-3 bg-[#F9F3EF] border border-transparent rounded-2xl focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary/30 focus:shadow-[0_4px_16px_rgba(232,180,168,0.15)] transition-all text-sm"
            style="font-weight: 300"
          />
        </div>
        <div class="flex gap-3">
          <button class="flex items-center gap-2 px-5 py-3 bg-muted/40 hover:bg-muted/60 rounded-2xl hover:shadow-[0_2px_12px_rgba(232,180,168,0.15)] transition-all text-sm" style="font-weight: 400">
            <Filter class="w-4 h-4" :stroke-width="1.5" />
            Filter
          </button>
          <button class="p-3 bg-muted/40 hover:bg-muted/60 rounded-2xl hover:shadow-[0_2px_12px_rgba(232,180,168,0.15)] transition-all">
            <Grid3x3 class="w-4 h-4" :stroke-width="1.5" />
          </button>
          <button class="p-3 bg-muted/40 hover:bg-muted/60 rounded-2xl hover:shadow-[0_2px_12px_rgba(232,180,168,0.15)] transition-all">
            <List class="w-4 h-4" :stroke-width="1.5" />
          </button>
        </div>
      </div>
    </div>

    <!-- Listings Grid -->
    <div v-if="filteredListings.length > 0" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <ItemCard v-for="item in filteredListings" :key="item.id" :item="item" type="listing" />
    </div>
    <div v-else class="bg-card rounded-3xl p-16 border-2 border-[#D4AF7A]/20 shadow-[0_4px_20px_rgba(232,180,168,0.08)] text-center">
      <div class="w-20 h-20 mx-auto mb-5 rounded-3xl bg-gradient-to-br from-muted/30 to-secondary/20 flex items-center justify-center shadow-[0_2px_12px_rgba(232,180,168,0.08)]">
        <Search class="w-9 h-9 text-muted-foreground" :stroke-width="1.5" />
      </div>
      <h3 class="text-foreground mb-2" style="font-weight: 400">No listings found</h3>
      <p class="text-muted-foreground text-sm mb-8" style="font-weight: 300">
        Try adjusting your search or filter criteria
      </p>
      <button
        @click="clearFilters"
        class="px-7 py-3 bg-primary/10 text-primary rounded-2xl hover:bg-primary/20 hover:shadow-[0_4px_16px_rgba(232,180,168,0.2)] transition-all text-sm"
        style="font-weight: 400"
      >
        Clear Filters
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { Plus, Filter, Grid3x3, List, Search } from 'lucide-vue-next'
import ItemCard from '../components/ItemCard.vue'

const allListings = [
  { id: 1, image: "vintage camera", title: "Vintage Film Camera", price: 145, status: "active", views: 234, likes: 18 },
  { id: 2, image: "leather jacket", title: "Brown Leather Jacket", price: 89, status: "sold", views: 456, likes: 32 },
  { id: 3, image: "ceramic vase", title: "Handmade Ceramic Vase", price: 52, status: "active", views: 128, likes: 9 },
  { id: 4, image: "vintage typewriter", title: "Classic Typewriter", price: 178, status: "active", views: 312, likes: 24 },
  { id: 5, image: "retro sunglasses", title: "Retro Round Sunglasses", price: 34, status: "sold", views: 189, likes: 15 },
  { id: 6, image: "wooden chair", title: "Mid-Century Dining Chair", price: 125, status: "active", views: 267, likes: 21 },
  { id: 7, image: "vintage watch", title: "Classic Wristwatch", price: 220, status: "pending", views: 445, likes: 38 },
  { id: 8, image: "art print", title: "Abstract Art Print", price: 45, status: "active", views: 156, likes: 12 },
  { id: 9, image: "vinyl records", title: "Vintage Vinyl Collection", price: 95, status: "sold", views: 378, likes: 29 },
]

const filter = ref('all')
const searchTerm = ref('')

const filteredListings = computed(() => {
  return allListings.filter((item) => {
    const matchesFilter = filter.value === 'all' || item.status === filter.value
    const matchesSearch = item.title.toLowerCase().includes(searchTerm.value.toLowerCase())
    return matchesFilter && matchesSearch
  })
})

const stats = computed(() => ({
  all: allListings.length,
  active: allListings.filter((i) => i.status === 'active').length,
  sold: allListings.filter((i) => i.status === 'sold').length,
  pending: allListings.filter((i) => i.status === 'pending').length,
}))

const clearFilters = () => {
  filter.value = 'all'
  searchTerm.value = ''
}
</script>
