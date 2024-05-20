package com.manji.base.config;

import com.manji.base.filter.JwtAuthenticationFilter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
@Slf4j
public class SecurityConfiguration {

    @Resource
    private LogoutHandler logoutHandler;
    @Resource
    private JwtAuthenticationFilter jwtAuthentication;
    @Resource
    private AuthenticationProvider authenticationProvider;

    @Value("${security.cors.allowed-origins}")
    private List<String> ALLOWED_ORIGINS;

    @Value("${security.whitelist-paths}")
    private String[] WHITELIST_PATHS;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req -> req.requestMatchers(WHITELIST_PATHS)
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthentication, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .logout(logout -> logout.logoutUrl("/api/v1/auth/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()));
        return http.build();
    }

    /**
     * 系统跨域配置
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 允许的来源，可以根据实际情况进行设置
        ALLOWED_ORIGINS.forEach(configuration::addAllowedOrigin);
        // 允许的 HTTP 方法，这里列出了一些常用的，根据实际情况添加
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedMethod("PUT");
        configuration.addAllowedMethod("DELETE");
        // 允许的请求头，根据实际情况添加
        configuration.addAllowedHeader("Authorization");
        configuration.addAllowedHeader("Content-Type");
        // 是否允许发送 Cookie
        configuration.setAllowCredentials(true);
        // 设置预检请求的有效期，单位秒
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
