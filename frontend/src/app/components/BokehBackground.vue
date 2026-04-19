<template>
  <!-- Gradient Background -->
  <div 
    class="fixed inset-0 -z-10"
    style="background: linear-gradient(135deg, #FFF5F8 0%, #FFE6F2 25%, #FFFFFF 50%, #FFF0F7 75%, #FFF5F8 100%)"
  ></div>

  <!-- Floating Bokeh Orbs -->
  <div class="fixed inset-0 -z-10 overflow-hidden">
    <div
      v-for="(orb, index) in orbs"
      :key="index"
      class="absolute rounded-full opacity-40 blur-3xl animate-float"
      :style="{
        width: `${orb.size}px`,
        height: `${orb.size}px`,
        top: orb.top,
        left: orb.left,
        background: `radial-gradient(circle, rgba(255, 179, 217, 0.4) 0%, rgba(255, 230, 242, 0.2) 50%, transparent 100%)`,
        animationDelay: orb.delay,
        animationDuration: orb.duration,
      }"
    ></div>
  </div>

  <!-- Subtle noise texture overlay -->
  <div 
    class="fixed inset-0 -z-10 opacity-[0.015] pointer-events-none noise-bg"
  ></div>
</template>

<script setup lang="ts">
const orbs = [
  { size: 300, top: '10%', left: '15%', delay: '0s', duration: '25s' },
  { size: 200, top: '60%', left: '75%', delay: '5s', duration: '30s' },
  { size: 250, top: '35%', left: '85%', delay: '3s', duration: '28s' },
  { size: 180, top: '75%', left: '10%', delay: '8s', duration: '26s' },
  { size: 220, top: '20%', left: '50%', delay: '2s', duration: '32s' },
  { size: 150, top: '85%', left: '60%', delay: '6s', duration: '24s' },
  { size: 280, top: '45%', left: '5%', delay: '4s', duration: '29s' },
  { size: 190, top: '5%', left: '70%', delay: '7s', duration: '27s' },
]
</script>

<style>
.noise-bg {
  background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 200 200' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='noiseFilter'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.9' numOctaves='3' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23noiseFilter)'/%3E%3C/svg%3E");
}
@keyframes float {
  0%, 100% {
    transform: translate(0, 0) scale(1);
    opacity: 0.3;
  }
  25% {
    transform: translate(30px, -30px) scale(1.1);
    opacity: 0.4;
  }
  50% {
    transform: translate(-20px, -60px) scale(0.9);
    opacity: 0.35;
  }
  75% {
    transform: translate(-40px, -30px) scale(1.05);
    opacity: 0.45;
  }
}
.animate-float {
  animation: float infinite ease-in-out;
}
</style>
