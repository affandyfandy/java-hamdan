package com.example.demo.config;

import com.example.demo.filter.ApiKeyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

@Configuration
public class WebConfig {

    @Autowired
    private ApiKeyFilter apiKeyFilter;

    @Bean
    public FilterRegistrationBean<ApiKeyFilter> apiKeyFilterRegistrationBean() {
        FilterRegistrationBean<ApiKeyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(apiKeyFilter);
        registrationBean.addUrlPatterns("/api/*");
        return registrationBean;
    }
}
