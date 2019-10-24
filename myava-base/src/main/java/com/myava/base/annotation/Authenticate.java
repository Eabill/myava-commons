package com.myava.base.annotation;

import com.myava.base.enums.Algorithm;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 认证
 *
 * @author biao
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Authenticate {

    Algorithm signAlgorithm() default Algorithm.MD5;
}
