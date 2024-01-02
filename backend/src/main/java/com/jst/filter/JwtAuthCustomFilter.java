package com.jst.filter;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jst.common.error.ErrorCode;
import com.jst.domain.login.service.JwtService;
import com.jst.exception.AuthorizationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
//SecurityContextPersistenceFilter
public class JwtAuthCustomFilter extends OncePerRequestFilter {
    private static final List<String> EXCLUDE_URL =
            List.of("/api/login","/api/sign","/swagger-ui","/v3/api-docs","/swagger-resources","/api/refresh-token");

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            jwtService.invailedToken(request);
            filterChain.doFilter(request,response);
        } catch (TokenExpiredException exception) {
            setErrorResponse(response, ErrorCode.TOCKEN_EXPIRED_REQUEST);
        }catch (SignatureVerificationException exception){
            setErrorResponse(response, ErrorCode.INVALIDE_TOKEN);
        } catch (Exception exception) {
            log.debug("e" + exception.getMessage());
            setErrorResponse(response, ((AuthorizationException) exception).getErrorCode());
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return EXCLUDE_URL.stream().anyMatch(exclude -> request.getServletPath().startsWith(exclude));
    }

    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode){
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        JwtAuthorizationFilter.ErrorResponse errorResponse = new JwtAuthorizationFilter.ErrorResponse(errorCode.getHttpStatus().value(), errorCode.getMessage());
        try{
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    @Data
    public static class ErrorResponse{
        private final Integer code;
        private final String message;
    }
}
