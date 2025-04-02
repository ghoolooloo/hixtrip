package com.hixtrip.sample.domain.strategy;

import com.hixtrip.sample.domain.order.OrderDomainService;
import com.hixtrip.sample.domain.pay.PayDomainService;
import com.hixtrip.sample.domain.pay.model.CommandPay;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 支付重复策略
 */
@Component
@RequiredArgsConstructor
public class PaymentDuplicateStrategy implements PaymentCallbackStrategy {
    private final OrderDomainService orderDomainService;
    private final PayDomainService payDomainService;

    @Override
    public String handlePaymentCallback(CommandPay commandPay) {
        orderDomainService.orderPayDuplicate(commandPay);
        payDomainService.payRecord(commandPay);
        return commandPay.getOrderId() + "订单支付重复";
    }
}
