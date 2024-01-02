package com.jst.config;

import com.jst.domain.login.service.JwtService;
import com.jst.filter.JwtAuthorizationFilter;
import com.jst.filter.TestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Autowired
    private  JwtService jwtService;
//    @Bean
    public FilterRegistrationBean<TestFilter> filter(){
        FilterRegistrationBean<TestFilter> bean = new FilterRegistrationBean<>(new TestFilter());
        bean.setOrder(0);
        return bean;
    }
}
