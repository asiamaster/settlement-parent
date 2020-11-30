package com.dili.settlement.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.dili.commons.bstable.TableResult;
import com.dili.settlement.dispatcher.PayDispatcher;
import com.dili.settlement.dispatcher.RefundDispatcher;
import com.dili.settlement.domain.SettleConfig;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.dto.PrintDto;
import com.dili.settlement.dto.SettleOrderDto;
import com.dili.settlement.enums.*;
import com.dili.settlement.handler.TokenHandler;
import com.dili.settlement.rpc.BusinessRpc;
import com.dili.settlement.service.SettleOrderLinkService;
import com.dili.settlement.service.SettleOrderService;
import com.dili.settlement.service.SettleWayService;
import com.dili.settlement.util.DateUtil;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.util.MoneyUtils;
import com.dili.uap.sdk.domain.UserTicket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-05 16:39:11.
 */

@Controller
@RequestMapping("/settleOrder")
public class SettleOrderController extends AbstractController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SettleOrderController.class);

    @Autowired
    private SettleOrderService settleOrderService;

    @Autowired
    private SettleWayService settleWayService;

    @Autowired
    private TokenHandler tokenHandler;

    @Autowired
    private PayDispatcher payDispatcher;

    @Autowired
    private RefundDispatcher refundDispatcher;

    @Autowired
    private SettleOrderLinkService settleOrderLinkService;

    @Autowired
    private BusinessRpc businessRpc;

    /**
     * 跳转到支付页面
     * @return
     */
    @RequestMapping(value = "/forwardPayIndex.html")
    public String forwardPayIndex() {
        return "pay/index";
    }

    /**
     * 根据客户id查询缴费单
     * @param customerId
     * @return
     */
    @RequestMapping(value = "/listPayOrders.action")
    @ResponseBody
    public TableResult<SettleOrder> listPayOrders(Long customerId) {
        return listSettleOrders(customerId, SettleTypeEnum.PAY.getCode());
    }

    /**
     * 提取公共查询结算单方法
     * @param customerId
     * @param type
     * @return
     */
    private TableResult<SettleOrder> listSettleOrders(Long customerId, Integer type) {
        if (customerId == null) {
            return new TableResult<>(1 ,0L, new ArrayList<>(0));
        }
        UserTicket userTicket = getUserTicket();
        SettleOrderDto query = new SettleOrderDto();
        query.setType(type);
        query.setState(SettleStateEnum.WAIT_DEAL.getCode());
        query.setCustomerId(customerId);
        query.setMarketId(userTicket.getFirmId());
        query.setReverse(ReverseEnum.NO.getCode());
        List<SettleOrder> settleOrderList = settleOrderService.list(query);
        return new TableResult<>(1 ,(long)settleOrderList.size(), settleOrderList);
    }

    /**
     * 跳转到支付页面
     * @param settleOrderDto
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/forwardPay.html")
    public String forwardPay(SettleOrderDto settleOrderDto, ModelMap modelMap) {
        if (StrUtil.isBlank(settleOrderDto.getIds())) {
            return "pay/pay";
        }
        settleOrderDto.setIdList(Stream.of(settleOrderDto.getIds().split(",")).map(Long::parseLong).collect(Collectors.toList()));
        Long totalAmount = settleOrderService.queryTotalAmount(settleOrderDto);
        modelMap.addAttribute("totalAmount", totalAmount);
        modelMap.addAttribute("totalAmountView", MoneyUtils.centToYuan(totalAmount));
        UserTicket userTicket = getUserTicket();
        List<SettleConfig> wayList = settleWayService.payChooseList(userTicket.getFirmId(), settleOrderDto.getIdList().size() > 1);
        modelMap.addAttribute("wayList", wayList);
        modelMap.addAttribute("token", tokenHandler.generate(createTokenStr(userTicket, settleOrderDto)));
        modelMap.addAttribute("ids", settleOrderDto.getIds());
        return "pay/pay";
    }

    /**
     * 跳转到支付个性化页面
     * @param settleOrderDto
     * @return
     */
    @RequestMapping(value = "/forwardPaySpecial.html")
    public String forwardPaySpecial(SettleOrderDto settleOrderDto, ModelMap modelMap) {
        settleOrderDto.setMarketId(getUserTicket().getFirmId());
        return payDispatcher.forwardSpecial(settleOrderDto, modelMap);
    }

    /**
     * 跳转到退款页面
     * @param settleOrderDto
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/forwardRefund.html")
    public String forwardRefund(SettleOrderDto settleOrderDto, ModelMap modelMap) {
        if (StrUtil.isBlank(settleOrderDto.getIds())) {
            return "refund/refund";
        }
        settleOrderDto.setIdList(Stream.of(settleOrderDto.getIds().split(",")).map(Long::parseLong).collect(Collectors.toList()));
        Long totalAmount = settleOrderService.queryTotalAmount(settleOrderDto);
        modelMap.addAttribute("totalAmount", totalAmount);
        modelMap.addAttribute("totalAmountView", MoneyUtils.centToYuan(totalAmount));
        UserTicket userTicket = getUserTicket();
        List<SettleConfig> wayList = settleWayService.refundChooseList(userTicket.getFirmId());
        modelMap.addAttribute("wayList", wayList);
        modelMap.addAttribute("token", tokenHandler.generate(createTokenStr(userTicket, settleOrderDto)));
        modelMap.addAttribute("ids", settleOrderDto.getIds());
        return "refund/refund";
    }

    /**
     * 跳转到退款个性化页面
     * @param settleOrderDto
     * @return
     */
    @RequestMapping(value = "/forwardRefundSpecial.html")
    public String forwardRefundSpecial(SettleOrderDto settleOrderDto, ModelMap modelMap) {
        settleOrderDto.setMarketId(getUserTicket().getFirmId());
        return refundDispatcher.forwardSpecial(settleOrderDto, modelMap);
    }

    /**
     * 跳转到业务详情页面
     * @param response
     */
    @RequestMapping(value = "/showDetail.html")
    public void showDetail(Long id, HttpServletResponse response) {
        if (id == null) {
            throw new BusinessException("", "查询业务详情URL参数错误");
        }
        String url = settleOrderLinkService.getUrl(id, LinkTypeEnum.DETAIL.getCode());
        response.setStatus(302);
        response.setHeader("Location", url);
    }

    /**
     * 加载业务打印数据
     * @param
     * @return
     */
    @RequestMapping(value = "/loadPrintData.action")
    @ResponseBody
    public BaseOutput<PrintDto> loadPrintData(Long id, Integer reprint) {
        if (id == null || reprint == null) {
            throw new BusinessException("", "获取打印数据参数错误");
        }
        String url = settleOrderLinkService.getUrl(id, LinkTypeEnum.PRINT.getCode());
        if (StrUtil.isBlank(url)) {
            throw new BusinessException("", "获取打印数据URL错误");
        }
        return businessRpc.loadPrintData(url + "&reprint=" + reprint);
    }

    /**
     * 跳转到结算单列表页面
     * @return
     */
    @RequestMapping(value = "/forwardList.html")
    public String forwardList(ModelMap modelMap) {
        UserTicket userTicket = getUserTicket();
        modelMap.put("marketId", userTicket.getFirmId());
        LocalDate date = DateUtil.nowDate();
        String operateTimeStart = DateUtil.formatDate(date.minus(2L, ChronoUnit.DAYS), "yyyy-MM-dd") + " 00:00:00";
        String operateTimeEnd = DateUtil.formatDate(date, "yyyy-MM-dd") + " 23:59:59";
        modelMap.addAttribute("operateTimeStart", operateTimeStart);
        modelMap.addAttribute("operateTimeEnd", operateTimeEnd);
        modelMap.addAttribute("businessTypeList", BizTypeEnum.values());
        return "settleOrder/index";
    }

    /**
     * 分页查询列表数据
     * @return
     */
    @RequestMapping(value = "/listPage.action")
    @ResponseBody
    public TableResult<SettleOrder> listPage(SettleOrderDto settleOrderDto) {
        //删除数组中空值元素(null 或 "")
        settleOrderDto.setBusinessTypeArray(deleteEmptyElement(settleOrderDto.getBusinessTypeArray()));
        if (ArrayUtil.isEmpty(settleOrderDto.getBusinessTypeArray())) {
            return new TableResult<SettleOrder>(1, 0L, new ArrayList(0));
        }
        UserTicket userTicket = getUserTicket();
        settleOrderDto.setMarketId(userTicket.getFirmId());
        PageOutput<List<SettleOrder>> pageOutput = settleOrderService.listPagination(settleOrderDto);
        if (pageOutput.isSuccess()) {
            return new TableResult<>(settleOrderDto.getPage(), pageOutput.getTotal(), pageOutput.getData());
        }
        return new TableResult<SettleOrder>(1, 0L, new ArrayList(0));
    }

    /**
     * 生成用于签名的字符串
     * @param userTicket
     * @param settleOrderDto
     * @return
     */
    private String createTokenStr(UserTicket userTicket, SettleOrderDto settleOrderDto) {
        StringBuilder builder = new StringBuilder();
        builder.append(settleOrderDto.getIds());
        builder.append(userTicket.getId());
        builder.append(userTicket.getFirmId());
        return builder.toString();
    }
}