package com.myava.base.annotation;

import com.myava.base.util.DateUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.ParseException;
import java.util.Date;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * 日期格式校验
 *
 * @author biao
 */
@Target({ METHOD, FIELD, CONSTRUCTOR, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = { DateFormat.DateFormatValidator.class })
public @interface DateFormat {

    String message() default "日期格式不合法";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    boolean required() default false;

    String pattern() default "yyyy-MM-dd HH:mm:ss";

    class DateFormatValidator implements ConstraintValidator<DateFormat, String> {

        private boolean required;

        private String pattern;

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (StringUtils.isEmpty(value)) {
                return !required;
            }
            Date date = null;
            try {
                date = DateUtil.parseDate(value, pattern);
            } catch (ParseException e) {
            }
            return date == null ? false : true;
        }

        @Override
        public void initialize(DateFormat constraintAnnotation) {
            this.required = constraintAnnotation.required();
            this.pattern = constraintAnnotation.pattern();
        }
    }

}
