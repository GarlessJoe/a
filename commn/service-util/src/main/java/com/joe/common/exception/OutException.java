package com.joe.common.exception;

import com.joe.common_.result.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OutException extends RuntimeException {
    private Integer code ;
    private String msg;
    public OutException(ResultCode resultCode){
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
        this.msg = resultCode.getMessage();
    }

    @Override
    public String toString() {
        return "OutException{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
