package com.yzxie.easy.log.conf.xml;

/**
 * @author xieyizun
 * @date 31/10/2018 23:51
 * @description:
 */
public class StorageConfig extends XmlConfig {

    public static final String REDIS_HOST = "storage.redis.host";
    public static final String REDIS_PORT = "storage.redis.port";
    public static final String REDIS_PASSWORD = "storage.redis.password";
    public static final String REDIS_MAX_IDLE = "storage.redis.maxIdle";
    public static final String REDIS_MAX_TOTAL = "storage.redis.maxTotal";
    public static final String REDIS_MAX_WAIT = "storage.redis.maxWait";
    public static final String REDIS_TEST_ON_BORROW = "storage.redis.testOnBorrow";

    public StorageConfig() { super("storage.xml"); }
}
