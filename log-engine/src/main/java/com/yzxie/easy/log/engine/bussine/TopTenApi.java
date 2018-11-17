package com.yzxie.easy.log.engine.bussine;

import com.yzxie.easy.log.common.data.log.impl.StdOutILogMessage;
import com.yzxie.easy.log.storage.handler.RedisHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xieyizun
 * @date 17/11/2018 21:28
 * @description: 访问量前十的API
 */
@Slf4j
public class TopTenApi implements Runnable {
    public static final String REDIS_KEY = "top_ten_api";

    private StdOutILogMessage stdOutILogMessage;

    public TopTenApi(StdOutILogMessage stdOutILogMessage) {
        this.stdOutILogMessage = stdOutILogMessage;
    }

    @Override
    public void run() {
        String apiPath = stdOutILogMessage.getApiPath();
        log.info("apiPath: {}", apiPath);
        // todo 线程安全问题
        RedisHandler.increaseScore(REDIS_KEY, apiPath);
    }
}
