package com.hixtrip.sample.domain.strategy;

import lombok.Getter;

/**
 * 支付状态
 */
@Getter
public enum PayStatus {
    UNPAID("未支付"),
    PAID("已支付"),
    PAY_FAIL("支付失败"),
    PAY_DUPLICATE("重复支付");

    private final String name;

    PayStatus(String name) {
        this.name = name;
    }

    /**
     * 根据 name 值返回对应的枚举量
     *
     * @param name 枚举量的名称
     * @return 对应的枚举量
     * @throws IllegalArgumentException 如果找不到对应的枚举量
     */
    public static PayStatus fromName(String name) {
        for (PayStatus status : PayStatus.values()) {
            if (status.getName().equals(name)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown PayStatus name: " + name);
    }
}
