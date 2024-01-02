package com.jst.domain.login.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenVo {
    private String accessToken;
    private String refreshToken;
}
