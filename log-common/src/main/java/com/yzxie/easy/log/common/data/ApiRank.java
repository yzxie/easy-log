package com.yzxie.easy.log.common.data;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xieyizun
 * @date 1/11/2018 22:44
 * @description:
 */
@Data
public class ApiRank implements Serializable {
    /**
     * api路径
     */
    private String apiPath;
    /**
     * 请求次数
     */
    private Long requestCount;
}
