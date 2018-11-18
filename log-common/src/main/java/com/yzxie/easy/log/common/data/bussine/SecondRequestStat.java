package com.yzxie.easy.log.common.data.bussine;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author xieyizun
 * @date 18/11/2018 15:18
 * @description:
 */
@Data
@AllArgsConstructor
public class SecondRequestStat {
    private String secondStamp;
    private double requestCount;
}
