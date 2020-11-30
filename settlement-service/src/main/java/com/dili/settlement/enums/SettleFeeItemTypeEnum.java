package com.dili.settlement.enums;

/**
 * 结算类型枚举
 */
public enum SettleFeeItemTypeEnum {

    PAY_ITEM(1, "缴费项"),
    DEDUCT_ITEM(2, "抵扣项");

    private int code;
    private String name;

    SettleFeeItemTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static SettleFeeItemTypeEnum getByCode(int code) {
        for (SettleFeeItemTypeEnum temp : SettleFeeItemTypeEnum.values()) {
            if (temp.getCode() == code) {
                return temp;
            }
        }
        return null;
    }

    public static String getNameByCode(int code) {
        for (SettleFeeItemTypeEnum temp : SettleFeeItemTypeEnum.values()) {
            if (temp.getCode() == code) {
                return temp.getName();
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
