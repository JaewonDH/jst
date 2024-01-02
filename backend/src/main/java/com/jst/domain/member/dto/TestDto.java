package com.jst.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestDto
{

    @NotBlank(message = "이메일 입력해주세요 ")
    private String id;

    @NotBlank(message = "이름을 입력해주세요 ")
    private String userNm;

    @NotBlank(message = "폰 정보를 입력해주세요 ")
    private String phone;
}
