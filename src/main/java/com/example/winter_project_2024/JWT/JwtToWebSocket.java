package com.example.winter_project_2024.JWT;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Slf4j
public class JwtToWebSocket implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler webSocketHandler, Map<String, Object> attributes) throws Exception{
        String queryString = request.getURI().getQuery();
        if (queryString != null && queryString.contains("token=")) {
            String token = extractTokenFromQuery(queryString);
            if(token != null && !token.isEmpty()){
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication != null && authentication.isAuthenticated()) {
                    attributes.put("SPRING_SECURITY_CONTEXT", authentication);
                    return true;
                }
            }
        }
        return false;
    }

    private String extractTokenFromQuery(String queryString) {
        // 쿼리 파라미터에서 "token" 값 추출
        String[] params = queryString.split("&");
        for (String param : params) {
            if (param.startsWith("token=")) {
                return param.substring("token=".length());
            }
        }
        return null;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        log.info(" >>> handshake success");
    }
}
