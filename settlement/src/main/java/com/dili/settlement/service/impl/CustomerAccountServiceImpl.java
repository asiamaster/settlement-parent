package com.dili.settlement.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.dili.assets.sdk.enums.BusinessChargeItemEnum;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.settlement.domain.CustomerAccount;
import com.dili.settlement.domain.CustomerAccountSerial;
import com.dili.settlement.dto.CustomerAccountDto;
import com.dili.settlement.dto.EarnestTransferDto;
import com.dili.settlement.enums.ActionEnum;
import com.dili.settlement.enums.RelationTypeEnum;
import com.dili.settlement.enums.SceneEnum;
import com.dili.settlement.mapper.CustomerAccountMapper;
import com.dili.settlement.service.CustomerAccountSerialService;
import com.dili.settlement.service.CustomerAccountService;
import com.dili.settlement.util.DateUtil;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.exception.BusinessException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
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
    public CustomerAccount lockGetById(Long id) {
        return getActualDao().lockGetById(id);
    }

    @Transactional
    @Override
    public void create(CustomerAccount customerAccount) {
        try {
            if (exists(customerAccount.getMchId(), customerAccount.getCustomerId())) {
                return;
            }
            getActualDao().save(customerAccount);
        } catch (SQLIntegrityConstraintViolationException e) {
            //捕获唯一索引冲突异常
        }
    }

    /**
     * 冻结定金
     * @param mchId
     * @param customerId
     * @param amount 正值
     */
    @Transactional
    @Override
    public void freeze(Long mchId, Long customerId, Long amount) {
        if (amount == 0L) {
            return;
        }
        CustomerAccount customerAccount = lockGet(mchId, customerId);
        if (customerAccount == null) {
            throw new BusinessException("", "客户定金账户不存在");
        }
        long availableAmount = customerAccount.getAmount() - customerAccount.getFrozenAmount();
        if (availableAmount - amount < 0) {
            throw new BusinessException("", "客户定金账户余额不足");
        }
        customerAccount.setFrozenAmount(customerAccount.getFrozenAmount() + amount);
        getActualDao().updateAmount(customerAccount);
    }

    @Transactional
    @Override
    public void unfreeze(Long mchId, Long customerId, Long amount) {
        if (amount == 0L) {
            return;
        }
        CustomerAccount customerAccount = lockGet(mchId, customerId);
        if (customerAccount == null) {
            throw new BusinessException("", "客户定金账户不存在");
        }
        if (customerAccount.getFrozenAmount() - amount < 0) {
            throw new BusinessException("", "客户定金账户冻结金额不足");
        }
        customerAccount.setFrozenAmount(customerAccount.getFrozenAmount() - amount);
        getActualDao().updateAmount(customerAccount);
    }

    @Override
    public PageOutput<List<CustomerAccount>> listPagination(CustomerAccountDto query) {
        PageHelper.startPage(query.getPage(), query.getRows());
        List<CustomerAccount> itemList = getActualDao().list(query);

        Page<CustomerAccount> page = (Page)itemList;
        PageOutput<List<CustomerAccount>> output = PageOutput.success();
        output.setData(itemList);
        output.setPageNum(page.getPageNum());
        output.setPageSize(page.getPageSize());
        output.setTotal(page.getTotal());
        output.setStartRow(page.getStartRow());
        output.setEndRow(page.getEndRow());
        output.setPages(page.getPages());
        return output;
    }

    /**
     * 定金转移
     * @param transferDto
     */
    @Transactional
    @Override
    public void transfer(EarnestTransferDto transferDto) {
        CustomerAccount payAccount = lockGetById(transferDto.getAccountId());
        if (payAccount == null) {
            throw new BusinessException("", "转出账户不存在");
        }
        long availableAmount = payAccount.getAmount() - payAccount.getFrozenAmount();
        if (availableAmount < transferDto.getAmount()) {
            throw new BusinessException("", "转出账户余额不足");
        }
        CustomerAccount receiveAccount = lockGetAndCreate(CustomerAccount.build(payAccount.getMarketId(), payAccount.getMarketCode(), payAccount.getMchId(), payAccount.getMchName(), transferDto.getCustomerId(), transferDto.getCustomerName(), transferDto.getCustomerPhone(), transferDto.getCustomerCertificate()));

        payAccount.setAmount(payAccount.getAmount() - transferDto.getAmount());
        receiveAccount.setAmount(receiveAccount.getAmount() + transferDto.getAmount());

        LocalDateTime localDateTime = DateUtil.nowDateTime();
        getActualDao().updateAmount(payAccount);
        CustomerAccountSerial payAccountSerial = CustomerAccountSerial.build(BusinessChargeItemEnum.SystemSubjectType.定金.getCode(), ActionEnum.EXPENSE.getCode(), SceneEnum.TRANSFER_OUT.getCode(), transferDto.getAmount(), localDateTime, transferDto.getOperatorId(), transferDto.getOperatorName(), transferDto.getRelationCode(), RelationTypeEnum.TRANSFER_ORDER.getCode(), String.format("转出到：%s；转移原因：%s；", receiveAccount.getCustomerName(), StrUtil.isBlank(transferDto.getNotes()) ? "" : transferDto.getNotes()));
        payAccountSerial.setCustomerAccountId(payAccount.getId());
        customerAccountSerialService.insertSelective(payAccountSerial);
        getActualDao().updateAmount(receiveAccount);
        CustomerAccountSerial receiveAccountSerial = CustomerAccountSerial.build(BusinessChargeItemEnum.SystemSubjectType.定金.getCode(), ActionEnum.INCOME.getCode(), SceneEnum.TRANSFER_IN.getCode(), transferDto.getAmount(), localDateTime, transferDto.getOperatorId(), transferDto.getOperatorName(), transferDto.getRelationCode(), RelationTypeEnum.TRANSFER_ORDER.getCode(), String.format("来源：%s；转移原因：%s；", payAccount.getCustomerName(), StrUtil.isBlank(transferDto.getNotes()) ? "" : transferDto.getNotes()));
        receiveAccountSerial.setCustomerAccountId(receiveAccount.getId());
        customerAccountSerialService.insertSelective(receiveAccountSerial);
    }

    /**
     * 操作定金账户及流水
     * @param mchId
     * @param customerId
     * @param amount 正负值
     * @param frozenAmount 正负值
     * @param serialList
     */
    @Transactional
    @Override
    public void handleEarnest(Long mchId, Long customerId, Long amount, Long frozenAmount, List<CustomerAccountSerial> serialList) {
        CustomerAccount customerAccount = lockGet(mchId, customerId);
        if (customerAccount == null) {
            throw new BusinessException("", "客户定金账户不存在");
        }
        long totalFrozenAmount = customerAccount.getFrozenAmount() + frozenAmount;
        if (totalFrozenAmount < 0) {
            throw new BusinessException("", "客户定金账户冻结金额不足");
        }
        customerAccount.setFrozenAmount(totalFrozenAmount);
        long totalAmount = (customerAccount.getAmount() - customerAccount.getFrozenAmount()) + amount;
        if (totalAmount < 0) {
            throw new BusinessException("", "客户定金账户余额不足");
        }
        customerAccount.setAmount(totalAmount);
        getActualDao().updateAmount(customerAccount);
        customerAccountSerialService.batchInsert(serialList, customerAccount.getId());
    }

    /**
     * 冻结转抵金额
     * @param mchId
     * @param customerId
     * @param amount 正值
     */
    @Transactional
    @Override
    public void freezeTransfer(Long mchId, Long customerId, Long amount) {
        if (amount == 0L) {
            return;
        }
        CustomerAccount customerAccount = lockGet(mchId, customerId);
        if (customerAccount == null) {
            throw new BusinessException("", "客户转抵账户不存在");
        }
        long availableAmount = customerAccount.getTransferAmount() - customerAccount.getFrozenTransferAmount();
        if (availableAmount - amount < 0) {
            throw new BusinessException("", "客户转抵账户余额不足");
        }
        customerAccount.setFrozenTransferAmount(customerAccount.getFrozenTransferAmount() + amount);
        getActualDao().updateTransferAmount(customerAccount);
    }

    /**
     * 操作转抵账户及流水
     * @param mchId
     * @param customerId
     * @param amount 正负值
     * @param frozenAmount 正负值
     * @param serialList
     */
    @Transactional
    @Override
    public void handleTransfer(Long mchId, Long customerId, Long amount, Long frozenAmount, List<CustomerAccountSerial> serialList) {
        CustomerAccount customerAccount = lockGet(mchId, customerId);
        if (customerAccount == null) {
            throw new BusinessException("", "客户转抵账户不存在");
        }
        long totalFrozenAmount = customerAccount.getFrozenTransferAmount() + frozenAmount;
        if (totalFrozenAmount < 0) {
            throw new BusinessException("", "客户转抵账户冻结金额不足");
        }
        customerAccount.setFrozenTransferAmount(totalFrozenAmount);
        long totalAmount = (customerAccount.getTransferAmount() - customerAccount.getFrozenTransferAmount()) + amount;
        if (totalAmount < 0) {
            throw new BusinessException("", "客户转抵账户余额不足");
        }
        customerAccount.setTransferAmount(totalAmount);
        getActualDao().updateTransferAmount(customerAccount);
        customerAccountSerialService.batchInsert(serialList, customerAccount.getId());
    }

    /**
     * 解冻转抵金额
     * @param mchId
     * @param customerId
     * @param amount 正值
     */
    @Transactional
    @Override
    public void unfreezeTransfer(Long mchId, Long customerId, long amount) {
        if (amount == 0L) {
            return;
        }
        CustomerAccount customerAccount = lockGet(mchId, customerId);
        if (customerAccount == null) {
            throw new BusinessException("", "客户转抵账户不存在");
        }
        if (customerAccount.getFrozenTransferAmount() - amount < 0) {
            throw new BusinessException("", "客户转抵账户冻结金额不足");
        }
        customerAccount.setFrozenTransferAmount(customerAccount.getFrozenTransferAmount() - amount);
        getActualDao().updateTransferAmount(customerAccount);
    }

    @Transactional
    @Override
    public void updateCustomerInfo(CustomerExtendDto customer) {
        getActualDao().updateCustomerInfo(customer);
    }

    /**
     * 加锁获取 如为空则创建
     * @param account
     * @return
     */
    private CustomerAccount lockGetAndCreate(CustomerAccount account) {
        CustomerAccount po = getActualDao().lockGet(account.getMchId(), account.getCustomerId());
        if (po != null) {
            return po;
        }
        try {
            getActualDao().save(account);
        } catch (SQLIntegrityConstraintViolationException e) {
            lockGetAndCreate(account);
        }
        return lockGetAndCreate(account);
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