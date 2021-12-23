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

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${app.login.name}")
    private String uservar;

    @Value("${app.login.password}")
    private String passvar;

    @Value("${frontend.app.ip}")
    private String authIp;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                //.withUser("springuser").password(passwordEncoder().encode("spring123")).roles("USER")
                //.and()
                .withUser(uservar).password(passwordEncoder().encode(passvar)).roles("ADMIN", "USER");
    }


    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                .antMatchers("/**").access("hasIpAddress('127.0.0.1') or hasIpAddress('::1') or isAuthenticated()")
                //.antMatchers("/**").hasRole("ADMIN")
                //.antMatchers(HttpMethod.POST, "/adduser").hasRole("ADMIN")
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
