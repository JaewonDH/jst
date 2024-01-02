package com.jst.domain.login.service;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.jst.common.Common;
import com.jst.common.ResultVo;
import com.jst.domain.login.dto.LoginDto;
import com.jst.domain.login.dto.TokenDto;
import com.jst.domain.login.vo.TokenVo;
import com.jst.exception.AuthorizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    public ResultVo onLogin(LoginDto loginDto) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getId(), loginDto.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (Exception e) {
            throw new AuthorizationException();
        }
        TokenVo tokenVo=jwtService.createToken(loginDto.getId());
        return new ResultVo(Common.ResultErrorCode.SUCCESS, "", tokenVo);
    }

    public ResultVo onRefreshToken(TokenDto tokenDto) {
        if(!StringUtils.hasText(tokenDto.getRefreshToken()) || !StringUtils.hasText(tokenDto.getAccessToken())){
            throw new AuthorizationException();
        }

        TokenVo tokenVo=null;
        try {
            tokenVo=jwtService.invailedRefreshToken(tokenDto);
        } catch (Exception e) {
            throw new AuthorizationException();
        }

        return new ResultVo(Common.ResultErrorCode.SUCCESS, "",tokenVo);
    }
}
