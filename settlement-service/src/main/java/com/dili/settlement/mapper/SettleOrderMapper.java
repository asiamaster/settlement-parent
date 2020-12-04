package com.dili.settlement.mapper;

import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.SettleAmountDto;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.ss.base.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SettleOrderMapper extends MyMapper<SettleOrder> {
    /**
     * 根据条件查询结算单列表 返回po
     * @param query
     * @return
     */
    List<SettleOrder> list(SettleOrderDto query);

    /**
     * 根据id修改结算单状态
     * @param settleOrder
     * @return
     */
    int updateState(SettleOrder settleOrder);

    /**
     * 根据结算单号查询结算单
     * @param code
     * @return
     */
    SettleOrder getByCode(String code);

    /**
     * 结算修改
     * @param po
     * @return
     */
    int settleUpdate(SettleOrder po);

    /**
     * 根据id列表查询总金额
     * @param settleOrderDto
     * @return
     */
    SettleAmountDto queryAmount(SettleOrderDto settleOrderDto);

    /**
     * 带版本条件删除
     * @param po
     * @return
     */
    int delWithVersion(SettleOrder po);

    /**
     * 批量修改金额
     * @param map
     */
    int batchUpdateAmount(Map<String, Object> map);

    /**
     * 根据id列表查询并锁定记录
     * @param ids
     * @return
     */
    List<SettleOrder> lockList(@Param("ids") List<Long> ids);

    /**
     * 批量结算修改
     * @param settleOrderList
     */
    int batchSettleUpdate(@Param("settleOrderList") List<SettleOrder> settleOrderList, @Param("tradeNo") String tradeNo);
}