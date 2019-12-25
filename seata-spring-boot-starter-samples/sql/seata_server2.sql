/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 100133
Source Host           : 127.0.0.1:3306
Source Database       : seata_server2

Target Server Type    : MYSQL
Target Server Version : 100133
File Encoding         : 65001

Date: 2019-12-25 10:06:03
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for seata
-- ----------------------------
DROP TABLE IF EXISTS `seata`;
CREATE TABLE `seata` (
  `id` varchar(64) NOT NULL,
  `sid` varchar(255) DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of seata
-- ----------------------------
INSERT INTO `seata` VALUES ('', 'soul', '99999');

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
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of undo_log
-- ----------------------------
