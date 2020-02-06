package com.dili.settlement.enums;

/**
 * 路径配置 url类型枚举
 */
public enum UrlTypeEnum {
    DETAIL(1, "详情"),
    PRINT_DATA(2, "打印数据");

    private int code;
    private String name;

    UrlTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static UrlTypeEnum getByCode(int code) {
        for (UrlTypeEnum temp : UrlTypeEnum.values()) {
            if (temp.getCode() == code) {
                return temp;
            }
        }
        return null;
    }

    public static String getNameByCode(int code) {
        for (UrlTypeEnum temp : UrlTypeEnum.values()) {
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
