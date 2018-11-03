package com.yzxie.easy.log.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yzxie.easy.log.common.api.LogQueryServiceApi;
import com.yzxie.easy.log.common.data.ApiRank;
import com.yzxie.easy.log.web.data.ResData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author xieyizun
 * @date 2/11/2018 00:10
 * @description:
 */

@RestController
public class IndexController {

    @Autowired
    private LogQueryServiceApi logStorageService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResData index() {
        List<ApiRank> apiRanks = logStorageService.getRankingOfApiCall("runoobkey");
        return new ResData(apiRanks);
    }
}
