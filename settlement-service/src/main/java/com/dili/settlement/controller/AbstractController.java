package com.dili.settlement.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;

import java.util.stream.Stream;

/**
 * 抽象基础controller
 */
public abstract class AbstractController {

    /**
     * 获取用户登录信息
     * @return
     */
    protected UserTicket getUserTicket() {
        SessionContext sessionContext = SessionContext.getSessionContext();
        UserTicket userTicket = sessionContext.getUserTicket();
        if(userTicket == null) {
            throw new BusinessException("","无法获取用户登录信息,请重新登录");
        }
        return userTicket;
    }

    /**
     * 去除数组中空元素
     * @param array
     * @return
     */
    protected String[] deleteEmptyElement(String[] array) {
        if (ArrayUtil.isEmpty(array)) {
            return new String[]{};
        }
        return Stream.of(array).filter(StrUtil::isNotBlank).toArray(String[]::new);
    }
}
