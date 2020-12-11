package com.dili.settlement.dto;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @Auther: miaoguoxin
 * @Date: 2020/6/19 10:27
 * @Description:
 */
public class BaseDto implements Serializable {
    /** */
	private static final long serialVersionUID = 746716184736078528L;
	/**操作员id*/
    private Long opId;
    /**操作员名字*/
    private String opName;
    /** 操作员工号*/
    private String opNo;
    /** 市场ID*/
    private Long firmId;
    /**页码*/
    private Integer page;
    /**每页多少条*/
    private Integer rows;
    /**顺序or降序 ASC、DESC*/
    private String order;
    /**排序字段*/
    private String sort;

    public BaseDto setDefSort(String defSort) {
        if (StringUtils.isBlank(this.getSort())) {
            this.setSort(defSort);
        }
        return this;
    }

    public BaseDto setDefOrder(String defColumn) {
        if (StringUtils.isBlank(this.getOrder())) {
            this.setOrder(defColumn);
        }
        return this;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Long getOpId() {
        return opId;
    }

    public void setOpId(Long opId) {
        this.opId = opId;
    }

    public String getOpName() {
        return opName;
    }

    public void setOpName(String opName) {
        this.opName = opName;
    }

    public String getOpNo() {
        return opNo;
    }

    public void setOpNo(String opNo) {
        this.opNo = opNo;
    }

    public Long getFirmId() {
        return firmId;
    }

    public void setFirmId(Long firmId) {
        this.firmId = firmId;
    }
}
