package com.dili.settlement.api;

import cn.hutool.core.util.StrUtil;
import com.dili.settlement.domain.SettleWayDetail;
import com.dili.settlement.service.SettleWayDetailService;
import com.dili.ss.domain.BaseOutput;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 查询结算方式明细
 */
@RestController
@RequestMapping(value = "/api/settleWayDetail")
public class SettleWayDetailApi {

    @Resource
    private SettleWayDetailService settleWayDetailService;

    /**
     * @param settleOrderCode 根据结算单号查询结算明细列表
     * @return
     */
    @RequestMapping(value = "/listByCode")
    public BaseOutput<List<SettleWayDetail>> listByCode(String settleOrderCode) {
        if (StrUtil.isBlank(settleOrderCode)) {
            return BaseOutput.failure("结算单号为空");
        }
        return BaseOutput.success().setData(settleWayDetailService.listBySettleOrderCode(settleOrderCode));
    }
}
