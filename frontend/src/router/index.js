import { createRouter, createWebHistory } from 'vue-router'
import index from '@/views/index.vue'
import login from '@/views/login.vue'
import detail from '@/views/detail.vue'
import profile from '@/views/profile.vue'
import send from '@/views/send.vue'
import Mainlaout from '@/components/Mainlayout.vue'
import Managelogin from '@/views/managelogin.vue'
import Managelayout from '@/components/managelayout.vue'
import Manageuserinfo from '@/views/manageuserinfo.vue'
import Managedealinfo from '@/views/managedealinfo.vue'
import Managegoodinfo from '@/views/managegoodinfo.vue'
import Discuss from '@/components/discuss.vue'
import Manageindex from "@/views/manageindex.vue"
import ProfileHome from '@/app/pages/ProfileHome.vue'
import ProfileLayout from '@/app/components/ProfileLayout.vue'
import MyListings from '@/app/pages/MyListings.vue'
import MyPurchases from '@/app/pages/MyPurchases.vue'
import Favorites from '@/app/pages/Favorites.vue'
import AccountSettings from '@/app/pages/AccountSettings.vue'
import Otheruser from '@/views/otheruser.vue'
import Community from '@/views/community.vue'
import Post from '@/views/post.vue'
import Search from '@/views/search.vue'
import Category from '@/views/category.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: login,
    },
    {
      path: "/mlogin",
      name: "mlogin",
      component: Managelogin,
    },
    {
      path: '/manage',
      name: 'manage',
      component: Managelayout,
      children: [
        {
          path: "userinfo",
          name: "userinfo",
          component: Manageuserinfo,
        },
        {
          path: "mindex",
          name: "mindex",
          component: Manageindex,
        },
                {
          path: "goodinfo",
          name: "goodinfo",
          component: Managegoodinfo,
        },
                {
          path: "dealinfo",
          name: "dealinfo",
          component: Managedealinfo
        },

      ]
    },
    {
      path: '/chat/:userId',
      name: 'chat',
      component: Discuss,
    },
    {
      path:'/',
      component: Mainlaout,
      children: [
        {
          path: '/',
          name: 'index',
          component: index,
        },
        {
          path: '/detail',
          name: 'detail',
          component: detail,
        },
        {
          path: '/send',
          name: 'send',
          component: send
        },
              {
        path: '/profile',
        name: 'profile',
        component: profile,
      },
      {
        path: "/community",
        name: 'community',
        component: Community,
      },

      {
        path: '/post/:id',
        name: 'post',
        component: Post,
      },
      {
        path: '/search',
        name: 'search',
        component: Search,
      },
      {
        path: '/category/:type?',
        name: 'category',
        component: Category,
      },
      ]
    },

    {
      path: '/otheruser/:userId',
      name: 'otheruser',
      component: Otheruser,
    },


  ],
})

export default router
