package com.swagger.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;

@EnableKnife4j//启用knife4j
@Configuration
//@EnableOpenApi
public class SwaggerConfig implements WebMvcConfigurer {

    /**
     * 不配置这个访问不了knife4j首页
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public Docket docket(Environment environment) {
        Profiles profiles = Profiles.of("dev", "test"); // 设置要显示swagger的环境
        boolean isOpen = environment.acceptsProfiles(profiles); // 判断当前是否处于该环境

        return new Docket(DocumentationType.OAS_30)
                // 文档信息配置
                .apiInfo(apiInfo())
                // 设置是否启动Swagger，通过当前环境进行判断：isOpen
                .enable(isOpen)
                // 配置扫描的接口
//                .select()
                // 配置扫描哪里的接口
//                .apis(RequestHandlerSelectors.basePackage("com.swagger.controller"))
//                // 过滤请求，只扫描请求以/category开头的接口
//                .paths(PathSelectors.ant("/user/**"))
//                .build()
                ;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("外卖项目接口文档") // 文档标题
                .description("基本的一些接口说明") // 文档基本描述
                .contact(new Contact("xxxx", "https://blog.csdn.net", "xxxx@qq.com")) // 联系人信息
                .termsOfServiceUrl("http://terms.service.url/组织链接") // 组织链接
                .version("1.0") // 版本
                .license("Apache 2.0 许可") // 许可
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0") // 许可链接
                .extensions(new ArrayList<>()) // 拓展
                .build();
    }

}
