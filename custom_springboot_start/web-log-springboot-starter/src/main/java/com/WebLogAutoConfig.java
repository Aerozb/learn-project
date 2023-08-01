package com;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
 
@Configuration //表示这个类为配置类
@EnableConfigurationProperties({WebLogProperties.class})//使 使用 @ConfigurationProperties 注解的类生效
@ConditionalOnProperty(prefix = "weblog",value = "enabled",matchIfMissing = true)
public class WebLogAutoConfig {
 
    @Bean
    @ConditionalOnMissingBean
    public WebLogAspect webLogAspect(){
        return new WebLogAspect();
    }
 
}