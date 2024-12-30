package com.example.winter_project_2024.Config;


import com.example.winter_project_2024.Handler.IndianPokerHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

//    private final WebsocketView
    private final IndianPokerHandler indianPokerHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry){
        //registry.addHandler(indianPokerHandler, "/ws/connect").setAllowedOrigins("https://indianpoker.mooo.com");
        registry.addHandler(indianPokerHandler, "/ws/connect").setAllowedOrigins("http://118.218.179.22:9998", "http://192.168.45.85:80");
    }
}
