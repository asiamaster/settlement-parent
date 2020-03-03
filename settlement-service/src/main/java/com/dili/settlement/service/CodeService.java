package com.dili.settlement.service;

/**
 * 编号生成service
 */
public interface CodeService {

    /**
     * 生成编号
     * @return
     */
    String generate(String type);

    /**
     * 根据前缀生成
     * @param prefix
     * @return
     */
    String generate(String prefix, String type);
}
