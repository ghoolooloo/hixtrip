package com.hixtrip.sample.domain.strategy;

import com.hixtrip.sample.domain.pay.model.CommandPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付回调上下文
 */
@Component
public class PaymentCallbackContext {
    private final Map<PayStatus, PaymentCallbackStrategy> strategyMap = new HashMap<>();

    @Autowired
    public PaymentCallbackContext(PaymentSuccessStrategy paymentSuccessStrategy,
                                  PaymentFailureStrategy paymentFailureStrategy,
                                  PaymentDuplicateStrategy paymentDuplicateStrategy) {
        strategyMap.put(PayStatus.PAID, paymentSuccessStrategy);
        strategyMap.put(PayStatus.PAY_FAIL, paymentFailureStrategy);
        strategyMap.put(PayStatus.PAY_DUPLICATE, paymentDuplicateStrategy);
    }

    /**
     * 根据支付状态选择对应的策略
     *
     * @param commandPay 支付信息
     * @param status 支付状态
     * @return 处理结果
     */
    public String handlePaymentCallback(CommandPay commandPay, PayStatus status) {
        PaymentCallbackStrategy strategy = strategyMap.get(status);
        if (strategy != null) {
            return strategy.handlePaymentCallback(commandPay);
        } else {
            throw new IllegalArgumentException("未知的支付状态: " + status.getName());
        }
    }
}
