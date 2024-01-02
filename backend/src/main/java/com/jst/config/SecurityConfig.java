package com.jst.config;

import com.jst.domain.login.service.JwtService;
import com.jst.filter.JwtAuthCustomFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Log4j2
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    private JwtService jwtService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

//        JwtAuthenticationFilter jwtAuthenticationFilter=new JwtAuthenticationFilter(authenticationManagerBean(),jwtService);
//        jwtAuthenticationFilter.setFilterProcessesUrl("/api/login");
        return http
                .authorizeHttpRequests((authorizeRequests) ->{
                            authorizeRequests
                                    .requestMatchers("/swagger-resources/**").permitAll()
                                    .requestMatchers("/swagger-ui/**").permitAll()
                                    .requestMatchers("/v3/api-docs/**").permitAll()
                                    .requestMatchers("/api/refresh-token").permitAll()
                                    .requestMatchers("/api/login/**").permitAll()
                                    .requestMatchers("/api/sign").permitAll()
//                                    .requestMatchers("/api/users").permitAll()
                                    .anyRequest().authenticated();
                        }
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
// 직접 login api에서 생성 해야 함
                .addFilterBefore(new JwtAuthCustomFilter(jwtService), UsernamePasswordAuthenticationFilter.class)
//세션 방식
//                .addFilter(new JwtAuthorizationFilter(authenticationManagerBean(),jwtService))
//                .addFilter(jwtAuthenticationFilter)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
