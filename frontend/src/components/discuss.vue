<template>
    <div class="discuss-bg" @mousemove="handleGlobalMouseMove" @mouseleave="handleGlobalMouseLeave">
        <!-- 全局鼠标光晕 -->
        <div class="mouse-glow" :style="glowStyle"></div>

        <div class="discuss">
            <div class="message-box" ref="msgBox">
                <!-- 消息列表 -->
                <div v-for="(msg, index) in messages" :key="index" class="message">
                    {{ msg }}
                </div>
            </div>
            <div class="input-box">
                <input type="text" v-model="inputMsg" @keyup.enter="sendMsg">
                <button @click="sendMsg">发送</button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { onMounted, ref, nextTick, reactive, computed } from 'vue';
import { connect, send, disconnect } from "@/static/js/discuss/discuss.js";
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter();

const messages = ref([]);
const inputMsg = ref();

// 全局鼠标光晕位置
const glowPosition = reactive({
    x: 0,
    y: 0,
    visible: false
});

const glowStyle = computed(() => ({
    left: `${glowPosition.x}px`,
    top: `${glowPosition.y}px`,
    opacity: glowPosition.visible ? 1 : 0
}));

function handleGlobalMouseMove(e) {
    glowPosition.x = e.clientX;
    glowPosition.y = e.clientY;
    glowPosition.visible = true;
}

function handleGlobalMouseLeave() {
    glowPosition.visible = false;
}

const msgBox = ref(null);
const token = localStorage.getItem("token");

function parseJwt(token) {
    try {
        const base64Payload = token.split('.')[1];
        const payload = atob(base64Payload);
        return JSON.parse(payload);
    } catch (e) {
        return null;
    }
}

let info = parseJwt(token);

function startWs() {
    connect(token, (data) => {
        console.log(data);
        messages.value.push(data);
        scrollToBottom();
    });
}

function sendMsg() {
    const toUserId = route.params.userId;
    
    if (!toUserId) {
        alert("未指定接收用户");
        return;
    }
    
    if (toUserId == info.userId) {
        alert("不能给自己发消息");
        return;
    }
    
    send({ content: inputMsg.value, toUserId: Number(toUserId) });
    inputMsg.value = "";
    scrollToBottom();
}

function checkId() {
    const toUserId = route.params.userId;
    
    if (toUserId == info.userId) {
        alert("不能和自己聊天");
        router.push('/');
        return;
    }
}

function scrollToBottom() {
    nextTick(() => {
        if (msgBox.value) {
            msgBox.value.scrollTop = msgBox.value.scrollHeight;
        }
    });
}

onMounted(() => {
    checkId();
    startWs();
});
</script>

<style scoped>
.discuss-bg {
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
    padding: 24px;
}

@keyframes subtleGradient {
    0%, 100% { background-position: 0% 50%; }
    50% { background-position: 100% 50%; }
}

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

.discuss {
    display: flex;
    flex-direction: column;
    height: calc(100vh - 48px);
    max-width: 800px;
    margin: 0 auto;
}

.message-box {
    flex: 1;
    overflow-y: auto;
    padding: 16px;
    background: rgba(255, 255, 255, 0.4);
    backdrop-filter: blur(20px) saturate(180%);
    border: 1px solid rgba(255, 255, 255, 0.8);
    border-radius: 16px;
    margin-bottom: 12px;
    box-shadow: 0 4px 20px rgba(236, 72, 153, 0.04);
}

.message {
    padding: 10px 14px;
    margin: 8px 0;
    background: rgba(255, 255, 255, 0.5);
    backdrop-filter: blur(10px);
    border: 1px solid rgba(255, 255, 255, 0.8);
    border-radius: 12px;
    max-width: 75%;
    font-size: 14px;
    color: #374151;
}

.input-box {
    display: flex;
    gap: 10px;
}

input {
    flex: 1;
    padding: 12px 16px;
    border: 1px solid rgba(255, 255, 255, 0.5);
    border-radius: 12px;
    background: rgba(255, 255, 255, 0.4);
    backdrop-filter: blur(20px) saturate(180%);
    outline: none;
    font-size: 14px;
    color: #374151;
}

input:focus {
    background: rgba(255, 255, 255, 0.7);
    border-color: rgba(236, 72, 153, 0.3);
}

button {
    padding: 12px 20px;
    background: linear-gradient(135deg, #f472b6 0%, #ec4899 50%, #f43f5e 100%);
    color: #fff;
    border: none;
    border-radius: 12px;
    cursor: pointer;
    font-size: 14px;
    font-weight: 500;
    box-shadow: 0 4px 12px rgba(236, 72, 153, 0.2);
}

button:hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 16px rgba(236, 72, 153, 0.3);
}

.message-box::-webkit-scrollbar {
    width: 6px;
}

.message-box::-webkit-scrollbar-track {
    background: transparent;
}

.message-box::-webkit-scrollbar-thumb {
    background: rgba(236, 72, 153, 0.2);
    border-radius: 3px;
}
</style>