package com.hixtrip.sample.domain.inventory;

import com.hixtrip.sample.domain.inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 库存领域服务
 * 库存设计，忽略仓库、库存品、计量单位等业务
 */
@Component
@RequiredArgsConstructor
public class InventoryDomainService {
    private final InventoryRepository inventoryRepository;

    /**
     * 获取skuId当前库存
     *
     * @param skuId 商品规格id
     * @return 当前库存数量
     */
    public Integer getInventory(String skuId) {
        return inventoryRepository.getInventory(skuId);
    }

    /**
     * 修改库存
     *
     * @param skuId 商品规格id
     * @param sellableQuantity    可售库存
     * @param withholdingQuantity 预占库存
     * @param occupiedQuantity    占用库存
     * @return true:修改成功 false:修改失败
     */
    public Boolean changeInventory(String skuId, Long sellableQuantity, Long withholdingQuantity, Long occupiedQuantity) {
        return inventoryRepository.changeInventory(skuId, sellableQuantity, withholdingQuantity, occupiedQuantity);
    }
}
