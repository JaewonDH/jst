package com.jst.exception;

import com.jst.common.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BaseException  extends RuntimeException{
    private final ErrorCode errorCode;
    private final String message;
}