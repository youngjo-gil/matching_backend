package com.matching.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    // Client websocket 연결 API 경로 설정 메소드
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp/chat")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    // 메시지 받을 관련 경로 설정
    // queue, topic 두 경로가 prefix로 붙은 경우, messageBroker가 캐치하여 채당 채팅방 클라이언트에게 메시지 전달
    // 주로 queue는 1대 1 , topic 1대 다 사용
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
       registry.enableSimpleBroker("/sub");
       registry.setApplicationDestinationPrefixes("/pub");
    }
}
