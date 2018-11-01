package com.yzxie.easy.log.web.data;

import lombok.Data;

/**
 * @author xieyizun
 * @date 2/11/2018 00:12
 * @description:
 */
@Data
public class ResData {
    int ret;
    long serverTime;
    Object data;

    public ResData() {
        this.ret = 0;
        this.serverTime = System.currentTimeMillis();
    }

    public ResData(Object data) {
        this.data = data;
        this.ret = 0;
        this.serverTime = System.currentTimeMillis();
    }
}
