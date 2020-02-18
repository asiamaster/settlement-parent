package com.dili.settlement.dto;

import java.util.Map;

/**
 * 打印传输对象
 */
public class PrintDto {

    //打印模板名称
    private String name;
    private Map<String, String> item;

    public PrintDto(String name, Map<String, String> item){
        this.name = name;
        this.item = item;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getItem() {
        return item;
    }

    public void setItem(Map<String, String> item) {
        this.item = item;
    }
}
