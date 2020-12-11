package com.dili.settlement.enums;

/**
 * 交易渠道枚举
 */
public enum TradeChannelEnum {
    BALANCE(1, "账户余额"),
    CASH(2, "现金"),
    POS(3, "POS"),
    E_BANK(4, "网银"),
    WECHAT(10, "微信"),
    ALI(11, "支付宝"),
    SJ_BANK(28, "盛京银行"),
    VIRTUAL(50, "虚拟支付"),
    MIXED(51, "组合支付")
    ;
    private int code;
    private String name;

    TradeChannelEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static TradeChannelEnum getByCode(int code) {
        for (TradeChannelEnum tradeChannel : TradeChannelEnum.values()) {
            if (tradeChannel.getCode() == code) {
                return tradeChannel;
            }
        }
        return null;
    }

    public static String getNameByCode(int code) {
        for (TradeChannelEnum tradeChannel : TradeChannelEnum.values()) {
            if (tradeChannel.getCode() == code) {
                return tradeChannel.name;
            }
        }
        return null;
    }
}
