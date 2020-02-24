package com.dili.settlement.dto;

import java.util.Map;

/**
 * 打印传输对象
 */
public class PrintDto {

    //打印模板名称
    private String name;
    private Map<String, Object> item;

    public PrintDto(String name, Map<String, Object> item){
        this.name = name;
        this.item = item;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getItem() {
        return item;
    }

    public void setItem(Map<String, Object> item) {
        this.item = item;
    }
}
