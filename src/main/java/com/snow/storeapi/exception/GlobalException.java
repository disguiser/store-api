package com.snow.storeapi.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = Exception.class) // 捕获的异常类型
    public Object globalException(Exception ex) {

        ex.printStackTrace();

        return "出现异常";
    }

    @ExceptionHandler(value = ArithmeticException.class)
    public Object arithmeticException(ArithmeticException ex) {

        ex.printStackTrace();
        return "by zero异常";
    }
}
