package com.yzxie.easy.log.starter;

import com.yzxie.easy.log.collector.LogCollectorService;
import com.yzxie.easy.log.engine.LogEngineService;

/**
 * @author xieyizun
 * @date 27/10/2018 23:38
 * @description:
 */
public final class ServerChainLauncher {
    private ServerChain serverChain = new ServerChain();

    public void init() {
        serverChain.init()
                    .setNextServer(new LogCollectorService())
                    .setNextServer(new LogEngineService());
    }

    public void start() {
        serverChain.start();
    }

    public void stop() {
        serverChain.stop();
    }

}
