package com.dili.settlement.api;

import com.dili.settlement.domain.CustomerAccount;
import com.dili.settlement.dto.CustomerAccountDto;
import com.dili.settlement.dto.CustomerAccountSerialDto;
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

    /**
     * 冻结
     * @return
     */
    @RequestMapping(value = "/freeze")
    public BaseOutput<?> freeze(@RequestBody CustomerAccountDto customerAccountDto) {
        if (customerAccountDto.getId() == null) {
            return BaseOutput.failure("客户定金账户ID为空");
        }
        if (customerAccountDto.getAmount() == null || customerAccountDto.getAmount() < 0L) {
            return BaseOutput.failure("冻结金额不合法");
        }
        customerAccountService.freeze(customerAccountDto.getId(), customerAccountDto.getAmount());
        return BaseOutput.success();
    }

    /**
     * 解冻
     * @return
     */
    @RequestMapping(value = "/unfreeze")
    public BaseOutput<?> unfreeze(@RequestBody CustomerAccountDto customerAccountDto) {
        if (customerAccountDto.getId() == null) {
            return BaseOutput.failure("客户定金账户ID为空");
        }
        if (customerAccountDto.getAmount() == null || customerAccountDto.getAmount() < 0L) {
            return BaseOutput.failure("冻结金额不合法");
        }
        customerAccountService.unfreeze(customerAccountDto.getId(), customerAccountDto.getAmount());
        return BaseOutput.success();
    }
}
