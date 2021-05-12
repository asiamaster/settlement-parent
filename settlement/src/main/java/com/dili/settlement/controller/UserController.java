package com.dili.settlement.controller;

import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.domain.dto.UserQuery;
import com.dili.uap.sdk.rpc.UserRpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 用户相关 controller
 */
@Controller
@RequestMapping(value = "/user")
public class UserController extends AbstractController {

    @Autowired
    private UserRpc userRpc;

    /**
     * 查询用户列表
     * @return
     */
    @RequestMapping(value = "/list.action")
    @ResponseBody
    public BaseOutput<List<User>> list(UserQuery userQuery) {
        UserTicket userTicket = getUserTicket();
        userQuery.setFirmCode(userTicket.getFirmCode());
        return userRpc.listByExample(userQuery);
    }
}
