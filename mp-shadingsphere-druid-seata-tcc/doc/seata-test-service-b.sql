/*
 Navicat Premium Data Transfer

 Source Server         : mysql_188_master
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Host           : 127.0.0.1:3307
 Source Schema         : seata-test-service-b

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 29/04/2021 14:55:31
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for company_product
-- ----------------------------
DROP TABLE IF EXISTS `company_product`;
CREATE TABLE `company_product`
(
    `id`           int(11) NOT NULL,
    `product_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    `account`      bigint(255) NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of company_product
-- ----------------------------
INSERT INTO `company_product`
VALUES (1, '测试', 100);

SET
FOREIGN_KEY_CHECKS = 1;
