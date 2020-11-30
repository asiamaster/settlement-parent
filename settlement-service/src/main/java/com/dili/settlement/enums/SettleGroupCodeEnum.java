package com.dili.settlement.enums;

/**
 * 结算配置 组编码枚举
 */
public enum SettleGroupCodeEnum {
    SETTLE_WAY_PAY(101, "支付方式"),
    SETTLE_WAY_REFUND(102, "退款方式");

    private int code;
    private String name;

    SettleGroupCodeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static SettleGroupCodeEnum getByCode(int code) {
        for (SettleGroupCodeEnum temp : SettleGroupCodeEnum.values()) {
            if (temp.getCode() == code) {
                return temp;
            }
        }
        return null;
    }

    public static String getNameByCode(int code) {
        for (SettleGroupCodeEnum temp : SettleGroupCodeEnum.values()) {
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
