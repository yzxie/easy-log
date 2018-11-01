package com.yzxie.easy.log.common.api;

import com.yzxie.easy.log.common.data.ApiRank;

import java.util.List;

/**
 * @author xieyizun
 * @date 1/11/2018 13:02
 * @description:
 */
public interface LogQueryServiceApi {
    List<ApiRank> getRankingOfApiCall(String host);
}
