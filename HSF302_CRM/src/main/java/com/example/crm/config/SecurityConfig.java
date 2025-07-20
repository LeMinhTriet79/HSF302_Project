package com.example.crm.config;

import com.example.crm.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/login", "/css/**", "/static/css/js/**", "/example.html").permitAll()
                        .requestMatchers("/users/employees").hasAuthority("LEADER")
                        .requestMatchers("/roles/**", "/users/**", "/taskstatus/**").hasAuthority("ADMIN")
                        // Cho phép MEMBER được GET /tasks/**
                        .requestMatchers(HttpMethod.GET, "/tasks/**").hasAnyAuthority("ADMIN", "LEADER", "MEMBER")
                        // Các method còn lại (POST, PUT, DELETE) chỉ ADMIN, LEADER
                        .requestMatchers("/tasks/update").hasAnyAuthority("MEMBER")
                        .requestMatchers("/tasks/**").hasAnyAuthority("ADMIN", "LEADER")
                        .requestMatchers("/projects/**", "/projectmembers/**").hasAnyAuthority("ADMIN", "LEADER","MEMBER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/")
                        .loginProcessingUrl("/do-login")
                        .successHandler(customAuthenticationSuccessHandler)
                        .permitAll()
                )
                .logout(logout -> logout.permitAll());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //return new BCryptPasswordEncoder();
        return NoOpPasswordEncoder.getInstance(); // Không mã hóa, dùng mật khẩu thuần
    }
}
