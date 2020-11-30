package com.dili.settlement.enums;

/**
 * 结算类型枚举
 */
public enum SettleTypeEnum {

    PAY(1, "交款"),
    REFUND(2, "退款");

    private int code;
    private String name;

    SettleTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static SettleTypeEnum getByCode(int code) {
        for (SettleTypeEnum temp : SettleTypeEnum.values()) {
            if (temp.getCode() == code) {
                return temp;
            }
        }
        return null;
    }

    public static String getNameByCode(int code) {
        for (SettleTypeEnum temp : SettleTypeEnum.values()) {
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
