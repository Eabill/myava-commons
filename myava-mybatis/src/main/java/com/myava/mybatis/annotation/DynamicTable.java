package com.myava.mybatis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 动态表注解
 *
 * @author biao
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface DynamicTable {

    /**
     * 分表前缀
     * @return
     */
    String prefix();

    /**
     * 分表容量
     * @return
     */
    int capacity() default 0;

    /**
     * 分表字段
     * @return
     */
    String field() default "id";

    /**
     * 连接符
     * @return
     */
    String symbol() default "_";
}
