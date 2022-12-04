

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
-- ----------------------------
-- Table structure for sys_account
-- ----------------------------
DROP TABLE IF EXISTS `sys_account`;
CREATE TABLE `sys_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `balance` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for sys_order
-- ----------------------------
DROP TABLE IF EXISTS `sys_order`;
CREATE TABLE `sys_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_number` bigint(20)  NULl ,
  `account_id` bigint(20) NOT NULL DEFAULT '-1',
  `stock_id` bigint(20) NOT NULL DEFAULT '-1',
  `quantity` bigint(20) NOT NULL DEFAULT '1',
  `amount` decimal(10,2) NOT NULL DEFAULT '0.00',
  `note` varchar(100) NULL ,
  UNIQUE KEY `ux_account_number` (`order_number`) USING BTREE,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_order
-- ----------------------------
BEGIN;
INSERT INTO `sys_order` VALUES (8, 1,1, 1, 10000, 10.00,null);
INSERT INTO `sys_order` VALUES (12, 2,1, 1, 10, 10.00,'note');
INSERT INTO `sys_order` VALUES (15, 3,1, 1, 10, 10.00,'order');
COMMIT;

-- ----------------------------
-- Table structure for sys_stock
-- ----------------------------
DROP TABLE IF EXISTS `sys_stock`;
CREATE TABLE `sys_stock` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `quantity` bigint(20) NOT NULL DEFAULT '0',
  `price` decimal(10,2) DEFAULT '0.00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_stock
-- ----------------------------
BEGIN;
INSERT INTO `sys_stock` VALUES (1, 969, 1.00);
COMMIT;

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of undo_log
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
