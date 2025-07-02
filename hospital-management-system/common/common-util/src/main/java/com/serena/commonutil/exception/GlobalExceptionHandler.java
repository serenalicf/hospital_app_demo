package com.serena.commonutil.exception;

import com.serena.commonutil.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        e.printStackTrace();
        return Result.fail();
    }

    @ExceptionHandler(CustomException.class)
    public Result handleException(CustomException e) {
        e.printStackTrace();
        return Result.fail();
    }
}
