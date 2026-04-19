<template>
  <div class="good-body">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2 class="page-title">商品中心</h2>
      <p class="page-desc">来自 Spring /good 接口的商品数据</p>
    </div>

    <!-- 分类筛选 -->
    <div class="category-filter">
      <button
        v-for="category in categories"
        :key="category"
        :class="['category-btn', { active: activeCategory === category }]"
        @click="handleCategory(category)"
      >
        {{ category }}
      </button>
    </div>

    <!-- 错误/加载提示 -->
    <div v-if="error" class="error-tip">{{ error }}</div>
    <div v-if="loading" class="loading-tip">加载中...</div>

    <!-- 新增商品表单 -->
    <div class="add-form glass-card" v-if="showAddForm">
      <h3 class="form-title">新增商品</h3>
      <form @submit.prevent="addGood">
        <div class="form-grid">
          <div class="form-item">
            <label>卖家ID：</label>
            <input type="number" v-model="newGood.sellerId" placeholder="必填" required>
          </div>
          <div class="form-item">
            <label>商品名称：</label>
            <input type="text" v-model="newGood.name" placeholder="必填" required>
          </div>
          <div class="form-item">
            <label>单价(分)：</label>
            <input type="number" v-model="newGood.unitPrice" min="1" placeholder="必填" required>
          </div>
          <div class="form-item">
            <label>商品数量：</label>
            <input type="number" v-model="newGood.count" min="1" placeholder="默认1">
          </div>
          <div class="form-item">
            <label>商品标签：</label>
            <input type="text" v-model="newGood.label" placeholder="必填" required>
          </div>
          <div class="form-item">
            <label>商品状态：</label>
            <select v-model="newGood.status" required>
              <option value="0">审核中</option>
              <option value="1">上架</option>
              <option value="2">售出</option>
              <option value="3">失效</option>
            </select>
          </div>
          <div class="form-item full-width">
            <label>商品描述：</label>
            <textarea v-model="newGood.content" placeholder="选填"></textarea>
          </div>
          <div class="form-item full-width">
            <label>图片链接：</label>
            <input type="text" v-model="newGood.imgUrl" placeholder="选填">
          </div>
          <div class="form-item full-width">
            <label>交易地址：</label>
            <input type="text" v-model="newGood.address" placeholder="选填">
          </div>
        </div>
        <div class="form-actions">
          <button type="submit" class="submit-btn">提交新增</button>
          <button type="button" class="cancel-btn" @click="showAddForm = false">取消</button>
        </div>
      </form>
    </div>

    <!-- 新增按钮 -->
    <button class="add-btn" @click="showAddForm = true">
      <Plus class="btn-icon" :size="18" :stroke-width="1.5" />
      <span>新增商品</span>
    </button>

    <!-- 商品卡片网格 -->
    <div class="products-grid">
      <div v-if="shownProducts.length === 0 && !loading" class="empty-tip">
        暂无商品数据
      </div>
      
      <div
        v-for="product in shownProducts"
        :key="product.id"
        class="product-card glass-card shine-effect"
      >
        <!-- 商品图片 -->
        <div class="product-image">
          <img
            :src="product.imgUrl || 'https://images.unsplash.com/photo-1472851294608-062f824d29cc?w=400&h=400&fit=crop'"
            :alt="product.name"
            class="product-img"
          >
          <button class="favorite-btn" @click="toggleFavorite(product)">
            <Heart class="icon" :size="16" :stroke-width="1.5" />
          </button>
        </div>

        <!-- 商品信息 -->
        <div class="product-info">
          <div class="product-header">
            <h3 class="product-name">{{ product.name }}</h3>
            <span class="product-tag">{{ product.label || '未分类' }}</span>
          </div>

          <div class="product-stats">
            <div class="stat-item">
              <Star class="icon star" :size="14" />
              <span class="stat-value">{{ (product.viewCount || 0) + (product.favoriteCount || 0) }}</span>
            </div>
            <span class="stat-label">({{ product.viewCount || 0 }} 浏览)</span>
          </div>

          <div class="product-footer">
            <span class="product-price">¥{{ formatPrice(product.unitPrice) }}</span>
            <div class="product-actions">
              <button class="action-btn edit" @click="openEditModal(product)">
                <Edit2 class="icon" :size="14" :stroke-width="1.5" />
              </button>
              <button class="action-btn delete" @click="deleteGood(product)">
                <Trash2 class="icon" :size="14" :stroke-width="1.5" />
              </button>
            </div>
          </div>

          <!-- 状态标签 -->
          <div class="product-status">
            <span :class="['status-badge', getStatusClass(product.status)]">
              {{ getStatusText(product.status) }}
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- 分页控件 -->
    <div class="pagination">
      <button @click="getGoodList(currentPage - 1)" :disabled="currentPage <= 1">上一页</button>
      <span>第 {{ currentPage }} 页</span>
      <button @click="getGoodList(currentPage + 1)">下一页</button>
    </div>

    <!-- 编辑弹窗 -->
    <Teleport to="body">
      <div v-if="showEditModal" class="modal-overlay" @click.self="closeEditModal">
        <div class="modal-card glass-card">
          <div class="modal-header">
            <h3 class="modal-title">编辑商品 #{{ editingProduct?.id }}</h3>
            <button class="modal-close" @click="closeEditModal">
              <X class="icon" :size="20" :stroke-width="1.5" />
            </button>
          </div>
          <div class="modal-body">
            <div class="form-grid">
              <div class="form-item">
                <label>商品名称 <span class="required">*</span></label>
                <input type="text" v-model="editingProduct.name" class="form-input" placeholder="必填">
              </div>
              <div class="form-item">
                <label>单价(分) <span class="required">*</span></label>
                <input type="number" v-model="editingProduct.unitPrice" class="form-input" min="1" placeholder="必填">
              </div>
              <div class="form-item">
                <label>数量</label>
                <input type="number" v-model="editingProduct.count" class="form-input" min="1">
              </div>
              <div class="form-item">
                <label>标签 <span class="required">*</span></label>
                <input type="text" v-model="editingProduct.label" class="form-input" placeholder="必填">
              </div>
              <div class="form-item">
                <label>状态</label>
                <select v-model="editingProduct.status" class="form-select">
                  <option value="0">审核中</option>
                  <option value="1">上架</option>
                  <option value="2">售出</option>
                  <option value="3">失效</option>
                </select>
              </div>
              <div class="form-item">
                <label>卖家ID</label>
                <input type="number" v-model="editingProduct.sellerId" class="form-input" min="1">
              </div>
              <div class="form-item full-width">
                <label>商品描述</label>
                <textarea v-model="editingProduct.content" class="form-textarea" placeholder="选填"></textarea>
              </div>
              <div class="form-item full-width">
                <label>图片链接</label>
                <input type="text" v-model="editingProduct.imgUrl" class="form-input" placeholder="选填">
              </div>
              <div class="form-item full-width">
                <label>交易地址</label>
                <input type="text" v-model="editingProduct.address" class="form-input" placeholder="选填">
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button class="btn-cancel" @click="closeEditModal">取消</button>
            <button class="btn-confirm" @click="confirmUpdate">确认保存</button>
          </div>
        </div>
      </div>
    </Teleport>
  </div>
</template>

<script setup>
import axios from "axios";
import { onMounted, ref, computed, toRaw } from "vue";
import { Plus, Heart, Star, Edit2, Trash2, X } from 'lucide-vue-next';

// 响应式数据
const rawTarget = ref([]);
const allGoods = ref([]); // 存储所有商品数据，用于分类筛选
const currentPage = ref(1);
const activeCategory = ref('全部');
const loading = ref(true);
const error = ref('');
const showAddForm = ref(false);

// 弹窗相关
const showEditModal = ref(false);
const editingProduct = ref(null);

// 新增商品表单数据
const newGood = ref({
  sellerId: "",
  name: "",
  content: "",
  unitPrice: "",
  count: 1,
  imgUrl: "",
  label: "",
  status: 0,
  address: "",
  viewCount: 0,
  favoriteCount: 0
});

// 分类列表（基于所有商品数据）
const categories = computed(() => {
  const values = Array.from(new Set(allGoods.value.map(item => item.label).filter(Boolean)));
  return ['全部', ...values];
});

// 筛选后的商品列表（前端筛选，不调用接口）
const shownProducts = computed(() => {
  if (activeCategory.value === '全部') return rawTarget.value;
  return rawTarget.value.filter(item => item.label === activeCategory.value);
});

// 格式化价格（分转元）
const formatPrice = (price) => {
  if (!price) return "0.00";
  return (Number(price) / 100).toFixed(2);
};

// 获取商品状态文本
const getStatusText = (status) => {
  const statusMap = {
    0: "审核中",
    1: "上架",
    2: "售出",
    3: "失效"
  };
  return statusMap[status] || "未知状态";
};

// 获取商品状态样式类
const getStatusClass = (status) => {
  const classMap = {
    0: "review",
    1: "on",
    2: "sold",
    3: "invalid"
  };
  return classMap[status] || "";
};

// 获取商品列表
async function getGoodList(page = 1) {
  loading.value = true;
  error.value = '';
  try {
    const res = await axios.post("/good/info", { page: page });
    const data = res.data?.data || [];
    const processedData = JSON.parse(JSON.stringify(data)).map(item => ({
      ...item,
      isEditing: false,
      originData: { ...item }
    }));
    rawTarget.value = processedData;
    allGoods.value = processedData; // 同时更新所有商品数据
    currentPage.value = page;
    activeCategory.value = '全部'; // 重置分类筛选
  } catch (err) {
    console.error("获取商品列表失败：", err);
    error.value = `获取商品数据失败：${err.response?.data?.msg || err.message}`;
    rawTarget.value = [];
    allGoods.value = [];
  } finally {
    loading.value = false;
  }
}

// 分类筛选处理（前端筛选，不调用接口）
function handleCategory(category) {
  activeCategory.value = category;
  // 不需要调用接口，shownProducts 会自动根据 activeCategory 从 rawTarget 中筛选
}

// 打开编辑弹窗
const openEditModal = (product) => {
  editingProduct.value = JSON.parse(JSON.stringify(toRaw(product)));
  showEditModal.value = true;
  document.body.style.overflow = 'hidden';
};

// 关闭编辑弹窗
const closeEditModal = () => {
  showEditModal.value = false;
  editingProduct.value = null;
  document.body.style.overflow = '';
};

// 确认更新商品
const confirmUpdate = async () => {
  try {
    if (!editingProduct.value || !editingProduct.value.id) {
      alert("商品数据异常，请重新打开编辑窗口！");
      return;
    }
    if (!editingProduct.value.sellerId || editingProduct.value.sellerId < 1) return alert("卖家ID必须为正整数！");
    if (!editingProduct.value.name?.trim()) return alert("商品名称不能为空！");
    if (!editingProduct.value.unitPrice || editingProduct.value.unitPrice < 1) return alert("单价必须≥1分！");
    if (!editingProduct.value.label?.trim()) return alert("商品标签不能为空！");

    const goodDTO = {
      id: editingProduct.value.id,
      sellerId: Number(editingProduct.value.sellerId),
      name: editingProduct.value.name.trim(),
      content: editingProduct.value.content || "",
      unitPrice: Number(editingProduct.value.unitPrice),
      count: Number(editingProduct.value.count) || 1,
      imgUrl: editingProduct.value.imgUrl || "",
      label: editingProduct.value.label.trim(),
      status: Number(editingProduct.value.status),
      address: editingProduct.value.address || "",
      viewCount: editingProduct.value.viewCount || 0,
      favoriteCount: editingProduct.value.favoriteCount || 0
    };

    const response = await axios.post("/good/update", goodDTO);

    if (response.data.code === 200) {
      // 更新列表中的数据
      const productId = editingProduct.value.id;
      const index = rawTarget.value.findIndex(item => item.id === productId);
      if (index !== -1) {
        rawTarget.value[index] = { ...editingProduct.value, originData: { ...editingProduct.value } };
      }
      closeEditModal();
      alert(`商品ID: ${productId} 修改成功！`);
    } else {
      alert(`修改失败：${response.data.msg}`);
    }
  } catch (err) {
    console.error("更新商品失败：", err);
    alert(`更新失败：${err.response?.data?.msg || err.message}`);
  }
};

// 删除商品
const deleteGood = async (item) => {
  if (!confirm(`确定删除商品ID: ${item.id} 吗？`)) return;

  try {
    const response = await axios.post("/good/deleteByID", { id: item.id });

    if (response.data.code === 200) {
      alert("删除成功！");
      getGoodList(currentPage.value);
    } else {
      alert(`删除失败：${response.data.msg}`);
    }
  } catch (err) {
    console.error("删除商品失败：", err);
    alert(`删除失败：${err.response?.data?.msg || err.message}`);
  }
};

// 收藏切换（模拟）
const toggleFavorite = (product) => {
  product.favoriteCount = (product.favoriteCount || 0) + 1;
};

// 新增商品
const addGood = async () => {
  try {
    if (!newGood.value.sellerId) return alert("卖家ID不能为空！");
    if (!newGood.value.name.trim()) return alert("商品名称不能为空！");
    if (!newGood.value.unitPrice || newGood.value.unitPrice < 1) return alert("单价必须≥1分！");
    if (!newGood.value.label.trim()) return alert("商品标签不能为空！");

    const goodDTO = {
      sellerId: Number(newGood.value.sellerId),
      name: newGood.value.name.trim(),
      content: newGood.value.content || "",
      unitPrice: Number(newGood.value.unitPrice),
      count: Number(newGood.value.count) || 1,
      imgUrl: newGood.value.imgUrl || "",
      label: newGood.value.label.trim(),
      status: Number(newGood.value.status),
      address: newGood.value.address || "",
      viewCount: 0,
      favoriteCount: 0
    };

    const response = await axios.post("/good/insert", goodDTO);

    if (response.data.code === 200) {
      alert("新增商品成功！");
      showAddForm.value = false;
      newGood.value = {
        sellerId: "",
        name: "",
        content: "",
        unitPrice: "",
        count: 1,
        imgUrl: "",
        label: "",
        status: 0,
        address: "",
        viewCount: 0,
        favoriteCount: 0
      };
      getGoodList(1);
    } else {
      alert(`新增失败：${response.data.msg}`);
    }
  } catch (err) {
    console.error("新增商品失败：", err);
    alert(`新增失败：${err.response?.data?.msg || err.message}`);
  }
};

onMounted(() => {
  getGoodList(1);
});
</script>

<style scoped>
/* ==================== Good Body Container ==================== */
.good-body {
  flex: 1;
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 24px;
  padding: 0;
  min-height: 100%;
  background: transparent;
}

/* ==================== Page Header ==================== */
.page-header {
  margin-bottom: 8px;
}

.page-title {
  font-size: 36px;
  font-weight: 300;
  letter-spacing: 0.02em;
  color: #374151;
  margin: 0 0 8px 0;
}

.page-desc {
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #9ca3af;
  margin: 0;
}

/* ==================== Category Filter ==================== */
.category-filter {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.category-btn {
  padding: 10px 24px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.5);
  color: #6b7280;
}

.category-btn:hover {
  background: rgba(255, 255, 255, 0.7);
  color: #374151;
}

.category-btn.active {
  background: linear-gradient(135deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
  color: #fff;
  border-color: transparent;
  box-shadow: 0 4px 16px rgba(236, 72, 153, 0.25);
}

/* ==================== Error/Loading Tip ==================== */
.error-tip {
  padding: 12px 16px;
  background: rgba(236, 72, 153, 0.1);
  border-radius: 12px;
  color: #ec4899;
  font-size: 14px;
}

.loading-tip {
  padding: 12px 16px;
  background: rgba(255, 255, 255, 0.5);
  border-radius: 12px;
  color: #9ca3af;
  font-size: 14px;
}

/* ==================== Glass Card ==================== */
.glass-card {
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(30px) saturate(180%);
  -webkit-backdrop-filter: blur(30px) saturate(180%);
  border: 1px solid rgba(255, 255, 255, 0.9);
  box-shadow: 0 8px 32px rgba(236, 72, 153, 0.06);
}

/* ==================== Add Form ==================== */
.add-form {
  border-radius: 24px;
  padding: 24px;
}

.form-title {
  margin: 0 0 20px 0;
  font-size: 20px;
  font-weight: 300;
  letter-spacing: 0.02em;
  background: linear-gradient(135deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.form-item {
  display: flex;
  align-items: center;
  gap: 12px;
}

.form-item.full-width {
  grid-column: span 2;
}

.form-item label {
  width: 80px;
  text-align: right;
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #374151;
}

.form-item input,
.form-item select,
.form-item textarea {
  flex: 1;
  padding: 10px 14px;
  background: rgba(255, 255, 255, 0.4);
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: 12px;
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #374151;
  transition: all 0.3s ease;
}

.form-item input:focus,
.form-item select:focus,
.form-item textarea:focus {
  border-color: rgba(236, 72, 153, 0.3);
  box-shadow: 0 0 8px rgba(236, 72, 153, 0.08);
  background: rgba(255, 255, 255, 0.6);
}

.form-item textarea {
  min-height: 80px;
  resize: vertical;
}

.form-actions {
  display: flex;
  gap: 12px;
  margin-top: 20px;
  justify-content: flex-end;
}

.submit-btn {
  padding: 12px 24px;
  background: linear-gradient(135deg, #4caf50 0%, #388e3c 100%);
  color: #fff;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  letter-spacing: 0.02em;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(76, 175, 80, 0.2);
}

.submit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(76, 175, 80, 0.3);
}

.cancel-btn {
  padding: 12px 24px;
  background: linear-gradient(135deg, #ef4444 0%, #d32f2f 100%);
  color: #fff;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  letter-spacing: 0.02em;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(239, 68, 68, 0.2);
}

.cancel-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(239, 68, 68, 0.3);
}

/* ==================== Add Button ==================== */
.add-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 20px;
  background: linear-gradient(135deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
  color: #fff;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  letter-spacing: 0.02em;
  transition: all 0.3s ease;
  box-shadow: 0 4px 16px rgba(236, 72, 153, 0.25);
}

.add-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(236, 72, 153, 0.35);
}

.btn-icon {
  color: #fff;
}

/* ==================== Products Grid ==================== */
.products-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
}

.empty-tip {
  grid-column: span 3;
  text-align: center;
  padding: 48px 0;
  color: #ec4899;
  font-size: 16px;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 24px;
}

/* ==================== Product Card ==================== */
.product-card {
  border-radius: 24px;
  padding: 24px;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
  cursor: pointer;
}

.product-card:hover {
  background: rgba(255, 255, 255, 0.65);
  transform: translateY(-4px);
  box-shadow: 0 16px 48px rgba(236, 72, 153, 0.1);
}

/* Shine Effect */
.product-card::after {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(
    45deg,
    transparent 30%,
    rgba(255, 255, 255, 0.3) 50%,
    transparent 70%
  );
  transform: translateX(-100%);
  transition: transform 0.6s ease;
}

.product-card:hover::after {
  transform: translateX(100%);
}

/* ==================== Product Image ==================== */
.product-image {
  position: relative;
  margin-bottom: 20px;
  border-radius: 20px;
  overflow: hidden;
}

.product-img {
  width: 100%;
  aspect-ratio: 1;
  object-fit: cover;
  transition: transform 0.7s ease;
}

.product-card:hover .product-img {
  transform: scale(1.1);
}

.favorite-btn {
  position: absolute;
  top: 12px;
  right: 12px;
  padding: 10px;
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: 50%;
  cursor: pointer;
  opacity: 0;
  transition: all 0.3s ease;
}

.product-card:hover .favorite-btn {
  opacity: 1;
}

.favorite-btn:hover {
  background: #ec4899;
  border-color: #ec4899;
}

.favorite-btn .icon {
  color: #6b7280;
  transition: color 0.3s ease;
}

.favorite-btn:hover .icon {
  color: #fff;
}

/* ==================== Product Info ==================== */
.product-info {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.product-header {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.product-name {
  font-size: 18px;
  font-weight: 300;
  letter-spacing: 0.02em;
  color: #374151;
  margin: 0;
}

.product-tag {
  font-size: 12px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #6b7280;
  padding: 4px 12px;
  background: rgba(255, 255, 255, 0.4);
  border-radius: 8px;
}

.product-stats {
  display: flex;
  align-items: center;
  gap: 8px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.stat-item .icon.star {
  color: #f59e0b;
  fill: #f59e0b;
}

.stat-value {
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #374151;
}

.stat-label {
  font-size: 12px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #9ca3af;
}

.product-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-top: 12px;
}

.product-price {
  font-size: 28px;
  font-weight: 300;
  letter-spacing: 0.02em;
  color: #374151;
}

.product-actions {
  display: flex;
  gap: 8px;
}

.action-btn {
  padding: 10px;
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.action-btn .icon {
  color: #6b7280;
}

.action-btn.edit:hover {
  background: #ec4899;
  border-color: #ec4899;
}

.action-btn.edit:hover .icon {
  color: #fff;
}

.action-btn.delete:hover {
  background: #ef4444;
  border-color: #ef4444;
}

.action-btn.delete:hover .icon {
  color: #fff;
}

.product-status {
  display: flex;
  align-items: center;
}

/* ==================== Status Badge ==================== */
.status-badge {
  display: inline-block;
  padding: 6px 16px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 400;
  letter-spacing: 0.02em;
}

.status-badge.review {
  background: rgba(33, 150, 243, 0.1);
  color: #2196f3;
}

.status-badge.on {
  background: rgba(76, 175, 80, 0.1);
  color: #4caf50;
}

.status-badge.sold {
  background: rgba(255, 152, 0, 0.1);
  color: #ff9800;
}

.status-badge.invalid {
  background: rgba(158, 158, 158, 0.1);
  color: #9e9e9e;
}

/* ==================== Edit Overlay ==================== */
.edit-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(30px);
  border-radius: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10;
}

.edit-form {
  display: flex;
  flex-direction: column;
  gap: 12px;
  width: 90%;
  max-width: 300px;
}

.edit-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.edit-item label {
  width: 70px;
  font-size: 12px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #374151;
}

.edit-input,
.edit-select {
  flex: 1;
  padding: 8px 12px;
  background: rgba(255, 255, 255, 0.5);
  border: 1px solid rgba(236, 72, 153, 0.2);
  border-radius: 8px;
  font-size: 12px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #374151;
}

.edit-input:focus,
.edit-select:focus {
  border-color: rgba(236, 72, 153, 0.4);
  box-shadow: 0 0 6px rgba(236, 72, 153, 0.1);
}

.edit-actions {
  display: flex;
  gap: 8px;
  margin-top: 12px;
  justify-content: center;
}

.btn-confirm {
  padding: 8px 16px;
  background: linear-gradient(135deg, #4caf50 0%, #388e3c 100%);
  color: #fff;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 12px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.btn-confirm:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(76, 175, 80, 0.2);
}

.btn-cancel {
  padding: 8px 16px;
  background: linear-gradient(135deg, #ef4444 0%, #d32f2f 100%);
  color: #fff;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 12px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.btn-cancel:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(239, 68, 68, 0.2);
}

/* ==================== Pagination ==================== */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  padding: 16px 0;
}

.pagination button {
  padding: 10px 20px;
  background: linear-gradient(135deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
  color: #fff;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  letter-spacing: 0.02em;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(236, 72, 153, 0.2);
}

.pagination button:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(236, 72, 153, 0.3);
}

.pagination button:disabled {
  background: rgba(236, 72, 153, 0.3);
  cursor: not-allowed;
  box-shadow: none;
}

.pagination span {
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #374151;
}

/* ==================== Responsive ==================== */
@media (max-width: 1200px) {
  .products-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .empty-tip {
    grid-column: span 2;
  }
}

@media (max-width: 768px) {
  .products-grid {
    grid-template-columns: 1fr;
  }

  .empty-tip {
    grid-column: span 1;
  }

  .form-grid {
    grid-template-columns: 1fr;
  }

  .form-item.full-width {
    grid-column: span 1;
  }
}

/* ==================== Modal Styles ==================== */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(8px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  animation: fadeIn 0.2s ease;
  padding: 20px;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

.modal-card {
  width: 100%;
  max-width: 600px;
  max-height: 90vh;
  overflow-y: auto;
  border-radius: 20px;
  padding: 0;
  animation: slideUp 0.3s ease;
  background: #ffffff;
  border: 2px solid #3b82f6;
  box-shadow:
    0 20px 60px rgba(59, 130, 246, 0.15),
    0 8px 24px rgba(0, 0, 0, 0.1);
}

@keyframes slideUp {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

.modal-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 24px;
  border-bottom: 1px solid #e5e7eb;
  position: sticky;
  top: 0;
  background: #f8fafc;
  z-index: 1;
}

.modal-title {
  font-size: 18px;
  font-weight: 600;
  letter-spacing: 0.02em;
  color: #1f2937;
  margin: 0;
}

.modal-close {
  padding: 8px;
  background: rgba(59, 130, 246, 0.1);
  border: none;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.modal-close:hover { background: #3b82f6; }
.modal-close:hover .icon { color: #fff; }
.modal-close .icon { color: #3b82f6; }

.modal-body {
  padding: 24px;
  background: #ffffff;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 16px 24px;
  border-top: 1px solid #e5e7eb;
  position: sticky;
  bottom: 0;
  background: #f8fafc;
}

.form-textarea {
  padding: 12px 16px;
  background: #f9fafb;
  border: 2px solid #e5e7eb;
  border-radius: 12px;
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #374151;
  min-height: 80px;
  resize: vertical;
  width: 100%;
  transition: all 0.3s ease;
}

.form-textarea:focus {
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
  background: #ffffff;
  outline: none;
}

.modal-footer .btn-confirm {
  padding: 12px 24px;
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  color: #fff;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  letter-spacing: 0.02em;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
}

.modal-footer .btn-confirm:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(59, 130, 246, 0.4);
}

.modal-footer .btn-cancel {
  padding: 12px 24px;
  background: #f3f4f6;
  color: #6b7280;
  border: 2px solid #e5e7eb;
  border-radius: 12px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  letter-spacing: 0.02em;
  transition: all 0.3s ease;
}

.modal-footer .btn-cancel:hover {
  background: #e5e7eb;
  border-color: #d1d5db;
}

/* Modal 内的表单样式 */
.modal-body .form-input,
.modal-body .form-select {
  padding: 12px 16px;
  background: #f9fafb;
  border: 2px solid #e5e7eb;
  border-radius: 12px;
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #374151;
  transition: all 0.3s ease;
  width: 100%;
}

.modal-body .form-input:focus,
.modal-body .form-select:focus {
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
  background: #ffffff;
  outline: none;
}
</style>