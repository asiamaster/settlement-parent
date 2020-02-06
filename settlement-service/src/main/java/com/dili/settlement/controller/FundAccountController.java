package com.dili.settlement.controller;

import com.dili.settlement.domain.FundAccount;
import com.dili.settlement.service.FundAccountService;
import com.dili.ss.domain.BaseOutput;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-06 17:32:45.
 */
@Api("/fundAccount")
@Controller
@RequestMapping("/fundAccount")
public class FundAccountController {
    @Autowired
    FundAccountService fundAccountService;

    /**
     * 跳转到FundAccount页面
     * @param modelMap
     * @return String
     */
    @ApiOperation("跳转到FundAccount页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "fundAccount/index";
    }

    /**
     * 分页查询FundAccount，返回easyui分页信息
     * @param fundAccount
     * @return String
     * @throws Exception
     */
    @ApiOperation(value="分页查询FundAccount", notes = "分页查询FundAccount，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="FundAccount", paramType="form", value = "FundAccount的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute FundAccount fundAccount) throws Exception {
        return fundAccountService.listEasyuiPageByExample(fundAccount, true).toString();
    }

    /**
     * 新增FundAccount
     * @param fundAccount
     * @return BaseOutput
     */
    @ApiOperation("新增FundAccount")
    @ApiImplicitParams({
		@ApiImplicitParam(name="FundAccount", paramType="form", value = "FundAccount的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@ModelAttribute FundAccount fundAccount) {
        fundAccountService.insertSelective(fundAccount);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改FundAccount
     * @param fundAccount
     * @return BaseOutput
     */
    @ApiOperation("修改FundAccount")
    @ApiImplicitParams({
		@ApiImplicitParam(name="FundAccount", paramType="form", value = "FundAccount的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute FundAccount fundAccount) {
        fundAccountService.updateSelective(fundAccount);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除FundAccount
     * @param id
     * @return BaseOutput
     */
    @ApiOperation("删除FundAccount")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "FundAccount的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        fundAccountService.delete(id);
        return BaseOutput.success("删除成功");
    }
}