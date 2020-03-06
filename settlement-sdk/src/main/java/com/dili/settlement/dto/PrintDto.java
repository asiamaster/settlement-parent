package com.dili.settlement.dto;

import java.util.Map;

/**
 * 打印传输对象
 */
public class PrintDto {

    //打印模板名称
    private String name;
    //打印数据 由业务系统和客户端沟通
    private Object item;

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

    public Object getItem() {
        return item;
    }

    public void setItem(Object item) {
        this.item = item;
    }
}
