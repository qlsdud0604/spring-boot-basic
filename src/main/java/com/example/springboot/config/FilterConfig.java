package com.example.springboot.config;

import com.example.springboot.filter.TestFilter01;
import com.example.springboot.filter.TestFilter02;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean testFilter01Register() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new TestFilter01());
        registrationBean.setOrder(1);

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean testFilter02Register() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new TestFilter02());
        registrationBean.setOrder(2);

        return registrationBean;
    }
}
