package com.michaeltang.usermanagement.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * The simple security configuration. need to enhanced depending on the real requirements.
 * @author tangyh
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration  extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.csrf().disable()
        	.authorizeRequests()
        	.antMatchers("/v3/api-docs").permitAll()
        	.anyRequest().permitAll();
    }
}
