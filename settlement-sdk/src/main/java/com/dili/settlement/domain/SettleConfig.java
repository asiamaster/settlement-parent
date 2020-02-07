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

    @Column(name = "`market_id`")
    private Long marketId;

    @Column(name = "`group_code`")
    private Integer groupCode;

    @Column(name = "`code`")
    private Byte code;

    @Column(name = "`val`")
    private String val;

    @Column(name = "`state`")
    private Byte state;

    @Column(name = "`notes`")
    private String notes;

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
    public Byte getCode() {
        return code;
    }

    /**
     * @param code
     */
    public void setCode(Byte code) {
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
    public Byte getState() {
        return state;
    }

    /**
     * @param state
     */
    public void setState(Byte state) {
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
}