package com.dili.settlement.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * 由MyBatis Generator工具自动生成
 * 接入应用表
 * This file was generated on 2020-02-25 10:31:29.
 */
@Table(name = "`market_application`")
public class MarketApplication extends BaseDomain {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`market_id`")
    private Long marketId;

    @Column(name = "`app_id`")
    private Long appId;

    @Column(name = "`name`")
    private String name;

    /**
     * 状态   1 启用  2  禁用
     */
    @Column(name = "`state`")
    private Integer state;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`create_time`")
    private Date createTime;

    /**
     * @return id
     */
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return market_id
     */
    @FieldDef(label="marketId")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getMarketId() {
        return marketId;
    }

    /**
     * @param marketId
     */
    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    /**
     * @return app_id
     */
    @FieldDef(label="appId")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getAppId() {
        return appId;
    }

    /**
     * @param appId
     */
    public void setAppId(Long appId) {
        this.appId = appId;
    }

    /**
     * @return name
     */
    @FieldDef(label="name", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取状态   1 启用  2  禁用
     *
     * @return state - 状态   1 启用  2  禁用
     */
    @FieldDef(label="状态   1 启用  2  禁用")
    @EditMode(editor = FieldEditor.Text, required = false)
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态   1 启用  2  禁用
     *
     * @param state 状态   1 启用  2  禁用
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * @return create_time
     */
    @FieldDef(label="createTime")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}