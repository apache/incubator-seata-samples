/*
 Navicat Premium Data Transfer

 Source Server         : mysql_188_master
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Host           : 127.0.0.1:3307
 Source Schema         : seata-test-service-a

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 29/04/2021 14:55:25
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user_order
-- ----------------------------
DROP TABLE IF EXISTS `user_order`;
CREATE TABLE `user_order`
(
    `id`       bigint(255) NOT NULL,
    `uid`      bigint(255) NULL DEFAULT NULL,
    `order_id` bigint(20) NULL DEFAULT NULL,
    `p_id`     bigint(20) NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET
FOREIGN_KEY_CHECKS = 1;
