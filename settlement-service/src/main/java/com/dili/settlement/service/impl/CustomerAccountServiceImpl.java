package com.dili.settlement.service.impl;

import com.dili.settlement.domain.CustomerAccount;
import com.dili.settlement.domain.CustomerAccountSerial;
import com.dili.settlement.mapper.CustomerAccountMapper;
import com.dili.settlement.service.CustomerAccountSerialService;
import com.dili.settlement.service.CustomerAccountService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-12-01 16:37:13.
 */
@Service
public class CustomerAccountServiceImpl extends BaseServiceImpl<CustomerAccount, Long> implements CustomerAccountService {

    @Autowired
    private CustomerAccountSerialService customerAccountSerialService;

    public CustomerAccountMapper getActualDao() {
        return (CustomerAccountMapper)getDao();
    }

    @Override
    public CustomerAccount lockGet(Long mchId, Long customerId) {
        return getActualDao().lockGet(mchId, customerId);
    }

    @Override
    public CustomerAccount getBy(Long mchId, Long customerId) {
        return getActualDao().getBy(mchId, customerId);
    }

    @Override
    public void create(CustomerAccount customerAccount) {
        try {
            if (exists(customerAccount.getMchId(), customerAccount.getCustomerId())) {
                return;
            }
            customerAccount.setAmount(0L);
            customerAccount.setFrozenAmount(0L);
            getActualDao().save(customerAccount);
        } catch (SQLIntegrityConstraintViolationException e) {
            //捕获调唯一索引冲突异常
        }
    }

    /**
     * @param mchId
     * @param customerId
     * @param amount 有正负值
     * @param accountSerialList
     */
    @Transactional
    @Override
    public void handlePay(Long mchId, Long customerId, Long amount, List<CustomerAccountSerial> accountSerialList) {
        if (customerId == null) {
            throw new BusinessException("", "客户ID为空");
        }
        CustomerAccount customerAccount = lockGet(mchId, customerId);
        if (customerAccount == null) {
            throw new BusinessException("", "客户定金账户不存在");
        }
        if (amount != 0L) {
            long availableAmount = customerAccount.getAmount() - customerAccount.getFrozenAmount();
            customerAccount.setAmount(customerAccount.getAmount() + amount);
            if (availableAmount + amount < 0) {
                throw new BusinessException("", "客户定金账户余额不足");
            }
            getActualDao().updateAmount(customerAccount);
        }
        customerAccountSerialService.batchInsert(accountSerialList, customerAccount.getId());
    }

    /**
     *
     * @param mchId
     * @param customerId
     * @param amount 负值
     * @param accountSerialList
     */
    @Transactional
    @Override
    public void handleRefund(Long mchId, Long customerId, Long amount, List<CustomerAccountSerial> accountSerialList) {
        if (customerId == null) {
            throw new BusinessException("", "客户ID为空");
        }
        CustomerAccount customerAccount = lockGet(mchId, customerId);
        if (customerAccount == null) {
            throw new BusinessException("", "客户定金账户不存在");
        }
        if (amount != 0L) {
            customerAccount.setAmount(customerAccount.getAmount() + amount);
            customerAccount.setFrozenAmount(customerAccount.getFrozenAmount() + amount);
            if (customerAccount.getFrozenAmount() < 0L) {
                throw new BusinessException("", "客户定金账户冻结金额不足");
            }
            getActualDao().updateAmount(customerAccount);
        }
        customerAccountSerialService.batchInsert(accountSerialList, customerAccount.getId());
    }

    /**
     * 验证客户定金账户是否存在
     * @param mchId
     * @param customerId
     * @return
     */
    private boolean exists(Long mchId, Long customerId) {
        CustomerAccount po = getActualDao().getBy(mchId, customerId);
        return po != null;
    }
}