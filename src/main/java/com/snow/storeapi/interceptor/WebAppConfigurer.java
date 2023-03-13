package com.snow.storeapi.interceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * 拦截配置--调用链
 */
@Configuration
@RequiredArgsConstructor
public class WebAppConfigurer implements WebMvcConfigurer {
    private final SysInterceptor sysInterceptor;
    /**
     * 排除以下url进入拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sysInterceptor)
                .excludePathPatterns("/user/sendPhoneCode/**")
                .excludePathPatterns("/user/QRCode")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/order/test")
                .addPathPatterns("/**");
    }

}