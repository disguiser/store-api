package com.snow.storeapi.interceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * 拦截配置--调用链
 */
@EnableWebMvc
@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {

    /**
     * 排除以下url进入拦截器
     */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		String[] patterns = new String[] { "/login","/*.html","/swagger-resources/**"};
		registry.addInterceptor(new SysInterceptor())
                .excludePathPatterns("/demo/login")
				.excludePathPatterns("/user/login")
                .excludePathPatterns("/*.html")
                .excludePathPatterns("/swagger-resources/**")
		        .addPathPatterns("/**");
	}

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
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("POST", "GET", "PUT", "PATCH", "OPTIONS", "DELETE")
                .maxAge(3600)
                .allowCredentials(true);
    }

}