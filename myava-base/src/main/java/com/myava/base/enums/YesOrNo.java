package com.myava.base.enums;

/**
 * 通用是否枚举类
 *
 * @author biao
 */
public enum YesOrNo {

    NO(0, "否"), YES(1, "是");

    private Integer code;

    private String description;

    YesOrNo(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
