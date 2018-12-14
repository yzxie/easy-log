package com.yzxie.easy.log.storage.processor;

import com.yzxie.easy.log.common.data.log.ILogMessage;

/**
 * @author xieyizun
 * @date 14/12/2018 16:28
 * @description:
 */
public interface IStorageProcessor {
    void save(ILogMessage msg);
}
