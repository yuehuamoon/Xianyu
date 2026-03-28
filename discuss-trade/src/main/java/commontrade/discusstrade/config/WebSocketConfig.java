package commontrade.discusstrade.config;

import commontrade.discusstrade.handle.MyWsHandler;
import commontrade.discusstrade.interceptor.WsAuthInterceptor;
import org.apache.tomcat.websocket.WsWebSocketContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import jakarta.annotation.Resource;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * 原生 WebSocket 配置（无 STOMP 依赖）
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Resource
    private WsAuthInterceptor wsAuthInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(wsHandler(), "/ws")
                .addInterceptors(wsAuthInterceptor) // 安全拦截
                .setAllowedOrigins("*");
    }

    @Bean
    public TextWebSocketHandler wsHandler() {
        return new MyWsHandler();
    }
}