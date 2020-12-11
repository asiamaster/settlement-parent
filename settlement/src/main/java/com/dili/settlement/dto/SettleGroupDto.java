package com.dili.settlement.dto;

import com.dili.settlement.domain.SettleOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * 结算分组dto
 */
public class SettleGroupDto {

    //商户ID
    private Long mchId;
    //商户名称
    private String mchName;
    //结算单列表
    private List<SettleOrder> settleOrderList = new ArrayList<>();

    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }

    public String getMchName() {
        return mchName;
    }

    public void setMchName(String mchName) {
        this.mchName = mchName;
    }

    public List<SettleOrder> getSettleOrderList() {
        return settleOrderList;
    }

    public void setSettleOrderList(List<SettleOrder> settleOrderList) {
        this.settleOrderList = settleOrderList;
    }

    public void append(SettleOrder settleOrder) {
        this.settleOrderList.add(settleOrder);
    }

    /**
     * 构建分组对象
     * @param mchId
     * @param mchName
     * @param settleOrder
     * @return
     */
    public static SettleGroupDto build(Long mchId, String mchName, SettleOrder settleOrder) {
        SettleGroupDto settleGroupDto = new SettleGroupDto();
        settleGroupDto.setMchId(mchId);
        settleGroupDto.setMchName(mchName);
        settleGroupDto.append(settleOrder);
        return settleGroupDto;
    }
}
