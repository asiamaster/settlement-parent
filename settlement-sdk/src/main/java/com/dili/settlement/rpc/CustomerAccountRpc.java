package com.dili.settlement.rpc;

import com.dili.settlement.domain.CustomerAccount;
import com.dili.settlement.dto.CustomerAccountDto;
import com.dili.settlement.dto.CustomerAccountSerialDto;
import com.dili.settlement.dto.EarnestTransferDto;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 结算 客户定金账户相关api
 */
@FeignClient(name = "settlement", contextId = "customerAccountRpc", url="${SettleRpc.url:}")
public interface CustomerAccountRpc {

    /**
     * 查询账户列表
     * @param query
     * @return
     */
    @RequestMapping(value = "/list")
    BaseOutput<List<CustomerAccount>> list(CustomerAccount query);

    /**
     * 查询账户列表 分页
     * @param query
     * @return
     */
    @RequestMapping(value = "/listPage")
    PageOutput<List<CustomerAccount>> listPage(CustomerAccountDto query);

    /**
     * 定金转移
     * @return
     */
    @RequestMapping(value = "/transfer")
    BaseOutput<?> transfer(EarnestTransferDto transferDto);

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @RequestMapping(value = "/getById")
    BaseOutput<CustomerAccount> getById(@RequestParam Long id);

    /**
     * 分页查询账户流水列表
     * @return
     */
    @RequestMapping(value = "/listSerialPage")
    PageOutput<List<CustomerAccountSerialDto>> listSerialPage(CustomerAccountSerialDto query);
}
