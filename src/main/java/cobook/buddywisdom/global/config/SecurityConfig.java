package cobook.buddywisdom.global.config;

import cobook.buddywisdom.global.jwt.JwtAccessDeniedHandler;
import cobook.buddywisdom.global.jwt.JwtAuthenticationEntryPoint;
import cobook.buddywisdom.global.jwt.TokenProvider;
import cobook.buddywisdom.global.security.CustomAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import java.util.Collections;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final CustomAuthenticationProvider customAuthenticationProvider;

    // public path 는 모두 허용
    private static final String PUBLIC_PATH = "/api/v1/public/**";

    public SecurityConfig(TokenProvider tokenProvider
            , JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAccessDeniedHandler jwtAccessDeniedHandler, CustomAuthenticationProvider customAuthenticationProvider) {
        this.tokenProvider = tokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.customAuthenticationProvider = customAuthenticationProvider;
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(customAuthenticationProvider));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                /** 쿠키-세션 기반 disable */
                .csrf()
                .disable()

                /** Token 인증 시, 세션 강제 종료 */
                // 서버에서 관리되는 세션 없이 클라이언트에서 요청하는 헤더에 token 을 담아보내는 인증방식을 사용
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                /** 인증 진입점(authenticationEntryPoint)으로 이용할 것을 설정 */
                // 설정하지 않을 시
                // 401 Unauthorized : 자격 증명 제공 필요
                // 403 Forbidden : 필요한 권한을 찾을 수 없음
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                // 인증
                .authenticationProvider(customAuthenticationProvider)
                .authorizeHttpRequests(auth ->
                    auth
                        // 모두 허용
                        .requestMatchers(HttpMethod.GET, "/").permitAll()
                        .requestMatchers(HttpMethod.POST, PUBLIC_PATH).permitAll()
                        // 권한에 따른 path 를 지정
                        .requestMatchers("/api/v1/admin/**").hasAnyRole("ADMIN")
                        .requestMatchers("/api/v1/coaches/**").hasAnyRole("COACH")
                        .requestMatchers("/api/v1/mentees/**").hasAnyRole("MENTEE")
                        .anyRequest().authenticated()
                )

                /** Token 생성 및 유효성 검증을 위한 TokenProvider 를 생성  */
                // JwtFilter 를 SecurityConfig 에 적용할 JwtSecurityConfig 를 생성
                .apply(new JwtSecurityConfig(tokenProvider))
                .and()
                .formLogin().disable()
        ;
        return http.build();
    }
}
