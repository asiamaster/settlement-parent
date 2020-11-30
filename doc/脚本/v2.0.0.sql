use dili_settlement;
/* 调整 start*/
ALTER TABLE settle_order DROP COLUMN edit_enable;
ALTER TABLE settle_order DROP COLUMN return_url;
ALTER TABLE settle_order ADD COLUMN deduct_enable TINYINT DEFAULT 0 COMMENT '是否可抵扣';
ALTER TABLE settle_order ADD COLUMN mch_id BIGINT COMMENT '商户ID';
ALTER TABLE settle_order ADD COLUMN trailer_number VARCHAR(20) COMMENT '挂号(沈阳特有)';
ALTER TABLE settle_order MODIFY COLUMN business_type VARCHAR(120);

ALTER TABLE settle_way_detail CHANGE COLUMN order_id settle_order_id bigint;
ALTER TABLE settle_way_detail CHANGE COLUMN order_code settle_order_code VARCHAR(32);

drop table retry_error;
/* 调整 end*/

/* 建表 start*/
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
   `type`                 tinyint comment '类型 1- 缴费 2 - 抵扣',
   `fee_item_id`          bigint comment '费用项ID',
   `fee_item_name`        varchar(50) comment '费用项名称',
   `fee_type`             int comment '费用类型',
   `fee_type_name`        varchar(50) comment '费用类型名称',
   `amount`               bigint comment '金额',
   primary key (id)
)
ENGINE = InnoDB
COLLATE = utf8_general_ci;

alter table settle_fee_item comment '结算费用项';
CREATE INDEX ix_settle_order_id ON settle_fee_item(`settle_order_id`);
/* 建表 end*/