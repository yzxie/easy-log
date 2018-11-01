package com.yzxie.easy.log.common.data;

import lombok.Data;

/**
 * @author xieyizun
 * @date 1/11/2018 22:44
 * @description:
 */
@Data
public class ApiRank {
    /**
     * api路径
     */
    private String apiPath;
    /**
     * 请求次数
     */
    private Long requestCount;
}
