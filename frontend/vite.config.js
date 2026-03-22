import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    proxy: {
      '/login': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/user': {
        target: 'http://localhost:8081',
        changeOrigin: true
      },
      '/good': {
        target: 'http://localhost:8083',
        changeOrigin: true
      },
      '/deal': {
        target: 'http://localhost:8084',
        changeOrigin: true
      }
    }
  }
})
