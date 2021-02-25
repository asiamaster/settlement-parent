use dili_settlement;
/* 建表 start*/
drop table if exists retry_record;
create table retry_record
(
   id                   bigint not NULL COMMENT '直接存储结算单ID',
   create_time          datetime default CURRENT_TIMESTAMP,
   primary key (id)
)
ENGINE = InnoDB
COLLATE = utf8_general_ci;

alter table retry_record comment '保存记录以供任务扫描';

drop table if exists settle_order_link;
create table settle_order_link
(
   `id`                   bigint not null auto_increment comment '主键ID',
   `settle_order_id`      bigint comment '结算单ID',
   `type`                 tinyint comment '类型',
   `url`                  varchar(255) comment '路径',
   primary key (id)
)
ENGINE = InnoDB
COLLATE = utf8_general_ci;

alter table settle_order_link COMMENT '结算单链接表';
CREATE unique index ix_unique_link ON settle_order_link(settle_order_id, `type`);

drop table if exists settle_fee_item;

create table settle_fee_item
(
   `id`                   bigint not null auto_increment comment '主键ID',
   `settle_order_id`      bigint comment '结算单ID',
   `settle_order_code`    varchar(32) comment '结算单编码',
   `charge_item_id`       bigint comment '收费项ID',
   `charge_item_name`     varchar(50) comment '收费项名称',
   `fee_type`             int comment '费用类型',
   `fee_name`             varchar(50) comment '费用名称',
   `amount`               bigint comment '金额',
   primary key (id)
)
ENGINE = InnoDB
COLLATE = utf8_general_ci;

alter table settle_fee_item comment '结算费用项';
CREATE INDEX ix_settle_order_id ON settle_fee_item(`settle_order_id`);

drop table if exists customer_account;
create table customer_account
(
   `id`                   bigint not null auto_increment comment '主键ID',
   `market_id`            bigint comment '市场ID',
   `market_code`          varchar(20) comment '市场编码',
   `mch_id`               bigint comment '商户ID',
   `mch_name`             varchar(20) comment '商户名称',
   `customer_id`          bigint comment '客户ID',
   `customer_name`        varchar(40) comment '客户姓名',
   `customer_phone`       varchar(40) comment '客户手机号',
   `customer_certificate` varchar(40) comment '客户证件号',
   `amount`               bigint comment '金额',
   `frozen_amount`        bigint comment '冻结金额',
   `version`              int default 1,
   primary key (id)
)
ENGINE = InnoDB
COLLATE = utf8_general_ci;

alter table customer_account comment '客户资金表';
CREATE UNIQUE INDEX ix_mch_customer_id ON customer_account(mch_id, customer_id);

drop table if exists customer_account_serial;
create table customer_account_serial
(
   `id`                   bigint not null auto_increment comment '主键ID',
   `customer_account_id`  bigint comment '客户资金ID',
   `action`               tinyint comment '资金动作 1-入账 2-出账',
   `scene`                tinyint comment '场景 1-缴费...',
   `amount`               bigint comment '金额',
   `operate_time`         datetime comment '时间',
   `operator_id`          bigint comment '操作员ID',
   `operator_name`        varchar(40) comment '操作员姓名',
   `relation_code`        varchar(32) comment '关联单号',
   `relation_type`        tinyint comment '关联类型',
   `notes`                varchar(200) comment '备注',
   primary key (id)
)
ENGINE = InnoDB
COLLATE = utf8_general_ci;

alter table customer_account_serial comment '客户资金流水';
CREATE INDEX ix_customer_account_id ON customer_account_serial(customer_account_id);
/* 建表 end*/

/* 调整 start*/

-- 同步缴费单详情URL
INSERT INTO settle_order_link(`settle_order_id`, `type`, `url`)
	SELECT
	temp.id,
	1,
	case temp.business_type
		when 1 then
			CONCAT('https://ia.diligrp.com/leaseOrder/view.action?orderCode=', temp.order_code, '&businessType=', temp.business_type)
		when 2 then
			CONCAT('https://ia.diligrp.com/earnestOrder/view.action?orderCode=', temp.order_code, '&businessType=', temp.business_type)
		when 3 then
		   CONCAT('https://ia.diligrp.com/depositOrder/view.action?orderCode=', temp.order_code, '&businessType=', temp.business_type)
		end
    FROM  settle_order temp WHERE temp.`type` = 1 AND temp.business_type IN (1,2,3) AND temp.reverse = 0;

-- 同步退款单详情URL
INSERT INTO settle_order_link(`settle_order_id`, `type`, `url`)
	SELECT
	temp.id,
	1,
	case temp.business_type
		when 1 then
			CONCAT('https://ia.diligrp.com/refundOrder/view.action?orderCode=', temp.order_code, '&businessType=', temp.business_type)
		when 2 then
			CONCAT('https://ia.diligrp.com/refundOrder/view.action?orderCode=', temp.order_code, '&businessType=', temp.business_type)
		when 3 then
		   CONCAT('https://ia.diligrp.com/refundOrder/view.action?orderCode=', temp.order_code, '&businessType=', temp.business_type)
		end
    FROM  settle_order temp WHERE temp.`type` = 2 AND temp.business_type IN (1,2,3) AND temp.reverse = 0;

-- 同步缴费单打印数据URL
INSERT INTO settle_order_link(`settle_order_id`, `type`, `url`)
	SELECT
	temp.id,
	2,
	case temp.business_type
		when 1 then
			CONCAT('https://ia.diligrp.com/api/leaseOrder/queryPrintData?orderCode=', temp.order_code, '&businessType=', temp.business_type)
		when 2 then
			CONCAT('https://ia.diligrp.com/api/earnestOrder/queryPrintData?orderCode=', temp.order_code, '&businessType=', temp.business_type)
		when 3 then
		   CONCAT('https://ia.diligrp.com/api/depositOrder/queryPrintData?orderCode=', temp.order_code, '&businessType=', temp.business_type)
		end
    FROM  settle_order temp WHERE temp.`type` = 1 AND temp.business_type IN (1,2,3) AND temp.reverse = 0;

-- 同步退款单打印数据URL
INSERT INTO settle_order_link(`settle_order_id`, `type`, `url`)
	SELECT
	temp.id,
	2,
	case temp.business_type
		when 1 then
			CONCAT('https://ia.diligrp.com/api/refundOrder/queryPrintData?orderCode=', temp.order_code, '&businessType=', temp.business_type)
		when 2 then
			CONCAT('https://ia.diligrp.com/api/refundOrder/queryPrintData?orderCode=', temp.order_code, '&businessType=', temp.business_type)
		when 3 then
		   CONCAT('https://ia.diligrp.com/api/refundOrder/queryPrintData?orderCode=', temp.order_code, '&businessType=', temp.business_type)
		end
    FROM  settle_order temp WHERE temp.`type` = 2 AND temp.business_type IN (1,2,3) AND temp.reverse = 0;

-- 同步结算回调URL
INSERT INTO settle_order_link(`settle_order_id`, `type`, `url`)
	SELECT
	temp.id,
	3,
	temp.return_url
    FROM  settle_order temp WHERE temp.business_type IN (1,2,3) AND temp.reverse = 0;

ALTER TABLE settle_order DROP COLUMN edit_enable;
ALTER TABLE settle_order DROP COLUMN return_url;
ALTER TABLE settle_order ADD COLUMN market_code VARCHAR(20) COMMENT '市场编码';
ALTER TABLE settle_order ADD COLUMN deduct_enable TINYINT DEFAULT 0 COMMENT '是否可抵扣';
ALTER TABLE settle_order ADD COLUMN trailer_number VARCHAR(20) COMMENT '挂号(沈阳特有)';
ALTER TABLE settle_order ADD COLUMN mch_id bigint COMMENT '商户ID';
ALTER TABLE settle_order ADD COLUMN mch_name VARCHAR(20) COMMENT '商户名称';
ALTER TABLE settle_order ADD COLUMN customer_certificate VARCHAR(40) COMMENT '客户证件号';
ALTER TABLE settle_order ADD COLUMN deduct_amount BIGINT DEFAULT 0 COMMENT '抵扣金额';
ALTER TABLE settle_order ADD COLUMN hold_name VARCHAR(40) COMMENT '持卡人';
ALTER TABLE settle_order ADD COLUMN hold_certificate_number VARCHAR(40) COMMENT '持卡人证件号';
ALTER TABLE settle_order ADD COLUMN hold_contacts_phone VARCHAR(40) COMMENT '持卡人电话';
ALTER TABLE settle_order ADD COLUMN trade_customer_code VARCHAR(20) COMMENT '交易客户编号';
ALTER TABLE settle_order MODIFY COLUMN business_type VARCHAR(120);
ALTER TABLE settle_order MODIFY COLUMN serial_number VARCHAR(100);

CREATE INDEX ix_settle_code ON settle_order(`code`);

ALTER TABLE settle_way_detail CHANGE COLUMN order_id settle_order_id bigint;
ALTER TABLE settle_way_detail CHANGE COLUMN order_code settle_order_code VARCHAR(32);

drop table retry_error;
drop table application_config;
drop table market_application;

UPDATE settle_order SET mch_id = 8,mch_name = '寿光',market_code = 'sg' WHERE market_id = 8;
UPDATE settle_order SET mch_id = 11,mch_name = '杭州水产',market_code = 'hzsc' WHERE market_id = 11;
UPDATE settle_order SET mch_id = 17,mch_name = '革新',market_code = 'gx' WHERE market_id = 17;

UPDATE settle_config SET `state` = 2 WHERE `group_code` = 101 AND `code` = 6;

-- 定金数据，处理已结算的单子
UPDATE dili_settlement.settle_order se INNER JOIN (
   SELECT
	po.settlement_code,
	alo.earnest_deduction
FROM
	dili_ia.`assets_lease_order` alo
	LEFT JOIN dili_ia.payment_order po ON alo.id = po.business_id
WHERE
	po.id IN (
	SELECT
		MIN( po.id )
	FROM
		dili_ia.`assets_lease_order` alo
		LEFT JOIN dili_ia.payment_order po ON alo.id = po.business_id
	WHERE
		alo.assets_type = 1
		AND po.state != 3
		AND ( alo.earnest_deduction > 0 OR alo.transfer_deduction > 0 )
	GROUP BY
	alo.id
	)
) temp ON se.code = temp.settlement_code AND se.state = 2
  SET se.amount = se.amount + temp.earnest_deduction, se.deduct_amount = temp.earnest_deduction,se.deduct_enable = 1;

-- 定金数据，处理待处理的单子
UPDATE dili_settlement.settle_order se INNER JOIN (
   SELECT
	po.settlement_code,
	alo.earnest_deduction
FROM
	dili_ia.`assets_lease_order` alo
	LEFT JOIN dili_ia.payment_order po ON alo.id = po.business_id
WHERE
	po.id IN (
	SELECT
		MIN( po.id )
	FROM
		dili_ia.`assets_lease_order` alo
		LEFT JOIN dili_ia.payment_order po ON alo.id = po.business_id
	WHERE
		alo.assets_type = 1
		AND po.state != 3
		AND ( alo.earnest_deduction > 0 OR alo.transfer_deduction > 0 )
	GROUP BY
	alo.id
	)
) temp ON se.code = temp.settlement_code AND se.state = 1
  SET se.amount = se.amount + temp.earnest_deduction, se.deduct_enable = 1;
/* 调整 end*/

-- 增加结算日志业务类型
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`, `order_number`, `name`, `code`, `description`, `state`) VALUES ('log_business_type', 12, '结算', 'settlement', NULL, 1);
