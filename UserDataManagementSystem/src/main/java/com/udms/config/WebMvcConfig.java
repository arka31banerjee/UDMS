package com.udms.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.udms.interceptors.RequestValidationInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final RequestValidationInterceptor requestValidationInterceptor;

    @Autowired
    public WebMvcConfig(RequestValidationInterceptor requestValidationInterceptor) {
        this.requestValidationInterceptor = requestValidationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestValidationInterceptor).addPathPatterns("/udms/**");
    }
}






