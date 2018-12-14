package com.yzxie.easy.log.storage.processor.impl;

import com.yzxie.easy.log.common.data.log.ILogMessage;
import com.yzxie.easy.log.storage.processor.AbstractStorageProcessor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xieyizun
 * @date 14/12/2018 16:39
 * @description:
 */
@Slf4j
public class HBaseProcessor extends AbstractStorageProcessor {

    @Override
    public void process(ILogMessage msg) {
        
    }

    /**
     * 单例实现
     */
    private HBaseProcessor() {}

    private static class InstanceHolder {
        public static HBaseProcessor instance = new HBaseProcessor();
    }

    public static HBaseProcessor getInstance() {
        return InstanceHolder.instance;
    }
}
