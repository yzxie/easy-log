package com.yzxie.easy.log.common.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xieyizun
 * @date 27/10/2018 22:58
 * @description:
 */
public abstract class AbstractServer {
    protected AbstractServer next;

    /**
     * override by specific server to custom start/stop process.
     */
    public abstract void start();

    public abstract void stop();

    public void startNext() {
        if (next != null) {
            next.start();
        }
    }

    public void stopNext() {
        if (next != null) {
            next.stop();
        }
    }

    public AbstractServer setNext(AbstractServer next) {
        if (this.next != null) {
            //TODO log already exits
        } else {
            this.next = next;
        }
        return next;
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public String getNextName() {
        if (next != null) {
            return next.getName();
        }
        return null;
    }
}
