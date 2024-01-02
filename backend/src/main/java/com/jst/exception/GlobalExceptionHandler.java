package com.jst.exception;

import com.jst.common.error.ErrorCode;
import com.jst.common.error.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BaseException.class)
    private ResponseEntity<ErrorResponse> handleCustomException(BaseException baseException) {
        ErrorCode errorCode = baseException.getErrorCode();
        return handleExceptionInternal(errorCode,baseException.getMessage());
    };

    private ResponseEntity<ErrorResponse> handleExceptionInternal(ErrorCode errorCode,String message) {
        return ResponseEntity
                .status(errorCode.getHttpStatus().value())
                .body(new ErrorResponse(errorCode,message));
    }
}
