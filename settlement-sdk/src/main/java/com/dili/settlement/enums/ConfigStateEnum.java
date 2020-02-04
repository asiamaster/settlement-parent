package com.dili.settlement.enums;

/**
 * 结算配置 状态枚举
 */
public enum ConfigStateEnum {
    ;
    private int code;
    private String name;

    ConfigStateEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static ConfigStateEnum getByCode(int code) {
        for (ConfigStateEnum temp : ConfigStateEnum.values()) {
            if (temp.getCode() == code) {
                return temp;
            }
        }
        return null;
    }

    public static String getNameByCode(int code) {
        for (ConfigStateEnum temp : ConfigStateEnum.values()) {
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
