package com.myava.base.enums;

/**
 * 通用状态枚举类
 *
 * @author biao
 */
public enum Status {

    SUCCESS(200, "操作成功");

    private Integer code;

    private String message;

    Status(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    /**
     * 是否成功
     * @param status
     * @return
     */
    public static boolean success(Status status) {
        return Status.SUCCESS == status;
    }
}
