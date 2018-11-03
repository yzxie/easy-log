package com.yzxie.easy.log.web.service;

import com.yzxie.easy.log.common.api.LogQueryServiceApi;
import com.yzxie.easy.log.common.data.ApiRank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xieyizun
 * @date 3/11/2018 15:48
 * @description:
 */
@Service
public class IndexService {
    @Autowired
    private LogQueryServiceApi logStorageService;

    public List<ApiRank> getTop10VisitedUri(String host) {
        return logStorageService.getRankingOfApiCall(host);
    }
}
