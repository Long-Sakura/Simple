package com.itxxy.pojo.common;

import lombok.Data;

/**
 * 通用返回类
 */
@Data
public class Result {
    private int code;//0->正确
    private String message;
    private String description;
    private Object data;


    public Result(int code, String message,String description, Object data) {
        this.code = code;
        this.message = message;
        this.description=description;
        this.data = data;
    }
    public static Result success(Object data){
        return new Result(0,"ok","",data);
    }
    public static Result success(ErrorCode errorCode,Object data){
        return new Result(errorCode.getCode(),errorCode.getMessage(),errorCode.getDescription(),data);
    }

    public static Result error(){
        return new Result(1,null,"",null);
    }
    public static Result error(ErrorCode errorCode){
        return new Result(errorCode.getCode(),errorCode.getMessage(),errorCode.getDescription(),null);
    }
    public static Result error(ErrorCode errorCode,String message,String description){
        return new Result(errorCode.getCode(),message,description,null);
    }
    public static Result error(int code,String message,String description){
        return new Result(code,message,description,null);
    }
}
