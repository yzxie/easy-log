package com.yzxie.easy.log.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * @author xieyizun
 * @date 26/10/2018 14:51
 * @description:
 */

@SpringBootApplication
@ImportResource(locations= {"classpath:dubbo_consumer.xml"})
public class LogWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogWebApplication.class, args);
    }
}

