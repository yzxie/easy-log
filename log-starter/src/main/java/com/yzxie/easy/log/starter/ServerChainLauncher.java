package com.yzxie.easy.log.starter;

import com.yzxie.easy.log.collector.LogCollectorService;
import com.yzxie.easy.log.engine.LogEngineService;
import com.yzxie.easy.log.rpc.LogRpcService;
import com.yzxie.easy.log.storage.LogStorageService;

/**
 * @author xieyizun
 * @date 27/10/2018 23:38
 * @description:
 */
public final class ServerChainLauncher {
    private ServerChain serverChain = new ServerChain();

    public void init() {
        serverChain.init()
                    .setNextServer(LogCollectorService.getInstance())
                    .setNextServer(LogEngineService.getInstance())
                    .setNextServer(new LogStorageService())
                    .setNextServer(new LogRpcService());
    }

    public void start() {
        serverChain.start();
    }

    public void stop() {
        serverChain.stop();
    }

}
