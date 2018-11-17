package com.yzxie.easy.log.common.data.log.impl;

import com.alibaba.fastjson.JSONObject;
import com.yzxie.easy.log.common.data.log.AbstractLogMessage;
import com.yzxie.easy.log.common.data.log.LogType;
import lombok.Data;

/**
 * @author xieyizun
 * @date 17/11/2018 17:14
 * @description:
 */
@Data
public class StdOutILogMessage extends AbstractLogMessage {
    private String requestTime;
    private String clientIp;
    private String apiPath;
    private Long costTime; // ms
    private JSONObject msg; // 应用相关数据，如产品，用户等

    public StdOutILogMessage(String content) {
        super(content);
        populateValues();
    }

    public LogType getLogType() {
        return LogType.STDOUT;
    }

    private void populateValues() {
        // todo 在配置文件配置日志格式
        // requestTime | clientIp | apiPath | costTime | other
        String[] logSegments = super.getContent().split("|");
        this.requestTime = logSegments[0];
        this.clientIp = logSegments[1];
        this.apiPath = logSegments[2];
        this.costTime = Long.valueOf(logSegments[3]);
        //this.msg = JSONObject.parseObject(logSegments[4]);
    }
}
