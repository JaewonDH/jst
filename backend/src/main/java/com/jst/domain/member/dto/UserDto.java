package com.jst.domain.member.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class UserDto {

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Create{
        @NotBlank(message = "이름을 입력해주세요 ")
        private String userNm;

        @NotBlank(message = "이름을 입력해주세요 ")
        private String email;

        @NotBlank(message = "폰 정보를 입력해주세요 ")
        private String phone;

        @NotBlank(message = "비밀번호를 입력 해주세요")
        private String password;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Update{
        @NotBlank(message = "id를 입력해주세요 ")
        private String userNm;
        private String phone;
    }

}
