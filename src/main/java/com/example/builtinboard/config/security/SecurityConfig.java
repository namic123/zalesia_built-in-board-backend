package com.example.builtinboard.config.security;

import com.example.builtinboard.config.jwt.JWTFilter;
import com.example.builtinboard.config.oauth2.CustomSuccessHandler;
import com.example.builtinboard.service.auth.CustomOAuth2UserService;
import com.example.builtinboard.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
    // AuthenticationManager가 인자로 받을 AuthenticationConfiguration 객체 생성자 주입
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final CustomOAuth2UserService customOAuth2UserService;
    private CustomSuccessHandler customSuccessHandler;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil, CustomOAuth2UserService customOAuth2UserService, CustomSuccessHandler customSuccessHandler) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
        this.customOAuth2UserService = customOAuth2UserService;
        this.customSuccessHandler = customSuccessHandler;
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // cors 설정
        httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));
        // csrf disable
        httpSecurity
                .csrf((auth) -> auth.disable());
        // Form 로그인 방식 disable
        httpSecurity
                .formLogin((auth) -> auth.disable());
        // http basic 인증 방식 disable
        httpSecurity
                .httpBasic((auth) -> auth.disable());
        // OAuth2 로그인 과정에서 사용자 정보를 처리하기 위한 커스텀 서비스를 등록
        httpSecurity
                .oauth2Login((oauth2) -> oauth2
                        // 엔드포인트 설정. 인증 후에 사용자 정보를 가져오는 방법을 정의
                        .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                                // 인증 성공 후, 사용자 정보를 로드하고 처리하는 데 사용될 UserService의 구현체를 지정
                                .userService(customOAuth2UserService))
                        // 인증 성공 후, 실행될 클래스
                        .successHandler(customSuccessHandler)
                );
        // 경로별 인가 작업
        httpSecurity
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests.requestMatchers(new AntPathRequestMatcher("/**")).permitAll()
                                // 경로별 인증 요구
                                .requestMatchers(HttpMethod.POST, "/api/boards").hasAuthority("ROLE_GENERAL_MEMBER")
                                .requestMatchers(HttpMethod.PUT, "/api/boards/*").hasAuthority("ROLE_GENERAL_MEMBER")
                                .requestMatchers(HttpMethod.DELETE, "/api/boards/*").hasAuthority("ROLE_GENERAL_MEMBER")
                                .anyRequest().authenticated()
                );
        httpSecurity
                // JWTFilter(토큰 유효성 검증)를 LoginCustomFilter 전에 실행
                .addFilterBefore(new JWTFilter(jwtUtil), LoginCustomFilter.class)
                // 커스텀 필터로 필터링 (로그인 검증)
                .addFilterAt(new LoginCustomFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);
        // 세션 사용 안함
        httpSecurity
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(STATELESS)
                );
        return httpSecurity.build();
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //  도메인, HTTP 메서드, 헤더, 인증 정보(쿠키, HTTP인증 및 SSL 인증서등), 요청결과 캐싱, 클라이언트에서 접근 가능 헤더 요청 허용
        // singletonList는 단 하나의 요소만을 허용(불변성을 위함), 추가할 경우 UnsupportedOperationException 발생.
        corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:8080"));
        corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(3600L);

        corsConfiguration.setExposedHeaders(Collections.singletonList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration); // 모든 경로에 허용
        return source;
    }
}
