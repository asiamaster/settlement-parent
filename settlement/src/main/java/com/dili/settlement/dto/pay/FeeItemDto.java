package com.dili.settlement.dto.pay;

/**
 * 费用项
 * @author xuliang
 */
public class FeeItemDto {

    /** 金额*/
    private Long amount;
    /** 费用类型*/
    private Integer type;
    /** 费用类型名称*/
    private String typeName;

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * 创建费用参数
     * @return
     */
    public static FeeItemDto build(Long amount, Integer type, String typeName) {
        FeeItemDto feeItem = new FeeItemDto();
        feeItem.setAmount(amount);
        feeItem.setType(type);
        feeItem.setTypeName(typeName);
        return feeItem;
    }
}
