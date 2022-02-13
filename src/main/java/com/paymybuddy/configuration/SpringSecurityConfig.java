package com.paymybuddy.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * SpringSecurity Configuration
 *
 * As this API is not intended to be public-facing, and will only be accessed by the frontend application itself,
 * configuration is currently set up to simply block all access from non-authorized IP addresses.
 *
 * The application.properties file has an entry for an IP addresses in frontend.app.ip
 * This variable is set to a localhost IP by default, and should be updated to whatever IP the frontend will connect from
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${frontend.app.ip}")
    private String authIp;

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        String security = "hasIpAddress('127.0.0.1') or hasIpAddress('::1') or hasIpAddress('" + authIp + "') or isAuthenticated()";

        httpSecurity.authorizeRequests()
                .antMatchers("/**")
                .access(security)
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .formLogin();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
