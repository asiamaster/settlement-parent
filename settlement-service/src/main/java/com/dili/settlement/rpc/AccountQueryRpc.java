package com.dili.settlement.rpc;

import com.dili.settlement.dto.AccountSimpleResponseDto;
import com.dili.settlement.dto.UserAccountCardResponseDto;
import com.dili.settlement.dto.UserAccountSingleQueryDto;
import com.dili.ss.domain.BaseOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 卡相关rpc
 */
@FeignClient(name = "account-service", contextId = "accountQueryService",
		path = "api/account" /*, url = "http://127.0.0.1:8186" */)
public interface AccountQueryRpc {

    /**
     * 查询单个
     * @author miaoguoxin
     * @date 2020/7/28
     */
    @PostMapping(value = "/getSingle")
    BaseOutput<UserAccountCardResponseDto> findSingle(UserAccountSingleQueryDto cardQuery);

    /**
     * 账户信息，包含余额
     * @author miaoguoxin
     * @date 2020/7/7
     */
    @GetMapping("/simpleInfo")
    BaseOutput<AccountSimpleResponseDto> getInfoByCardNo(@RequestParam String cardNo, @RequestParam Long firmId);
}
