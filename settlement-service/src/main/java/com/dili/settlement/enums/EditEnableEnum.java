package com.dili.settlement.enums;

/**
 * 是否可编辑枚举
 */
public enum EditEnableEnum {
    YES(1, "是"),
    NO(2, "否");

    private int code;
    private String name;

    EditEnableEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static EditEnableEnum getByCode(int code) {
        for (EditEnableEnum temp : EditEnableEnum.values()) {
            if (temp.getCode() == code) {
                return temp;
            }
        }
        return null;
    }

    public static String getNameByCode(int code) {
        for (EditEnableEnum temp : EditEnableEnum.values()) {
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
