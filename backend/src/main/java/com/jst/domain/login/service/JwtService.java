package com.jst.domain.login.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jst.common.error.ErrorCode;
import com.jst.domain.login.dto.TokenDto;
import com.jst.domain.login.repository.TokenEntity;
import com.jst.domain.login.repository.TokenRepository;
import com.jst.domain.login.vo.TokenVo;
import com.jst.domain.member.repository.UserEntity;
import com.jst.domain.member.repository.UserRepository;
import com.jst.domain.member.service.UserPrincipal;
import com.jst.exception.AuthorizationException;
import com.jst.exception.BaseException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String ID_CLAIM = "id";
    private static final String BEARER = "Bearer ";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;


    /**
     * AccessToken 생성 메소드
     */
    public String createAccessToken(String id) {
        return createToken(ACCESS_TOKEN_SUBJECT, id, accessTokenExpirationPeriod);
    }

    /**
     * RefreshToken 생성
     */
    public String createRefreshToken() {
        return createToken(REFRESH_TOKEN_SUBJECT, "", refreshTokenExpirationPeriod);
    }

    private String createToken(String subject, String claim, Long expiredTime) {
        Date now = new Date();
        return JWT.create()
                .withSubject(subject)  // 제목
                .withClaim(ID_CLAIM, claim)  // jwt에 이메일 내용 추가
                .withExpiresAt(new Date(now.getTime() + expiredTime))  // 만료 시간 추가
                .sign(Algorithm.HMAC512(secretKey));  // 어떤걸로 암호화
    }

    /**
     * AccessToken 헤더에 실어서 보내기
     */
    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(accessHeader, accessToken);
        log.info("재발급된 Access Token : {}", accessToken);
    }

    /**
     * AccessToken + RefreshToken 헤더에 실어서 보내기
     */
    public void sendAccessAndRefreshToken(HttpServletResponse response, String id) {
        response.setStatus(HttpServletResponse.SC_OK);
        setAccessTokenHeader(response, createAccessToken(id));
        setRefreshTokenHeader(response, createRefreshToken());
        log.info("Access Token, Refresh Token 헤더 설정 완료");
    }

    /**
     * 헤더에서 RefreshToken 추출
     * 토큰 형식 : Bearer XXX에서 Bearer를 제외하고 순수 토큰만 가져오기 위해서
     * 헤더를 가져온 후 "Bearer"를 삭제(""로 replace)
     */
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    /**
     * 헤더에서 AccessToken 추출
     * 토큰 형식 : Bearer XXX에서 Bearer를 제외하고 순수 토큰만 가져오기 위해서
     * 헤더를 가져온 후 "Bearer"를 삭제(""로 replace)
     */
    public Optional<String> extractAccessToken(HttpServletRequest request) throws Exception {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    /**
     * AccessToken에서 Email 추출
     * 추출 전에 JWT.require()로 검증기 생성
     * verify로 AceessToken 검증 후
     * 유효하다면 getClaim()으로 이메일 추출
     * 유효하지 않다면 빈 Optional 객체 반환
     */
    public Optional<String> extractEmail(String accessToken) throws Exception {
        // 토큰 유효성 검사하는 데에 사용할 알고리즘이 있는 JWT verifier builder 반환
        return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                .build() // 반환된 빌더로 JWT verifier 생성
                .verify(accessToken) // accessToken을 검증하고 유효하지 않다면 예외 발생
                .getClaim(ID_CLAIM) // claim(Emial) 가져오기
                .asString());
    }

    public Optional<String> extracToken(String token) throws Exception {
        return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                .build() // 반환된 빌더로 JWT verifier 생성
                .verify(token).getToken()); // accessToken을 검증하고 유효하지 않다면 예외 발생
    }

    /**
     * AccessToken 헤더 설정
     */
    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, accessToken);
    }

    /**
     * RefreshToken 헤더 설정
     */
    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshHeader, refreshToken);
    }

    public void invailedToken(HttpServletRequest request) throws Exception {
        Optional<String> optional = extractAccessToken(request);
        String token = optional.orElseThrow(() -> new AuthorizationException());

        optional = extractEmail(token);
        String email = optional.orElseThrow(() -> new AuthorizationException());

        Optional<UserEntity> optionalUser = userRepository.findById(email);
        UserEntity userEntity = optionalUser.orElseThrow(() -> new AuthorizationException());

        Optional<TokenEntity> optionalToken = tokenRepository.findByAccessToken(token);
        optionalToken.orElseThrow(() -> new AuthorizationException());

        UserPrincipal userPrincipal = new UserPrincipal(userEntity);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public TokenVo invailedRefreshToken(TokenDto tokenDto) throws Exception {
        String email = JWT.decode(tokenDto.getAccessToken()).getClaim(ID_CLAIM).asString();

        Optional<UserEntity> optionalUser = userRepository.findById(email);
        UserEntity userEntity = optionalUser.orElseThrow(() -> new AuthorizationException());

        Optional<TokenEntity> optionalToken = tokenRepository.findById(tokenDto.getRefreshToken());
        TokenEntity token = optionalToken.orElseThrow(() -> new AuthorizationException());

        if (!token.getRefreshToken().equals(tokenDto.getRefreshToken())) {
            throw new AuthorizationException();
        }

        try {
            extracToken(token.getRefreshToken());
        } catch (Exception e) {
            tokenRepository.delete(token);
            throw new AuthorizationException();
        }

        tokenRepository.delete(token);
        TokenVo tokenVo = createToken(userEntity.getId());

        UserPrincipal userPrincipal = new UserPrincipal(userEntity);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return tokenVo;
    }

    public TokenVo createToken(String id) {
        TokenVo tokenVo = TokenVo.builder()
                .accessToken(createAccessToken(id))
                .refreshToken(createRefreshToken())
                .build();

        TokenEntity token = TokenEntity.builder()
                .accessToken(tokenVo.getAccessToken())
                .refreshToken(tokenVo.getRefreshToken()).build();

        tokenRepository.save(token);

        return tokenVo;
    }
}
