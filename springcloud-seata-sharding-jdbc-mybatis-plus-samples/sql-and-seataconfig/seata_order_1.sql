/*
SQLyog Ultimate v13.1.1 (64 bit)
MySQL - 8.0.22 : Database - seata_order_1
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE
DATABASE /*!32312 IF NOT EXISTS*/`seata_order_1` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE
`seata_order_1`;

/*Table structure for table `order_info_0` */

DROP TABLE IF EXISTS `order_info_0`;

CREATE TABLE `order_info_0`
(
    `id`          bigint NOT NULL COMMENT '订单id，采用分布式id',
    `order_name`  varchar(128) DEFAULT NULL COMMENT '订单名称',
    `product_id`  bigint       DEFAULT NULL COMMENT '购买产品id',
    `buy_num`     int          DEFAULT NULL COMMENT '购买数量',
    `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='订单表';

/*Data for the table `order_info_0` */

insert into `order_info_0`(`id`, `order_name`, `product_id`, `buy_num`, `create_time`, `update_time`)
values (1387382592208240641, '测试数据', NULL, 2, '2021-04-28 20:26:00', '2021-04-28 20:26:00');

/*Table structure for table `order_info_1` */

DROP TABLE IF EXISTS `order_info_1`;

CREATE TABLE `order_info_1`
(
    `id`          bigint NOT NULL COMMENT '订单id，采用分布式id',
    `order_name`  varchar(128) DEFAULT NULL COMMENT '订单名称',
    `product_id`  bigint       DEFAULT NULL COMMENT '购买产品id',
    `buy_num`     int          DEFAULT NULL COMMENT '购买数量',
    `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='订单表';

/*Data for the table `order_info_1` */

/*Table structure for table `order_info_2` */

DROP TABLE IF EXISTS `order_info_2`;

CREATE TABLE `order_info_2`
(
    `id`          bigint NOT NULL COMMENT '订单id，采用分布式id',
    `order_name`  varchar(128) DEFAULT NULL COMMENT '订单名称',
    `product_id`  bigint       DEFAULT NULL COMMENT '购买产品id',
    `buy_num`     int          DEFAULT NULL COMMENT '购买数量',
    `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='订单表';

/*Data for the table `order_info_2` */

insert into `order_info_2`(`id`, `order_name`, `product_id`, `buy_num`, `create_time`, `update_time`)
values (1387382407474315265, '测试数据', NULL, 2, '2021-04-28 20:25:17', '2021-04-28 20:25:17');

/*Table structure for table `undo_log` */

DROP TABLE IF EXISTS `undo_log`;

CREATE TABLE `undo_log`
(
    `id`            bigint       NOT NULL AUTO_INCREMENT COMMENT 'increment id',
    `branch_id`     bigint       NOT NULL COMMENT 'branch transaction id',
    `xid`           varchar(100) NOT NULL COMMENT 'global transaction id',
    `context`       varchar(128) NOT NULL COMMENT 'undo_log context,such as serialization',
    `rollback_info` longblob     NOT NULL COMMENT 'rollback info',
    `log_status`    int          NOT NULL COMMENT '0:normal status,1:defense status',
    `log_created`   datetime     NOT NULL COMMENT 'create datetime',
    `log_modified`  datetime     NOT NULL COMMENT 'modify datetime',
    PRIMARY KEY (`id`),
    UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=406 DEFAULT CHARSET=utf8 COMMENT='AT transaction mode undo table';

/*Data for the table `undo_log` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
