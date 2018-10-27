package com.yzxie.easy.log.starter;

import com.yzxie.easy.log.common.server.AbstractServer;

/**
 * @author xieyizun
 * @date 27/10/2018 23:52
 * @description:
 */
public final class ServerChain {
    /**
     * the last server in this chain
     */
    private final AbstractServer boot = new AbstractServer() {
        @Override
        protected void start() {
            startNext();
            //todo log server chain start.
        }

        @Override
        protected void stop() {
            stopNext();
        }
    };

    private AbstractServer last = boot;

    public ServerChain setNextServer(AbstractServer nextServer) {
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
