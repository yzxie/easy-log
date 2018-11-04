package com.yzxie.easy.log.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

/**
 * @author xieyizun
 * @date 4/11/2018 14:41
 * @description:
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketHandler.class);

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 允许使用socketJs方式访问，访问点为webSocketServer，允许跨域
        // 在网页上我们就可以通过这个链接
        // http://localhost:8088/broadcastEndPoint
        // 来和服务器的WebSocket连接
        registry.addEndpoint("/broadcastEndPoint").setAllowedOrigins("http://localhost:3000").withSockJS();
        registry.addEndpoint("/p2pEndPoint").setAllowedOrigins("http://localhost:3000").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // topic 广播消息代理
        // queue 点对点消息代理，不加多一个queue，也可以使用topic。
        // 点对点：需要在客户端加上前戳/user,
        // 使用/user拼接convertAndSendToUser方法对应destination参数订阅， 如/user/queue/chat，作为sockjs的subscribe的topic
        registry.enableSimpleBroker("/topic", "/queue");
        // 客户端请求前戳
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.addDecoratorFactory(new WebSocketHandlerDecoratorFactory() {
            @Override
            public WebSocketHandler decorate(WebSocketHandler webSocketHandler) {
                return new WebSocketHandlerDecorator(webSocketHandler) {
                    @Override
                    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
                        // 客户端与服务器端建立连接后，此处记录谁上线了
                        //String username = session.getPrincipal().getName();
                        String sessionId = session.getId();
                        LOG.info("online: " + sessionId);
                        super.afterConnectionEstablished(session);
                    }

                    @Override
                    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                        // 客户端与服务器端断开连接后，此处记录谁下线了
                        //String username = session.getPrincipal().getName();
                        String sessionId = session.getId();
                        LOG.info("offline: " + sessionId);
                        super.afterConnectionClosed(session, closeStatus);
                    }
                };
            }
        });
    }
}
