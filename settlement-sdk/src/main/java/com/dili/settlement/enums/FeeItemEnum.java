package com.dili.settlement.enums;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author jcy
 * @createTime 2020-02-17 18:51
 */
public enum FeeItemEnum {
    //静态收费项，费用项定义，神农号段 200 ~ 300
    定金(201L, "定金"),
    保证金(202L, " 保证金"),

    //静态收费项，费用项定义，神农号段 300 ~ 400
    车辆进门办卡费(301L, "车辆进门办卡费")
    ;

    private long id;
    private String name;

    FeeItemEnum(long id, String name){
        this.id = id;
        this.name = name;
    }

    public static FeeItemEnum getByCode(long id) {
        for (FeeItemEnum temp : FeeItemEnum.values()) {
            if (temp.getId() == id) {
                return temp;
            }
        }
        return null;
    }

    public static String getNameByCode(long id) {
        for (FeeItemEnum temp : FeeItemEnum.values()) {
            if (temp.getId() == id) {
                return temp.getName();
            }
        }
        return null;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
