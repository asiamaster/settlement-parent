-- 增加系统编码 统一结算 settlement
INSERT INTO `uap`.`data_dictionary_value` (`dd_code`, `firm_id`, `firm_code`, `order_number`, `name`, `code`, `description`, `state`) VALUES ('system_code', 1, 'group', 10, '统一结算系统', 'settlement', '', 1);
-- 增加消息场景 账户通知 accountNotice
INSERT INTO `uap`.`data_dictionary_value` (`dd_code`, `firm_id`, `firm_code`, `order_number`, `name`, `code`, `description`, `state`) VALUES ('message_scene', 1, 'group', 20, '账户通知', 'accountNotice', '', 1);