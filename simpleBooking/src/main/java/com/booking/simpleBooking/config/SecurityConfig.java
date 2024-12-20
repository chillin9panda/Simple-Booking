package com.booking.simpleBooking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
class SecurityConfig {
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(authz -> authz
        .requestMatchers("/login").permitAll()// Allow access to login page
        .requestMatchers("/css/**", "/images/**", "/js/**").permitAll()
        .anyRequest().authenticated() // Require authentication for other page
    )
        .formLogin(form -> form
            .loginPage("/login") // custom login page
            .usernameParameter("employee_id")
            .passwordParameter("password")
            .successHandler(customSuccessHandler())
            .failureHandler(customFailureHandler())
            .permitAll());

    return http.build();
  }

  @Bean
  public AuthenticationSuccessHandler customSuccessHandler() {
    return (request, response, authentication) -> {
      response.sendRedirect("/booking");
    };
  }

  @Bean
  public AuthenticationFailureHandler customFailureHandler() {
    return (request, response, exception) -> {
      response.sendRedirect("/login?error=true");
    };
  }

}
