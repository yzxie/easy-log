package com.yzxie.easy.log.common.data.log.impl;

import com.yzxie.easy.log.common.data.log.AbstractLogMessage;
import com.yzxie.easy.log.common.data.log.LogType;
import lombok.Data;

/**
 * @author xieyizun
 * @date 17/11/2018 17:14
 * @description:
 */
@Data
public class StdErrorILogMessage extends AbstractLogMessage {
    public StdErrorILogMessage(String appId, String content) {
        super(appId, content);
    }

    public LogType getLogType() {
        return LogType.STDERROR;
    }
}
