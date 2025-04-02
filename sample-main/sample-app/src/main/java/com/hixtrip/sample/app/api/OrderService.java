package com.hixtrip.sample.app.api;

import com.hixtrip.sample.client.order.dto.CommandOrderCreateDTO;
import com.hixtrip.sample.client.order.dto.CommandPayDTO;

/**
 * 订单的service层
 */
public interface OrderService {


    /**
     * 下订单
     *
     * @param commandOrderCreateDTO 下单数据
     * @return 订单id
     */
    String order(CommandOrderCreateDTO commandOrderCreateDTO);

    /**
     * 支付回调
     *
     * @param commandPayDTO 支付回调数据
     * @return 支付结果
     */
    String payCallback(CommandPayDTO commandPayDTO);
}
