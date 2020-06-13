package com.myava.springboot.controller;

import com.myava.base.RespResult;
import com.myava.base.ServiceException;
import com.myava.base.enums.Status;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.beans.PropertyEditorSupport;

/**
 * Spring controller advice处理器
 *
 * @author biao
 */
@ControllerAdvice
public class SpringControllerAdvice {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // 对文本进行html转义，防止XSS攻击
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
            }
            @Override
            public String getAsText() {
                Object value = getValue();
                return value == null ? "" : value.toString();
            }
        });
    }

    /**
     * 统一处理异常
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object exceptionHandle(Exception ex) {
        ex.printStackTrace();
        if (!(ex instanceof ServiceException)) {
            return RespResult.error();
        }
        ServiceException serviceEx = (ServiceException) ex;
        return RespResult.of(serviceEx.getCode(), serviceEx.getMessage());
    }

    /**
     * MethodArgumentNotValidException异常处理
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object exceptionHandle(MethodArgumentNotValidException ex) {
        StringBuilder sb = new StringBuilder();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            sb.append(error.getField()).append(":").append(error.getDefaultMessage()).append(",");
        }
        String message = StringUtils.removeEnd(sb.toString(), ",");
        return RespResult.of(Status.ERR_400.getCode(), message);
    }

    /**
     * ConstraintViolationException异常处理
     * @param ex
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object exceptionHandle(ConstraintViolationException ex) {
        StringBuilder sb = new StringBuilder();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            sb.append(violation.getMessage()).append(",");
        }
        String message = StringUtils.removeEnd(sb.toString(), ",");
        return RespResult.of(Status.ERR_400.getCode(), message);
    }

    /**
     * MissingServletRequestParameterException异常处理
     * @param ex
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object exceptionHandle(MissingServletRequestParameterException ex) {
        return RespResult.of(Status.ERR_400.getCode(), ex.getMessage());
    }

    /**
     * HttpRequestMethodNotSupportedException异常处理
     * @param ex
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Object exceptionHandle(HttpRequestMethodNotSupportedException ex) {
        return RespResult.of(Status.ERR_405.getCode(), ex.getMessage());
    }
}
