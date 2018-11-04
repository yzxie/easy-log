package com.yzxie.easy.log.web.controller;

import com.yzxie.easy.log.common.data.ApiRank;
import com.yzxie.easy.log.web.data.ResData;
import com.yzxie.easy.log.web.service.IndexService;
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
    private IndexService indexService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResData index() {
        List<ApiRank> apiRanks = indexService.getTop10VisitedUri("runoobkey");
        return new ResData(apiRanks);
    }
}
