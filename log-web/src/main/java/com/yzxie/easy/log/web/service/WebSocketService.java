package com.yzxie.easy.log.web.service;

import com.yzxie.easy.log.web.data.websocket.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * @author xieyizun
 * @date 4/11/2018 15:09
 * @description:
 */
@Service
public class WebSocketService {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    /**
     * 向浏览器广播消息
     */
    public void broadcastMessageToClients(long broadcastCount) {
        simpMessagingTemplate.convertAndSend("/topic/realLog",
                new Response("hello world: " + broadcastCount));
    }

    public void broadcastMessageToClients(String content) {
        simpMessagingTemplate.convertAndSend("/topic/realLog",
                new Response(content));
    }

    public void sendChatMessage(String username, String content) {
        simpMessagingTemplate.convertAndSendToUser(username, "/queue/chat", content);
    }
}
