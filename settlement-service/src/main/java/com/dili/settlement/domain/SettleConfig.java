package com.dili.settlement.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2020-02-07 11:11:14.
 */
@Table(name = "`settle_config`")
public class SettleConfig extends BaseDomain {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`group_code`")
    private Integer groupCode;

    @Column(name = "`code`")
    private Integer code;

    @Column(name = "`val`")
    private String val;

    @Column(name = "`state`")
    private Integer state;

    @Column(name = "`notes`")
    private String notes;

    @Column(name = "`market_id`")
    private Long marketId;

    @Column(name = "`sort_field`")
    private Integer sortField;

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
     * @return group_code
     */
    @FieldDef(label="groupCode")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getGroupCode() {
        return groupCode;
    }

    /**
     * @param groupCode
     */
    public void setGroupCode(Integer groupCode) {
        this.groupCode = groupCode;
    }

    /**
     * @return code
     */
    @FieldDef(label="code")
    @EditMode(editor = FieldEditor.Text, required = false)
    public Integer getCode() {
        return code;
    }

    /**
     * @param code
     */
    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * @return val
     */
    @FieldDef(label="val", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getVal() {
        return val;
    }

    /**
     * @param val
     */
    public void setVal(String val) {
        this.val = val;
    }

    /**
     * @return state
     */
    @FieldDef(label="state")
    @EditMode(editor = FieldEditor.Text, required = false)
    public Integer getState() {
        return state;
    }

    /**
     * @param state
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * @return notes
     */
    @FieldDef(label="notes", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getNotes() {
        return notes;
    }

    /**
     * @param notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * getter
     * @return
     */
    public Long getMarketId() {
        return marketId;
    }

    /**
     * setter
     * @return
     */
    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    /**
     * getter
     * @return
     */
    public Integer getSortField() {
        return sortField;
    }

    /**
     * setter
     * @return
     */
    public void setSortField(Integer sortField) {
        this.sortField = sortField;
    }
}