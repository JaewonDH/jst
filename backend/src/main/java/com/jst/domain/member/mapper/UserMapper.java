package com.jst.domain.member.mapper;

import com.jst.domain.member.dto.TestDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    List<TestDto> getUsers();

    Map<String, Object> getUser(String id);

    void addUser(TestDto create);

    void updateUser(TestDto create);

    void deleteUser(String id);
}