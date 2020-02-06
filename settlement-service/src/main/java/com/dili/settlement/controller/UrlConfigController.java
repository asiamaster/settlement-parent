package com.dili.settlement.controller;

import com.dili.settlement.domain.UrlConfig;
import com.dili.settlement.service.UrlConfigService;
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
 * This file was generated on 2020-02-06 17:33:44.
 */
@Api("/urlConfig")
@Controller
@RequestMapping("/urlConfig")
public class UrlConfigController {
    @Autowired
    UrlConfigService urlConfigService;

    /**
     * 跳转到UrlConfig页面
     * @param modelMap
     * @return String
     */
    @ApiOperation("跳转到UrlConfig页面")
    @RequestMapping(value="/index.html", method = RequestMethod.GET)
    public String index(ModelMap modelMap) {
        return "urlConfig/index";
    }

    /**
     * 分页查询UrlConfig，返回easyui分页信息
     * @param urlConfig
     * @return String
     * @throws Exception
     */
    @ApiOperation(value="分页查询UrlConfig", notes = "分页查询UrlConfig，返回easyui分页信息")
    @ApiImplicitParams({
		@ApiImplicitParam(name="UrlConfig", paramType="form", value = "UrlConfig的form信息", required = false, dataType = "string")
	})
    @RequestMapping(value="/listPage.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String listPage(@ModelAttribute UrlConfig urlConfig) throws Exception {
        return urlConfigService.listEasyuiPageByExample(urlConfig, true).toString();
    }

    /**
     * 新增UrlConfig
     * @param urlConfig
     * @return BaseOutput
     */
    @ApiOperation("新增UrlConfig")
    @ApiImplicitParams({
		@ApiImplicitParam(name="UrlConfig", paramType="form", value = "UrlConfig的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/insert.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput insert(@ModelAttribute UrlConfig urlConfig) {
        urlConfigService.insertSelective(urlConfig);
        return BaseOutput.success("新增成功");
    }

    /**
     * 修改UrlConfig
     * @param urlConfig
     * @return BaseOutput
     */
    @ApiOperation("修改UrlConfig")
    @ApiImplicitParams({
		@ApiImplicitParam(name="UrlConfig", paramType="form", value = "UrlConfig的form信息", required = true, dataType = "string")
	})
    @RequestMapping(value="/update.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput update(@ModelAttribute UrlConfig urlConfig) {
        urlConfigService.updateSelective(urlConfig);
        return BaseOutput.success("修改成功");
    }

    /**
     * 删除UrlConfig
     * @param id
     * @return BaseOutput
     */
    @ApiOperation("删除UrlConfig")
    @ApiImplicitParams({
		@ApiImplicitParam(name="id", paramType="form", value = "UrlConfig的主键", required = true, dataType = "long")
	})
    @RequestMapping(value="/delete.action", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody BaseOutput delete(Long id) {
        urlConfigService.delete(id);
        return BaseOutput.success("删除成功");
    }
}