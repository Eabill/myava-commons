package com.myava.springboot.interceptor;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器
 *
 * @author biao
 */
@Slf4j
public class LoginInterceptor extends HandlerInterceptorAdapter {

    /** 无需拦截地址 **/
    public static final String NO_INTERCEPT_URI = ".*/(error).*";
    /** 请求开始时间 */
    private final ThreadLocal<Long> threadLocal = new NamedThreadLocal<>("StartTime");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        threadLocal.set(System.currentTimeMillis());
        String uri = request.getRequestURI();
        log.info("RequestURI：{}, Params: {}", uri, JSON.toJSONString(request.getParameterMap()));
        if (uri.matches(NO_INTERCEPT_URI)) {
            return true;
        }
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mv) throws Exception {
        super.postHandle(request, response, handler, mv);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (handler instanceof HandlerMethod) {
            String path = request.getRequestURI();
            log.info("RequestURI: {}, spend(ms): {}", path, System.currentTimeMillis() - threadLocal.get());
            threadLocal.remove();
        }
        super.afterCompletion(request, response, handler, ex);
    }
}
