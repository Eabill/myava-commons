package com.myava.base;

import com.myava.base.enums.Status;
import lombok.Data;

import java.io.Serializable;

/**
 * 响应结果
 *
 * @author biao
 */
@Data
public class RespResult<T> implements Serializable {

    private static final long serialVersionUID = -8175960154775317812L;

    private Integer code;

    private String message;

    private T data;

    public RespResult() {
        this.code = Status.SUCCESS.getCode();
        this.message = Status.SUCCESS.getMessage();
    }

    public RespResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public RespResult(T data) {
        this();
        this.data = data;
    }

    public RespResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static RespResult success() {
        return new RespResult();
    }

    public static RespResult success(Object data) {
        return new RespResult(data);
    }

    public static RespResult error() {
        return new RespResult(Status.ERR_500.getCode(), Status.ERR_500.getMessage());
    }

    public static RespResult of(Integer code, String message) {
        return new RespResult(code, message);
    }

    public static RespResult of(Integer code, String message, Object data) {
        return new RespResult(code, message, data);
    }

    public boolean isSuccess() {
        return Status.SUCCESS.getCode().equals(code);
    }

}
