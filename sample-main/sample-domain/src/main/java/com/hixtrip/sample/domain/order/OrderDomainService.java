package com.hixtrip.sample.domain.order;

import com.hixtrip.sample.domain.order.model.Order;
import com.hixtrip.sample.domain.order.repository.OrderRepository;
import com.hixtrip.sample.domain.pay.model.CommandPay;
import com.hixtrip.sample.domain.strategy.PayStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 订单领域服务
 */
@Component
@RequiredArgsConstructor
public class OrderDomainService {
    private final OrderRepository orderRepository;

    /**
     * 创建待付款订单
     */
    public void createOrder(Order order) {
        orderRepository.save(order);
    }

    /**
     * 待付款订单支付成功
     */
    public void orderPaySuccess(CommandPay commandPay) {
        Order order = orderRepository.findById(commandPay.getOrderId());
        assert order != null : commandPay.getOrderId() + "订单不存在";
        order.paySuccess();
        orderRepository.update(order);
    }

    /**
     * 待付款订单支付失败
     */
    public void orderPayFail(CommandPay commandPay) {
        Order order = orderRepository.findById(commandPay.getOrderId());
        assert order != null : commandPay.getOrderId() + "订单不存在";
        order.payFailed();
        orderRepository.update(order);
    }

    /**
     * 待付款订单重复支付
     */
    public void orderPayDuplicate(CommandPay commandPay) {
        Order order = orderRepository.findById(commandPay.getOrderId());
        assert order != null : commandPay.getOrderId() + "订单不存在";
        // 如果订单状态不是已支付，则更新订单状态为已支付
        if (!PayStatus.PAID.getName().equals(order.getPayStatus())) {
            order.paySuccess();
            orderRepository.update(order);
        }
    }
}
