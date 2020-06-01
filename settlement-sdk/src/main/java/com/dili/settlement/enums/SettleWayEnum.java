package com.dili.settlement.enums;

/**
 * 结算方式枚举
 */
public enum SettleWayEnum {
    CASH(1, "现金"),
    POS(2, "POS"),
    BANK(3, "银行卡转账"),
    ALI_PAY(4, "支付宝"),
    WECHAT_PAY(5, "微信"),
    MIXED_PAY(6, "组合支付"),
    VIRTUAL_PAY(7, "虚拟支付");

    private int code;
    private String name;

    SettleWayEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static SettleWayEnum getByCode(int code) {
        for (SettleWayEnum temp : SettleWayEnum.values()) {
            if (temp.getCode() == code) {
                return temp;
            }
        }
        return null;
    }

    public static String getNameByCode(int code) {
        for (SettleWayEnum temp : SettleWayEnum.values()) {
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
