package com.dili.settlement.enums;

/**
 * 链接类型
 */
public enum LinkTypeEnum {

    DETAIL(1, "详情"),
    PRINT(2, "打印"),
    CALLBACK(3, "回调");

    private int code;
    private String name;

    LinkTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static LinkTypeEnum getByCode(int code) {
        for (LinkTypeEnum temp : LinkTypeEnum.values()) {
            if (temp.getCode() == code) {
                return temp;
            }
        }
        return null;
    }

    public static String getNameByCode(int code) {
        for (LinkTypeEnum temp : LinkTypeEnum.values()) {
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
