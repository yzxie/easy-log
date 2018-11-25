package com.yzxie.easy.log.common.data.rpc;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author xieyizun
 * @date 25/11/2018 14:07
 * @description:
 */
@Data
public class LogTypeWithApps implements Serializable {
    private String logType;
    private List<String> apps;
}
