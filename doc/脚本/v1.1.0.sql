/*==============================================================*/
/* Table: settle_way_detail                                     */
/*==============================================================*/
create table settle_way_detail
(
   id                   bigint not null auto_increment,
   order_id      bigint,
   order_code    varchar(32),
   way                  tinyint,
   amount               bigint,
   serial_number        varchar(40),
   charge_date          date,
   notes                varchar(40),
   primary key (id)
)
ENGINE = InnoDB
COLLATE = utf8_general_ci;

CREATE INDEX ix_order_code ON settle_way_detail(`order_code`);

INSERT INTO `settle_config` (`group_code`, `code`, `val`, `state`, `notes`) VALUES
	(101, 6, '组合支付', 1, '初始数据(101支付方式)'),
	(101, 7, '虚拟支付', 1, '初始数据(101支付方式)');

ALTER TABLE settle_order ADD COLUMN charge_date DATE COMMENT '收款日期';