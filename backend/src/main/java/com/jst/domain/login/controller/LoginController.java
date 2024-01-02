package com.jst.domain.login.controller;

import com.jst.common.ResultVo;
import com.jst.domain.login.dto.LoginDto;
import com.jst.domain.login.dto.TokenDto;
import com.jst.domain.login.service.LoginService;
import com.jst.domain.member.dto.UserDto;
import com.jst.domain.member.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@Tag(name="로그인", description = "로그인 관련 API")
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "api/refresh-token", method = RequestMethod.POST)
    public ResponseEntity<ResultVo> refreshToken(@RequestBody TokenDto tokenDto) throws Exception {
        return ResponseEntity.ok(loginService.onRefreshToken(tokenDto));
    }

    @RequestMapping(value="api/login" ,method = RequestMethod.POST)
    public ResponseEntity<ResultVo> onLogin(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(loginService.onLogin(loginDto));
    }

    @RequestMapping(value = "api/sign", method = RequestMethod.POST)
    public ResponseEntity<ResultVo> insertUser(@Valid @RequestBody UserDto.Create create) {
        return ResponseEntity.ok(userService.insertUser(create,""));
    }
}
