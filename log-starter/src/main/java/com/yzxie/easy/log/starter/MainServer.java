package com.yzxie.easy.log.starter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xieyizun
 * @date 26/10/2018 14:57
 * @description:
 */
public class MainServer {
    private static final Logger logger = LoggerFactory.getLogger(MainServer.class);

    public static void main(String[] args) {
        ServerChainLauncher launcher = new ServerChainLauncher();
        launcher.init();
        launcher.start();
        addShutdownHook(launcher);
    }

    public static void addShutdownHook(ServerChainLauncher launcher) {

    }
}
