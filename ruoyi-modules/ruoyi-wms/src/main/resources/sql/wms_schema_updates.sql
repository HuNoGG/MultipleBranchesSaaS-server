-- 仓储管理模块 (WMS) 数据库表结构更新脚本

-- 1. 为出货记录表 (wms_shipment) 增加领用出库相关字段
ALTER TABLE `wms_shipment`
ADD COLUMN `recipient_name` varchar(100) NULL COMMENT '领用人名称' AFTER `amount`,
ADD COLUMN `reason` varchar(500) NULL COMMENT '出库原因' AFTER `recipient_name`,
ADD COLUMN `applicant_id` bigint NULL COMMENT '申请人ID' AFTER `reason`;

-- 更新出货类型的注释以包含新的“领用出库”类型
ALTER TABLE `wms_shipment`
MODIFY COLUMN `shipment_type` tinyint NOT NULL COMMENT '出货类型(1:分店调货, 2:损耗报废, 3:退货给供应商, 4:领用出库)';

-- 2. 为库存表 (wms_stock) 增加最低库存预警字段
ALTER TABLE `wms_stock`
ADD COLUMN `min_quantity` int NOT NULL DEFAULT 0 COMMENT '最低库存量' AFTER `quantity`;

-- 3. 为分店表 (hrp_stores) 增加仓库管理员关联字段
ALTER TABLE `hrp_stores`
ADD COLUMN `manager_id` bigint NULL COMMENT '仓库管理员ID (关联 sys_user 表 id)' AFTER `contact`;

-- 提示: 在执行此脚本前，请确保您的数据库中已存在 wms_shipment, wms_stock, 和 hrp_stores 表。
-- 提示: manager_id 字段应与用户表 (通常是 sys_user) 的主键相关联。