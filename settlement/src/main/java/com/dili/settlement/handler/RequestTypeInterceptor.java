package com.dili.settlement.handler;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 拦截器 处理返回值类型
 * 默认只要方法上添加 @ResponseBody 或者 类上有 @RestController则认为返回json
 */
public class RequestTypeInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod)handler;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(ResponseBody.class) || handlerMethod.getBeanType().isAnnotationPresent(RestController.class)) {
            request.setAttribute("return_json_type", true);
        }
        return true;
    }
}
