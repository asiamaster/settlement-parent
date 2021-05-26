package com.dili.settlement.controller;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.dili.assets.sdk.enums.BusinessChargeItemEnum;
import com.dili.commons.bstable.TableResult;
import com.dili.logger.sdk.domain.BusinessLog;
import com.dili.logger.sdk.rpc.BusinessLogRpc;
import com.dili.settlement.config.CallbackConfiguration;
import com.dili.settlement.dispatcher.PayDispatcher;
import com.dili.settlement.dispatcher.RefundDispatcher;
import com.dili.settlement.domain.*;
import com.dili.settlement.dto.*;
import com.dili.settlement.enums.*;
import com.dili.settlement.handler.ServiceNameHolder;
import com.dili.settlement.handler.TokenHandler;
import com.dili.settlement.resolver.RpcResultResolver;
import com.dili.settlement.rpc.BusinessRpc;
import com.dili.settlement.serializer.DisplayTextAfterFilter;
import com.dili.settlement.service.*;
import com.dili.settlement.task.AsyncTaskExecutor;
import com.dili.settlement.task.CallbackRetryTask;
import com.dili.settlement.util.DateUtil;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.domain.PageOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.util.MoneyUtils;
import com.dili.uap.sdk.domain.UserTicket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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

    @Autowired
    private CustomerAccountService customerAccountService;

    @Autowired
    private SettleWayDetailService settleWayDetailService;

    @Autowired
    private SettleFeeItemService settleFeeItemService;

    @Autowired
    private TransferDetailService transferDetailService;

    @Autowired
    private BusinessLogRpc businessLogRpc;

    @Autowired
    private CallbackConfiguration callbackConfiguration;

    @Value("${project.serverPath}")
    private String serverPath;

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
    public String listPayOrdersByCustomerId(Long customerId, ModelMap modelMap) {
        SettleOrderDto query = buildSettleListQuery(SettleTypeEnum.PAY.getCode());
        query.setCustomerId(customerId);
        List<SettleOrder> settleOrderList = customerId == null ? new ArrayList<>(0) : settleOrderService.list(query);
        List<SettleGroupDto> items = buildSettleGroupDto(settleOrderList);
        modelMap.addAttribute("groupOrderList", items);
        return "pay/table";
    }

    /**
     * 根据挂号查询缴费单
     * @param trailerNumber
     * @return
     */
    @RequestMapping(value = "/listPayOrdersByTrailerNumber.action")
    public String listPayOrdersByTrailerNumber(String trailerNumber, ModelMap modelMap) {
        SettleOrderDto query = buildSettleListQuery(SettleTypeEnum.PAY.getCode());
        query.setTrailerNumber(trailerNumber);
        List<SettleOrder> settleOrderList = StrUtil.isBlank(trailerNumber) ? new ArrayList<>(0) : settleOrderService.list(query);
        List<SettleGroupDto> items = buildSettleGroupDto(settleOrderList);
        modelMap.addAttribute("groupOrderList", items);
        return "pay/table";
    }

    /**
     * 根据提交人ID查询缴费单
     * @param submitterId
     * @return
     */
    @RequestMapping(value = "/listPayOrdersBySubmitterId.action")
    public String listPayOrdersBySubmitterId(Long submitterId, ModelMap modelMap) {
        SettleOrderDto query = buildSettleListQuery(SettleTypeEnum.PAY.getCode());
        query.setSubmitterId(submitterId);
        List<SettleOrder> settleOrderList = submitterId == null ? new ArrayList<>(0) : settleOrderService.list(query);
        List<SettleGroupDto> items = buildSettleGroupDto(settleOrderList);
        modelMap.addAttribute("groupOrderList", items);
        return "pay/table";
    }

    /**
     * 构建待结算查询条件
     * @param type
     * @return
     */
    private SettleOrderDto buildSettleListQuery(Integer type) {
        UserTicket userTicket = getUserTicket();
        SettleOrderDto query = new SettleOrderDto();
        query.setType(type);
        query.setState(SettleStateEnum.WAIT_DEAL.getCode());
        query.setMarketId(userTicket.getFirmId());
        query.setReverse(ReverseEnum.NO.getCode());
        return query;
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
        SettleAmountDto settleAmountDto = settleOrderService.queryAmount(settleOrderDto);
        modelMap.addAttribute("mchId", settleOrderDto.getMchId());
        modelMap.addAttribute("settleAmountDto", settleAmountDto);
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
        modelMap.addAttribute("chargeDate", DateUtil.nowDate());
        return payDispatcher.forwardSpecial(settleOrderDto, modelMap);
    }

    /**
     * 页面支付接口
     * @param settleOrderDto
     * @return
     */
    @RequestMapping(value = "/pay.action")
    @ResponseBody
    public BaseOutput<List<SettleOrder>> pay(SettleOrderDto settleOrderDto) {
        prepareSettle(settleOrderDto);
        List<SettleOrder> settleOrderList = payDispatcher.settle(settleOrderDto);
        for (SettleOrder po : settleOrderList) {
            AsyncTaskExecutor.submit(new CallbackRetryTask(callbackConfiguration.getTimes(), callbackConfiguration.getIntervalMills(), settleOrderService, po));
        }
        return BaseOutput.success().setData(settleOrderList);
    }

    /**
     * 结算前准备
     * @param settleOrderDto
     */
    private void prepareSettle(SettleOrderDto settleOrderDto) {
        if (settleOrderDto.getMchId() == null) {
            throw new BusinessException("", "商户ID为空");
        }
        UserTicket userTicket = getUserTicket();
        if (!tokenHandler.valid(createTokenStr(userTicket, settleOrderDto), settleOrderDto.getToken())) {
            throw new BusinessException("", "非法请求");
        }
        settleOrderDto.setMarketId(userTicket.getFirmId());
        settleOrderDto.setOperatorId(userTicket.getId());
        settleOrderDto.setOperatorName(userTicket.getRealName());
        settleOrderDto.setOperatorNo(userTicket.getUserName());
        settleOrderDto.setIdList(Stream.of(settleOrderDto.getIds().split(",")).map(Long::parseLong).collect(Collectors.toList()));
        SettleAmountDto settleAmountDto = settleOrderService.queryAmount(settleOrderDto);
        settleOrderDto.setSettleAmountDto(settleAmountDto);
    }

    /**
     * 跳转到退款页面
     * @return
     */
    @RequestMapping(value = "/forwardRefundIndex.html")
    public String forwardRefundIndex() {
        return "refund/index";
    }

    /**
     * 根据客户id查询退款单
     * @param customerId
     * @return
     */
    @RequestMapping(value = "/listRefundOrders.action")
    public String listRefundOrders(Long customerId, ModelMap modelMap) {
        SettleOrderDto query = buildSettleListQuery(SettleTypeEnum.REFUND.getCode());
        query.setCustomerId(customerId);
        List<SettleOrder> settleOrderList = customerId == null ? new ArrayList<>(0) : settleOrderService.list(query);
        List<SettleGroupDto> items = buildSettleGroupDto(settleOrderList);
        modelMap.addAttribute("groupOrderList", items);
        return "refund/table";
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
        SettleAmountDto settleAmountDto = settleOrderService.queryAmount(settleOrderDto);
        modelMap.addAttribute("customerId", settleOrderDto.getCustomerId());
        modelMap.addAttribute("customerName", settleOrderDto.getCustomerName());
        modelMap.addAttribute("mchId", settleOrderDto.getMchId());
        modelMap.addAttribute("settleAmountDto", settleAmountDto);
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
        modelMap.addAttribute("customerName", settleOrderDto.getCustomerName());
        return refundDispatcher.forwardSpecial(settleOrderDto, modelMap);
    }

    /**
     * 页面退款接口
     * @param settleOrderDto
     * @return
     */
    @RequestMapping(value = "/refund.action")
    @ResponseBody
    public BaseOutput<List<SettleOrder>> refund(SettleOrderDto settleOrderDto) {
        prepareSettle(settleOrderDto);
        List<SettleOrder> settleOrderList = refundDispatcher.settle(settleOrderDto);
        for (SettleOrder po : settleOrderList) {
            AsyncTaskExecutor.submit(new CallbackRetryTask(callbackConfiguration.getTimes(), callbackConfiguration.getIntervalMills(), settleOrderService, po));
        }
        return BaseOutput.success().setData(settleOrderList);
    }

    /**
     * 跳转到业务详情页面
     * @param response
     */
    @RequestMapping(value = "/showDetail.html")
    public void showDetail(Long id, Integer reverse, HttpServletResponse response) {
        if (id == null || reverse == null) {
            throw new BusinessException("", "查询业务详情URL参数错误");
        }
        //按照作废业务逻辑，如果是冲正单，则转换为原单ID，从而查看详情
        if (Integer.valueOf(ReverseEnum.YES.getCode()).equals(reverse)) {
            id = settleOrderService.convertReverseOrderId(id);
        }
        String url = settleOrderLinkService.getUrl(id, LinkTypeEnum.DETAIL.getCode());
        response.setStatus(302);
        response.setHeader("Location", url);
    }

    /**
     * 跳转到费用详情页面
     * @param
     */
    @RequestMapping(value = "/showAmountDetail.action")
    public String showAmountDetail(Long id, Integer reverse, ModelMap modelMap) {
        if (id == null || reverse == null) {
            throw new BusinessException("", "查询费用详情参数错误");
        }
        //按照作废业务逻辑，如果是冲正单，则转换为原单ID，从而查看详情
        if (Integer.valueOf(ReverseEnum.YES.getCode()).equals(reverse)) {
            id = settleOrderService.convertReverseOrderId(id);
        }
        List<SettleFeeItem> settleFeeItemList = settleFeeItemService.listBySettleOrderId(id);
        modelMap.addAttribute("amountItemList", settleFeeItemList.stream().filter(temp -> temp.getChargeItemType().equals(ChargeItemTypeEnum.FEE.getCode())).collect(Collectors.toList()));
        return "settleOrder/amount_detail";
    }

    /**
     * 跳转到抵扣详情页面
     * @param
     */
    @RequestMapping(value = "/showDeductDetail.action")
    public String showDeductDetail(Long id, Integer reverse, ModelMap modelMap) {
        if (id == null || reverse == null) {
            throw new BusinessException("", "查询抵扣详情参数错误");
        }
        //按照作废业务逻辑，如果是冲正单，则转换为原单ID，从而查看详情
        if (Integer.valueOf(ReverseEnum.YES.getCode()).equals(reverse)) {
            id = settleOrderService.convertReverseOrderId(id);
        }
        List<SettleFeeItem> settleFeeItemList = settleFeeItemService.listBySettleOrderId(id);
        modelMap.addAttribute("deductItemList", settleFeeItemList.stream().filter(temp -> temp.getChargeItemType().equals(ChargeItemTypeEnum.DEDUCT.getCode())).collect(Collectors.toList()));
        return "settleOrder/deduct_detail";
    }

    /**
     * 跳转到转抵详情页面
     * @param
     */
    @RequestMapping(value = "/showTransferDetail.action")
    public String showTransferDetail(Long id, Integer reverse, ModelMap modelMap) {
        if (id == null || reverse == null) {
            throw new BusinessException("", "查询转抵详情参数错误");
        }
        //按照作废业务逻辑，如果是冲正单，则转换为原单ID，从而查看详情
        if (Integer.valueOf(ReverseEnum.YES.getCode()).equals(reverse)) {
            id = settleOrderService.convertReverseOrderId(id);
        }
        List<TransferDetail> transferDetailList = transferDetailService.listBySettleOrderId(id);
        modelMap.addAttribute("transferDetailList", transferDetailList);
        return "settleOrder/transfer_detail";
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
        return businessRpc.loadPrintData(url, reprint);
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
     * 跳转到信息修改页面
     * @param id
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/forwardChange.action")
    public String forwardChange(Long id, ModelMap modelMap) {
        if (id == null) {
            throw new BusinessException("", "ID为空");
        }
        SettleOrder settleOrder = settleOrderService.get(id);
        if (settleOrder == null) {
            throw new BusinessException("", "该记录不存在");
        }
        modelMap.addAttribute("settleOrder", settleOrder);
        //组合支付
        if (Integer.valueOf(SettleWayEnum.MIXED_PAY.getCode()).equals(settleOrder.getWay())) {
            List<SettleWayDetail> detailList = settleWayDetailService.listBySettleOrderId(settleOrder.getId());
            modelMap.addAttribute("settleWayList", JSON.parseArray(JSON.toJSONString(detailList, new DisplayTextAfterFilter())));
            return "settleOrder/change_mixed";
        }
        return "settleOrder/change";
    }

    /**
     * 修改
     * @param chargeDateDto
     * @return
     */
    @RequestMapping(value = "/change.action")
    @ResponseBody
    public BaseOutput<?> change(ChargeDateDto chargeDateDto) {
        if (chargeDateDto.getId() == null) {
            throw new BusinessException("", "ID为空");
        }
        SettleOrder settleOrder = settleOrderService.get(chargeDateDto.getId());
        if (settleOrder == null) {
            throw new BusinessException("", "该记录不存在");
        }
        StringBuilder content = new StringBuilder("");
        if (Integer.valueOf(SettleWayEnum.MIXED_PAY.getCode()).equals(settleOrder.getWay())) {
            List<SettleWayDetail> detailList = settleWayDetailService.listBySettleOrderId(settleOrder.getId());
            for (SettleWayDetail temp : detailList) {
                content.append("结算明细ID：").append(temp.getId()).append("， ");
                content.append("结算方式：").append(SettleWayEnum.getNameByCode(temp.getWay())).append("， ");
                SettleWayDetail settleWayDetail = chargeDateDto.getSettleWayDetailList().stream().filter(t -> temp.getId().equals(t.getId())).findFirst().orElse(null);
                if (settleWayDetail == null) {
                    continue;
                }
                content.append("收款日期：从").append(temp.getChargeDate() != null ? DateUtil.formatDate(temp.getChargeDate(), "yyyy-MM-dd") : "").append("改为").append(settleWayDetail.getChargeDate() != null ? DateUtil.formatDate(settleWayDetail.getChargeDate(), "yyyy-MM-dd") : "").append("； ");
            }
            settleWayDetailService.batchUpdateChargeDate(chargeDateDto.getSettleWayDetailList());
        } else {
            content.append("收款日期：从").append(settleOrder.getChargeDate() != null ? DateUtil.formatDate(settleOrder.getChargeDate(), "yyyy-MM-dd") : "").append("改为").append(chargeDateDto.getChargeDate() != null ? DateUtil.formatDate(chargeDateDto.getChargeDate(), "yyyy-MM-dd") : "").append("； ");
            settleOrderService.updateChargeDate(chargeDateDto);
        }
        try {
            UserTicket userTicket = getUserTicket();
            BusinessLog businessLog = new BusinessLog();
            businessLog.setOperationType("edit");
            businessLog.setBusinessId(settleOrder.getId());
            businessLog.setBusinessCode(settleOrder.getCode());
            businessLog.setBusinessType("settlement");
            businessLog.setMarketId(userTicket.getFirmId());
            businessLog.setOperatorId(userTicket.getId());
            businessLog.setOperatorName(userTicket.getRealName());
            businessLog.setSystemCode("IA");
            businessLog.setContent(content.toString());
            RpcResultResolver.resolver(businessLogRpc.save(businessLog, serverPath), ServiceNameHolder.LOGGER_SERVICE_NAME);
        } catch (Exception e) {
            LOGGER.error("change log error", e);
        }
        return BaseOutput.success();
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
        builder.append(settleOrderDto.getMchId());
        builder.append(userTicket.getId());
        builder.append(userTicket.getFirmId());
        return builder.toString();
    }

    /**
     * 构建分组数据
     * @param settleOrderList
     * @return
     */
    private List<SettleGroupDto> buildSettleGroupDto(List<SettleOrder> settleOrderList) {
        List<SettleGroupDto> items = new ArrayList<>(settleOrderList.size());
        Map<Long, SettleGroupDto> map = new TreeMap<>();
        for (SettleOrder settleOrder : settleOrderList) {
            if (map.containsKey(settleOrder.getMchId())) {
                map.get(settleOrder.getMchId()).append(settleOrder);
            } else {
                map.put(settleOrder.getMchId(), SettleGroupDto.build(settleOrder.getMchId(), settleOrder.getMchName(), settleOrder));
            }
        }
        items.addAll(map.values());
        return items;
    }
}