package com.yzxie.easy.log.common.api;

import com.yzxie.easy.log.common.data.rpc.LogTypeWithApps;

import java.util.List;

/**
 * @author xieyizun
 * @date 18/11/2018 22:36
 * @description:
 */
public interface LogConfServiceApi {
    List<LogTypeWithApps> listLogTypeAndApps();
}
