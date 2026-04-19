<!-- App.vue -->
<template>
  <!-- 根路由出口：login 直接渲染这里，其他页面渲染带导航栏的布局 -->
  <router-view></router-view>
</template>

<script setup>
// 无需任何逻辑，仅作为路由出口
</script>

<style scoped>
/* 可保留全局基础样式，比如页面重置 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}
</style>