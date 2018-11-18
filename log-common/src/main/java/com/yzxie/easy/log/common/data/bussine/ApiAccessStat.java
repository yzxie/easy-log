package com.yzxie.easy.log.common.data.bussine;

import lombok.Data;

/**
 * @author xieyizun
 * @date 18/11/2018 15:05
 * @description:
 */
@Data
public class ApiAccessStat {
    private String appId;
    private String apiPath;
    private double requestCount;
}
