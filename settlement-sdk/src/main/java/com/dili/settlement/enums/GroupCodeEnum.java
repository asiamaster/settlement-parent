package com.dili.settlement.enums;

/**
 * 结算配置 组编码枚举
 */
public enum GroupCodeEnum {
    SETTLE_WAY_PAY(101, "支付方式"),
    SETTLE_WAY_REFUND(102, "退款方式"),
    SETTLE_SIGN_CALLBACK(201, "回调签名"),
    SETTLE_BUSINESS_TYPE(301, "业务类型");

    private int code;
    private String name;

    GroupCodeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static GroupCodeEnum getByCode(int code) {
        for (GroupCodeEnum temp : GroupCodeEnum.values()) {
            if (temp.getCode() == code) {
                return temp;
            }
        }
        return null;
    }

    public static String getNameByCode(int code) {
        for (GroupCodeEnum temp : GroupCodeEnum.values()) {
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
