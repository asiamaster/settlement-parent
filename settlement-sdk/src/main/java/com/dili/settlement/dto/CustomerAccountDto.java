package com.dili.settlement.dto;

import com.dili.settlement.domain.CustomerAccount;

/**
 * 客户账户相关dto
 */
public class CustomerAccountDto extends CustomerAccount {
    //客户名称模糊匹配
    private String customerNameMatch;

    public String getCustomerNameMatch() {
        return customerNameMatch;
    }

    public void setCustomerNameMatch(String customerNameMatch) {
        this.customerNameMatch = customerNameMatch;
    }
}
