package com.dili.settlement.handler;

import com.alibaba.fastjson.JSON;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.util.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 统一异常处理类
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public String handleBusinessException(BusinessException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOGGER.error("", e);
        return this.dealResult(e.getCode(), e.getMessage(), e, request, response);
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(Exception.class)
    public String handleBusinessException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOGGER.error("", e);
        return this.dealResult(ResultCode.APP_ERROR, "系统异常,请稍后重试", e, request, response);
    }

    /**
     * 处理返回结果
     * @param code
     * @param message
     * @param e
     * @param request
     * @param response
     */
    private String dealResult(String code, String message, Throwable e, HttpServletRequest request, HttpServletResponse response) throws IOException {
       Boolean returnJsonType = (Boolean) request.getAttribute("return_json_type");
       if (Boolean.TRUE.equals(returnJsonType)) {
           response.setContentType("application/json;charset=UTF-8");
           response.getWriter().write(JSON.toJSONString(BaseOutput.create(code, message)));
           return null;
       }
        request.setAttribute("exception", e);
        return SpringUtil.getProperty("error.page.default", "error/default");
    }
}
