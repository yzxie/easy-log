package com.yzxie.easy.log.engine.handler.impl;

import com.yzxie.easy.log.common.conf.KafkaConfig;
import com.yzxie.easy.log.common.data.log.impl.StdOutLogMessage;
import com.yzxie.easy.log.common.kafka.KafkaGroup;
import com.yzxie.easy.log.engine.bussine.SecondLevelFlow;
import com.yzxie.easy.log.engine.bussine.TopTenApi;
import com.yzxie.easy.log.engine.handler.AbstractEngineHandler;
import com.yzxie.easy.log.storage.LogStorageService;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author xieyizun
 * @date 17/11/2018 16:07
 * @description:
 */
@Slf4j
public class StdOutEngineHandler extends AbstractEngineHandler<StdOutLogMessage> {
    /**
     * 业务线程池
     */
    private ExecutorService businessExecutorService;
    /**
     * 每个应用对应一个队列，对应一个线程处理器
     */
    private ExecutorService messageReceivedExecutorService;
    private Map<String, BlockingQueue<StdOutLogMessage>> messageLists; // appId -> blockingQueue

    @Override
    public StdOutEngineHandler startUp() {
        List<KafkaGroup> kafkaGroupList = KafkaConfig.listKafkaGroup();
        messageReceivedExecutorService = Executors.newFixedThreadPool(kafkaGroupList.size());
        messageLists = new HashMap<>();
        for (KafkaGroup kafkaGroup : kafkaGroupList) {
            String appId = kafkaGroup.getGroupId();
            BlockingQueue<StdOutLogMessage> messageList = new LinkedBlockingQueue();
            messageLists.put(appId, messageList);
            messageReceivedExecutorService.execute(new MessageProcessor(messageList));
        }

        this.businessExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        return this;
    }

    /**
     * STD类型日志，并发量较大，每个应用对应一个单独的线程处理器
     * @param logMessage
     */
    @Override
    public void handle(StdOutLogMessage logMessage) {
        this.messageLists.get(logMessage.getAppId()).add(logMessage);
    }

    @Override
    protected void process(StdOutLogMessage logMessage) {
        log.info("process {}, {}", logMessage.getAppId(), Thread.currentThread().getId());
        /**
         * 实时日志分析
         */
        processTopTenApi(logMessage);
        processSecondLevelFlow(logMessage);
        /**
         * 将日志存储到Redis或HBase，之后进行离线分析
         */
        LogStorageService.dispatch(logMessage);
    }


    private void processTopTenApi(StdOutLogMessage logMessage) {
        this.businessExecutorService.execute(new TopTenApi(logMessage));
    }

    private void processSecondLevelFlow(StdOutLogMessage logMessage) {
        this.businessExecutorService.execute(new SecondLevelFlow(logMessage));
    }
}
