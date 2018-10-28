package com.yzxie.easy.log.collector.kafka;

/**
 * @author xieyizun
 * @date 26/10/2018 17:36
 * @description:
 */
public class KafkaMessage {
    private String topic;
    /**
     * partition key. optional
     */
    private String key;
    private String content;

    public KafkaMessage(String topic, String key, String content) {
        this.topic = topic;
        this.key = key;
        this.content = content;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
