<template>
  <!-- 页面优化：搭建规整表单容器，替代原有的table标签（表单场景更适配form+栅格布局） -->
  <div class="form-container">
    <h3 class="form-name">商品信息填写表单</h3>
    <form class="info-form">
      <!-- 每个input对应data的不同属性，绑定正确的v-model，添加标签和占位符提升易用性 -->
      <div class="form-item">
        <label class="form-label">标题：</label>
        <input type="text" v-model="data.name" placeholder="请输入商品标题" class="form-input" />
      </div>

      <!-- 修改：类型改为下拉选择框，可选固定类别 -->
      <div class="form-item">
        <label class="form-label">类型：</label>
        <select v-model="data.label" class="form-select form-input">
          <option value="" disabled>请选择商品类型</option>
          <option value="生活用品">生活用品</option>
          <option value="电子产品">电子产品</option>
          <option value="图书">图书</option>
          <option value="其他">其他</option>
        </select>
      </div>

      <div class="form-item">
        <label class="form-label">内容：</label>
        <input type="text" v-model="data.content" placeholder="请输入商品详情内容" class="form-input" />
      </div>

      <div class="form-item">
        <label class="form-label">商品图片：</label>
        <!-- 文件选择 -->
        <input
          type="file"
          accept="image/*"
          @change="handleUpload"
          class="form-input"
        />
        <!-- 实时预览 -->
        <img
          v-if="previewUrl"
          :src="previewUrl"
          alt="预览"
          style="width:120px;height:120px;object-fit:cover;margin-top:8px;display:block"
        />
      </div>

      <div class="form-item">
        <label class="form-label">数量：</label>
        <input type="text" v-model="data.count" placeholder="请输入商品库存数量" class="form-input" />
      </div>

      <div class="form-item">
        <label class="form-label">成交价格：</label>
        <input type="text" v-model="data.unitPrice" placeholder="请输入商品成交价格（元）" class="form-input" />
      </div>
      
      <!-- 提交按钮：添加禁用状态，提升用户感知 -->
      <button type="button" @click="send_table" :disabled="!isFormValid" class="submit-btn">
        提交
      </button>
    </form>
  </div>
</template>

<script setup>
import axios from 'axios';
import { onMounted, ref, computed } from 'vue';

const apiUrl = import.meta.env.VITE_API_URL;


const data1 = ref({ image: '' })   // 最终保存的 HTTPS URL
const previewUrl = ref('')        // 本地预览地址

async function handleUpload(e) {
  const file = e.target.files[0]
  if (!file) return

  // 1. 本地预览
  previewUrl.value = URL.createObjectURL(file)


  // 3. 构造 FormData 直传 OSS/S3
  const form = new FormData()
  const random = Math.random().toString(36).slice(-6)      // 6 位随机字母/数字
  const time   = Date.now()
  const ext    = file.name.split('.').pop()
  const key    = `${time}-${random}.${ext}`
  console.log(key)
  data.value.img = key


  

  // 4. 拼接 CDN 地址并回写
  data.value.image = `${auth.cdnHost}/${key}`

  // 5. 清理预览内存
  URL.revokeObjectURL(previewUrl.value)
}

// 优化：data属性保持不变，仅整理格式（去除多余引号，保持属性名一致性，提升可读性）
const data = ref({
  name: '',
  type: '',
  sellerId: '',
  content: '',
  imgUrl: '',
  count: '',
  unitPrice: '',
  label: '',
  address: 'fandouhuayuan',
  
});

// 新增：计算属性 - 表单合法性校验（判空核心）
const isFormValid = computed(() => {
  // 必选字段：标题、类型、数量、成交价格（可根据需求调整）
  const { name, label, count, unitPrice } = data.value;
  // 非空判断 + 数量/价格为有效数字判断
  return !!name.trim() && 
         !!label && 
         !!count.trim() && /^\d+$/.test(count.trim()) && 
         !!unitPrice.trim() && /^\d+(\.\d{1,2})?$/.test(unitPrice.trim());
});

function parseJwt(token) {
  try {
    const base64Payload = token.split('.')[1]; // JWT 中间段
    const payload = atob(base64Payload);      // Base64 解码
    return JSON.parse(payload);                // 转成对象
  } catch (e) {
    return null;
  }
}

// 优化：send_table函数添加判空、错误处理、数据修正
async function send_table() {
  // 1. 二次校验（防止绕过禁用状态）
  if (!isFormValid.value) {
    alert('表单填写不完整或格式错误！\n1. 标题不能为空\n2. 请选择商品类型\n3. 数量为正整数\n4. 价格为数字（最多保留2位小数）');
    return;
  }

  // 2. 获取token并校验
  let token = localStorage.getItem('token');
  if (!token) {
    alert('未获取到登录状态，请先登录！');
    return;
  }

  // 3. 解析用户信息并校验
  const userInfo = parseJwt(token);
  console.log(userInfo)
  if (!userInfo || !userInfo.userId) {
    alert('用户信息解析失败，请重新登录！');
    return;
  }

  // 4. 修正：给ref对象的value赋值userid（原有代码直接赋值data.userid会报错）
  data.value.sellerId = userInfo.userId;

  // 5. 发送请求：添加async/await + try/catch错误处理
  try {
    let recordRes = await axios.post(
      `/good/insert`,
      data.value,
      { headers: { Authorization: `Bearer ${token}`, 'token': token } }
    );

    // 请求成功提示
    alert('商品信息提交成功！');
    
    // 可选：提交成功后重置表单
    // data.value = { name: '', type: '', userid: '', content: '', image: '', count: '', unitPrice: '' };
  } catch (error) {
    // 请求失败提示
    alert('商品信息提交失败：' + (error.response?.data?.message || '网络异常，请稍后重试'));
  }
}   
</script>

<style scoped>
/* Liquid Glass Style - Based on Admin Dashboard Design */
.form-container {
  width: 80%;
  max-width: 800px;
  margin: 40px auto;
  padding: 32px;
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(30px) saturate(180%);
  -webkit-backdrop-filter: blur(30px) saturate(180%);
  border: 1px solid rgba(255, 255, 255, 0.9);
  border-radius: 24px;
  box-shadow:
    0 8px 32px rgba(236, 72, 153, 0.06),
    inset 0 1px 1px rgba(255, 255, 255, 0.8);
}

/* 标题 */
.form-name {
  font-size: 22px;
  font-weight: 300;
  letter-spacing: 0.02em;
  margin-bottom: 24px;
  padding-bottom: 12px;
  text-align: center;
  background: linear-gradient(135deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  border-bottom: 1px solid rgba(255, 255, 255, 0.5);
}

/* 表单整体 */
.info-form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* 单行 */
.form-item {
  display: flex;
  width: 100%;
  align-items: center;
  gap: 16px;
}

/* 标签 */
.form-label {
  width: 120px;
  text-align: right;
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #374151;
}

/* 输入框 / 下拉框 */
.form-input {
  flex: 1;
  padding: 12px 16px;
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.4);
  backdrop-filter: blur(20px) saturate(180%);
  -webkit-backdrop-filter: blur(20px) saturate(180%);
  outline: none;
  color: #374151;
  transition: all 0.3s ease;
}

.form-input::placeholder {
  color: rgba(107, 114, 128, 0.6);
}

.form-input:focus {
  background: rgba(255, 255, 255, 0.7);
  border-color: rgba(236, 72, 153, 0.3);
  box-shadow:
    0 0 0 3px rgba(236, 72, 153, 0.06),
    0 4px 16px rgba(236, 72, 153, 0.08);
}

/* select 样式 */
.form-select {
  appearance: none;
  cursor: pointer;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='12' viewBox='0 0 12 12'%3E%3Cpath fill='%236b7280' d='M6 8L2 4h8z'/%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: right 12px center;
  padding-right: 36px;
}

/* 提交按钮 */
.submit-btn {
  margin-top: 24px;
  padding: 14px 0;
  background: linear-gradient(135deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
  color: #fff;
  font-size: 14px;
  font-weight: 500;
  letter-spacing: 0.02em;
  border: none;
  border-radius: 16px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 16px rgba(236, 72, 153, 0.25);
}

.submit-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow:
    0 12px 40px rgba(236, 72, 153, 0.25),
    0 4px 12px rgba(236, 72, 153, 0.15);
}

.submit-btn:active:not(:disabled) {
  transform: translateY(0);
}

/* 禁用状态 */
.submit-btn:disabled {
  background: rgba(236, 72, 153, 0.3);
  color: rgba(255, 255, 255, 0.6);
  cursor: not-allowed;
  box-shadow: none;
}

/* 文件上传区域 */
.form-item input[type="file"] {
  padding: 10px;
}

/* 响应式 */
@media (max-width: 520px) {
  .form-item {
    flex-direction: column;
    align-items: stretch;
  }

  .form-label {
    width: auto;
    text-align: left;
    margin-bottom: 4px;
  }
}
</style>
