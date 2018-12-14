package com.yzxie.easy.log.rpc.service;

import com.yzxie.easy.log.common.api.LogQueryServiceApi;
import com.yzxie.easy.log.common.data.ApiRank;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xieyizun
 * @date 1/11/2018 13:03
 * @description: RPC服务，web项目调用
 */
public class LogQueryService implements LogQueryServiceApi {

    @Override
    public List<ApiRank> getRankingOfApiCall(String host) {
        List<ApiRank> apiRanks = new ArrayList<>();
//        List<Map<String, Object>> apiRequestCountList = RedisUtils.getTop10WithScore(host);
//
//        for (Map<String, Object> apiRequestCount : apiRequestCountList) {
//            ApiRank apiRank = new ApiRank();
//            apiRank.setApiPath(apiRequestCount.get("value").toString());
//            apiRank.setRequestCount(NumberUtils.toLong(apiRequestCount.get("score").toString()));
//            apiRanks.add(apiRank);
//        }
        // 消息不能太大，否则dubbo报错：Dubbo client can not supported string message
        return apiRanks.size() > 10 ? apiRanks.subList(0, 10) : apiRanks;
    }
}
