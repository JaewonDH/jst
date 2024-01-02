package com.jst.domain.login.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TokenDto {
    @NotBlank(message = "accessToken 값이 없습니다.")
    private String accessToken;
    @NotBlank(message = "refreshToken 값이 없습니다.")
    private String refreshToken;
}
