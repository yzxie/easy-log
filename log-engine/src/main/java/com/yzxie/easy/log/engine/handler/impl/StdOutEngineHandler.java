package com.yzxie.easy.log.engine.handler.impl;

import com.yzxie.easy.log.common.data.log.impl.StdOutILogMessage;
import com.yzxie.easy.log.engine.bussine.SecondLevelFlow;
import com.yzxie.easy.log.engine.bussine.TopTenApi;
import com.yzxie.easy.log.engine.handler.AbstractEngineHandler;
import com.yzxie.easy.log.storage.LogStorageService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xieyizun
 * @date 17/11/2018 16:07
 * @description:
 */
@Slf4j
public class StdOutEngineHandler extends AbstractEngineHandler<StdOutILogMessage> {
    private ExecutorService executorService;

    public StdOutEngineHandler() {
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Override
    protected void process(StdOutILogMessage logMessage) {
        /**
         * 实时日志分析
         */
        processTopTenApi(logMessage);
        processMinuteLevelFlow(logMessage);
        /**
         * 将日志存储到Redis或HBase，之后进行离线分析
         */
        LogStorageService.dispatch(logMessage);
    }

    private void processTopTenApi(StdOutILogMessage logMessage) {
        this.executorService.execute(new TopTenApi(logMessage));
    }

    private void processMinuteLevelFlow(StdOutILogMessage logMessage) {
        this.executorService.execute(new SecondLevelFlow(logMessage));
    }
}
