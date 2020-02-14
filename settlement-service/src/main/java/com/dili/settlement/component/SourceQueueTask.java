package com.dili.settlement.component;

import cn.hutool.core.util.StrUtil;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.CallbackDto;
import com.dili.settlement.enums.GroupCodeEnum;
import com.dili.settlement.service.SettleConfigService;
import com.dili.settlement.util.DateUtil;
import com.dili.settlement.util.GeneralUtil;

import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.Callable;

/**
 * 用于处理原始数据
 */
public class SourceQueueTask implements Callable<Boolean> {

    //是否签名
    private boolean sign;
    //默认签名秘钥
    private String defaultSignKey;
    //重试次数
    private int times;
    //间隔时间
    private int interval;
    //加载配置
    private SettleConfigService settleConfigService;

    public SourceQueueTask(boolean sign, String defaultSignKey, int times, int interval, SettleConfigService settleConfigService) {
        this.sign = sign;
        this.defaultSignKey = defaultSignKey;
        this.times = times;
        this.interval = interval;
        this.settleConfigService = settleConfigService;
    }

    @Override
    public Boolean call()  {
        while (true) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SettleOrder settleOrder = CallbackHolder.pollSource();
            if (settleOrder == null) {
                continue;
            }
            SortedMap<String, String> map = getMapData(settleOrder);
            if (sign) {
                String signKey = settleConfigService.getSignKey(settleOrder.getMarketId(), GroupCodeEnum.SETTLE_SIGN_CALLBACK.getCode());
                sign(map, signKey);
            }
            CallbackDto callbackDto = new CallbackDto();
            callbackDto.setTimes(times);
            callbackDto.setInterval(interval);
            callbackDto.setUrl(settleOrder.getReturnUrl());
            callbackDto.setData(map);
            CallbackHolder.offerExecute(callbackDto);
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
