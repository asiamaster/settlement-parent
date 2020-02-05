package com.dili.settlement.service;

/**
 * 编号生成service
 */
public interface CodeService {

    String generate();
    String generate(String prefix);
}
