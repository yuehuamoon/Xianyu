<template>
  <div class="space-y-8">
    <!-- Header -->
    <div>
      <h1 class="text-foreground mb-2" style="font-size: 2rem; font-weight: 300; letter-spacing: -0.01em">Account Settings</h1>
      <p class="text-muted-foreground text-sm" style="font-weight: 300">Manage your account preferences and security</p>
    </div>

    <!-- Profile Picture -->
    <div class="bg-card rounded-3xl p-8 border-2 border-[#D4AF7A]/20 shadow-[0_4px_20px_rgba(232,180,168,0.08)]">
      <h3 class="text-foreground mb-5" style="font-weight: 400">Profile Picture</h3>
      <div class="flex items-center gap-6">
        <div class="w-28 h-28 rounded-3xl bg-gradient-to-br from-accent to-secondary flex items-center justify-center relative group shadow-[0_4px_16px_rgba(232,180,168,0.15)]">
          <User class="w-14 h-14 text-primary" :stroke-width="1.5" />
          <div class="absolute inset-0 bg-black/50 rounded-3xl opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center">
            <Camera class="w-7 h-7 text-white" :stroke-width="1.5" />
          </div>
        </div>
        <div class="flex-1">
          <p class="text-sm text-foreground mb-4" style="font-weight: 300">Upload a new profile picture</p>
          <div class="flex gap-3">
            <button class="px-5 py-2.5 bg-primary/10 text-primary rounded-2xl hover:bg-primary/20 hover:shadow-[0_2px_12px_rgba(232,180,168,0.15)] transition-all text-sm" style="font-weight: 400">
              Upload Photo
            </button>
            <button class="px-5 py-2.5 bg-muted/40 text-foreground rounded-2xl hover:bg-muted/60 hover:shadow-[0_2px_12px_rgba(232,180,168,0.1)] transition-all text-sm" style="font-weight: 400">
              Remove
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Personal Information -->
    <div class="bg-card rounded-3xl p-8 border-2 border-[#D4AF7A]/20 shadow-[0_4px_20px_rgba(232,180,168,0.08)]">
      <div class="flex items-center gap-3 mb-6">
        <div class="w-11 h-11 rounded-2xl bg-gradient-to-br from-primary/15 to-accent/10 flex items-center justify-center shadow-[0_2px_8px_rgba(232,180,168,0.1)]">
          <User class="w-5 h-5 text-primary" :stroke-width="1.5" />
        </div>
        <h3 class="text-foreground" style="font-weight: 400">Personal Information</h3>
      </div>

      <div class="space-y-5">
        <div class="grid grid-cols-2 gap-4">
          <div>
            <label class="text-sm text-foreground mb-2 block" style="font-weight: 400">First Name</label>
            <input
              type="text"
              value="Emma"
              class="w-full px-4 py-3 bg-[#F9F3EF] border border-transparent rounded-2xl focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary/30 focus:shadow-[0_4px_16px_rgba(232,180,168,0.15)] transition-all text-sm"
              style="font-weight: 300"
            />
          </div>
          <div>
            <label class="text-sm text-foreground mb-2 block" style="font-weight: 400">Last Name</label>
            <input
              type="text"
              value="Thompson"
              class="w-full px-4 py-3 bg-[#F9F3EF] border border-transparent rounded-2xl focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary/30 focus:shadow-[0_4px_16px_rgba(232,180,168,0.15)] transition-all text-sm"
              style="font-weight: 300"
            />
          </div>
        </div>

        <div>
          <label class="text-sm text-foreground mb-2 block" style="font-weight: 400">Username</label>
          <input
            type="text"
            value="@emmathompson"
            class="w-full px-4 py-3 bg-[#F9F3EF] border border-transparent rounded-2xl focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary/30 focus:shadow-[0_4px_16px_rgba(232,180,168,0.15)] transition-all text-sm"
            style="font-weight: 300"
          />
        </div>

        <div>
          <label class="text-sm text-foreground mb-2 block" style="font-weight: 400">Email</label>
          <input
            type="email"
            value="emma.thompson@email.com"
            class="w-full px-4 py-3 bg-[#F9F3EF] border border-transparent rounded-2xl focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary/30 focus:shadow-[0_4px_16px_rgba(232,180,168,0.15)] transition-all text-sm"
            style="font-weight: 300"
          />
        </div>

        <div>
          <label class="text-sm text-foreground mb-2 block" style="font-weight: 400">Bio</label>
          <textarea
            rows="3"
            class="w-full px-4 py-3 bg-[#F9F3EF] border border-transparent rounded-2xl focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary/30 focus:shadow-[0_4px_16px_rgba(232,180,168,0.15)] transition-all text-sm resize-none"
            style="font-weight: 300"
          >Vintage enthusiast and sustainable fashion advocate</textarea>
        </div>

        <div class="flex justify-end gap-3 pt-2">
          <button class="px-6 py-2.5 bg-muted/50 text-foreground rounded-2xl hover:bg-muted hover:shadow-[0_2px_12px_rgba(232,180,168,0.1)] transition-all text-sm" style="font-weight: 400">
            Cancel
          </button>
          <button class="px-6 py-2.5 bg-gradient-to-r from-primary to-accent text-primary-foreground rounded-2xl hover:shadow-[0_6px_20px_rgba(232,180,168,0.35)] transition-all text-sm" style="font-weight: 400">
            Save Changes
          </button>
        </div>
      </div>
    </div>

    <!-- Security -->
    <div class="bg-card rounded-3xl p-8 border-2 border-[#D4AF7A]/20 shadow-[0_4px_20px_rgba(232,180,168,0.08)]">
      <div class="flex items-center gap-3 mb-6">
        <div class="w-11 h-11 rounded-2xl bg-gradient-to-br from-primary/15 to-accent/10 flex items-center justify-center shadow-[0_2px_8px_rgba(232,180,168,0.1)]">
          <Lock class="w-5 h-5 text-primary" :stroke-width="1.5" />
        </div>
        <h3 class="text-foreground" style="font-weight: 400">Security</h3>
      </div>

      <div class="space-y-3">
        <button class="w-full flex items-center justify-between p-5 rounded-2xl bg-gradient-to-br from-secondary/20 to-secondary/5 hover:shadow-[0_2px_12px_rgba(232,180,168,0.08)] transition-all text-left">
          <div class="flex items-center gap-3">
            <Shield class="w-5 h-5 text-primary" :stroke-width="1.5" />
            <div>
              <p class="text-sm text-foreground" style="font-weight: 400">Two-Factor Authentication</p>
              <p class="text-xs text-muted-foreground" style="font-weight: 300">Add extra security to your account</p>
            </div>
          </div>
          <span class="text-xs px-3 py-1.5 bg-green-50 text-green-600 rounded-full shadow-[0_2px_8px_rgba(134,239,172,0.15)]" style="font-weight: 400">Enabled</span>
        </button>

        <button class="w-full flex items-center justify-between p-5 rounded-2xl bg-gradient-to-br from-secondary/20 to-secondary/5 hover:shadow-[0_2px_12px_rgba(232,180,168,0.08)] transition-all text-left">
          <div class="flex items-center gap-3">
            <Lock class="w-5 h-5 text-primary" :stroke-width="1.5" />
            <div>
              <p class="text-sm text-foreground" style="font-weight: 400">Change Password</p>
              <p class="text-xs text-muted-foreground" style="font-weight: 300">Update your account password</p>
            </div>
          </div>
          <span class="text-primary text-sm" style="font-weight: 400">Change →</span>
        </button>
      </div>
    </div>

    <!-- Notifications -->
    <div class="bg-card rounded-3xl p-8 border-2 border-[#D4AF7A]/20 shadow-[0_4px_20px_rgba(232,180,168,0.08)]">
      <div class="flex items-center gap-3 mb-6">
        <div class="w-11 h-11 rounded-2xl bg-gradient-to-br from-primary/15 to-accent/10 flex items-center justify-center shadow-[0_2px_8px_rgba(232,180,168,0.1)]">
          <Bell class="w-5 h-5 text-primary" :stroke-width="1.5" />
        </div>
        <h3 class="text-foreground" style="font-weight: 400">Notification Preferences</h3>
      </div>

      <div class="space-y-3">
        <div
          v-for="item in notificationSettings"
          :key="item.key"
          class="flex items-center justify-between p-5 rounded-2xl bg-gradient-to-br from-secondary/20 to-secondary/5 shadow-[0_2px_12px_rgba(232,180,168,0.06)]"
        >
          <div>
            <p class="text-sm text-foreground mb-0.5" style="font-weight: 400">{{ item.label }}</p>
            <p class="text-xs text-muted-foreground" style="font-weight: 300">{{ item.desc }}</p>
          </div>
          <label class="relative inline-flex items-center cursor-pointer">
            <input
              type="checkbox"
              v-model="notifications[item.key]"
              class="sr-only peer"
            />
            <div class="w-11 h-6 bg-muted peer-focus:outline-none peer-focus:ring-2 peer-focus:ring-primary/20 rounded-full peer peer-checked:after:translate-x-full rtl:peer-checked:after:-translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:start-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-primary shadow-[0_2px_8px_rgba(232,180,168,0.1)]"></div>
          </label>
        </div>
      </div>
    </div>

    <!-- Other Settings -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <!-- Language & Region -->
      <div class="bg-card rounded-3xl p-8 border-2 border-[#D4AF7A]/20 shadow-[0_4px_20px_rgba(232,180,168,0.08)]">
        <div class="flex items-center gap-3 mb-5">
          <div class="w-11 h-11 rounded-2xl bg-gradient-to-br from-primary/15 to-accent/10 flex items-center justify-center shadow-[0_2px_8px_rgba(232,180,168,0.1)]">
            <Globe class="w-5 h-5 text-primary" :stroke-width="1.5" />
          </div>
          <h3 class="text-foreground" style="font-weight: 400">Language & Region</h3>
        </div>
        <div class="space-y-4">
          <div>
            <label class="text-sm text-foreground mb-2 block" style="font-weight: 400">Language</label>
            <select class="w-full px-4 py-3 bg-[#F9F3EF] border border-transparent rounded-2xl focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary/30 focus:shadow-[0_4px_16px_rgba(232,180,168,0.15)] transition-all text-sm" style="font-weight: 300">
              <option>English (US)</option>
              <option>Spanish</option>
              <option>French</option>
              <option>German</option>
            </select>
          </div>
          <div>
            <label class="text-sm text-foreground mb-2 block" style="font-weight: 400">Currency</label>
            <select class="w-full px-4 py-3 bg-[#F9F3EF] border border-transparent rounded-2xl focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary/30 focus:shadow-[0_4px_16px_rgba(232,180,168,0.15)] transition-all text-sm" style="font-weight: 300">
              <option>USD ($)</option>
              <option>EUR (€)</option>
              <option>GBP (£)</option>
              <option>JPY (¥)</option>
            </select>
          </div>
        </div>
      </div>

      <!-- Appearance -->
      <div class="bg-card rounded-3xl p-8 border-2 border-[#D4AF7A]/20 shadow-[0_4px_20px_rgba(232,180,168,0.08)]">
        <div class="flex items-center gap-3 mb-5">
          <div class="w-11 h-11 rounded-2xl bg-gradient-to-br from-primary/15 to-accent/10 flex items-center justify-center shadow-[0_2px_8px_rgba(232,180,168,0.1)]">
            <Palette class="w-5 h-5 text-primary" :stroke-width="1.5" />
          </div>
          <h3 class="text-foreground" style="font-weight: 400">Appearance</h3>
        </div>
        <div class="space-y-2">
          <button class="w-full px-4 py-3 text-sm text-left bg-gradient-to-r from-primary/15 to-accent/10 text-primary rounded-2xl border border-[#D4AF7A]/20 shadow-[0_2px_8px_rgba(232,180,168,0.1)]" style="font-weight: 400">
            Light Theme (Active)
          </button>
          <button class="w-full px-4 py-3 text-sm text-left bg-muted/40 text-foreground rounded-2xl hover:bg-muted/60 hover:shadow-[0_2px_8px_rgba(232,180,168,0.08)] transition-all" style="font-weight: 300">
            Dark Theme
          </button>
          <button class="w-full px-4 py-3 text-sm text-left bg-muted/40 text-foreground rounded-2xl hover:bg-muted/60 hover:shadow-[0_2px_8px_rgba(232,180,168,0.08)] transition-all" style="font-weight: 300">
            Auto (System)
          </button>
        </div>
      </div>
    </div>

    <!-- Payment Methods -->
    <div class="bg-card rounded-3xl p-8 border-2 border-[#D4AF7A]/20 shadow-[0_4px_20px_rgba(232,180,168,0.08)]">
      <div class="flex items-center gap-3 mb-6">
        <div class="w-11 h-11 rounded-2xl bg-gradient-to-br from-primary/15 to-accent/10 flex items-center justify-center shadow-[0_2px_8px_rgba(232,180,168,0.1)]">
          <CreditCard class="w-5 h-5 text-primary" :stroke-width="1.5" />
        </div>
        <h3 class="text-foreground" style="font-weight: 400">Payment Methods</h3>
      </div>
      <div class="space-y-3 mb-5">
        <div class="p-5 rounded-2xl bg-gradient-to-br from-secondary/20 to-secondary/5 flex items-center justify-between shadow-[0_2px_12px_rgba(232,180,168,0.06)]">
          <div class="flex items-center gap-3">
            <div class="w-14 h-10 bg-gradient-to-br from-blue-500 to-blue-600 rounded-xl shadow-[0_2px_8px_rgba(59,130,246,0.3)]" />
            <div>
              <p class="text-sm text-foreground" style="font-weight: 400">Visa •••• 4242</p>
              <p class="text-xs text-muted-foreground" style="font-weight: 300">Expires 12/2027</p>
            </div>
          </div>
          <span class="text-xs px-3 py-1.5 bg-primary/10 text-primary rounded-full shadow-[0_2px_8px_rgba(232,180,168,0.1)]" style="font-weight: 400">Default</span>
        </div>
      </div>
      <button class="px-5 py-2.5 bg-muted/40 text-foreground rounded-2xl hover:bg-muted/60 hover:shadow-[0_2px_12px_rgba(232,180,168,0.1)] transition-all text-sm" style="font-weight: 400">
        Add Payment Method
      </button>
    </div>

    <!-- Danger Zone -->
    <div class="bg-card rounded-3xl p-8 border-2 border-destructive/20 shadow-[0_4px_20px_rgba(201,139,126,0.12)]">
      <div class="flex items-center gap-3 mb-5">
        <div class="w-11 h-11 rounded-2xl bg-destructive/10 flex items-center justify-center shadow-[0_2px_8px_rgba(201,139,126,0.15)]">
          <Trash2 class="w-5 h-5 text-destructive" :stroke-width="1.5" />
        </div>
        <h3 class="text-foreground" style="font-weight: 400">Danger Zone</h3>
      </div>
      <p class="text-sm text-muted-foreground mb-5" style="font-weight: 300">
        Once you delete your account, there is no going back. Please be certain.
      </p>
      <button class="px-6 py-2.5 bg-destructive/10 text-destructive rounded-2xl hover:bg-destructive/20 hover:shadow-[0_4px_16px_rgba(201,139,126,0.2)] transition-all text-sm" style="font-weight: 400">
        Delete Account
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { User, Lock, Bell, CreditCard, Shield, Globe, Palette, Trash2, Camera } from 'lucide-vue-next'

const notifications = ref({
  newMessages: true,
  orderUpdates: true,
  promotions: false,
  priceDrops: true,
})

const notificationSettings = [
  { key: "newMessages", label: "New Messages", desc: "Get notified when you receive messages" },
  { key: "orderUpdates", label: "Order Updates", desc: "Track your orders and shipments" },
  { key: "promotions", label: "Promotions & Offers", desc: "Receive special deals and discounts" },
  { key: "priceDrops", label: "Price Drops", desc: "Alert when favorited items go on sale" },
] as const
</script>
