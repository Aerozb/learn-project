package com;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//表示这个类为配置类
@Configuration
//使 使用 @ConfigurationProperties 注解的类生效
@EnableConfigurationProperties({WebLogProperties.class})
//matchIfMissing属性：默认情况下matchIfMissing为false，也就是说如果未进行属性配置，则自动配置不生效。如果matchIfMissing为true，则表示如果没有对应的属性配置，则也生效
@ConditionalOnProperty(prefix = "weblog",value = "enabled",matchIfMissing = true)
public class WebLogAutoConfig {
 
    @Bean
    @ConditionalOnMissingBean
    public WebLogAspect webLogAspect(){
        return new WebLogAspect();
    }
 
}