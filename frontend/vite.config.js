import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
import tailwindcss from '@tailwindcss/vite'


// https://vite.dev/config/
export default defineConfig({
  server: {
    proxy: {
      // 前端请求 /login → 转发到 8081/login
      '/user': {
        target: 'http://127.0.0.1:8080',
        changeOrigin: true,
        rewrite: path => path.replace(/^\/user/, '/user') // 保留 /login
      },
      // 前端请求 /good → 转发到 8083/good
      '/good': {
        target: 'http://127.0.0.1:8080',
        changeOrigin: true,
        rewrite: path => path.replace(/^\/good/, '/good')
      },
      // 前端请求 /deal → 转发到 8084/deal
      '/deal': {
        target: 'http://127.0.0.1:8080',
        changeOrigin: true,
        rewrite: path => path.replace(/^\/deal/, '/deal')
      },
      '/discuss': {
        target: 'ws://127.0.0.1:8085/ws',
        ws: true,
        changeOrigin: true,
      },
      '/community': {
        target: 'http://127.0.0.1:8087',
        changeOrigin: true,
        rewrite: path => path.replace(/^\/community/, '/community'),
                bypass: (req) => {
          if (req.method === 'GET') return req.url
        }
      },
      '/search': {
        target: "http://127.0.0.1:8088",
        changeOrigin: true,
        rewrite: path => path.replace(/^\/search/, '/search'),
        bypass: (req) => {
          if (req.method === 'GET') return req.url
        }
      }
    }
  },
  plugins: [ vue(), vueDevTools(), tailwindcss() ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  assetsInclude: ['**/*svg', '**/*.csv'],
})