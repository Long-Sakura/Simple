package com.itxxy.exception;

import com.itxxy.pojo.common.ErrorCode;
import lombok.Getter;

/**
 * 自定义业务异常（进行全局异常处理）
 */
@Getter
public class BusinessException extends RuntimeException {
    private final int code;
    private final String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code=code;
        this.description=description;
    }
    public BusinessException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.code=errorCode.getCode();
        this.description= errorCode.getDescription();
    }
    public BusinessException(ErrorCode errorCode,String description){
        super(errorCode.getMessage());
         this.code=errorCode.getCode();
         this.description=description;
    }
}
