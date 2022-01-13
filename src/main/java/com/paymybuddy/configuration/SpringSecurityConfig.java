package com.paymybuddy.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.net.InetAddress;

/**
 * SpringSecurity Configuration
 *
 * As this API is not intended to be public-facing, and will only be accessed by the frontend application itself,
 * configuration is currently set up to simply block all access from non-authorized IP addresses.
 *
 * The application.properties file has entries for two IP addresses frontend.app.ip, and frontend.app.ip2
 * These variables are set to localhost IPs by default, and should be updated to whatever IP the frontend will connect from
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${frontend.app.ip}")
    private String authIp;

    @Value("${frontend.app.ip2}")
    private String authIptwo;

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        String security = "hasIpAddress('"+ authIp + "') or hasIpAddress('" + authIptwo + "') or isAuthenticated()";

        httpSecurity.authorizeRequests()
                .antMatchers("/**")
                //.access("hasIpAddress('127.0.0.1') or hasIpAddress('::1') or isAuthenticated()")
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
