<template>
  <div class="min-h-screen">
    <!-- Bokeh Background -->
    <BokehBackground />

    <!-- Top Navigation Bar - Frosted Glass -->
    <header class="h-16 sticky top-0 z-50">
      <div 
        class="h-full backdrop-blur-2xl bg-white/40 border-b border-white/60 shadow-[0_8px_32px_rgba(255,179,217,0.12)]"
        style="background: linear-gradient(180deg, rgba(255, 255, 255, 0.7) 0%, rgba(255, 255, 255, 0.5) 100%)"
      >
        <div class="h-full max-w-[1600px] mx-auto px-8 flex items-center justify-between">
          <!-- Logo with glass effect -->
          <div class="flex items-center gap-3">
            <div 
              class="w-10 h-10 rounded-3xl backdrop-blur-xl flex items-center justify-center shadow-[0_8px_32px_rgba(255,179,217,0.3)] border border-white/60"
              style="background: linear-gradient(135deg, rgba(255, 255, 255, 0.8) 0%, rgba(255, 179, 217, 0.3) 100%)"
            >
              <div class="w-4 h-4 bg-primary/60 rounded-md" style="image-rendering: pixelated" />
            </div>
            <h1 class="text-foreground tracking-wide">SecondLife</h1>
          </div>

          <!-- Search Bar - Glass effect -->
          <div class="flex-1 max-w-xl mx-12">
            <div class="relative">
              <Search class="absolute left-4 top-1/2 -translate-y-1/2 w-4 h-4 text-muted-foreground/70" />
              <input
                type="text"
                placeholder="Search for items..."
                class="w-full pl-11 pr-4 py-2.5 backdrop-blur-xl bg-white/50 border border-white/60 rounded-3xl focus:outline-none focus:ring-2 focus:ring-primary/30 focus:border-primary/40 focus:shadow-[0_8px_32px_rgba(255,179,217,0.2)] transition-all text-sm placeholder:text-muted-foreground/50"
                style="background: linear-gradient(135deg, rgba(255, 255, 255, 0.6) 0%, rgba(255, 255, 255, 0.4) 100%)"
              />
            </div>
          </div>

          <!-- Right Actions with glass -->
          <div class="flex items-center gap-4">
            <button 
              class="w-11 h-11 rounded-3xl backdrop-blur-xl flex items-center justify-center transition-all hover:shadow-[0_8px_32px_rgba(255,179,217,0.25)] relative border border-white/60"
              style="background: linear-gradient(135deg, rgba(255, 255, 255, 0.6) 0%, rgba(255, 255, 255, 0.3) 100%)"
            >
              <Bell class="w-4 h-4 text-foreground" :stroke-width="2" />
              <span class="absolute top-1.5 right-1.5 w-2 h-2 bg-primary rounded-full shadow-[0_0_12px_rgba(255,179,217,0.8)]" />
            </button>
            <button 
              class="px-6 py-2.5 backdrop-blur-xl rounded-3xl hover:shadow-[0_8px_32px_rgba(255,179,217,0.35)] transition-all text-sm border border-white/60"
              style="background: linear-gradient(135deg, rgba(255, 179, 217, 0.5) 0%, rgba(255, 201, 227, 0.4) 100%)"
              @click="toSell()"
            >
              Sell Item
            </button>
          </div>
        </div>
      </div>
    </header>

    <div class="max-w-[1600px] mx-auto px-8 py-12 flex gap-10">
      <!-- Left Sidebar - Liquid Glass -->
      <aside class="w-72 flex-shrink-0">
        <div class="sticky top-28">
          <div 
            class="rounded-[2rem] p-8 backdrop-blur-2xl border border-white/60 shadow-[0_16px_64px_rgba(255,179,217,0.15)] relative overflow-hidden"
            style="background: linear-gradient(135deg, rgba(255, 255, 255, 0.7) 0%, rgba(255, 230, 242, 0.5) 100%)"
          >
            <!-- Glossy highlight -->
            <div 
              class="absolute top-0 left-0 right-0 h-32 opacity-60 pointer-events-none"
              style="background: linear-gradient(180deg, rgba(255, 255, 255, 0.6) 0%, transparent 100%)"
            ></div>

            <!-- User Avatar with glass effect -->
            <div class="relative z-10 mb-8 text-center">
              <div 
                class="w-24 h-24 mx-auto rounded-[2rem] backdrop-blur-xl flex items-center justify-center mb-4 shadow-[0_16px_48px_rgba(255,179,217,0.3)] border-2 border-white/70"
                style="background: linear-gradient(135deg, rgba(255, 255, 255, 0.8) 0%, rgba(255, 179, 217, 0.4) 100%)"
              >
                <User class="w-10 h-10 text-primary" :stroke-width="2" />
              </div>
              <h3 class="text-foreground mb-1.5">Emma Thompson</h3>
              <p class="text-muted-foreground text-xs">Member since 2024</p>
            </div>

            <!-- Menu Items -->
            <nav class="relative z-10 space-y-2">
              <router-link
                v-for="item in menuItems"
                :key="item.to"
                :to="item.to"
                custom
                v-slot="{ isActive, href, navigate }"
              >
                <a
                  :href="href"
                  @click="navigate"
                  :class="[
                    'flex items-center gap-3 px-5 py-3.5 rounded-3xl transition-all duration-300 backdrop-blur-xl border',
                    isActive || (item.exact && $route.path === item.to)
                      ? 'bg-white/60 border-white/80 text-foreground shadow-[0_8px_32px_rgba(255,179,217,0.2)]'
                      : 'text-muted-foreground hover:bg-white/40 hover:text-foreground hover:shadow-[0_4px_24px_rgba(255,179,217,0.15)] border-transparent'
                  ]"
                >
                  <component :is="item.icon" class="w-4 h-4" :stroke-width="2" />
                  <span class="text-sm">{{ item.label }}</span>
                </a>
              </router-link>
            </nav>

            <!-- Logout -->
            <button class="relative z-10 w-full mt-8 flex items-center gap-3 px-5 py-3.5 rounded-3xl text-muted-foreground hover:bg-white/40 hover:text-foreground transition-all text-sm backdrop-blur-xl hover:shadow-[0_4px_24px_rgba(255,179,217,0.15)]">
              <LogOut class="w-4 h-4" :stroke-width="2" />
              <span>Log Out</span>
            </button>
          </div>
        </div>
      </aside>

      <!-- Main Content Area -->
      <main class="flex-1 min-w-0 relative">
        <router-view></router-view>
      </main>
    </div>
  </div>
</template>

<script setup lang="ts">
import { User, Package, ShoppingBag, Heart, Settings, LogOut, Bell, Search } from 'lucide-vue-next'
import BokehBackground from '@/app/components/BokehBackground.vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const menuItems = [
  { to: '/profile', icon: User, label: '个人主页', exact: true },
  { to: '/profile/listings', icon: Package, label: '我发布的' },
  { to: '/profile/purchases', icon: ShoppingBag, label: '我购买的' },
  { to: '/profile/favorites', icon: Heart, label: '喜欢' },
  { to: '/profile/settings', icon: Settings, label: '设置' },
]

function toSell() {
  router.push('/send')
}
</script>
