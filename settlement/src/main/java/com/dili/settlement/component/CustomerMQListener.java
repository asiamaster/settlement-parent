package com.dili.settlement.component;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.dili.customer.sdk.constants.MqConstant;
import com.dili.customer.sdk.domain.dto.CustomerExtendDto;
import com.dili.settlement.service.CustomerAccountService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description： 
 *          客户信息变动时更新账户冗余信息
 * @author ：xuliang
 * @time ：2020年8月14日上午10:36:38
 */
@Component
public class CustomerMQListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerMQListener.class);

    @Autowired
    private CustomerAccountService customerAccountService;
    /**
     * 客户信息修改后，更新账户冗余信息
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "settlement.customer.info", autoDelete = "false"),
            exchange = @Exchange(value = MqConstant.CUSTOMER_MQ_FANOUT_EXCHANGE, type = ExchangeTypes.FANOUT)
    ))
    public void processCustomerInfo(Channel channel, Message message) {
        try {
            String data = new String(message.getBody(), "UTF-8");
			LOGGER.info("客户信息修改同步>>>>>" + data);
            if (!StrUtil.isBlank(data)) {
            	CustomerExtendDto customer = JSONObject.parseObject(data, CustomerExtendDto.class);
                customerAccountService.updateCustomerInfo(customer);
            }
        } catch (Exception e) {
            LOGGER.error("客户信息修改失败", e);
        }
    }
}
