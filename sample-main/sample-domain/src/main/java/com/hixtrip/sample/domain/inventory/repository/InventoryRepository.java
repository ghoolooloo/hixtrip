package com.hixtrip.sample.domain.inventory.repository;

/**
 * 库存仓储
 */
public interface InventoryRepository {
    /**
     * 获取skuId当前库存
     *
     * @param skuId 商品规格id
     * @return 当前库存数量
     */
    Integer getInventory(String skuId);

    /**
     * 修改库存
     *
     * @param skuId 商品规格id
     * @param sellableQuantity    可售库存
     * @param withholdingQuantity 预占库存
     * @param occupiedQuantity    占用库存
     * @return true:修改成功 false:修改失败
     */
    Boolean changeInventory(String skuId, Long sellableQuantity, Long withholdingQuantity, Long occupiedQuantity);
}
