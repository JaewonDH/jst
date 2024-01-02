package com.jst.exception;

import com.jst.common.error.ErrorCode;

public class TokenExpiredException  extends BaseException{
    public TokenExpiredException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
