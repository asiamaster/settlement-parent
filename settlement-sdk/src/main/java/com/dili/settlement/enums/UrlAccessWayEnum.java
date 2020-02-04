package com.dili.settlement.enums;

/**
 * 路径配置 url访问方式枚举
 */
public enum UrlAccessWayEnum {
    RPC(1, "RPC"),
    HTTP(2, "HTTP");

    private int code;
    private String name;

    UrlAccessWayEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static UrlAccessWayEnum getByCode(int code) {
        for (UrlAccessWayEnum temp : UrlAccessWayEnum.values()) {
            if (temp.getCode() == code) {
                return temp;
            }
        }
        return null;
    }

    public static String getNameByCode(int code) {
        for (UrlAccessWayEnum temp : UrlAccessWayEnum.values()) {
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
