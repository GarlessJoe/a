package com.joe.common_.result;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;
    //构造器私有化
    private Result(){
    }
    public static<T> Result<T> ok(){
        return build(null,ResultCode.SUCCESS);
    }
    public static<T> Result<T> ok(T data){
        return build(data,ResultCode.SUCCESS);
    }
    public static<T> Result<T> fail() {
        return build(null,ResultCode.FAIL);
    }
    public static<T> Result<T> fail(T data) {
        return build(data,ResultCode.FAIL);
    }
    public static<T> Result<T> build(T data,ResultCode resultCodeEnum){
        Result<T> result = new Result<>();
        if(data!=null)
            result.setData(data);
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }
    public Result<T> message(String msg){
        this.setMessage(msg);
        return this;
    }
}
