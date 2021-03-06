package com.dili.settlement.dto.pay;

/**
 * 费用项
 * @author xuliang
 */
public class FeeItemDto {

    /** 金额*/
    private Long amount;
    /** 费用类型*/
    private Long type;
    /** 费用类型名称*/
    private String typeName;
    /** 期初余额*/
    private Long balance;
    /** 描述格式 业务类型,流水备注*/
    private String description;

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 创建费用参数
     * @return
     */
    public static FeeItemDto build(Long amount, Long type, String typeName, String description) {
        FeeItemDto feeItem = new FeeItemDto();
        feeItem.setAmount(amount);
        feeItem.setType(type);
        feeItem.setTypeName(typeName);
        feeItem.setDescription(description);
        return feeItem;
    }
}
