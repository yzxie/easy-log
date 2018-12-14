package com.yzxie.easy.log.storage.processor;

import com.yzxie.easy.log.common.data.log.ILogMessage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author xieyizun
 * @date 14/12/2018 16:31
 * @description:
 */
public abstract class AbstractStorageProcessor implements IStorageProcessor {
    private static final int defaultThreadPoolSize = Runtime.getRuntime().availableProcessors();
    protected ExecutorService processExecutorService;

    public AbstractStorageProcessor() {
        processExecutorService = Executors.newFixedThreadPool(defaultThreadPoolSize);
    }

    public AbstractStorageProcessor(int processThreadPoolSize) {
        processExecutorService = Executors.newFixedThreadPool(processThreadPoolSize);
    }

    @Override
    public void save(ILogMessage msg) {
        processExecutorService.execute(new StorageHandler(msg));
    }

    /**
     * 实际处理存储逻辑
     * @param msg
     */
    protected abstract void process(ILogMessage msg);

    protected class StorageHandler implements Runnable {
        private ILogMessage msg;

        public StorageHandler(ILogMessage msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            process(msg);
        }
    }
}
