package com.example.GoAdventure.config;

import com.example.GoAdventure.model.enums.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(
                authorizeRequests -> authorizeRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/", "/login-register/**", "/login-register/login-error", "/contact-us/**", "/error","/confirm-account").permitAll()
                        .requestMatchers("/home", "/adventures").hasRole(Role.USER.name())
                        .requestMatchers("/**").hasRole(Role.ADMIN.name())
                        .anyRequest().authenticated()
        ).formLogin(
                formLogin -> {
                    formLogin
                            .loginPage("/login-register")
                            .usernameParameter("loginEmail")
                            .passwordParameter("loginPassword")
                            .defaultSuccessUrl("/home")
                            .failureForwardUrl("/login-register/login-error");
                }
        ).logout(
                logout -> {
                    logout
                            .logoutUrl("/logout")
                            .logoutSuccessUrl("/")
                            .invalidateHttpSession(true)
                            .deleteCookies("JSESSIONID");
                }
        ).build();
    }
}