package com.dili.settlement.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dili.settlement.config.CallbackConfiguration;
import com.dili.settlement.dispatcher.OrderDispatcher;
import com.dili.settlement.dispatcher.PayDispatcher;
import com.dili.settlement.dispatcher.RefundDispatcher;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.ChargeDateDto;
import com.dili.settlement.dto.InvalidRequestDto;
import com.dili.settlement.dto.SettleAmountDto;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.LinkTypeEnum;
import com.dili.settlement.enums.ReverseEnum;
import com.dili.settlement.enums.SettleStateEnum;
import com.dili.settlement.enums.SettleTypeEnum;
import com.dili.settlement.mapper.SettleOrderMapper;
import com.dili.settlement.service.RetryRecordService;
import com.dili.settlement.service.SettleOrderLinkService;
import com.dili.settlement.service.SettleOrderService;
import com.dili.settlement.util.DateUtil;
import com.dili.settlement.util.GeneralUtil;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.exception.BusinessException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-05 16:39:11.
 */
@Service
public class SettleOrderServiceImpl extends BaseServiceImpl<SettleOrder, Long> implements SettleOrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SettleOrderServiceImpl.class);

    @Autowired
    private PayDispatcher payDispatcher;

    @Autowired
    private RefundDispatcher refundDispatcher;

    @Autowired
    private OrderDispatcher orderDispatcher;

    @Autowired
    private RetryRecordService retryRecordService;

    @Autowired
    private SettleOrderLinkService settleOrderLinkService;

    @Autowired
    private CallbackConfiguration callbackConfiguration;

    public SettleOrderMapper getActualDao() {
        return (SettleOrderMapper)getDao();
    }

    @Override
    public boolean existsOrderCode(Long appId, String orderCode) {
        SettleOrderDto query = new SettleOrderDto();
        query.setAppId(appId);
        query.setOrderCode(orderCode);
        List<SettleOrder> itemList = getActualDao().list(query);
        return !CollUtil.isEmpty(itemList);
    }

    @Override
    public List<SettleOrder> list(SettleOrderDto query) {
        return getActualDao().list(query);
    }

    @Override
    public PageOutput<List<SettleOrder>> listPagination(SettleOrderDto query) {
        PageHelper.startPage(query.getPage(), query.getRows());
        List<SettleOrder> itemList = getActualDao().list(query);

        Page<SettleOrder> page = (Page)itemList;
        PageOutput<List<SettleOrder>> output = PageOutput.success();
        output.setData(itemList);
        output.setPageNum(page.getPageNum());
        output.setPageSize(page.getPageSize());
        output.setTotal(page.getTotal());
        output.setStartRow(page.getStartRow());
        output.setEndRow(page.getEndRow());
        output.setPages(page.getPages());
        return output;
    }

    @Override
    public SettleAmountDto queryAmount(SettleOrderDto settleOrderDto) {
        return getActualDao().queryAmount(settleOrderDto);
    }

    @Transactional
    @Override
    public int updateSettle(SettleOrder po) {
        return getActualDao().settleUpdate(po);
    }

    @Override
    public SettleOrder getByCode(String code) {
        return getActualDao().getByCode(code);
    }

    @Override
    public Long convertReverseOrderId(Long id) {
        SettleOrder reverseOrder = get(id);
        if (reverseOrder == null) {
            throw new BusinessException("", "冲正单记录不存在");
        }
        SettleOrder originOrder = getByCode(reverseOrder.getOrderCode());
        if (originOrder == null) {
            throw new BusinessException("", "结算单记录不存在");
        }
        return originOrder.getId();
    }

    @Override
    public List<SettleOrder> lockList(List<Long> ids) {
        return getActualDao().lockList(ids);
    }

    @Transactional
    @Override
    public int batchSettleUpdate(List<SettleOrder> settleOrderList, String tradeNo) {
        if (CollUtil.isEmpty(settleOrderList)) {
            return 0;
        }
        return getActualDao().batchSettleUpdate(settleOrderList, tradeNo);
    }

    @Override
    public SettleOrder get(Long appId, String orderCode) {
        SettleOrderDto query = new SettleOrderDto();
        query.setAppId(appId);
        query.setOrderCode(orderCode);
        return listByExample(query).stream().findFirst().orElse(null);
    }

    @Transactional
    @Override
    public void cancelById(Long id) {
        doCancel(get(id));
    }

    /**
     * 执行取消
     * @param po
     */
    private void doCancel(SettleOrder po) {
        if (po == null) {
            throw new BusinessException("", "未查询到结算单记录");
        }
        orderDispatcher.cancel(po);
    }

    @Transactional
    @Override
    public void cancelByCode(String code) {
        doCancel(getActualDao().getByCode(code));
    }

    @Transactional
    @Override
    public void cancel(Long appId, String orderCode) {
        doCancel(get(appId, orderCode));
    }

    /**
     * 目前根据需求，只针对交款单作废
     * @param param
     */
    @GlobalTransactional
    @Transactional
    @Override
    public void invalid(InvalidRequestDto param) {
        for (String orderCode : param.getOrderCodeList()) {
            SettleOrder po = get(param.getAppId(), orderCode);
            if (po == null) {//未查询到
                continue;
            }
            if (!Integer.valueOf(ReverseEnum.NO.getCode()).equals(po.getReverse())) {//冲正单不允许作废
                continue;
            }
            if (Integer.valueOf(SettleStateEnum.WAIT_DEAL.getCode()).equals(po.getState())) {//待处理
                doCancel(po);
            } else {
                if (Integer.valueOf(SettleTypeEnum.PAY.getCode()).equals(po.getType())) {//支付单
                    payDispatcher.invalid(po, param);
                } else {//退款单 逻辑未实现
                    refundDispatcher.invalid(po, param);
                }
            }
        }
    }

    @Override
    public int deleteById(Long id, Integer version) {
        return getActualDao().deleteById(id, version);
    }

    @Transactional
    @Override
    public int updateChargeDate(ChargeDateDto chargeDateDto) {
        return getActualDao().updateChargeDate(chargeDateDto);
    }

    @Override
    public boolean callback(SettleOrder settleOrder) {
        try {
            SortedMap<String, String> map = getMapData(settleOrder);
            if (callbackConfiguration.getSign()) {
                sign(map, callbackConfiguration.getSignKey());
            }
            String url = settleOrderLinkService.getUrl(settleOrder.getId(), LinkTypeEnum.CALLBACK.getCode());
            doCallback(url, map);
            retryRecordService.delete(settleOrder.getId());
            return true;
        } catch (Exception e) {
            LOGGER.error("Settle callback error", e);
        }
        return false;
    }

    /**
     * 发起http回调
     * @param url
     * @param map
     */
    private void doCallback(String url, Map<String, String> map) {
        HttpRequest request = HttpUtil.createPost(url);
        request.header("Content-Type", "application/json;charset=UTF-8");
        String responseBody = request.body(JSON.toJSONString(map)).execute().body();
        if (StrUtil.isBlank(responseBody)) {
            throw new BusinessException("", "回调返回内容为空");
        }
        BaseOutput<Boolean> baseOutput = JSON.parseObject(responseBody, new TypeReference<BaseOutput<Boolean>>(){}.getType());
        if (!baseOutput.isSuccess()) {
            throw new BusinessException("", baseOutput.getMessage());
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
        map.put("mchId", String.valueOf(settleOrder.getMchId()));
        map.put("appId", String.valueOf(settleOrder.getAppId()));
        map.put("code", settleOrder.getCode());
        map.put("orderCode", settleOrder.getOrderCode());
        map.put("businessCode", settleOrder.getBusinessCode());
        map.put("amount", String.valueOf(settleOrder.getAmount()));
        map.put("deductAmount", String.valueOf(settleOrder.getDeductAmount()));
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

    /**
     * 简单的数据签名
     * @param map
     * @param signKey
     */
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