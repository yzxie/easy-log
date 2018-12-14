package com.yzxie.easy.log.engine.processor;

/**
 * @author xieyizun
 * @date 29/10/2018 00:14
 * @description:
 */
public interface IEngineProcessor<T> {
    IEngineProcessor startUp();
    void handle(T logMessage);
}
