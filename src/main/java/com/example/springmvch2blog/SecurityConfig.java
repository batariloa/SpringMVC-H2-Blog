package com.example.springmvch2blog;

import com.example.springmvch2blog.config.*;
import com.example.springmvch2blog.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserAuthenticationEntryPoint userAuthenticationEntryPoint;
    private final UserAuthenticationManager userAuthenticationManager;
    private final AuthenticationService authenticationService;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("https://blogster-frontend.herokuapp.com/");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.cors()
                   .and()

                   .exceptionHandling()
                   .authenticationEntryPoint(userAuthenticationEntryPoint)
                   .and()
                   .httpBasic()
                   .and()
                   .addFilterBefore(new UsernamePasswordFilter(userAuthenticationManager), BasicAuthenticationFilter.class)
                   .addFilterBefore(new RefreshTokenFilter(userAuthenticationManager, authenticationService), UsernamePasswordFilter.class)
                   .addFilterBefore(new CookieAuthenticationFilter(userAuthenticationManager, authenticationService), RefreshTokenFilter.class)
                   //AccessTokenFilter is called before the RefreshTokenFilter
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