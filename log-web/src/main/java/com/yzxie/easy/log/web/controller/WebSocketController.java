package com.yzxie.easy.log.web.controller;

import com.yzxie.easy.log.web.data.ResData;
import com.yzxie.easy.log.web.service.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author xieyizun
 * @date 4/11/2018 18:06
 * @description:
 */
@RestController
public class WebSocketController {
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketController.class);

    private AtomicLong broadCastCount = new AtomicLong();

    @Autowired
    private WebSocketService webSocketService;

    /**
     * 测试广播
     * @return
     */
    @RequestMapping("/testBroadCast")
    public ResData testBroadCast() {
        LOG.info("WebSocketController testBroadCast.");
        long count = broadCastCount.incrementAndGet();
        webSocketService.broadcastMessageToClients(count);
        return new ResData("broadcastToClients");
    }

    /**
     * 接收客户端发送过来的消息
     * /acceptMessage对应客户端sockjs.send方法的topic参数
     * @param message
     */
    @MessageMapping("/acceptMessage")
    public void acceptMessage(Message message) {
        LOG.info("WebSocketController acceptMessage: {}", message.toString());
        webSocketService.broadcastMessageToClients("Welcome, i am easy-log.");
    }

    /**
     * 点对点聊天
     */
    @MessageMapping("/chat")
    public void chat(Message message) {
        LOG.info("WebSocketController chat: {}", message.toString());
        //TODO 接入登录功能spring security
        // 参考：https://www.jianshu.com/p/0f498adb3820
        // https://blog.csdn.net/u012373815/article/details/54380476
        webSocketService.sendChatMessage("xieyizun", "chat start.");
    }
}
