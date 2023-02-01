package com.example.springmvch2blog;

import com.example.springmvch2blog.config.CookieAuthenticationFilter;
import com.example.springmvch2blog.config.UserAuthenticationEntryPoint;
import com.example.springmvch2blog.config.UserAuthenticationManager;
import com.example.springmvch2blog.config.UsernamePasswordFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserAuthenticationEntryPoint userAuthenticationEntryPoint;
    private final UserAuthenticationManager userAuthenticationManager;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }




    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors()
                .and()

                .exceptionHandling().authenticationEntryPoint(userAuthenticationEntryPoint)
                .and()
                .httpBasic()
                .and()
                .addFilterBefore(new UsernamePasswordFilter(userAuthenticationManager), BasicAuthenticationFilter.class)
                .addFilterBefore(new CookieAuthenticationFilter(userAuthenticationManager), UsernamePasswordFilter.class)

                .csrf()
                .disable()

            .authorizeHttpRequests((requests) -> {

                requests.requestMatchers("/auth/*")
                        .permitAll()
                        .anyRequest()
                        .authenticated();
            })

            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .build();
    }
}