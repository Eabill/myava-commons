package com.myava.base.enums;

/**
 * 通用状态枚举类
 *
 * @author biao
 */
public enum Status {

    SUCCESS(200, "操作成功"),
    ERR_400(400, "参数错误"),
    ERR_401(401, "没有权限"),
    ERR_403(403, "禁止访问"),
    ERR_404(404, "资源不存在"),
    ERR_500(500, "系统繁忙");

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
    public static boolean isSuccess(Status status) {
        return Status.SUCCESS == status;
    }
}
