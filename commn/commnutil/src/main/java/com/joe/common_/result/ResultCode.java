package com.joe.common_.result;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
public enum ResultCode {
    SUCCESS(200,"成功"),
    FAIL(201,"失败"),
    LOGIN_MOBLE_ERROR(202,"登录失败");
    private Integer code;
    private String message;

    private ResultCode(Integer code,String message){
        this.code=code;
        this.message=message;
    }
}
