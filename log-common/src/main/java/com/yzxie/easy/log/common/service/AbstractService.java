package com.yzxie.easy.log.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xieyizun
 * @date 27/10/2018 22:58
 * @description:
 */
public abstract class AbstractService {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractService.class);

    protected AbstractService next;

    /**
     * override by specific service to custom start/stop process.
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

    public AbstractService setNext(AbstractService next) {
        if (this.next != null) {
            LOG.warn("setNext next is already existed.");
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
