package com.dili.settlement.util;

import java.util.UUID;

/**
 * 工具类
 */
public class GeneralUtil {

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
