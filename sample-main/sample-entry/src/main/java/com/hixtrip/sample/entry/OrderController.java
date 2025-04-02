package com.hixtrip.sample.entry;

import com.hixtrip.sample.app.api.OrderService;
import com.hixtrip.sample.client.order.dto.CommandOrderCreateDTO;
import com.hixtrip.sample.client.order.dto.CommandPayDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * todo 这是你要实现的
 */
@RequiredArgsConstructor
@RestController
public class OrderController {
    private final OrderService orderService;

    /**
     * 下单
     *
     * @param commandOrderCreateDTO 入参对象
     * @return 订单id
     */
    @PostMapping(path = "/command/order/create")
    public String order(@RequestBody CommandOrderCreateDTO commandOrderCreateDTO) {
        //登录信息可以在这里模拟
        var userId = "u001"; //假设这是当前登录用户id
        if (userId.equalsIgnoreCase(commandOrderCreateDTO.getUserId())) {
            throw new RuntimeException("用户信息不匹配");
        }

        return orderService.order(commandOrderCreateDTO);
    }

    /**
     * todo 这是模拟创建订单后，支付结果的回调通知
     * 【中、高级要求】需要使用策略模式处理至少三种场景：支付成功、支付失败、重复支付(自行设计回调报文进行重复判定)
     *
     * @param commandPayDTO 入参对象
     * @return 请修改出参对象
     */
    @PostMapping(path = "/command/order/pay/callback")
    public String payCallback(@RequestBody CommandPayDTO commandPayDTO) {
        return orderService.payCallback(commandPayDTO);
    }

}
