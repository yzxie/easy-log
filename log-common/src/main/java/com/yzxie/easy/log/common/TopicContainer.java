package com.yzxie.easy.log.common;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xieyizun
 * @date 29/10/2018 00:22
 * @description:
 */
public final class TopicContainer {
    private static List<String> topicsList = new ArrayList<>();

    static {
        // todo load all topic from xml settings file
        topicsList.add("testFlume");
    }

    public static final List<String> listTopics() {
        return topicsList;
    }
}
