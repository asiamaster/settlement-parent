package com.dili.settlement.enums;

/**
 * 签名类型枚举
 */
public enum SignTypeEnum {

    CALLBACK(1, "结算回调");

    private int code;
    private String name;

    SignTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static SignTypeEnum getByCode(int code) {
        for (SignTypeEnum temp : SignTypeEnum.values()) {
            if (temp.getCode() == code) {
                return temp;
            }
        }
        return null;
    }

    public static String getNameByCode(int code) {
        for (SignTypeEnum temp : SignTypeEnum.values()) {
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
