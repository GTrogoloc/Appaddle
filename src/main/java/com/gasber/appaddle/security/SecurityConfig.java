package com.gasber.appaddle.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.servlet.Filter;

@Configuration
public class SecurityConfig {
   
    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

 //   @Bean
   // public Filter jwtFilter() {
     //   return new JwtFilter(jwtUtil);
    //}
    
}
