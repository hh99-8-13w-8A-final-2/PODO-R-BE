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
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@RequiredArgsConstructor
public class WebSecurityConfig {

    @Value("${jwt.secret}")
    private String secretKey;
    @Autowired
    private final JwtTokenProvider jwtTokenProvider;

//    private final AccessDeniedHandlerException accessDeniedHandlerException;
//
//    private final AuthenticationEntryPointException authenticationEntryPointException;

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        cors 사용
        http.cors();

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
                .antMatchers("/api/login").permitAll()
                .antMatchers("/api/register").permitAll()
                .antMatchers("/swagger-ui.html/**").permitAll()
                .anyRequest().permitAll()
                .and()
                .oauth2Login()


//                필터 적용
                .and()
                .apply(new JwtConfiguration(secretKey, jwtTokenProvider));

        return http.build();
    }
}