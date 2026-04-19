import { createRouter, createWebHistory } from 'vue-router'
import ProfileLayout from './components/ProfileLayout.vue'
import ProfileHome from './pages/ProfileHome.vue'
import MyListings from './pages/MyListings.vue'
import MyPurchases from './pages/MyPurchases.vue'
import Favorites from './pages/Favorites.vue'
import AccountSettings from './pages/AccountSettings.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      component: ProfileLayout,
      children: [
        { path: '', component: ProfileHome },
        { path: 'listings', component: MyListings },
        { path: 'purchases', component: MyPurchases },
        { path: 'favorites', component: Favorites },
        { path: 'settings', component: AccountSettings },
      ],
    },
  ],
})

export default router
