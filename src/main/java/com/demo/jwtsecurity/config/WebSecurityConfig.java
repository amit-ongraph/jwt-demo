package com.demo.jwtsecurity.config;

import com.demo.jwtsecurity.utils.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .anyRequest().authenticated()
                .and()
                // We filter the api/login requests
                .addFilter(jwtAuthenticationFilter())
                // And filter other requests to check the presence of JWT in header
                .addFilterBefore(authenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Create a default account
        auth.inMemoryAuthentication().passwordEncoder(passwordEncoder())
                .withUser("user")
                .password(Constants.USER_PASSWORD_ENCODED)
                .roles(Constants.UserRoles.USER.name())
                .and()
                .withUser("admin")
                .password(Constants.ADMIN_PASSWORD_ENCODED)
                .roles(Constants.UserRoles.ADMIN.name());

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(5);
    }


    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() throws Exception {

        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter(authenticationManagerBean());

        return jwtAuthenticationFilter;
    }

    @Bean
    public JWTLoginFilter authenticationFilter() throws Exception {

        JWTLoginFilter authFilter = new JWTLoginFilter();

        authFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login","POST"));
        authFilter.setAuthenticationManager(authenticationManagerBean());
        authFilter.setUsernameParameter("username");
        authFilter.setPasswordParameter("password");

        return authFilter;
    }




}
