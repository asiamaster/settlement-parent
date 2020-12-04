package com.dili.settlement.mapper;

import com.dili.settlement.domain.CustomerAccount;
import com.dili.ss.base.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.sql.SQLIntegrityConstraintViolationException;

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
}