package com.dili.settlement.mapper;

import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.settlement.domain.CustomerAccount;
import com.dili.settlement.dto.CustomerAccountDto;
import com.dili.ss.base.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

public interface CustomerAccountMapper extends MyMapper<CustomerAccount> {
    /**
     * 查询并锁定
     * @param mchId
     * @param customerId
     * @return
     */
    CustomerAccount lockGet(@Param("mchId") Long mchId, @Param("customerId") Long customerId);

    /**
     * 查询
     * @param mchId
     * @param customerId
     * @return
     */
    CustomerAccount getBy(@Param("mchId") Long mchId, @Param("customerId") Long customerId);

    /**
     * 保存
     * @param customerAccount
     * @return
     */
    int save(CustomerAccount customerAccount) throws SQLIntegrityConstraintViolationException;

    /**
     * 修改金额
     * @param customerAccount
     * @return
     */
    int updateAmount(CustomerAccount customerAccount);

    /**
     * 查询并锁定
     * @param id
     * @return
     */
    CustomerAccount lockGetById(@Param("id") Long id);

    /**
     * 分页查询账户信息
     * @param query
     * @return
     */
    List<CustomerAccount> list(CustomerAccountDto query);

    /**
     * 根据客户id修改定金账户客户信息
     * @param customer
     * @return
     */
    int updateCustomerInfo(CustomerExtendDto customer);
}