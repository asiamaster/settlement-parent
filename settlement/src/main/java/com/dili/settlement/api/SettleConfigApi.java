package com.dili.settlement.api;

import cn.hutool.core.util.StrUtil;
import com.dili.settlement.domain.SettleConfig;
import com.dili.settlement.enums.ConfigStateEnum;
import com.dili.settlement.service.SettleConfigService;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用于操作结算方式接口
 */
@RestController
@RequestMapping(value = "/api/settleConfig")
public class SettleConfigApi {

    @Autowired
    private SettleConfigService settleConfigService;

    /**
     * 根据市场ID、组编码、编码启用
     * @param config
     * @return
     */
    @RequestMapping(value = "/enable")
    public BaseOutput<?> enable(@RequestBody SettleConfig config) {
        checkCommonParams(config);
        settleConfigService.enable(config);
        return BaseOutput.success();
    }

    /**
     * 根据市场ID、组编码、编码禁用
     * @param config
     * @return
     */
    @RequestMapping(value = "/disable")
    public BaseOutput<?> disable(@RequestBody SettleConfig config) {
        checkCommonParams(config);
        settleConfigService.disable(config);
        return BaseOutput.success();
    }

    /**
     * 根据市场ID、组编码、编码 新增
     * @param config
     * @return
     */
    @RequestMapping(value = "/add")
    public BaseOutput<?> add(@RequestBody SettleConfig config) {
        checkCommonParams(config);
        if (StrUtil.isBlank(config.getVal())) {
            return BaseOutput.failure("值VAL为空");
        }
        config.setSortField(config.getSortField() == null ? 0 : config.getSortField());
        config.setState(config.getState() == null ? ConfigStateEnum.ENABLE.getCode() : config.getState());
        settleConfigService.add(config);
        return BaseOutput.success();
    }

    /**
     * 根据市场ID、组编码、编码 修改
     * @param config
     * @return
     */
    @RequestMapping(value = "/change")
    public BaseOutput<?> change(@RequestBody SettleConfig config) {
        checkCommonParams(config);
        settleConfigService.change(config);
        return BaseOutput.success();
    }

    /**
     * 验证公共参数
     * @param config
     */
    private void checkCommonParams(SettleConfig config) {
        if (config.getMarketId() == null) {
            throw new BusinessException("", "市场ID为空");
        }
        if (config.getGroupCode() == null) {
            throw new BusinessException("", "组编码为空");
        }
        if (config.getCode() == null) {
            throw new BusinessException("", "编码为空");
        }
    }
}
