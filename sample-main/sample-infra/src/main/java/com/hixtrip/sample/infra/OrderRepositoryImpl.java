package com.hixtrip.sample.infra;

import com.hixtrip.sample.domain.order.model.Order;
import com.hixtrip.sample.domain.order.repository.OrderRepository;
import com.hixtrip.sample.infra.db.convertor.OrderDOConvertor;
import com.hixtrip.sample.infra.db.dataobject.OrderDO;
import com.hixtrip.sample.infra.db.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 这里简化处理，实际开发中需要根据业务场景进行分库分表处理
 */
@Component
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
    private final OrderMapper orderMapper;
    @Override
    public void save(Order order) {
        orderMapper.insert(OrderDOConvertor.INSTANCE.orderToOrderDO(order));
    }

    @Override
    public void update(Order order) {
        orderMapper.updateById(OrderDOConvertor.INSTANCE.orderToOrderDO(order));
    }

    @Override
    public Order findById(String orderId) {
        OrderDO orderDO = orderMapper.selectById(orderId);
        return OrderDOConvertor.INSTANCE.orderDOToOrder(orderDO);
    }
}
