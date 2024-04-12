package com.bestzpr.tally.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @className RestTemplateConfig
 * @Desc
 * @Author 张埔枘
 * @Date 2023/12/23 23:34
 * @Version 1.0
 */
@Configuration
public class RestTemplateConfig {

    // RestTemplateBuilder是由Spring Boot自动配置的，它包含了所有自动配置的定制选项
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // 使用builder来构建一个RestTemplate实例
        return builder.build();
    }
}