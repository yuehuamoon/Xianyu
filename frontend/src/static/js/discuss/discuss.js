let ws = null;

export function connect(token, onMessage) {
    if (ws) ws.close();
    
    ws = new WebSocket(`ws://localhost:8085/ws?token=${encodeURIComponent(token)}`);
    
    ws.onopen = () => {
        console.log("✅ WebSocket 连接成功");
    };
    
    ws.onmessage = (e) => {
        console.log("📩 收到消息:", e.data);
        onMessage && onMessage(e.data);
    };
    
    ws.onerror = (err) => {
        console.error("❌ WebSocket 错误:", err);
    };
    
    ws.onclose = () => {
        console.log("🔌 WebSocket 连接关闭");
    };
}

export function send(data) {
    if (ws && ws.readyState === WebSocket.OPEN) {
        ws.send(JSON.stringify({
            type: "private",
            toUserId: data.toUserId,
            content: data.content
        }));
    }
}

export function disconnect() {
    if (ws) {
        ws.close();
        ws = null;
    }
}