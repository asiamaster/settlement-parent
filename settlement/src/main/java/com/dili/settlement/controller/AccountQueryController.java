package com.dili.settlement.controller;

import cn.hutool.core.util.StrUtil;
import com.dili.settlement.component.MchIdHolder;
import com.dili.settlement.dto.AccountSimpleResponseDto;
import com.dili.settlement.dto.UserAccountCardQuery;
import com.dili.settlement.dto.UserAccountCardResponseDto;
import com.dili.settlement.dto.UserAccountSingleQueryDto;
import com.dili.settlement.dto.pay.PasswordRequestDto;
import com.dili.settlement.rpc.AccountQueryRpc;
import com.dili.settlement.rpc.PayRpc;
import com.dili.ss.domain.BaseOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 账户查询
 */
@Controller
@RequestMapping(value = "/accountQuery")
public class AccountQueryController extends AbstractController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountQueryController.class);

    @Autowired
    private AccountQueryRpc accountQueryRpc;
    @Autowired
    private PayRpc payRpc;
    /**
     * 查询账户信息
     * @param cardQuery
     * @return
     */
    @RequestMapping(value = "/findSingle.action")
    @ResponseBody
    public BaseOutput<UserAccountCardResponseDto> findSingle(UserAccountSingleQueryDto cardQuery) {
        cardQuery.setFirmId(getUserTicket().getFirmId());
        return accountQueryRpc.findSingle(cardQuery);
    }

    /**
     * 账户信息，包含余额
     * @param cardNo
     * @return
     */
    @RequestMapping(value = "/simpleInfo.action")
    @ResponseBody
    public BaseOutput<AccountSimpleResponseDto> getInfoByCardNo(String cardNo) {
        if (StrUtil.isBlank(cardNo)) {
            return BaseOutput.failure("园区卡号为空");
        }
        return accountQueryRpc.getInfoByCardNo(cardNo, getUserTicket().getFirmId());
    }

    /**
     * 校验验证密码
     * @param passwordRequest
     * @return
     */
    @RequestMapping(value = "/validatePayPassword.action")
    @ResponseBody
    public BaseOutput<?> validatePayPassword(PasswordRequestDto passwordRequest) {
        if (passwordRequest.getAccountId() == null || StrUtil.isBlank(passwordRequest.getPassword())) {
            return BaseOutput.failure("校验支付密码接口参数错误");
        }
        MchIdHolder.set(getUserTicket().getFirmId());
        BaseOutput baseOutput = payRpc.validateTradePassword(passwordRequest);
        MchIdHolder.clear();
        return baseOutput;
    }

    /**
     * 查询卡账户列表
     * @param customerId
     * @return
     */
    @RequestMapping(value = "/getList.action")
    @ResponseBody
    public BaseOutput<List<UserAccountCardResponseDto>> getList(Long customerId) {
        if (customerId == null) {
            return BaseOutput.success().setData(new ArrayList<>());
        }
        UserAccountCardQuery query = new UserAccountCardQuery();
        List<Long> customerIdList = new ArrayList<>(1);
        customerIdList.add(customerId);
        query.setCustomerIds(customerIdList);
        query.setFirmId(getUserTicket().getFirmId());
        return accountQueryRpc.getList(query);
    }
}
