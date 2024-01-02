package com.jst.filter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jst.common.error.ErrorCode;
import com.jst.domain.login.service.JwtService;
import com.jst.exception.AuthorizationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import java.io.IOException;
import java.util.List;

@Log4j2
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtService jwtService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,JwtService jwtService) {
        super(authenticationManager);
        this.jwtService=jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.debug("request.getRequestURL()="+request.getRequestURL());
        try {
            jwtService.invailedToken(request);
            super.doFilterInternal(request, response, chain);
        }catch (Exception exception){
            log.debug("e"+exception.getMessage());
            AuthorizationException authorizationException = (AuthorizationException) exception;
            setErrorResponse(response,authorizationException.getErrorCode());
        }
    }

    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode){
        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getHttpStatus().value(), errorCode.getMessage());
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
