package com.cusbee.kiosk.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

/**
 * Created by ahorbat on 01.02.17.
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "my_rest_api";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID).stateless(false);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests().antMatchers("/api/mobile/**").anonymous()
            .and()
                .requestMatchers().antMatchers("/api/admin/**").and()
                .requestMatchers().antMatchers("/api/device/**").and()
                .requestMatchers().antMatchers("/api/customer/**")
            .and()
            .authorizeRequests()
                .antMatchers("/api/admin/**").access("hasRole('ADMIN') or hasRole('ROLE_SALAD') or hasRole('ROLE_SANDWICH')or hasRole('ROLE_BURGER')")
                .antMatchers("/api/device/**").access("hasRole('ADMIN') or hasRole('ROLE_CUSTOMER')")
                .antMatchers("/api/customer/**").access("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_SALAD') or hasRole('ROLE_SANDWICH')or hasRole('ROLE_BURGER')")
            .and()
            .exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }
}
