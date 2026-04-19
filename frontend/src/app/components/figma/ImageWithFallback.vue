<template>
  <div v-if="didError" :class="['inline-block bg-gray-100 text-center align-middle', className]" :style="style">
    <div class="flex items-center justify-center w-full h-full">
      <img :src="ERROR_IMG_SRC" alt="Error loading image" :data-original-url="src" v-bind="$attrs" />
    </div>
  </div>
  <img v-else :src="src" :alt="alt" :class="className" :style="style" v-bind="$attrs" @error="handleError" />
</template>

<script setup lang="ts">
import { ref } from 'vue'

defineOptions({ inheritAttrs: false })

const props = defineProps<{
  src?: string
  alt?: string
  className?: string
  style?: any
}>()

const didError = ref(false)

const handleError = () => {
  didError.value = true
}

const ERROR_IMG_SRC =
  'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iODgiIGhlaWdodD0iODgiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyIgc3Ryb2tlPSIjMDAwIiBzdHJva2UtbGluZWpvaW49InJvdW5kIiBvcGFjaXR5PSIuMyIgZmlsbD0ibm9uZSIgc3Ryb2tlLXdpZHRoPSIzLjciPjxyZWN0IHg9IjE2IiB5PSIxNiIgd2lkdGg9IjU2IiByeD0iNiIvPjxwYXRoIGQ9Im0xNiA1OCAxNi0xOCAzMiAzMiIvPjxjaXJjbGUgY3g9IjUzIiBjeT0iMzUiIHI9IjciLz48L3N2Zz4KCg=='
</script>
