package com.dili.settlement.settle.refund;

import cn.hutool.core.util.StrUtil;
import com.dili.settlement.component.FirmIdHolder;
import com.dili.settlement.domain.RetryRecord;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.dto.UserAccountCardResponseDto;
import com.dili.settlement.dto.UserAccountSingleQueryDto;
import com.dili.settlement.enums.RetryTypeEnum;
import com.dili.settlement.enums.SettleWayEnum;
import com.dili.settlement.rpc.AccountQueryRpc;
import com.dili.settlement.rpc.resolver.GenericRpcResolver;
import com.dili.settlement.settle.RefundService;
import com.dili.ss.exception.BusinessException;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 园区卡
 */
@Service
public class CardRefundServiceImpl extends RefundServiceImpl implements RefundService {

    @Autowired
    private AccountQueryRpc accountQueryRpc;

    @Override
    public void validSubmitParams(SettleOrderDto settleOrderDto) {
        if (StrUtil.isBlank(settleOrderDto.getTradeCardNo())) {
            throw new BusinessException("", "交易卡号为空");
        }
        //调用接口验证园区卡是否可用
        UserAccountCardResponseDto responseDto = checkCardInfo(settleOrderDto.getTradeCardNo());
        settleOrderDto.setTradeFundAccountId(responseDto.getFundAccountId());
        settleOrderDto.setTradeAccountId(responseDto.getAccountId());
        settleOrderDto.setTradeCustomerId(responseDto.getCustomerId());
        settleOrderDto.setTradeCustomerName(responseDto.getCustomerName());
    }

    private UserAccountCardResponseDto checkCardInfo(String tradeCardNo) {
        UserAccountSingleQueryDto cardQuery = new UserAccountSingleQueryDto();
        cardQuery.setCardNo(tradeCardNo);
        return GenericRpcResolver.resolver(accountQueryRpc.findSingle(cardQuery), "account-service");
    }

    @Override
    public void validParamsSpecial(SettleOrderDto settleOrderDto) {
        if (StrUtil.isBlank(settleOrderDto.getTradeCardNo())) {
            throw new BusinessException("", "交易卡号为空");
        }
        if (settleOrderDto.getTradeFundAccountId() == null) {
            throw new BusinessException("", "交易资金账户ID为空");
        }
        if (settleOrderDto.getTradeAccountId() == null) {
            throw new BusinessException("", "交易账户ID为空");
        }
        if (settleOrderDto.getTradeCustomerId() == null) {
            throw new BusinessException("", "交易客户ID为空");
        }
        if (StrUtil.isBlank(settleOrderDto.getTradeCustomerName())) {
            throw new BusinessException("", "交易客户姓名为空");
        }
        //调用接口验证园区卡是否可用
        checkCardInfo(settleOrderDto.getTradeCardNo());
    }

    @Override
    public void settleParamSpecial(SettleOrder po, SettleOrderDto settleOrderDto) {
        po.setTradeCardNo(settleOrderDto.getTradeCardNo());
        po.setTradeAccountId(settleOrderDto.getTradeAccountId());
        po.setTradeFundAccountId(settleOrderDto.getTradeFundAccountId());
        po.setTradeCustomerId(settleOrderDto.getTradeCustomerId());
        po.setTradeCustomerName(settleOrderDto.getTradeCustomerName());
        po.setSerialNumber(settleOrderDto.getTradeCardNo() + "(" + settleOrderDto.getTradeCustomerName() + ")");
    }

    @GlobalTransactional
    @Transactional
    @Override
    public void settle(SettleOrder po, SettleOrderDto settleOrderDto) {
        settleBefore(po, settleOrderDto);
        FirmIdHolder.set(po.getMarketId());
        //TODO 创建交易
        int i = settleOrderService.updateSettle(po);
        if (i != 1) {
            throw new BusinessException("", "数据已变更,请稍后重试");
        }
        //存入回调重试记录  方便定时任务扫描
        RetryRecord retryRecord = new RetryRecord(RetryTypeEnum.SETTLE_CALLBACK.getCode(), po.getId(), po.getCode());
        retryRecordService.insertSelective(retryRecord);
        po.setRetryRecordId(retryRecord.getId());
        //TODO 提交交易

        settleAfter(po, settleOrderDto);
        FirmIdHolder.clear();
    }

    @Override
    public Integer supportWay() {
        return SettleWayEnum.CARD.getCode();
    }
}
