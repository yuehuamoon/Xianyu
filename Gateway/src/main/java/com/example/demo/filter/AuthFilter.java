package com.example.demo.filter;

import com.example.demo.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * 全局认证过滤器（所有路由共用放行规则，无需逐个配置）
 */
@Component
public class AuthFilter implements GlobalFilter {

    @Autowired
    private JwtUtil jwtUtil;

    // 全局放行路径（一次配置，所有路由生效）
    private static final List<String> GLOBAL_ALLOW_PATHS = Arrays.asList(
            "/user/login",    // 所有服务的 login 接口（如 /user/login、/order/login）
            "/**/register/**", // 所有服务的注册接口
            "/**/health/**",   // 所有服务的健康检查接口
            "/**/captcha/**"   // 验证码接口
    );

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String requestPath = request.getPath().value();

        // ========== 全局放行逻辑（所有路由共用） ==========
        for (String allowPath : GLOBAL_ALLOW_PATHS) {
            if (pathMatcher.match(allowPath, requestPath)) {
                return chain.filter(exchange); // 直接放行，无需认证
            }
        }

        // ========== 原有认证逻辑（仅对非放行URL生效） ==========
        String authHeader = request.getHeaders().getFirst("Authorization");
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            return buildErrorResponse(response, HttpStatus.UNAUTHORIZED, "Token不存在或格式错误（需Bearer + Token）");
        }

        String jwtToken = authHeader.substring(7).trim();
        try {
            Claims claims = jwtUtil.parseJwt(jwtToken);
            String userId = claims.get("userId", String.class);
            String username = claims.get("username", String.class);
            if (userId == null || username == null) {
                return buildErrorResponse(response, HttpStatus.UNAUTHORIZED, "Token中无有效用户信息");
            }

            // 传递用户信息到下游服务
            ServerHttpRequest newRequest = request.mutate()
                    .header("X-User-Id", userId)
                    .header("X-Username", username)
                    .build();
            exchange = exchange.mutate().request(newRequest).build();

        } catch (JwtException e) {
            return buildErrorResponse(response, HttpStatus.UNAUTHORIZED, "Token无效或已过期：" + e.getMessage());
        } catch (Exception e) {
            return buildErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "认证服务异常：" + e.getMessage());
        }

        return chain.filter(exchange);
    }

    private Mono<Void> buildErrorResponse(ServerHttpResponse response, HttpStatus status, String msg) {
        response.setStatusCode(status);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        String errorJson = String.format("{\"code\":%d,\"msg\":\"%s\",\"success\":false}", status.value(), msg);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(errorJson.getBytes(StandardCharsets.UTF_8))));
    }
}