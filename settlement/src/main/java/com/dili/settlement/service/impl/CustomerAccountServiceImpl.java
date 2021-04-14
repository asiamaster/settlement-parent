package com.dili.settlement.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
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

    @Override
    public CustomerAccount getBy(Long mchId, Long customerId) {
        return getActualDao().getBy(mchId, customerId);
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
    public void handle(Long mchId, Long customerId, Long amount, List<CustomerAccountSerial> accountSerialList) {
        if ((amount == 0L && CollUtil.isEmpty(accountSerialList)) || customerId == null) {
            return;
        }
        CustomerAccount customerAccount = lockGet(mchId, customerId);
        if (customerAccount == null) {
            throw new BusinessException("", "客户定金账户不存在");
        }
        if (amount != 0L) {
            long availableAmount = customerAccount.getAmount() - customerAccount.getFrozenAmount();
            if (availableAmount + amount < 0) {
                throw new BusinessException("", "客户定金账户余额不足");
            }
            customerAccount.setAmount(customerAccount.getAmount() + amount);
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
        if ((amount == 0L && CollUtil.isEmpty(accountSerialList)) || customerId == null) {
            return;
        }
        CustomerAccount customerAccount = lockGet(mchId, customerId);
        if (customerAccount == null) {
            throw new BusinessException("", "客户定金账户不存在");
        }
        if (amount != 0L) {
            if (customerAccount.getFrozenAmount() + amount < 0L) {
                throw new BusinessException("", "客户定金账户冻结金额不足");
            }
            customerAccount.setAmount(customerAccount.getAmount() + amount);
            customerAccount.setFrozenAmount(customerAccount.getFrozenAmount() + amount);
            getActualDao().updateAmount(customerAccount);
        }
        customerAccountSerialService.batchInsert(accountSerialList, customerAccount.getId());
    }

    /**
     *
     * @param id
     * @param amount 正值
     */
    @Transactional
    @Override
    public void freeze(Long id, Long amount) {
        if (amount == 0L) {
            return;
        }
        CustomerAccount customerAccount = lockGetById(id);
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

    /**
     *
     * @param id
     * @param amount 正值
     */
    @Transactional
    @Override
    public void unfreeze(Long id, Long amount) {
        if (amount == 0L) {
            return;
        }
        CustomerAccount customerAccount = lockGetById(id);
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
        CustomerAccountSerial payAccountSerial = CustomerAccountSerial.build(ActionEnum.EXPENSE.getCode(), SceneEnum.TRANSFER_OUT.getCode(), transferDto.getAmount(), localDateTime, transferDto.getOperatorId(), transferDto.getOperatorName(), transferDto.getRelationCode(), RelationTypeEnum.TRANSFER_ORDER.getCode(), String.format("转出到：%s；转移原因：%s；", receiveAccount.getCustomerName(), StrUtil.isBlank(transferDto.getNotes()) ? "" : transferDto.getNotes()));
        payAccountSerial.setCustomerAccountId(payAccount.getId());
        customerAccountSerialService.insertSelective(payAccountSerial);
        getActualDao().updateAmount(receiveAccount);
        CustomerAccountSerial receiveAccountSerial = CustomerAccountSerial.build(ActionEnum.INCOME.getCode(), SceneEnum.TRANSFER_IN.getCode(), transferDto.getAmount(), localDateTime, transferDto.getOperatorId(), transferDto.getOperatorName(), transferDto.getRelationCode(), RelationTypeEnum.TRANSFER_ORDER.getCode(), String.format("来源：%s；转移原因：%s；", payAccount.getCustomerName(), StrUtil.isBlank(transferDto.getNotes()) ? "" : transferDto.getNotes()));
        receiveAccountSerial.setCustomerAccountId(receiveAccount.getId());
        customerAccountSerialService.insertSelective(receiveAccountSerial);
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