package com.dili.settlement.service;

import com.dili.settlement.domain.SettleOrderLink;
import com.dili.ss.base.BaseService;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-11-26 16:29:48.
 */
public interface SettleOrderLinkService extends BaseService<SettleOrderLink, Long> {
    /**
     * 查询url
     * @param settleOrderId 结算单ID
     * @param type 链接类型
     * @return
     */
    String getUrl(Long settleOrderId, int type);

    /**
     * 根据结算单ID删除
     * @param settleOrderId
     * @return
     */
    int deleteBySettleOrderId(Long settleOrderId);
}