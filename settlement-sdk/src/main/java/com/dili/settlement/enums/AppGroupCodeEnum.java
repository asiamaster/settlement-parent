package com.dili.settlement.enums;

/**
 * 结算配置 组编码枚举
 */
public enum AppGroupCodeEnum {
    APP_BUSINESS_TYPE(101, "业务类型"),
    APP_BUSINESS_URL_DETAIL(102, "业务详情"),
    APP_BUSINESS_URL_PAY_PRINT(103, "缴费打印数据"),
    APP_BUSINESS_URL_REFUND_PRINT(104, "退款打印数据"),
    APP_SIGN_KEY(201, "回调签名");

    private int code;
    private String name;

    AppGroupCodeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static AppGroupCodeEnum getByCode(int code) {
        for (AppGroupCodeEnum temp : AppGroupCodeEnum.values()) {
            if (temp.getCode() == code) {
                return temp;
            }
        }
        return null;
    }

    public static String getNameByCode(int code) {
        for (AppGroupCodeEnum temp : AppGroupCodeEnum.values()) {
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
