<template>
  <div class="login-bg" @mousemove="handleGlobalMouseMove" @mouseleave="handleGlobalMouseLeave">
    <!-- 全局鼠标光晕 -->
    <div class="mouse-glow" :style="glowStyle"></div>

    <div class="card">
      <h2>登录 / 注册</h2>

    <!-- 公共字段 -->
    <input v-model="form.phone" placeholder="账号/手机号" v-if="!isReg" />
    <input v-model="form.password" type="password" placeholder="密码" />

    <!-- 注册额外字段 -->
    <template v-if="isReg">
      <input v-model="form.realName" placeholder="真实姓名" />
      <input v-model="form.nickName" placeholder="昵称" />
      <input v-model="form.phone" placeholder="手机号" />
      <input v-model="form.address" placeholder="地址" />
      <select v-model="form.gender" placeholder="性别">
        <option value="">请选择性别</option>
        <option value=0>男</option>
        <option value=1>女</option>
      </select>
      <input v-model="form.certNo" placeholder="身份证号（选填）" />
    </template>

    <!-- 按钮 -->
    <button @click="handleLogin" v-if="!isReg">登录</button>
    <button @click="handleReg" v-if="isReg">注册</button>

    <!-- 切换 -->
    <a @click="isReg = !isReg">{{ isReg ? '已有账号？去登录' : '没有账号？去注册' }}</a>

    <!-- 结果 -->
    <pre v-if="result">{{ result }}</pre>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, computed } from 'vue';
import axios from 'axios';
import md5 from 'md5';
import { useRouter } from 'vue-router';

const router = useRouter();

const apiUrl = import.meta.env.VITE_API_URL; // http://localhost:8080
const isReg = ref(false);
const result = ref('');

// 全局鼠标光晕位置
const glowPosition = reactive({
  x: 0,
  y: 0,
  visible: false,
});

// 计算光晕样式
const glowStyle = computed(() => ({
  left: `${glowPosition.x}px`,
  top: `${glowPosition.y}px`,
  opacity: glowPosition.visible ? 1 : 0,
}));

// 全局鼠标移动处理
function handleGlobalMouseMove(e) {
  glowPosition.x = e.clientX;
  glowPosition.y = e.clientY;
  glowPosition.visible = true;
}

// 全局鼠标离开处理
function handleGlobalMouseLeave() {
  glowPosition.visible = false;
}

// 初始化表单，字段名与后端UserVO完全匹配
const form = reactive({
  realName: '',      // 真实姓名
  nickName: '',      // 昵称
  password: '',      // 密码
  wallet: 0,         // 钱包初始值0
  phone: '',         // 手机号
  address: '',       // 地址
  gender: 0,     // 性别默认值
  avatarUrl: '',     // 头像URL（默认空）
  certNo: '',        // 身份证号
  role: "1",
});

/* 登录 */
async function handleLogin() {
  try {
    // 复制表单，避免修改原数据
    const submitForm = { ...form };
    
    // 密码加密
    submitForm.password = md5(submitForm.password);

    // 登录只需要手机号和密码
    const loginData = {
      phone: submitForm.phone,
      password: submitForm.password
    };

    const res = await axios.post('/user/login', loginData);
    result.value = res.data;

    if (res.data.code === 200) {
      localStorage.setItem('token', res.data.data);
      router.push('/manage/');
    }
  } catch (e) {
    result.value = e.response?.data || '登录失败';
  }
}

/* 注册 */
async function handleReg() {
  try {
    // 表单验证
    if (!form.realName) {
      result.value = '请输入真实姓名';
      return;
    }
    if (!form.phone) {
      result.value = '请输入手机号';
      return;
    }
    if (!form.password) {
      result.value = '请输入密码';
      return;
    }

    // 复制表单并加密密码
    const submitForm = { ...form };
    submitForm.password = md5(submitForm.password);

    const res = await axios.post('/user/register', submitForm);
    result.value = res.data;

    // 注册成功后自动切换到登录
    if (res.data.code === 200) {
      setTimeout(() => {
        isReg.value = false;
        // 清空密码，保留手机号
        form.password = '';
      }, 1500);
    }
  } catch (e) {
    result.value = e.response?.data || '注册失败';
  }
}
</script>

<style scoped>
/* Liquid Glass Style - Based on Admin Dashboard Design */
.login-bg {
  width: 100%;
  min-height: 100vh;
  background: linear-gradient(135deg,
    rgba(255, 240, 245, 1) 0%,
    rgba(252, 231, 243, 1) 25%,
    rgba(254, 242, 242, 1) 50%,
    rgba(255, 237, 241, 1) 75%,
    rgba(253, 242, 248, 1) 100%
  );
  background-size: 200% 200%;
  animation: subtleGradient 20s ease infinite;
  position: relative;
  overflow-x: hidden;
}

@keyframes subtleGradient {
  0%, 100% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
}

/* 全局鼠标光晕 */
.mouse-glow {
  position: fixed;
  width: 350px;
  height: 350px;
  border-radius: 50%;
  background: radial-gradient(
    circle at center,
    rgba(236, 72, 153, 0.18) 0%,
    rgba(244, 114, 182, 0.12) 25%,
    rgba(243, 63, 94, 0.06) 45%,
    transparent 65%
  );
  transform: translate(-50%, -50%);
  pointer-events: none;
  z-index: 9999;
  transition: opacity 0.3s ease;
  filter: blur(3px);
  mix-blend-mode: screen;
}

.card {
  width: 400px;
  margin: 80px auto;
  padding: 40px;
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(30px) saturate(180%);
  -webkit-backdrop-filter: blur(30px) saturate(180%);
  border: 1px solid rgba(255, 255, 255, 0.9);
  border-radius: 24px;
  box-shadow:
    0 8px 32px rgba(236, 72, 153, 0.06),
    inset 0 1px 1px rgba(255, 255, 255, 0.8);
}

h2 {
  text-align: center;
  font-size: 28px;
  font-weight: 300;
  letter-spacing: 0.02em;
  color: #374151;
  margin-bottom: 32px;
  background: linear-gradient(135deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

input, select {
  width: 100%;
  margin: 12px 0;
  padding: 14px 18px;
  box-sizing: border-box;
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.4);
  backdrop-filter: blur(20px) saturate(180%);
  -webkit-backdrop-filter: blur(20px) saturate(180%);
  color: #374151;
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  transition: all 0.3s ease;
}

input::placeholder {
  color: rgba(107, 114, 128, 0.6);
}

input:focus, select:focus {
  outline: none;
  background: rgba(255, 255, 255, 0.7);
  border-color: rgba(236, 72, 153, 0.3);
  box-shadow:
    0 0 0 3px rgba(236, 72, 153, 0.06),
    0 4px 16px rgba(236, 72, 153, 0.08);
}

select {
  cursor: pointer;
  appearance: none;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='12' viewBox='0 0 12 12'%3E%3Cpath fill='%236b7280' d='M6 8L2 4h8z'/%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: right 14px center;
  padding-right: 40px;
}

select option {
  background: rgba(255, 255, 255, 0.95);
  color: #374151;
}

button {
  width: 100%;
  margin-top: 20px;
  padding: 16px;
  background: linear-gradient(135deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
  color: white;
  border: none;
  border-radius: 16px;
  cursor: pointer;
  font-size: 16px;
  font-weight: 500;
  letter-spacing: 0.02em;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 16px rgba(236, 72, 153, 0.25);
}

button:hover {
  transform: translateY(-2px);
  box-shadow:
    0 12px 40px rgba(236, 72, 153, 0.25),
    0 4px 12px rgba(236, 72, 153, 0.15);
}

button:active {
  transform: translateY(0);
}

a {
  display: block;
  text-align: center;
  margin-top: 20px;
  color: #ec4899;
  cursor: pointer;
  text-decoration: none;
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  transition: all 0.3s ease;
}

a:hover {
  color: #f43f5e;
}

pre {
  margin-top: 20px;
  background: rgba(255, 255, 255, 0.4);
  padding: 14px;
  font-size: 12px;
  border-radius: 12px;
  color: #374151;
  border: 1px solid rgba(255, 255, 255, 0.5);
}
</style>