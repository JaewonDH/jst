package com.jst.exception;

import com.jst.common.error.ErrorCode;

public class AuthorizationException extends BaseException{
    public AuthorizationException(ErrorCode errorCode) {
        super(errorCode,null);
    }

    public AuthorizationException() {
        super(ErrorCode.UNAUTHORIZED_REQUEST,null);
    }
}
