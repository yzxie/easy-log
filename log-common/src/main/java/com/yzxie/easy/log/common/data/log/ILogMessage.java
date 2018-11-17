package com.yzxie.easy.log.common.data.log;

/**
 * @author xieyizun
 * @date 17/11/2018 17:15
 * @description:
 */
public interface ILogMessage {
    LogType getLogType();

    String getContent();
}
