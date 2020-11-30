package com.dili.settlement.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 接入应用配置
 * This file was generated on 2020-02-25 10:34:34.
 */
@Table(name = "`application_config`")
public class ApplicationConfig extends BaseDomain {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`app_id`")
    private Long appId;

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
}