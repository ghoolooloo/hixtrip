package com.hixtrip.sample.app.service;

import com.hixtrip.sample.app.api.OrderService;
import com.hixtrip.sample.app.convertor.CommandPayConvertor;
import com.hixtrip.sample.app.convertor.OrderConvertor;
import com.hixtrip.sample.client.order.dto.CommandOrderCreateDTO;
import com.hixtrip.sample.client.order.dto.CommandPayDTO;
import com.hixtrip.sample.domain.commodity.CommodityDomainService;
import com.hixtrip.sample.domain.inventory.InventoryDomainService;
import com.hixtrip.sample.domain.order.OrderDomainService;
import com.hixtrip.sample.domain.order.model.Order;
import com.hixtrip.sample.domain.strategy.PayStatus;
import com.hixtrip.sample.domain.strategy.PaymentCallbackContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * app层负责处理request请求，调用领域服务
 */
@Component
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final CommodityDomainService commodityDomainService;
    private final InventoryDomainService inventoryDomainService;
    private final OrderDomainService orderDomainService;
    private final PaymentCallbackContext paymentCallbackContext;

    @Override
    public String order(CommandOrderCreateDTO commandOrderCreateDTO) {
        // 计算价格
        BigDecimal skuPrice = commodityDomainService.getSkuPrice(commandOrderCreateDTO.getSkuId());
        BigDecimal totalPrice = skuPrice.multiply(new BigDecimal(commandOrderCreateDTO.getAmount()));

        // 构建订单
        Order order = OrderConvertor.INSTANCE.commandOrderCreateDTOToOrder(commandOrderCreateDTO);
        order.setId(UUID.randomUUID().toString()); // 这里简单使用UUID作为订单ID
        order.setMoney(totalPrice);
        order.setPayTime(LocalDateTime.now());
        order.setPayStatus(PayStatus.UNPAID.getName());
        order.setCreateBy(commandOrderCreateDTO.getUserId());
        order.setDelFlag(0L);

        // 检查库存
        Integer sellableQuantity = inventoryDomainService.getInventory(commandOrderCreateDTO.getSkuId());
        if (sellableQuantity < commandOrderCreateDTO.getAmount()) {
            throw new RuntimeException("库存不足");
        }

        // 预占库存
        if (!inventoryDomainService.changeInventory(order.getSkuId(),
                sellableQuantity.longValue() - order.getAmount(),
                order.getAmount().longValue(), 0L)) {
            throw new RuntimeException("预占库存失败");
        }

        // 保存订单
        orderDomainService.createOrder(order);

        return order.getId();
    }

    @Override
    public String payCallback(CommandPayDTO commandPayDTO) {
        return paymentCallbackContext.handlePaymentCallback(
                CommandPayConvertor.INSTANCE.commandPayDTOToCommandPay(commandPayDTO),
                PayStatus.fromName(commandPayDTO.getPayStatus()));
    }
}
