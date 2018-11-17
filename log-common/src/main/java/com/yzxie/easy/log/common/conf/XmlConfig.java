package com.yzxie.easy.log.common.conf;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author xieyizun
 * @date 29/10/2018 15:15
 * @description:
 */
@Slf4j
public class XmlConfig {
    private XMLConfiguration xmlConfiguration;

    public XmlConfig(String xmlFileName) {
        try {
            xmlConfiguration = new XMLConfiguration(xmlFileName);
        } catch (ConfigurationException e) {
            log.error("construct xml config {} exception", e);
        }
    }

    public String getString(String key) {
        if (xmlConfiguration != null) {
            return xmlConfiguration.getString(key);
        }
        return null;
    }

    public Integer getInt(String key) {
        if (xmlConfiguration != null) {
            return xmlConfiguration.getInt(key);
        }
        return null;
    }

    public boolean getBoolean(String key) {
        if (xmlConfiguration != null) {
            return xmlConfiguration.getBoolean(key);
        }
        return false;
    }

    public List<HierarchicalConfiguration> getList(String key) {
        if (xmlConfiguration != null) {
            return xmlConfiguration.configurationsAt(key);
        }
        return null;
    }
}
