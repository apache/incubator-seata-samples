#
Order
DROP
DATABASE IF EXISTS seata_order;
CREATE
DATABASE seata_order;
CREATE TABLE seata_order.orders
(
    id               INT(11) NOT NULL AUTO_INCREMENT,
    user_id          INT(11) DEFAULT NULL,
    product_id       INT(11) DEFAULT NULL,
    pay_amount       DECIMAL(10, 0) DEFAULT NULL,
    status           VARCHAR(100)   DEFAULT NULL,
    add_time         DATETIME       DEFAULT CURRENT_TIMESTAMP,
    last_update_time DATETIME       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;
CREATE TABLE seata_order.undo_log
(
    id            BIGINT(20) NOT NULL AUTO_INCREMENT,
    branch_id     BIGINT(20) NOT NULL,
    xid           VARCHAR(100) NOT NULL,
    context       VARCHAR(128) NOT NULL,
    rollback_info LONGBLOB     NOT NULL,
    log_status    INT(11) NOT NULL,
    log_created   DATETIME     NOT NULL,
    log_modified  DATETIME     NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY ux_undo_log (xid, branch_id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8
;
#
Stock
DROP
DATABASE IF EXISTS seata_stock;
CREATE
DATABASE seata_stock;
CREATE TABLE seata_stock.product
(
    id               INT(11) NOT NULL AUTO_INCREMENT,
    price            DOUBLE   DEFAULT NULL,
    stock            INT(11) DEFAULT NULL,
    last_update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;
INSERT INTO seata_stock.product (id, price, stock)
VALUES (1, 5, 10);
CREATE TABLE seata_stock.undo_log
(
    id            BIGINT(20) NOT NULL AUTO_INCREMENT,
    branch_id     BIGINT(20) NOT NULL,
    xid           VARCHAR(100) NOT NULL,
    context       VARCHAR(128) NOT NULL,
    rollback_info LONGBLOB     NOT NULL,
    log_status    INT(11) NOT NULL,
    log_created   DATETIME     NOT NULL,
    log_modified  DATETIME     NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY ux_undo_log (xid, branch_id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

#
Pay
DROP
DATABASE IF EXISTS seata_pay;
CREATE
DATABASE seata_pay;
CREATE TABLE seata_pay.account
(
    id               INT(11) NOT NULL AUTO_INCREMENT,
    balance          DOUBLE   DEFAULT NULL,
    last_update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;
CREATE TABLE seata_pay.undo_log
(
    id            BIGINT(20) NOT NULL AUTO_INCREMENT,
    branch_id     BIGINT(20) NOT NULL,
    xid           VARCHAR(100) NOT NULL,
    context       VARCHAR(128) NOT NULL,
    rollback_info LONGBLOB     NOT NULL,
    log_status    INT(11) NOT NULL,
    log_created   DATETIME     NOT NULL,
    log_modified  DATETIME     NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY ux_undo_log (xid, branch_id)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;
INSERT INTO seata_pay.account (id, balance)
VALUES (1, 1);

SELECT auto_increment
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'seata_order'
  AND TABLE_NAME = 'undo_log'