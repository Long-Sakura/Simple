package com.itxxy.exception;

import com.itxxy.pojo.common.ErrorCode;
import com.itxxy.pojo.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public Result businessExceptionHandler(BusinessException e){
        log.error("遇到BusinessException,message: "+e.getMessage(),e);
        return Result.error(e.getCode(),e.getMessage(),e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public Result RuntimeExceptionHandler(RuntimeException e){
        log.error("遇到RuntimeException,message: "+e.getMessage(),e);
        return Result.error(ErrorCode.SYSTEM_ERROR,e.getMessage(),"");
    }

}
