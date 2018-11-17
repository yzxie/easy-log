package com.yzxie.easy.log.engine.handler;

/**
 * @author xieyizun
 * @date 29/10/2018 00:14
 * @description:
 */
public interface IEngineHandler<T> {
    void handle(T logMessage);
}
