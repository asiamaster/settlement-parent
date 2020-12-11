package com.dili.settlement.enums;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author jcy
 * @createTime 2020-02-17 18:51
 */
public enum FeeTypeEnum {
    默认(1, "其它"),
    开卡工本费(101, "开卡工本费"),
    换卡工本费(102, "换卡工本费"),
    POS充值手续费(103, "POS充值手续费"),
    提现网银手续费(104, "提现网银手续费"),
    定金(201, "定金"),
    保证金(202, "保证金")
    ;

    private int code;
    private String name;

    FeeTypeEnum(int code, String name){
        this.code = code;
        this.name = name;
    }

    public static FeeTypeEnum getByCode(int code) {
        for (FeeTypeEnum temp : FeeTypeEnum.values()) {
            if (temp.getCode() == code) {
                return temp;
            }
        }
        return null;
    }

    public static String getNameByCode(int code) {
        for (FeeTypeEnum temp : FeeTypeEnum.values()) {
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
