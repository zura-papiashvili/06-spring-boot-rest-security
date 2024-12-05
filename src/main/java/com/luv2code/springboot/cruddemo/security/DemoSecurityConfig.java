package com.luv2code.springboot.cruddemo.security;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class DemoSecurityConfig {

    // add support for JDBC authentication (users are stored in a database)

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        // define query to retrieve user information
        jdbcUserDetailsManager
                .setUsersByUsernameQuery("select user_id, pw, active from members where user_id=?");

        // define query to retrieve role information

        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
                "select user_id, role from roles where user_id=?");

        return jdbcUserDetailsManager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer -> configurer
                .requestMatchers(HttpMethod.GET, "/api/employees").hasRole("EMPLOYEE")
                .requestMatchers(HttpMethod.GET, "/api/employees/**").hasRole("EMPLOYEE")

                .requestMatchers(
                        HttpMethod.POST, "/api/employees")
                .hasRole("MANAGER")
                .requestMatchers(
                        HttpMethod.PUT, "/api/employees")
                .hasRole("MANAGER")
                .requestMatchers(HttpMethod.DELETE, "/api/employees/**").hasRole("ADMIN"));
        http.httpBasic(Customizer.withDefaults());

        http.csrf(csrf -> csrf.disable());

        return http.build();

    }

    // @Bean
    // public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
    // UserDetails John = User.builder()
    // .username("John")
    // .password("{noop}test123")
    // .roles("EMPLOYEE")
    // .build();
    // UserDetails Mary = User.builder()
    // .username("Mary")
    // .password("{noop}test123")
    // .roles("EMPLOYEE", "MANAGER")
    // .build();
    // UserDetails Susan = User.builder()
    // .username("Susan")
    // .password("{noop}test123")
    // .roles("EMPLOYEE", "MANAGER", "ADMIN")
    // .build();

    // return new InMemoryUserDetailsManager(John, Mary, Susan);
    // }

}
