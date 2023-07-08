package com.boardify.boardify.configuration;

import com.boardify.boardify.repository.UserRepository;
import com.boardify.boardify.service.impl.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfiguration {

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new UserDetailsServiceImpl(userRepository);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                        .requestMatchers("/").permitAll() // Allow access to home page for all
                                .requestMatchers("/leaderboard").permitAll() // Allow access to home page for all
                                .requestMatchers("/join-tournament").permitAll() // Allow access to home page for all
                                .requestMatchers("/create-tournament").permitAll() // Allow access to home page for all
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Require role "ADMIN" for /admin/**
                        .requestMatchers("/regular/**").hasRole("USER") // Require role "USER" for /regular/**
                        .requestMatchers("/premium/**").hasRole("PREMIUM_USER") // Require role "PREMIUM_USER" for /premium/**
                        .anyRequest().authenticated() // Require authentication for all other URLs
                );

        return http.build();

    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/webjars/**", "/css/**");
    }
}
