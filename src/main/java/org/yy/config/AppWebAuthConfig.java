package org.yy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 请求拦截
 */
@Configuration
public class AppWebAuthConfig implements WebMvcConfigurer {
    @Resource
    AppAuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludePath = new ArrayList<>();
        excludePath.add("/uploadFiles/**");
        excludePath.add("/api/uniApp/mLogin");
        excludePath.add("/api/uniApp/logout");
        registry.addInterceptor(authInterceptor).addPathPatterns("/api/**").excludePathPatterns(excludePath);
    }
}
