package com.snow.storeapi.interceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * 拦截配置--调用链
 */
//@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {

    /**
     * swagger2配置,引入资源
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * Cors跨域访问配置
     */
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOriginPatterns("*")
//                .allowedMethods("POST", "GET", "PUT", "PATCH", "OPTIONS", "DELETE")
//                .maxAge(3600)
//                .allowCredentials(true);
//    }

}