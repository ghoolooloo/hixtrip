-- 订单表只保存近期有效数据（例如3个月以内，且del_flag=0），按user_id进行分片（例如10个库或表：orders_0 ~ orders_9）存储到关系数据库（例如MySQL）中，保证同一个用户的数据总是在同一个分片中。
-- 同时，将所有订单数据从订单表中定期同步到搜索引擎或数据仓库中（可以添加一些额外字段，如买家姓名等）。删除的订单（def_flag=1），直接存储到搜索引擎或数据仓库中，不经过订单表。
CREATE TABLE `orders` (
  `id` char(36) PRIMARY KEY NOT NULL COMMENT '订单号',
  `user_id` char(36) NOT NULL COMMENT '购买人',
  `sku_id` char(36) NOT NULL COMMENT 'SkuId',
  `amount` int(11) NOT NULL COMMENT '购买数量',
  `money` decimal(10, 2) NOT NULL COMMENT '购买金额',
  `pay_time` datetime NOT NULL COMMENT '购买时间',
  `pay_status` varchar(20) NOT NULL COMMENT '支付状态',
  `create_by` varchar(50) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(50) NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  INDEX `idx_user_id_orders` (`user_id`),
  INDEX `idx_sku_id_orders` (`sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 优化买家查询
CREATE INDEX `idx_buyer_orders` ON `orders` (`user_id`, `pay_status`, `pay_time` DESC);
-- 优化卖家查询
CREATE INDEX `idx_seller_orders` ON `orders` (`sku_id`, `pay_status`, `pay_time` DESC);
-- 平台客服搜索客诉订单和平台运营进行订单数据分析，直接在搜索引擎或数据仓库中进行。