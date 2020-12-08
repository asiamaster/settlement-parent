package com.dili.settlement.enums;

/**
 * 关联类型枚举
 */
public enum RelationTypeEnum {

    SETTLE_ORDER(1, "结算单"),
    TRANSFER_ORDER(2, "转移单")
    ;

    private int code;
    private String name;

    RelationTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static RelationTypeEnum getByCode(int code) {
        for (RelationTypeEnum temp : RelationTypeEnum.values()) {
            if (temp.getCode() == code) {
                return temp;
            }
        }
        return null;
    }

    public static String getNameByCode(int code) {
        for (RelationTypeEnum temp : RelationTypeEnum.values()) {
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
