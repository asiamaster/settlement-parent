/** 增加冲正标记字段 */
ALTER TABLE dili_settlement.settle_order ADD COLUMN `reverse` TINYINT COMMENT '是否冲正 0-否，1-是' DEFAULT 0;
/** 增加索引 */
CREATE INDEX ix_order_id ON dili_settlement.settle_way_detail(`order_id`);