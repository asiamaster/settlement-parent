package com.dili.settlement.enums;

/**
 * 资金动作枚举
 */
public enum ActionEnum {

    INCOME(1, "进账"),
    EXPENSE(2, "出账");
    private int code;
    private String name;

    private ActionEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static ActionEnum getByCode(int code) {
        for (ActionEnum actionType : ActionEnum.values()) {
            if (actionType.getCode() == code) {
                return actionType;
            }
        }
        return null;
    }

    public static String getNameByCode(int code) {
        for (ActionEnum actionType : ActionEnum.values()) {
            if (actionType.getCode() == code) {
                return actionType.name;
            }
        }
        return null;
    }
}
