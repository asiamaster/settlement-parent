package com.dili.settlement.enums;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author jcy
 * @createTime 2020-02-17 18:51
 */
public enum BizTypeEnum {
    BOOTH_LEASE("1", "摊位租赁","boothLease"),
    EARNEST("2", "定金","earnest"),
    DEPOSIT_ORDER("3", "保证金","deposit"),
    LOCATION_LEASE("4", "冷库租赁","locationLease"),
    LODGING_LEASE("5", "公寓租赁","lodgingLease"),
    BOUTIQUE_ENTRANCE("6", "精品停车费", "boutique"),
    PASSPORT("7", "通行证", "passport"),
    STOCKIN("8", "冷库入库", "stockIn"),
    STOCKOUT("9", "冷库出库", "stockout"),
    OTHER_FEE("10", "其它收费", "otherFee"),
    LABOR_VEST("11", "劳务马甲", "laborVest"),
    LABOR_VEST_RENAME("12", "劳务马甲更名费", "laborVestRename"),
    LABOR_VEST_REMODEL("13", "劳务马甲更型费", "laborVestRemodel"),
    MESSAGEFEE("14", "信息费", "messageFee"),
    WATER("15", "水费", "water"),
    ELECTRICITY("16", "电费", "electricity"),
    ENTRANCE_CARD("301", "车辆进门办卡", "entranceCard")
    ;

    private String name;
    private String enName;
    private String code ;

    BizTypeEnum(String code, String name, String enName){
        this.code = code;
        this.name = name;
        this.enName = enName;
    }

    public static BizTypeEnum getBizTypeEnum(String code) {
        for (BizTypeEnum anEnum : BizTypeEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return null;
    }

    public static String getNameByCode(String code) {
        for (BizTypeEnum anEnum : BizTypeEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum.getName();
            }
        }
        return "";
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getEnName() {
        return enName;
    }
}
