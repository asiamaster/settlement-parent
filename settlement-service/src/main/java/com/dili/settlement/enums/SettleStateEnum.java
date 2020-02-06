package com.dili.settlement.enums;

/**
 * 结算状态枚举
 */
public enum SettleStateEnum {
    WAIT_DEAL(1, "待处理"),
    DEAL(2, "已处理"),
    CANCEL(3, "已取消");

    private int code;
    private String name;

    SettleStateEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static SettleStateEnum getByCode(int code) {
        for (SettleStateEnum temp : SettleStateEnum.values()) {
            if (temp.getCode() == code) {
                return temp;
            }
        }
        return null;
    }

    public static String getNameByCode(int code) {
        for (SettleStateEnum temp : SettleStateEnum.values()) {
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
