package com.dili.settlement.enums;

/**
 * 收费项类型
 */
public enum ChargeItemTypeEnum {
    FEE(1, "费用项"),
    DEDUCT(2, "抵扣项");

    private int code;
    private String name;

    ChargeItemTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static ChargeItemTypeEnum getByCode(int code) {
        for (ChargeItemTypeEnum temp : ChargeItemTypeEnum.values()) {
            if (temp.getCode() == code) {
                return temp;
            }
        }
        return null;
    }

    public static String getNameByCode(int code) {
        for (ChargeItemTypeEnum temp : ChargeItemTypeEnum.values()) {
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
