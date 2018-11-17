package com.yzxie.easy.log.common.data.log;

import lombok.Data;

/**
 * @author xieyizun
 * @date 17/11/2018 21:05
 * @description:
 */
@Data
public abstract class AbstractLogMessage implements ILogMessage {
    private String content;

    public AbstractLogMessage(String content) {
        this.content = content;
    }
}
