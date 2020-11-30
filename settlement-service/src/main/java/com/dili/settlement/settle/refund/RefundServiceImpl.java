package com.dili.settlement.settle.refund;

import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.service.FundAccountService;
import com.dili.settlement.service.RetryRecordService;
import com.dili.settlement.service.SettleOrderService;
import com.dili.settlement.settle.RefundService;
import com.dili.settlement.settle.impl.SettleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;

/**
 * 退款数据验证基础实现类
 */
public abstract class RefundServiceImpl extends SettleServiceImpl implements RefundService {

    @Autowired
    protected SettleOrderService settleOrderService;
    @Autowired
    protected RetryRecordService retryRecordService;
    @Autowired
    protected FundAccountService fundAccountService;

    @Override
    public String forwardSpecial(SettleOrderDto settleOrderDto, ModelMap modelMap) {
        return "refund/special";
    }

    @Override
    public void settleParamSpecial(SettleOrder po, SettleOrderDto settleOrderDto) {
        return;
    }

    @Override
    public void settleAfter(SettleOrder po, SettleOrderDto settleOrderDto) {
        fundAccountService.sub(po.getMarketId(), po.getAppId(), po.getAmount());
    }
}
