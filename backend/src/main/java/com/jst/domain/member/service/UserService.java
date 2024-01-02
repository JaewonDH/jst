package com.jst.domain.member.service;

import com.jst.common.Common;
import com.jst.common.ResultVo;
import com.jst.common.error.ErrorCode;
import com.jst.domain.member.dto.TestDto;
import com.jst.domain.member.dto.UserDto;
import com.jst.domain.member.mapper.UserMapper;
import com.jst.domain.member.repository.UserEntity;
import com.jst.domain.member.repository.UserRepository;
import com.jst.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResultVo insertUser(UserDto.Create create, String type) {
        if (isMybatis(type)) {
            TestDto testDto = new TestDto();
            testDto.setId(create.getEmail());
            testDto.setUserNm(create.getUserNm());
            testDto.setPhone(create.getPhone());
            userMapper.addUser(testDto);
            return new ResultVo(Common.ResultErrorCode.SUCCESS, "저장 완료");
        } else {
            UserEntity userEntity = UserEntity.builder()
                    .id(create.getEmail())
                    .password(passwordEncoder.encode(create.getPassword()))
                    .phone(create.getPhone())
                    .userNm(create.getUserNm())
                    .build();
            userRepository.save(userEntity);
            return new ResultVo(Common.ResultErrorCode.SUCCESS, "저장 완료");
        }
    }

    public ResultVo updateUser(UserDto.Update update, String type) {
        if (isMybatis(type)) {
            Map<String, Object> user = userMapper.getUser("");
            if (user == null) {
                throw new BaseException(ErrorCode.BAD_REQUEST, "수정 할 수 없는 아이디 입니다.");
            }
            TestDto testDto = new TestDto();
            testDto.setUserNm(update.getUserNm());
            testDto.setPhone(update.getPhone());
            userMapper.updateUser(testDto);
        } else {
            Optional<UserEntity> optional = userRepository.findById("");
            UserEntity userEntity = optional.orElseThrow(() -> new BaseException(ErrorCode.BAD_REQUEST, "아이디가 없습니다."));

            if (update.getUserNm() != null) {
                userEntity.setUserNm(update.getUserNm());
            }

            if (update.getPhone() != null) {
                userEntity.setPhone(update.getPhone());
            }

            userRepository.save(userEntity);
        }

        return new ResultVo(Common.ResultErrorCode.SUCCESS, "수정완료");
    }

    public ResultVo getUsers(String type) {
        List list;
        if (isMybatis(type)) {
            list = userMapper.getUsers();
        } else {
            list = userRepository.findAll();
        }
        return new ResultVo(Common.ResultErrorCode.SUCCESS, "", list);
    }

    public ResultVo deleteUser(String email, String type) {
        if (isMybatis(type)) {
            Map<String, Object> user = userMapper.getUser(email);
            if (user == null) {
                throw new BaseException(ErrorCode.BAD_REQUEST, "삭제 할 수 없는 아이디 입니다.");
            }
            userMapper.deleteUser(email);
            return new ResultVo(Common.ResultErrorCode.SUCCESS, "삭제완료");
        } else {
            Optional<UserEntity> optional = userRepository.findById(email);
            UserEntity userEntity = optional.orElseThrow(() -> new BaseException(ErrorCode.BAD_REQUEST, "삭제 할 수 없는 아이디 입니다."));
            userRepository.delete(userEntity);
            return new ResultVo(Common.ResultErrorCode.SUCCESS, "삭제완료");
        }
    }

    private boolean isMybatis(String type) {
        return type != null && !type.isEmpty() && type.equals("mybatis");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> optional = userRepository.findById(username);
        UserEntity userEntity = optional.orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 아이디 입니다."));
        return new UserPrincipal(userEntity);
    }
}
