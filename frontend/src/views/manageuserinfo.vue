<template>
  <div class="user-body">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2 class="page-title">用户管理</h2>
      <p class="page-desc">来自 Spring /user 接口的用户数据</p>
    </div>

    <!-- 搜索区域 -->
    <div class="search-bar glass-card">
      <div class="search-title">用户搜索</div>
      <input
        type="text"
        v-model="searchKey"
        placeholder="用户名/昵称/手机号"
        class="search-input"
      >
      <button class="search-btn" @click="search">
        <Search class="icon" :size="16" :stroke-width="1.5" />
        <span>搜索</span>
      </button>
    </div>

    <!-- 错误/加载提示 -->
    <div v-if="error" class="error-tip">{{ error }}</div>
    <div v-if="loading" class="loading-tip">加载中...</div>

    <!-- 用户卡片网格 -->
    <div class="users-grid">
      <div v-if="rawTarget.length === 0 && !loading" class="empty-tip">
        暂无用户数据
      </div>

      <div
        v-for="user in rawTarget"
        :key="user.id"
        class="user-card glass-card shine-effect"
      >
        <!-- 用户头像 -->
        <div class="user-avatar">
          <img
            :src="user.avatarUrl || 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=100&h=100&fit=crop'"
            :alt="user.realName"
            class="avatar-img"
          >
          <div class="status-indicator" :class="user.status === 1 ? 'active' : 'inactive'"></div>
        </div>

        <!-- 用户信息 -->
        <div class="user-info">
          <div class="user-header">
            <h3 class="user-name">{{ user.realName }}</h3>
            <span class="user-nickname">{{ user.nickName || '未设置昵称' }}</span>
          </div>

          <div class="user-meta">
            <div class="meta-item">
              <Phone class="icon" :size="14" :stroke-width="1.5" />
              <span class="meta-value">{{ user.phone || '未填写' }}</span>
            </div>
            <div class="meta-item">
              <MapPin class="icon" :size="14" :stroke-width="1.5" />
              <span class="meta-value">{{ user.address || '未填写' }}</span>
            </div>
          </div>

          <div class="user-stats">
            <div class="stat-badge role" :class="user.role === 1 ? 'admin' : 'user'">
              {{ user.role === 1 ? '管理员' : '普通用户' }}
            </div>
            <div class="stat-badge wallet">
              <Wallet class="icon" :size="14" :stroke-width="1.5" />
              <span>¥{{ formatWallet(user.wallet) }}</span>
            </div>
          </div>

          <div class="user-footer">
            <div class="create-time">
              <Clock class="icon" :size="12" :stroke-width="1.5" />
              <span>{{ formatTime(user.createTime) }}</span>
            </div>
            <div class="action-buttons">
              <button class="action-btn edit" @click="openEditModal(user)">
                <Edit2 class="icon" :size="14" :stroke-width="1.5" />
              </button>
              <button class="action-btn delete" @click="deleteUser(user)">
                <Trash2 class="icon" :size="14" :stroke-width="1.5" />
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 分页控件 -->
    <div class="pagination">
      <button @click="getInfo(currentPage - 1)" :disabled="currentPage <= 1">上一页</button>
      <span>第 {{ currentPage }} 页</span>
      <button @click="getInfo(currentPage + 1)">下一页</button>
    </div>

    <!-- 编辑弹窗 -->
    <Teleport to="body">
      <div v-if="showEditModal" class="modal-overlay" @click.self="closeEditModal">
        <div class="modal-card glass-card">
          <div class="modal-header">
            <h3 class="modal-title">编辑用户 #{{ editingUser?.id }}</h3>
            <button class="modal-close" @click="closeEditModal">
              <X class="icon" :size="20" :stroke-width="1.5" />
            </button>
          </div>
          <div class="modal-body">
            <div class="form-grid">
              <div class="form-item">
                <label>真实姓名 <span class="required">*</span></label>
                <input type="text" v-model="editingUser.realName" class="form-input" placeholder="必填">
              </div>
              <div class="form-item">
                <label>昵称</label>
                <input type="text" v-model="editingUser.nickName" class="form-input" placeholder="选填">
              </div>
              <div class="form-item">
                <label>手机号</label>
                <input type="text" v-model="editingUser.phone" class="form-input" placeholder="选填">
              </div>
              <div class="form-item">
                <label>钱包余额</label>
                <input type="number" v-model="editingUser.wallet" class="form-input" step="0.01" min="0">
              </div>
              <div class="form-item">
                <label>角色</label>
                <select v-model="editingUser.role" class="form-select">
                  <option :value="2">普通用户</option>
                  <option :value="1">管理员</option>
                  <option :value="0">其他</option>
                </select>
              </div>
              <div class="form-item">
                <label>状态</label>
                <select v-model="editingUser.status" class="form-select">
                  <option :value="0">禁用</option>
                  <option :value="1">正常</option>
                </select>
              </div>
              <div class="form-item full-width">
                <label>地址</label>
                <input type="text" v-model="editingUser.address" class="form-input" placeholder="选填">
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
import { onMounted, ref, toRaw } from "vue";
import { Search, Phone, MapPin, Wallet, Clock, Edit2, Trash2, X } from 'lucide-vue-next';

// 响应式数据
const searchKey = ref("");
const rawTarget = ref([]);
const currentPage = ref(1);
const loading = ref(true);
const error = ref('');

// 弹窗相关
const showEditModal = ref(false);
const editingUser = ref(null);

// 格式化钱包余额
const formatWallet = (wallet) => {
  if (!wallet) return '0.00';
  return Number(wallet).toFixed(2);
};

// 时间格式化
const formatTime = (utcTime) => {
  if (!utcTime) return '未填写';
  const date = new Date(utcTime);
  return date.toLocaleString('zh-CN', {
    year: 'numeric', month: '2-digit', day: '2-digit',
    hour: '2-digit', minute: '2-digit'
  }).replace(/\//g, '-');
};

// 获取用户数据
async function getInfo(page = 1) {
  loading.value = true;
  error.value = '';
  try {
    const res = await axios.post("/user/page", { page: page });
    const data = res.data?.data || [];
    rawTarget.value = JSON.parse(JSON.stringify(data)).map(item => ({
      ...item,
      originData: { ...item }
    }));
    currentPage.value = page;
  } catch (err) {
    console.error("获取用户数据失败：", err);
    error.value = `数据加载失败：${err.message || "网络异常"}`;
    rawTarget.value = [];
  } finally {
    loading.value = false;
  }
}

// 打开编辑弹窗
const openEditModal = (user) => {
  editingUser.value = { ...user };
  showEditModal.value = true;
  document.body.style.overflow = 'hidden';
};

// 关闭编辑弹窗
const closeEditModal = () => {
  showEditModal.value = false;
  editingUser.value = null;
  document.body.style.overflow = '';
};

// 删除用户
const deleteUser = async (user) => {
  if (!confirm(`确定删除用户 ${user.realName} 吗？`)) return;

  try {
    const response = await axios.post("/user/delete", { phone: user.phone });

    if (response.data.code === 200) {
      alert("删除成功！");
      getInfo(currentPage.value);
    } else {
      alert(`删除失败：${response.data.msg}`);
    }
  } catch (err) {
    console.error("删除用户失败：", err);
    alert(`删除失败：${err.response?.data?.msg || err.message}`);
  }
};

// 确认更新
const confirmUpdate = async () => {
  try {
    if (!editingUser.value || !editingUser.value.id) {
      alert("用户数据异常，请重新打开编辑窗口！");
      return;
    }
    if (!editingUser.value.realName?.trim()) {
      alert("真实姓名不能为空！");
      return;
    }
    if (editingUser.value.wallet < 0) {
      alert("钱包余额不能为负数！");
      return;
    }
    if (editingUser.value.phone && !/^1[3-9]\d{9}$/.test(editingUser.value.phone.trim())) {
      alert("手机号格式不正确！");
      return;
    }

    const res = await axios.post(`/user/update`, {
      id: editingUser.value.id,
      realName: editingUser.value.realName?.trim() || "",
      nickName: editingUser.value.nickName || "",
      phone: editingUser.value.phone?.trim() || "",
      role: Number(editingUser.value.role ?? 2),
      status: Number(editingUser.value.status ?? 1),
      wallet: Number(editingUser.value.wallet ?? 0),
      address: editingUser.value.address || ""
    });

    if (res.data.code === 200) {
      const userName = editingUser.value.realName;
      const index = rawTarget.value.findIndex(item => item.id === editingUser.value.id);
      if (index !== -1) {
        rawTarget.value[index] = { ...editingUser.value, originData: { ...editingUser.value } };
      }
      closeEditModal();
      alert(`用户 ${userName} 修改成功！`);
    } else {
      alert(`修改失败：${res.data.msg}`);
    }
  } catch (err) {
    console.error("更新失败：", err);
    alert(`修改失败：${err.response?.data?.msg || err.message}`);
  }
};

// 搜索功能
async function search() {
  if (!searchKey.value.trim()) {
    getInfo(1);
    return;
  }
  const keyword = searchKey.value.trim().toLowerCase();
  const filtered = rawTarget.value.filter(
    (item) =>
      (item.realName && item.realName.toLowerCase().includes(keyword)) ||
      (item.nickName && item.nickName.toLowerCase().includes(keyword)) ||
      (item.phone && item.phone.includes(keyword))
  );
  rawTarget.value = filtered;
}

onMounted(() => {
  getInfo(1);
});
</script>

<style scoped>
.user-body {
  flex: 1;
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 24px;
  padding: 0;
  min-height: 100%;
  background: transparent;
}

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

.glass-card {
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(30px) saturate(180%);
  -webkit-backdrop-filter: blur(30px) saturate(180%);
  border: 1px solid rgba(255, 255, 255, 0.9);
  box-shadow: 0 8px 32px rgba(236, 72, 153, 0.06);
}

.search-bar {
  border-radius: 16px;
  padding: 16px 20px;
  display: flex;
  align-items: center;
  gap: 16px;
}

.search-title {
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #374151;
}

.search-input {
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

.search-input:focus {
  border-color: rgba(236, 72, 153, 0.3);
  box-shadow: 0 0 8px rgba(236, 72, 153, 0.08);
  background: rgba(255, 255, 255, 0.6);
}

.search-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 18px;
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

.search-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(236, 72, 153, 0.3);
}

.search-btn .icon { color: #fff; }

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

.users-grid {
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

.user-card {
  border-radius: 24px;
  padding: 24px;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
  cursor: pointer;
}

.user-card:hover {
  background: rgba(255, 255, 255, 0.65);
  transform: translateY(-4px);
  box-shadow: 0 16px 48px rgba(236, 72, 153, 0.1);
}

.user-card::after {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(45deg, transparent 30%, rgba(255, 255, 255, 0.3) 50%, transparent 70%);
  transform: translateX(-100%);
  transition: transform 0.6s ease;
}

.user-card:hover::after {
  transform: translateX(100%);
}

.user-avatar {
  position: relative;
  margin-bottom: 20px;
  display: flex;
  justify-content: center;
}

.avatar-img {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid rgba(236, 72, 153, 0.1);
  transition: all 0.3s ease;
}

.user-card:hover .avatar-img {
  border-color: rgba(236, 72, 153, 0.3);
  transform: scale(1.05);
}

.status-indicator {
  position: absolute;
  bottom: 5px;
  right: calc(50% - 45px);
  width: 16px;
  height: 16px;
  border-radius: 50%;
  border: 2px solid #fff;
}

.status-indicator.active { background: #4caf50; }
.status-indicator.inactive { background: #9e9e9e; }

.user-info {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.user-header { text-align: center; }

.user-name {
  font-size: 18px;
  font-weight: 300;
  letter-spacing: 0.02em;
  color: #374151;
  margin: 0 0 4px 0;
}

.user-nickname {
  font-size: 12px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #9ca3af;
}

.user-meta {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.meta-item .icon { color: #9ca3af; }

.meta-value {
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #374151;
}

.user-stats {
  display: flex;
  justify-content: center;
  gap: 12px;
}

.stat-badge {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 400;
  letter-spacing: 0.02em;
}

.stat-badge.role.admin {
  background: rgba(236, 72, 153, 0.1);
  color: #ec4899;
}

.stat-badge.role.user {
  background: rgba(33, 150, 243, 0.1);
  color: #2196f3;
}

.stat-badge.wallet {
  background: rgba(76, 175, 80, 0.1);
  color: #4caf50;
}

.stat-badge.wallet .icon { color: #4caf50; }

.user-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-top: 12px;
  border-top: 1px solid rgba(255, 255, 255, 0.3);
}

.create-time {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #9ca3af;
}

.create-time .icon { color: #9ca3af; }

.action-buttons {
  display: flex;
  gap: 8px;
}

.action-btn {
  padding: 8px;
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.action-btn .icon { color: #6b7280; }

.action-btn.edit:hover {
  background: #ec4899;
  border-color: #ec4899;
}

.action-btn.edit:hover .icon { color: #fff; }

.action-btn.delete:hover {
  background: #ef4444;
  border-color: #ef4444;
}

.action-btn.delete:hover .icon { color: #fff; }

/* Modal Styles */
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
  max-width: 560px;
  max-height: 90vh;
  border-radius: 20px;
  padding: 0;
  overflow-y: auto;
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
  background: #f8fafc;
  position: sticky;
  top: 0;
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

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.form-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-item.full-width { grid-column: span 2; }

.form-item label {
  font-size: 14px;
  font-weight: 500;
  letter-spacing: 0.02em;
  color: #374151;
}

.form-item label .required { color: #3b82f6; }

.form-input,
.form-select {
  padding: 12px 16px;
  background: #f9fafb;
  border: 2px solid #e5e7eb;
  border-radius: 12px;
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  color: #374151;
  transition: all 0.3s ease;
}

.form-input:focus,
.form-select:focus {
  border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
  background: #ffffff;
  outline: none;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 16px 24px;
  border-top: 1px solid #e5e7eb;
  background: #f8fafc;
  position: sticky;
  bottom: 0;
}

.btn-confirm {
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

.btn-confirm:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(59, 130, 246, 0.4);
}

.btn-cancel {
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

.btn-cancel:hover {
  background: #e5e7eb;
  border-color: #d1d5db;
}

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

@media (max-width: 1200px) {
  .users-grid { grid-template-columns: repeat(2, 1fr); }
  .empty-tip { grid-column: span 2; }
}

@media (max-width: 768px) {
  .users-grid { grid-template-columns: 1fr; }
  .empty-tip { grid-column: span 1; }
  .form-grid { grid-template-columns: 1fr; }
  .form-item.full-width { grid-column: span 1; }
}
</style>