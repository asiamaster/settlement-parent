package com.dili.settlement.service;

/**
 * 字典表相关
 */
public interface DictionaryService {

    /**
     * 获取单个字典表值
     * @param code
     * @param firmId
     * @return
     */
    String getSingleDictVal(String code, Long firmId);

    /**
     * 获取单个字典表值 当未查询到则返回默认值
     * @param code
     * @param firmId
     * @param defaultVal
     * @return
     */
    String getSingleDictVal(String code, Long firmId, String defaultVal);
}
