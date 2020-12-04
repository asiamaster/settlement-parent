package com.dili.settlement.task;

import cn.hutool.core.util.StrUtil;
import com.dili.settlement.component.CallbackHolder;
import com.dili.settlement.config.CallbackConfiguration;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.CallbackDto;
import com.dili.settlement.enums.LinkTypeEnum;
import com.dili.settlement.service.SettleOrderLinkService;
import com.dili.settlement.util.DateUtil;
import com.dili.settlement.util.GeneralUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.Callable;

/**
 * 用于处理原始数据
 */
public class PrepareQueueTask extends QueueTask implements Callable<Boolean> {
    private static Logger LOGGER = LoggerFactory.getLogger(PrepareQueueTask.class);

    //加载链接
    private SettleOrderLinkService settleOrderLinkService;

    public PrepareQueueTask(CallbackConfiguration callbackConfiguration, SettleOrderLinkService settleOrderLinkService) {
        super(callbackConfiguration);
        this.settleOrderLinkService = settleOrderLinkService;
    }

    @Override
    public Boolean call() {
        while (true) {
            SettleOrder settleOrder = CallbackHolder.pollSource();
            if (settleOrder == null) {
                try {
                    Thread.sleep(callbackConfiguration.getTaskThreadSleepMills());
                } catch (InterruptedException e) {
                    LOGGER.error("prepare thread sleep", e);
                }
                continue;
            }
            try {
                SortedMap<String, String> map = getMapData(settleOrder);
                if (callbackConfiguration.getSign()) {
                    sign(map, callbackConfiguration.getSignKey());
                }
                CallbackDto callbackDto = new CallbackDto();
                callbackDto.setTimes(callbackConfiguration.getTimes());
                callbackDto.setInterval(callbackConfiguration.getIntervalMills());
                callbackDto.setUrl(settleOrderLinkService.getUrl(settleOrder.getId(), LinkTypeEnum.CALLBACK.getCode()));
                callbackDto.setData(map);
                callbackDto.setSettleOrderId(settleOrder.getId());
                CallbackHolder.offerExecute(callbackDto);
            } catch (Exception e) {
                LOGGER.error("prepare task error", e);
            }
        }
    }

    /**
     * 用于处理返回字段
     * @param settleOrder
     * @return
     */
    private SortedMap<String, String> getMapData(SettleOrder settleOrder) {
        SortedMap<String, String> map = new TreeMap<>();
        map.put("marketId", String.valueOf(settleOrder.getMarketId()));
        map.put("appId", String.valueOf(settleOrder.getAppId()));
        map.put("code", settleOrder.getCode());
        map.put("orderCode", settleOrder.getOrderCode());
        map.put("businessCode", settleOrder.getBusinessCode());
        map.put("amount", String.valueOf(settleOrder.getAmount()));
        map.put("way", String.valueOf(settleOrder.getWay()));
        map.put("state", String.valueOf(settleOrder.getState()));
        map.put("operatorId", String.valueOf(settleOrder.getOperatorId()));
        map.put("operatorName", settleOrder.getOperatorName());
        map.put("operateTime", DateUtil.formatDateTime(settleOrder.getOperateTime(), "yyyy-MM-dd HH:mm:ss"));
        map.put("accountNumber", settleOrder.getAccountNumber());
        map.put("bankName", settleOrder.getBankName());
        map.put("bankCardHolder", settleOrder.getBankCardHolder());
        map.put("serialNumber", settleOrder.getSerialNumber());
        map.put("randomStr", GeneralUtil.uuid());
        map.put("notes", settleOrder.getNotes());
        map.put("tradeCardNo", settleOrder.getTradeCardNo());
        map.put("tradeCustomerId", String.valueOf(settleOrder.getTradeCustomerId()));
        map.put("tradeCustomerName", settleOrder.getTradeCustomerName());
        return map;
    }

    //简单的数据签名
    private void sign(SortedMap<String, String> map, String signKey) {
        StringBuilder builder = new StringBuilder();
        for (String key : map.keySet()) {
            //空值不参与签名
            if (StrUtil.isBlank(map.get(key))) {
                continue;
            }
            builder.append(key).append("=").append(map.get(key)).append("&");
        }
        if (!StrUtil.isBlank(signKey)) {
            builder.append("signKey=").append(signKey);
        }
        String signResult = GeneralUtil.md5(builder.toString());
        map.put("sign", signResult);
    }
}
