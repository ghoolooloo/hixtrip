package com.hixtrip.sample.infra;

import com.hixtrip.sample.domain.inventory.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

/**
 * infra层是domain定义的接口具体的实现
 */
@Component
public class InventoryRepositoryImpl implements InventoryRepository {
    private static final String INVENTORY_KEY_TEMPLATE = "inventory:%s";
    private static final String SELLABLE_KEY = "sellable";
    private static final String WITHHOLDING_KEY = "withholding";
    private static final String OCCUPIED_KEY = "occupied";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Integer getInventory(String skuId) {
        String inventoryKey = String.format(INVENTORY_KEY_TEMPLATE, skuId);
        return (Integer) redisTemplate.opsForHash().get(inventoryKey, SELLABLE_KEY);
    }

    @Override
    public Boolean changeInventory(String skuId, Long sellableQuantity, Long withholdingQuantity, Long occupiedQuantity) {
        String inventoryKey = String.format(INVENTORY_KEY_TEMPLATE, skuId);

        // 通过Lua 脚本实现原子性操作
        String luaScript = "local newSellable = tonumber(ARGV[1]) " +
                "local withholding = tonumber(redis.call('hget', KEYS[1], 'withholding')) " +
                "local occupied = tonumber(redis.call('hget', KEYS[1], 'occupied')) " +
                "local newWithholding = withholding + tonumber(ARGV[2]) " +
                "local newOccupied = occupied + tonumber(ARGV[3]) " +
                "if newSellable >= 0 and newWithholding >= 0 and newOccupied >= 0 then " +
                "  redis.call('hset', KEYS[1], 'sellable', newSellable) " +
                "  redis.call('hset', KEYS[1], 'withholding', newWithholding) " +
                "  redis.call('hset', KEYS[1],'occupied', newOccupied) " +
                "  return true " +
                "else " +
                "  return false " +
                "end";

        return redisTemplate.execute(new DefaultRedisScript<>(luaScript, Boolean.class),
                Collections.singletonList(inventoryKey),
                Arrays.asList(sellableQuantity, withholdingQuantity, occupiedQuantity));
    }
}
