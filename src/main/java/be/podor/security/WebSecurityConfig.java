package be.podor.security;


import be.podor.security.jwt.JwtConfiguration;
import be.podor.security.jwt.JwtFilter;
import be.podor.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        cors 사용
        http.cors().configurationSource(corsConfigurationSource());

//        csrf 비활성화
        http.csrf().disable()


//                세션 미사용 설정
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

//                api 허용 목록!
                .and()
                .authorizeRequests()
                .antMatchers("/api/oauth/**","/api/twitter/**","/api/member/**").permitAll()
                .antMatchers("/api/reviews/**","/api/musicals/**","/api/theaters/**").permitAll()
                .anyRequest().permitAll()
                .and()


//                필터 적용
                .apply(new JwtConfiguration(jwtTokenProvider));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // origin
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        // method
        configuration.setAllowedMethods(Arrays.asList("*"));
        // header
        configuration.setAllowedHeaders(Arrays.asList("*"));
        // Authorization, Refresh-Token 헤더 설정
        configuration.setExposedHeaders(Arrays.asList(
                JwtFilter.AUTHORIZATION_HEADER,
                JwtFilter.REFRESH_TOKEN_HEADER
        ));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}