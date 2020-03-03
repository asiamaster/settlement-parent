
/** ------- v1.0.0 ------- */
/**
 * 2020-03-03
 * 已执行:
 */
/** 接入应用表 */
CREATE TABLE `market_application` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`market_id` BIGINT(20) NULL DEFAULT NULL,
	`app_id` BIGINT(20) NULL DEFAULT NULL,
	`name` VARCHAR(40) NULL DEFAULT NULL,
	`state` TINYINT(4) NULL DEFAULT NULL COMMENT '状态   1 启用  2  禁用',
	`create_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`)
)
COMMENT='接入应用表'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;
CREATE unique index ix_market_app ON market_application(`market_id`,`app_id`);

/** 应用配置表 */
CREATE TABLE `application_config` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`app_id` BIGINT(20) NULL DEFAULT NULL,
	`group_code` INT(11) NULL DEFAULT NULL,
	`code` TINYINT(4) NULL DEFAULT NULL,
	`val` VARCHAR(40) NULL DEFAULT NULL,
	`state` TINYINT(4) NULL DEFAULT NULL,
	`notes` VARCHAR(40) NULL DEFAULT NULL,
	PRIMARY KEY (`id`)
)
COMMENT='接入应用配置'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;
CREATE unique index ix_app_group_code ON application_config(`app_id`,`group_code`,`code`);

/** 资金账户表 */
 CREATE TABLE `fund_account` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`market_id` BIGINT(20) NULL DEFAULT NULL COMMENT '市场ID',
	`app_id` BIGINT(20) NULL DEFAULT NULL COMMENT '应用ID',
	`amount` BIGINT(20) NULL DEFAULT NULL COMMENT '总金额',
	`version` INT(11) NULL DEFAULT '1',
	PRIMARY KEY (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;
CREATE unique index ix_market_app ON fund_account(`market_id`,`app_id`);

/** 结算配置表 */
CREATE TABLE `settle_config` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`group_code` INT(11) NULL DEFAULT NULL,
	`code` TINYINT(4) NULL DEFAULT NULL,
	`val` VARCHAR(40) NULL DEFAULT NULL,
	`state` TINYINT(4) NULL DEFAULT NULL,
	`notes` VARCHAR(40) NULL DEFAULT NULL,
	PRIMARY KEY (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;
CREATE unique index ix_group_code ON settle_config(`group_code`,`code`);


/** 结算单表 */
CREATE TABLE `settle_order` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`market_id` BIGINT(20) NULL DEFAULT NULL COMMENT '市场ID',
	`app_id` BIGINT(20) NULL DEFAULT NULL COMMENT '应用ID',
	`code` VARCHAR(32) NULL DEFAULT NULL COMMENT '结算单号',
	`business_code` VARCHAR(32) NULL DEFAULT NULL COMMENT '业务单号',
	`business_type` TINYINT(4) NULL DEFAULT NULL,
	`business_dep_id` BIGINT(20) NULL DEFAULT NULL,
	`business_dep_name` VARCHAR(40) NULL DEFAULT NULL,
	`customer_id` BIGINT(20) NULL DEFAULT NULL,
	`customer_name` VARCHAR(40) NULL DEFAULT NULL,
	`customer_phone` VARCHAR(40) NULL DEFAULT NULL,
	`amount` BIGINT(20) NULL DEFAULT NULL,
	`submitter_id` BIGINT(20) NULL DEFAULT NULL,
	`submitter_name` VARCHAR(40) NULL DEFAULT NULL,
	`submitter_dep_id` BIGINT(20) NULL DEFAULT NULL,
	`submitter_dep_name` VARCHAR(40) NULL DEFAULT NULL,
	`submit_time` DATETIME NULL DEFAULT NULL,
	`type` TINYINT(4) NULL DEFAULT NULL COMMENT '1 交款 2 退款',
	`way` TINYINT(4) NULL DEFAULT NULL,
	`state` TINYINT(4) NULL DEFAULT NULL COMMENT '1 待处理 2 已处理 3 已取消',
	`operator_id` BIGINT(20) NULL DEFAULT NULL,
	`operator_name` VARCHAR(40) NULL DEFAULT NULL,
	`operate_time` DATETIME NULL DEFAULT NULL,
	`account_number` VARCHAR(20) NULL DEFAULT NULL,
	`bank_name` VARCHAR(40) NULL DEFAULT NULL,
	`bank_card_holder` VARCHAR(40) NULL DEFAULT NULL,
	`serial_number` VARCHAR(40) NULL DEFAULT NULL,
	`edit_enable` TINYINT(4) NULL DEFAULT NULL COMMENT '是否可编辑 1 是 2 否',
	`notes` VARCHAR(40) NULL DEFAULT NULL,
	`return_url` VARCHAR(255) NULL DEFAULT NULL,
	`version` INT(11) NULL DEFAULT '1',
	PRIMARY KEY (`id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;

CREATE unique index ix_app_business ON settle_order(`app_id`,`business_code`);
CREATE INDEX ix_customer ON settle_order(`customer_id`);

-- 初始化表数据,注意配置配置各字段值
INSERT INTO market_application(`market_id`,`app_id`,`name`,`state`) VALUES(11,101,'智能档位',1);

INSERT INTO fund_account(`market_id`,`app_id`,`amount`) VALUES(11,101,0);

INSERT INTO `settle_config` (`group_code`, `code`, `val`, `state`, `notes`) VALUES
	(101, 1, '现金', 1, '初始数据(101支付方式)'),
	(101, 2, 'POS', 1, '初始数据(101支付方式)'),
	(101, 3, '银行卡', 1, '初始数据(101支付方式)'),
	(101, 4, '支付宝', 1, '初始数据(101支付方式)'),
	(101, 5, '微信', 1, '初始数据(101支付方式)'),
	(102, 1, '现金', 1, '初始数据(102退款方式)'),
	(102, 3, '银行卡', 1, '初始数据(102退款方式)');

INSERT INTO `application_config` (`app_id`, `group_code`, `code`, `val`, `state`, `notes`) VALUES
	(101, 101, 1, '摊位租赁', 1, 'group_code(101)表示业务类型'),
	(101, 101, 2, '订金', 1, 'group_code(101)表示业务类型'),
	(101, 102, 1, '', 1, 'group_code(102)表示业务详情URL'),
	(101, 102, 2, '', 1, 'group_code(102)表示业务详情URL'),
	(101, 103, 1, '', 1, 'group_code(103)表示业务打印数据URL'),
	(101, 103, 2, '', 1, 'group_code(103)表示业务打印数据URL'),
	(101, 201, 1, 'qaz@wsx', 1, 'group_code(201)表示回调签名KEY');