package com.jst.domain.member.controller;

import com.jst.common.Common;
import com.jst.common.ResultVo;
import com.jst.domain.member.dto.UserDto;
import com.jst.domain.member.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

@Log4j2
@RestController
@Tag(name="사용자", description = "사용자 관련 API")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value="api/users" ,method = RequestMethod.GET)
    public ResponseEntity<ResultVo> getUsers(@RequestParam(name="myBatis",required = false) String type) {
        return ResponseEntity.ok(userService.getUsers(type));
    }

    @RequestMapping(value="api/user" ,method = RequestMethod.PUT)
    public ResponseEntity<ResultVo> updateUser(UserDto.Update update,@RequestParam(name="myBatis",required = false) String type) {
        return ResponseEntity.ok(userService.updateUser(update,type));
    }

    @RequestMapping(value="api/user" ,method = RequestMethod.DELETE)
    public ResponseEntity<ResultVo> deleteUser(@RequestParam String email,@RequestParam(name="myBatis",required = false) String type){
        return ResponseEntity.ok(userService.deleteUser(email,type));
    }

    // 비동기 동작
    @RequestMapping(value="api/test" ,method = RequestMethod.GET)
    public Callable<ResponseEntity<ResultVo>> test() {
        return ()->{
            try{
                Thread.sleep(5000);
                return ResponseEntity.ok(new ResultVo(Common.ResultErrorCode.SUCCESS, ""));
            }catch (Exception e ){
                return ResponseEntity.ok(new ResultVo(Common.ResultErrorCode.ERROR, e.getMessage()));
            }
        };
    }
}
