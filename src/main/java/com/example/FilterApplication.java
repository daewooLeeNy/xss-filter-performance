package com.example;

import com.navercorp.lucy.security.xss.servletfilter.XssEscapeServletFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@ServletComponentScan
public class FilterApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilterApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean registrationLucyFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new XssEscapeServletFilter());
        registration.addUrlPatterns("/lucy/test");
        registration.setName("lucyFilter");

        return registration;
    }
}
