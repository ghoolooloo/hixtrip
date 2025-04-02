package com.hixtrip.sample.domain.strategy;

import com.hixtrip.sample.domain.pay.model.CommandPay;

/**
 * 支付回调策略接口
 */
public interface PaymentCallbackStrategy {
    /**
     * 处理支付回调
     * @param commandPay 支付回调信息
     * @return 处理结果
     */
    String handlePaymentCallback(CommandPay commandPay);
}
