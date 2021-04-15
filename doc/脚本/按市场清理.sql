-- 按市场清理数据
SET @marketId = ?;
-- 删除定金账户流水
DELETE a FROM dili_settlement.customer_account_serial a INNER JOIN dili_settlement.customer_account b ON a.customer_account_id = b.id WHERE b.market_id = @marketId;
-- 删除定金账户数据
DELETE FROM dili_settlement.customer_account WHERE market_id = @marketId;
-- 删除结算单关联重试记录
DELETE a FROM dili_settlement.retry_record a INNER JOIN dili_settlement.settle_order b ON a.id = b.id WHERE b.market_id = @marketId;
-- 删除结算单关联费用项
DELETE a FROM dili_settlement.settle_fee_item a INNER JOIN dili_settlement.settle_order b ON a.settle_order_id = b.id WHERE b.market_id = @marketId;
-- 删除结算单关联链接
DELETE a FROM dili_settlement.settle_order_link a INNER JOIN dili_settlement.settle_order b ON a.settle_order_id = b.id WHERE b.market_id = @marketId;
-- 删除结算单结算明细
DELETE a FROM dili_settlement.settle_way_detail a INNER JOIN dili_settlement.settle_order b ON a.settle_order_id = b.id WHERE b.market_id = @marketId;
-- 删除结算单记录
DELETE FROM dili_settlement.settle_order WHERE market_id = @marketId;