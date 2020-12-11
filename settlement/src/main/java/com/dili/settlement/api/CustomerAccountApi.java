package com.dili.settlement.api;

import cn.hutool.core.util.StrUtil;
import com.dili.settlement.domain.CustomerAccount;
import com.dili.settlement.dto.CustomerAccountDto;
import com.dili.settlement.dto.CustomerAccountSerialDto;
import com.dili.settlement.dto.EarnestTransferDto;
import com.dili.settlement.service.CustomerAccountSerialService;
import com.dili.settlement.service.CustomerAccountService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 客户账户相关API
 */
@RestController
@RequestMapping(value = "/api/customerAccount")
public class CustomerAccountApi {

    @Autowired
    private CustomerAccountService customerAccountService;

    @Autowired
    private CustomerAccountSerialService customerAccountSerialService;

    /**
     * 查询账户列表
     * @param query
     * @return
     */
    @RequestMapping(value = "/list")
    public BaseOutput<List<CustomerAccount>> list(@RequestBody CustomerAccount query) {
        return BaseOutput.success().setData(customerAccountService.listByExample(query));
    }

    /**
     * 查询账户列表 分页
     * @param query
     * @return
     */
    @RequestMapping(value = "/listPage")
    public PageOutput<List<CustomerAccount>> listPage(@RequestBody CustomerAccountDto query) {
        return customerAccountService.listPagination(query);
    }

    /**
     * 定金转移
     * @return
     */
    @RequestMapping(value = "/transfer")
    public BaseOutput<?> transfer(@RequestBody EarnestTransferDto transferDto) {
        if (transferDto.getAccountId() == null) {
            return BaseOutput.failure("转出账户ID为空");
        }
        if (transferDto.getCustomerId() == null) {
            return BaseOutput.failure("转入客户ID为空");
        }
        if (StrUtil.isBlank(transferDto.getCustomerName())) {
            return BaseOutput.failure("转入客户姓名为空");
        }
        if (StrUtil.isBlank(transferDto.getCustomerPhone())) {
            return BaseOutput.failure("转入客户手机号为空");
        }
        if (StrUtil.isBlank(transferDto.getCustomerCertificate())) {
            return BaseOutput.failure("转入客户证件号为空");
        }
        if (transferDto.getOperatorId() == null) {
            return BaseOutput.failure("操作员ID为空");
        }
        if (StrUtil.isBlank(transferDto.getOperatorName())) {
            return BaseOutput.failure("操作员姓名为空");
        }
        if (transferDto.getAmount() == null || transferDto.getAmount() <= 0L) {
            return BaseOutput.failure("转移金额不合法");
        }
        customerAccountService.transfer(transferDto);
        return BaseOutput.success();
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @RequestMapping(value = "/getById")
    public BaseOutput<CustomerAccount> getById(Long id) {
        if (id == null) {
            return BaseOutput.failure("客户定金账户ID为空");
        }
        return BaseOutput.success().setData(customerAccountService.get(id));
    }

    /**
     * 分页查询账户流水列表
     * @return
     */
    @RequestMapping(value = "/listSerialPage")
    public PageOutput<List<CustomerAccountSerialDto>> listSerialPage(@RequestBody CustomerAccountSerialDto query) {
        return customerAccountSerialService.listPagination(query);
    }
}
