package commontrade.discusstrade.handle;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 原生 WebSocket 处理器（无 STOMP/SockJS 依赖）
 */
@Component
public class WebSocketHandle extends TextWebSocketHandler {

    // 保存所有活跃的 WebSocket 会话（线程安全）
    private static final Set<WebSocketSession> SESSIONS = Collections.newSetFromMap(new ConcurrentHashMap<>());

    /**
     * 连接建立时触发
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        SESSIONS.add(session);
        // 向新连接的客户端发送欢迎消息
        session.sendMessage(new TextMessage(LocalDateTime.now() + " - 欢迎连接 WebSocket！"));
        // 广播新用户上线
        broadcastMessage(LocalDateTime.now() + " - 有新客户端上线，当前在线数：" + SESSIONS.size());
    }

    /**
     * 接收客户端消息时触发
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String receivedMsg = message.getPayload();
        String responseMsg = LocalDateTime.now() + " - 服务端收到并广播：" + receivedMsg;

        // 广播消息给所有客户端
        broadcastMessage(responseMsg);
    }

    /**
     * 连接关闭时触发
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        SESSIONS.remove(session);
        // 广播用户下线
        broadcastMessage(LocalDateTime.now() + " - 有客户端下线，当前在线数：" + SESSIONS.size());
    }

    /**
     * 处理消息传输错误
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close(CloseStatus.SERVER_ERROR);
        }
        SESSIONS.remove(session);
        broadcastMessage(LocalDateTime.now() + " - 客户端连接异常：" + exception.getMessage());
    }

    /**
     * 广播消息给所有在线客户端
     */
    private void broadcastMessage(String message) {
        for (WebSocketSession session : SESSIONS) {
            try {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(message));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}