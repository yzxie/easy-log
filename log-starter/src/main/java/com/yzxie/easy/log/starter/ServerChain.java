package com.yzxie.easy.log.starter;

import com.yzxie.easy.log.common.service.AbstractService;

/**
 * @author xieyizun
 * @date 27/10/2018 23:52
 * @description:
 */
public final class ServerChain {
    /**
     * the last service in this chain
     */
    private final AbstractService boot = new AbstractService() {
        @Override
        public void start() {
            startNext();
            //todo log service chain start.
        }

        @Override
        public void stop() {
            stopNext();
        }
    };

    private AbstractService last = boot;

    public ServerChain setNextServer(AbstractService nextServer) {
        this.last = last.setNext(nextServer);
        return this;
    }

    public ServerChain init() {
        return this;
    }

    public void start() {
        boot.start();
    }

    public void stop() {
        boot.stop();
    }
}
