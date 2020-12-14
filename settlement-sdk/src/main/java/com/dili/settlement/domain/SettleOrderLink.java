package com.dili.settlement.domain;

import com.dili.ss.domain.BaseDomain;

/**
 * 由MyBatis Generator工具自动生成
 * 结算单链接表
 * This file was generated on 2020-11-26 16:29:48.
 */
public class SettleOrderLink extends BaseDomain {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 结算单ID
     */
    private Long settleOrderId;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 路径
     */
    private String url;

    /**
     * 获取主键ID
     *
     * @return id - 主键ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键ID
     *
     * @param id 主键ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取结算单ID
     *
     * @return business_order_id - 结算单ID
     */
    public Long getSettleOrderId() {
        return settleOrderId;
    }

    /**
     * 设置结算单ID
     *
     * @param settleOrderId 结算单ID
     */
    public void setSettleOrderId(Long settleOrderId) {
        this.settleOrderId = settleOrderId;
    }

    /**
     * 获取类型
     *
     * @return type - 类型
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置类型
     *
     * @param type 类型
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取路径
     *
     * @return url - 路径
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置路径
     *
     * @param url 路径
     */
    public void setUrl(String url) {
        this.url = url;
    }
}