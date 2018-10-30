package com.yzxie.easy.log.common;

import com.yzxie.easy.log.conf.xml.TopicConfig;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xieyizun
 * @date 29/10/2018 00:22
 * @description:
 */
public final class TopicContainer {
    private static final Logger LOG = LoggerFactory.getLogger(TopicContainer.class);
    private static List<String> topicsList = new ArrayList<>();

    static {
        TopicConfig topicConfig = new TopicConfig();
        List<HierarchicalConfiguration> topicList = topicConfig.getList(TopicConfig.TOPIC_LIST);
        for (HierarchicalConfiguration topic : topicList) {
            LOG.info("===={}", topic.getString("name"));
            topicsList.add(topic.getString(TopicConfig.TOPIC_NAME));
        }
    }

    public static final List<String> listTopics() {
        return topicsList;
    }
}
