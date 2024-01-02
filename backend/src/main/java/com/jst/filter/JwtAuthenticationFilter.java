package com.jst.filter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jst.domain.login.dto.LoginDto;
import com.jst.domain.login.service.JwtService;
import com.jst.domain.member.service.UserPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.debug("failed.getMessage()="+failed.getMessage());
        super.unsuccessfulAuthentication(request, response, failed);
    }

    // 로그인 요청을 하면 로그인 시도를 위해 실행 되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getId(), loginDto.getPassword());
            Authentication authentication= authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            return authentication;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // attemptAuthentication 에서 인증이 성공되면 호출 되는 메서드 jwt토큰 발급
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UserPrincipal userPrincipal = (UserPrincipal) authResult.getPrincipal();
        log.debug("authResult.getPrincipal()="+userPrincipal.getUsername());
        log.debug("authResult.jwtService()="+jwtService);

        jwtService.sendAccessAndRefreshToken(response,userPrincipal.getUsername());
        //super.successfulAuthentication(request, response, chain, authResult);
    }
}
