package commontrade.discusstrade.interceptor;


import commontrade.discusstrade.Utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * WebSocket 安全认证拦截器
 * 只允许合法 Token 连接
 */
@Component
public class WsAuthInterceptor implements HandshakeInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {

        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
        HttpServletRequest req = servletRequest.getServletRequest();


        String[] string = request.getURI().toString().split("=");
        String[] string1 = string[1].split("&");
        String token = string1[0];
        if (token == null || token.isBlank()) {
            return false; // 拒绝连接
        }

        // ==================== 核心：校验 Token 并获取 userId ====================
        String userStr = jwtUtil.parseJwt(token).get("userId", String.class); // 你自己的JWT工具
        Long userId = Long.parseLong(userStr);

        System.out.println(userId);
        // 把真实 userId 存入会话属性
        attributes.put("userId", userId);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception ex) {}
}