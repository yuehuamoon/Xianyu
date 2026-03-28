package commontrade.discusstrade.handle;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;


@Component
public class MyWsHandler extends TextWebSocketHandler {

    // 在线用户
    public static final ConcurrentHashMap<Long, WebSocketSession> USER_SESSION_MAP = new ConcurrentHashMap<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    // ---------------------- 连接成功 ----------------------
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        try {
            Object userIdObj = session.getAttributes().get("userId");
            Long userId = Long.valueOf(userIdObj.toString());
            USER_SESSION_MAP.put(userId, session);

            // ✅ 一连接就发广播测试（所有人都能收到）
            broadcastMessage("系统公告：用户 " + userId + " 进入聊天室");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------------- 断开连接 ----------------------
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        try {
            Object userIdObj = session.getAttributes().get("userId");
            Long userId = Long.valueOf(userIdObj.toString());
            System.out.println(userId);
            USER_SESSION_MAP.remove(userId);
            System.out.println("❌ 用户断开：" + userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------------- 收到消息 → 广播给所有人 ----------------------




    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            String payload = message.getPayload();
            System.out.println("📩 收到消息：" + payload);

            // 当前用户
            Long fromUserId = Long.valueOf(session.getAttributes().get("userId").toString());

            // 解析 JSON
            Map<String, Object> map = objectMapper.readValue(payload, Map.class);

            String type = (String) map.get("type");
            if ("private".equals(type)) {
                // ✅ 私聊
                Long toUserId = Long.valueOf(map.get("toUserId").toString());
                String content =(String) map.get("content");

                sendPrivateMessage(fromUserId, toUserId, content);

            } else {
                // ✅ 广播
                broadcastMessage("用户" + fromUserId + "：" + payload);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------------- 【核心】广播方法（真正全局） ----------------------
    public static void broadcastMessage(String text) {
        TextMessage msg = new TextMessage(text);
        for (WebSocketSession s : USER_SESSION_MAP.values()) {
            try {
                if (s.isOpen()) {
                    s.sendMessage(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void sendPrivateMessage(Long fromUserId, Long toUserId, String content) {

        WebSocketSession toSession = USER_SESSION_MAP.get(toUserId);
        WebSocketSession fromSession = USER_SESSION_MAP.get(fromUserId);

        String msg = "【私聊】" + fromUserId + " → " + toUserId + "：" + content;

        try {
            // 发给对方
            if (toSession != null && toSession.isOpen()) {
                toSession.sendMessage(new TextMessage(msg));
            } else {
                // 对方不在线
                if (fromSession != null && fromSession.isOpen()) {
                    fromSession.sendMessage(new TextMessage("❌ 用户 " + toUserId + " 不在线"));
                }
                return;
            }

            // 发给自己（回显）
            if (fromSession != null && fromSession.isOpen()) {
                fromSession.sendMessage(new TextMessage(msg));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}