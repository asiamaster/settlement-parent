package com.dili.settlement.enums;

/**
 * 结算配置 组编码枚举
 */
public enum GroupCodeEnum {
    ;

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
