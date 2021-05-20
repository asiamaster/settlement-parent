package com.dili.settlement.enums;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author jcy
 * @createTime 2020-02-17 18:51
 */
public enum SceneEnum {
    PAYMENT(1, "交费"),
    DEDUCT(2, "抵扣消费"),
    TRANSFER_IN(3, "定金转入"),
    TRANSFER_OUT(4, "定金转出"),
    REFUND(5, "退款"),
    INVALID_IN(6, "业务交费作废转入"),
    INVALID_OUT(7, "业务交费作废转出"),
    REFUND_TRANSFER_IN(8, "转抵转入")
    ;

    private int code;
    private String name;

    SceneEnum(int code, String name){
        this.code = code;
        this.name = name;
    }

    public static SceneEnum getByCode(int code) {
        for (SceneEnum temp : SceneEnum.values()) {
            if (temp.getCode() == code) {
                return temp;
            }
        }
        return null;
    }

    public static String getNameByCode(int code) {
        for (SceneEnum temp : SceneEnum.values()) {
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
