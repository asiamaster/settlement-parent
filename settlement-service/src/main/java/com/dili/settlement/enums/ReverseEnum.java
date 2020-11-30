package com.dili.settlement.enums;

/**
 * 是否冲正标记
 */
public enum ReverseEnum {
    YES(1, "是"),
    NO(0, "否");

    private int code;
    private String name;

    ReverseEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static ReverseEnum getByCode(int code) {
        for (ReverseEnum temp : ReverseEnum.values()) {
            if (temp.getCode() == code) {
                return temp;
            }
        }
        return null;
    }

    public static String getNameByCode(int code) {
        for (ReverseEnum temp : ReverseEnum.values()) {
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
