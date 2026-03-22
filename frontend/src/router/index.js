import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Dashboard from '../views/Dashboard.vue'
import UserManage from '../views/UserManage.vue'
import ItemManage from '../views/ItemManage.vue'
import ClientLayout from '../views/client/Layout.vue'
import Home from '../views/client/Home.vue'
import Profile from '../views/client/Profile.vue'

const routes = [
  {
    path: '/',
    component: ClientLayout,
    redirect: '/home',
    children: [
      {
        path: 'home',
        name: 'Home',
        component: Home
      },
      {
        path: 'profile',
        name: 'Profile',
        component: Profile,
        meta: { requiresAuth: true }
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: Dashboard,
    meta: { requiresAuth: true },
    redirect: '/dashboard/user',
    children: [
      {
        path: 'user',
        name: 'UserManage',
        component: UserManage,
        meta: { requiresAuth: true }
      },
      {
        path: 'item',
        name: 'ItemManage',
        component: ItemManage,
        meta: { requiresAuth: true }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/login'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth && !token) {
    return '/login'
  }
})

export default router
