package com.yzxie.easy.log.rpc;

import com.yzxie.easy.log.common.service.AbstractService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * @author xieyizun
 * @date 1/11/2018 12:59
 * @description:
 */
public class LogRpcService extends AbstractService {
    private static final Logger LOG = LoggerFactory.getLogger(LogRpcService.class);
    private FileSystemXmlApplicationContext context;

    @Override
    public void start() {
        //加载dubbo配置并启动、注册
        String configFile = "classpath:storage_dubbo_provider.xml";
        context = new FileSystemXmlApplicationContext();
        context.setValidating(false);
        context.setConfigLocation(configFile);
        context.refresh();

        LOG.info("LogRpcService started.");
        startNext();
    }

    @Override
    public void stop() {
        LOG.info("LogRpcService stop.");
        stopNext();
    }
}
