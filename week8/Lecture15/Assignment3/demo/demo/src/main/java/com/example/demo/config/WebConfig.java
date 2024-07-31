package com.example.demo.config;

import com.example.demo.filter.ApiKeyFilter;
import com.example.demo.interceptor.ApiKeyInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private ApiKeyFilter apiKeyFilter;

    @Autowired
    private ApiKeyInterceptor apiKeyInterceptor;

    @Bean
    public FilterRegistrationBean<ApiKeyFilter> apiKeyFilterRegistrationBean() {
        FilterRegistrationBean<ApiKeyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(apiKeyFilter);
        registrationBean.addUrlPatterns("/api/*");
        return registrationBean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiKeyInterceptor).addPathPatterns("/api/**").excludePathPatterns("/api/login", "/api/register");
    }
}
