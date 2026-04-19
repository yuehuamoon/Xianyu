<template>
    <div class="comment">
        <div class="input">
            <input class="input-box"  type="text" v-model="msg">
            <div class="input-button" @click="send">发送</div>
        </div>
        <div class="msg">
            <div v-if="contents.length == 0">
                暂无评论
            </div>
            <comment 
            v-for="item in contents"
            :key="item.id"
            :data="item"
            />
        </div>

    </div>
</template>

<script setup>
import axios from 'axios';
import comment from '@/item/comment.vue'
import { onMounted, ref } from 'vue';

const userInfo = parseJwt(localStorage.getItem("token"));
const contents = ref([])
const props = defineProps({
  data: {
    type: Object,
    required: true,
    default: () => ({})
  }
})

const msg  = ref()

async function getMsg(){
    const res = await axios.post(
        "/community/getComment",
        {
            'id': props.data.id,
            'type': props.data.type,
        }
    )
    contents.value = res.data.data;
}


function parseJwt(token) {
  try {
    const base64Payload = token.split('.')[1]; // JWT 中间段
    const payload = atob(base64Payload);      // Base64 解码
    return JSON.parse(payload);                // 转成对象
  } catch (e) {
    return null;
  }
}

async function send() {
    console.log(props.data.data)
    const res = await axios.post(
        "/community/insertComment",
        {
          'id': props.data.id,
          'userId': userInfo.userId,
          'type': props.data.type,
          'content': msg.value,
          'userName': userInfo.phone,
          'goodId': props.data.id,
          'goodName': props.data.data?.name || '',
          'attitude': 1,
          'postId':  props.data.id,
          'toUserId': props.data.data?.id || '',
          'toUserName': props.data.data?.nickName || '',

        }
    );

    window.location.reload();
}

onMounted(getMsg)
</script>

<style scoped>
/* Liquid Glass Style - Based on Admin Dashboard Design */
.comment {
  padding: 16px;
  background: rgba(255, 255, 255, 0.5);
  backdrop-filter: blur(20px) saturate(180%);
  -webkit-backdrop-filter: blur(20px) saturate(180%);
  border: 1px solid rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(236, 72, 153, 0.04);
}

.input {
  display: flex;
  gap: 12px;
  padding: 16px;
  background: rgba(255, 255, 255, 0.6);
  border-radius: 16px;
  border: 2px solid rgba(236, 72, 153, 0.15);
  box-shadow:
    0 8px 32px rgba(236, 72, 153, 0.1),
    0 2px 8px rgba(236, 72, 153, 0.06),
    inset 0 1px 2px rgba(255, 255, 255, 0.9);
  transition: all 0.3s ease;
}

.input:focus-within {
  border-color: rgba(236, 72, 153, 0.3);
  box-shadow:
    0 12px 40px rgba(236, 72, 153, 0.15),
    0 4px 12px rgba(236, 72, 153, 0.08),
    inset 0 1px 2px rgba(255, 255, 255, 0.9);
}

.input-box {
  flex: 1;
  padding: 12px 16px;
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
  border: 1px solid rgba(236, 72, 153, 0.1);
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(20px) saturate(180%);
  -webkit-backdrop-filter: blur(20px) saturate(180%);
  outline: none;
  color: #374151;
  transition: all 0.3s ease;
  box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.05);
}

.input-box::placeholder {
  color: rgba(107, 114, 128, 0.6);
}

.input-box:focus {
  background: rgba(255, 255, 255, 0.7);
  border-color: rgba(236, 72, 153, 0.3);
  box-shadow:
    0 0 0 3px rgba(236, 72, 153, 0.06),
    0 4px 16px rgba(236, 72, 153, 0.08);
}

.input-button {
  padding: 12px 20px;
  font-size: 14px;
  font-weight: 500;
  letter-spacing: 0.02em;
  background: linear-gradient(135deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
  color: #fff;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 12px rgba(236, 72, 153, 0.2);
}

.input-button:hover {
  transform: translateY(-2px);
  box-shadow:
    0 12px 40px rgba(236, 72, 153, 0.25),
    0 4px 12px rgba(236, 72, 153, 0.15);
}

.msg {
  margin-top: 16px;
  color: #6b7280;
  font-size: 14px;
  font-weight: 400;
  letter-spacing: 0.02em;
}
</style>