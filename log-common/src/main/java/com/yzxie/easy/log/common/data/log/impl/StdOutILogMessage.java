package com.yzxie.easy.log.common.data.log.impl;

import com.alibaba.fastjson.JSONObject;
import com.yzxie.easy.log.common.data.log.AbstractLogMessage;
import com.yzxie.easy.log.common.data.log.LogType;
import lombok.Data;

import java.util.Arrays;

/**
 * @author xieyizun
 * @date 17/11/2018 17:14
 * @description:
 */
@Data
public class StdOutILogMessage extends AbstractLogMessage {
    private String requestTime; // 2018-11-18 15:42:12
    private String clientIp;
    private String apiPath;
    private Long costTime; // ms
    private JSONObject msg; // 应用相关数据，如产品，用户等

    public StdOutILogMessage(String appId, String content) {
        super(appId, content);
        populateValues();
    }

    public LogType getLogType() {
        return LogType.STDOUT;
    }

    private void populateValues() {
        // todo 在配置文件配置日志格式
        // requestTime | clientIp | apiPath | costTime | other
        if (super.getContent() != null) {
            String[] logSegments = Arrays.stream(super.getContent().split("\\|"))
                    .map(str -> str.trim()).toArray(String[]::new);
            this.requestTime = logSegments[0];
            this.clientIp = logSegments[1];
            this.apiPath = logSegments[2];
            this.costTime = Long.valueOf(logSegments[3]);
            try {
                this.msg = JSONObject.parseObject(logSegments[4]);
            } catch (Exception ignore) {}
        }
    }

    public static void main(String[] args) {
        String content = "2018-11-18 | 127.0.0.1 | /api/test | 20 |{'userId': 123}";
        String[] splits = Arrays.stream(content.split("\\|")).map(str -> str.trim()).toArray(String[]::new);
        for (int i = 0; i < splits.length; i++) {
            System.out.println(i + ":" + splits[i]);
        }
    }
}
