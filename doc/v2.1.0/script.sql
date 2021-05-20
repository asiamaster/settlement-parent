-- 删除fund_account表
DROP TABLE dili_settlement.fund_account;

-- 费用项增加类型字段并默认赋值 1
ALTER TABLE dili_settlement.settle_fee_item ADD COLUMN charge_item_type INT COMMENT '费用项类型 1-费用项,2-抵扣项';
UPDATE dili_settlement.settle_fee_item SET charge_item_type = 1;

-- 同步原有抵扣数据并删除对应字段
INSERT INTO dili_settlement.settle_fee_item(settle_order_id,settle_order_code,charge_item_id,charge_item_name,charge_item_type,fee_type,fee_name,amount)
   SELECT a.`id`,
       a.`code`,
		 (SELECT b.id FROM `dili-basic-data`.business_charge_item b WHERE b.market_id = a.market_id AND b.code = 'earnest' AND b.system_subject = 201) AS charge_item_id,
		 (SELECT b.charge_item FROM `dili-basic-data`.business_charge_item b WHERE b.market_id = a.market_id AND b.code = 'earnest' AND b.system_subject = 201) AS charge_item_name,
		 2,201,'定金',a.deduct_amount FROM dili_settlement.settle_order a WHERE a.deduct_enable = 1 AND a.deduct_amount > 0;
ALTER TABLE dili_settlement.settle_order DROP COLUMN deduct_enable;
ALTER TABLE dili_settlement.settle_order ADD COLUMN transfer_amount BIGINT COMMENT '转抵总额';
UPDATE dili_settlement.settle_order SET transfer_amount = 0;

-- 增加转抵账户字段以及转抵明细表
ALTER table dili_settlement.customer_account_serial ADD COLUMN fee_type INT COMMENT '费用类型 201-定金,203-转抵';
UPDATE dili_settlement.customer_account_serial SET fee_type = 201;
ALTER TABLE dili_settlement.customer_account
	ADD COLUMN transfer_amount BIGINT COMMENT '转抵金额',
	ADD COLUMN frozen_transfer_amount BIGINT COMMENT '转抵冻结金额';
UPDATE dili_settlement.customer_account SET transfer_amount = 0, frozen_transfer_amount = 0;

CREATE TABLE transfer_detail(
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
	settle_order_id BIGINT COMMENT '结算单ID',
	settle_order_code VARCHAR(32) COMMENT '结算单编码',
	customer_id BIGINT COMMENT '客户ID',
	customer_name VARCHAR(40) COMMENT '客户姓名',
	customer_phone VARCHAR(40) COMMENT '客户手机号',
	customer_certificate VARCHAR(40) COMMENT '客户证件号',
	amount BIGINT COMMENT '金额',
	charge_item_id BIGINT(20) COMMENT '费用项ID',
	charge_item_name VARCHAR(50) COMMENT '费用项名称'
)
ENGINE = InnoDB
COLLATE = UTF8_GENERAL_CI;
CREATE INDEX ix_settle_order_id ON transfer_detail(settle_order_id);