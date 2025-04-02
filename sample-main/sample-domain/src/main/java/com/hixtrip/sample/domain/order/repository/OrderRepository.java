package com.hixtrip.sample.domain.order.repository;

import com.hixtrip.sample.domain.order.model.Order;

/**
 * 订单仓储
 */
public interface OrderRepository {
    /**
     * 保存订单
     *
     * @param order 订单对象
     */
    void save(Order order);

    /**
     * 更新订单
     *
     * @param order 订单对象
     */
    void update(Order order);

    /**
     * 根据订单id查询订单
     *
     * @param orderId 订单id
     * @return 订单对象
     */
    Order findById(String orderId);
}
