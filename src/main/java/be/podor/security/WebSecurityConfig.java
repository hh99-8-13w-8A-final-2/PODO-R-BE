package be.podor.security;


import be.podor.security.jwt.JwtConfiguration;
import be.podor.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
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

    @Autowired
    private final JwtTokenProvider jwtTokenProvider;

//    private final AccessDeniedHandlerException accessDeniedHandlerException;
//
//    private final AuthenticationEntryPointException authenticationEntryPointException;
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

//               예외 처리
                .exceptionHandling()
//                .authenticationEntryPoint(authenticationEntryPointException)
//                .accessDeniedHandler(accessDeniedHandlerException)

//                세션 미사용 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

//                api 허용 목록!
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/**").permitAll()
                .antMatchers("/member/login").permitAll()
                .antMatchers("/api/register").permitAll()
                .antMatchers("/swagger-ui.html/**").permitAll()
                .anyRequest().permitAll()
                .and()
                .oauth2Login()


//                필터 적용
                .and()
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
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Refresh-Token"));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}