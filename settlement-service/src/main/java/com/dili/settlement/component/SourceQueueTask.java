package com.dili.settlement.component;

import cn.hutool.core.util.StrUtil;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.CallbackDto;
import com.dili.settlement.enums.AppGroupCodeEnum;
import com.dili.settlement.enums.SignTypeEnum;
import com.dili.settlement.service.ApplicationConfigService;
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
public class SourceQueueTask implements Callable<Boolean> {
    private static Logger LOGGER = LoggerFactory.getLogger(SourceQueueTask.class);
    //是否签名
    private boolean sign;
    //默认签名秘钥
    private String defaultSignKey;
    //重试次数
    private int times;
    //间隔时间
    private int interval;
    //加载配置
    private ApplicationConfigService applicationConfigService;

    public SourceQueueTask(boolean sign, String defaultSignKey, int times, int interval, ApplicationConfigService applicationConfigService) {
        this.sign = sign;
        this.defaultSignKey = defaultSignKey;
        this.times = times;
        this.interval = interval;
        this.applicationConfigService = applicationConfigService;
    }

    @Override
    public Boolean call()  {
        while (true) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                LOGGER.error("source thread sleep", e);
            }
            SettleOrder settleOrder = CallbackHolder.pollSource();
            if (settleOrder == null) {
                continue;
            }
            try {
                SortedMap<String, String> map = getMapData(settleOrder);
                if (sign) {
                    String signKey = applicationConfigService.getVal(settleOrder.getAppId(), AppGroupCodeEnum.APP_SIGN_KEY.getCode(), SignTypeEnum.CALLBACK.getCode());
                    sign(map, signKey);
                }
                CallbackDto callbackDto = new CallbackDto();
                callbackDto.setTimes(times);
                callbackDto.setInterval(interval);
                callbackDto.setUrl(settleOrder.getReturnUrl());
                callbackDto.setData(map);
                CallbackHolder.offerExecute(callbackDto);
            } catch (Exception e) {
                LOGGER.error("source task", e);
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
        if (StrUtil.isBlank(signKey)) {
            builder.append("signKey=").append(defaultSignKey);
        } else {
            builder.append("signKey=").append(signKey);
        }
        String signResult = GeneralUtil.md5(builder.toString());
        map.put("sign", signResult);
    }
}