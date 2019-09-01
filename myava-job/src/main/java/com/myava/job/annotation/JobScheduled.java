package com.myava.job.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * elastic-job注解，Example: @JobScheduled(cron = "0/2 * * * * ?")
 *
 * @author biao
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface JobScheduled {

    @AliasFor("cron")
    String value() default "";

    @AliasFor("value")
    String cron() default "";

    String jobName() default "";

    int shardingTotalCount() default 1;

    String shardingItemParameters() default "";

    String jobParameter() default "";

    String description() default "";

    boolean disabled() default false;

    boolean overwrite() default true;

}


