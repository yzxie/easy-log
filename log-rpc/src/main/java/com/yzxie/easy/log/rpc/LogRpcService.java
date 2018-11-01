package com.yzxie.easy.log.rpc;

import com.yzxie.easy.log.common.service.AbstractService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xieyizun
 * @date 1/11/2018 12:59
 * @description:
 */
public class LogRpcService extends AbstractService {
    private static final Logger LOG = LoggerFactory.getLogger(LogRpcService.class);

    @Override
    public void start() {
        LOG.info("LogRpcService started.");
    }

    @Override
    public void stop() {
        LOG.info("LogRpcService stop.");
    }
}
