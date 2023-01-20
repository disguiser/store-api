use test;
DROP TABLE IF EXISTS `bill`;
CREATE TABLE `bill` (
  `id` int NOT NULL AUTO_INCREMENT,
  `customer_id` int DEFAULT NULL,
  `amount` int DEFAULT NULL COMMENT '金额',
  `payment_channel` tinyint(1) DEFAULT NULL COMMENT '付款方式',
  `date` date DEFAULT NULL COMMENT '付款日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='批发用户账单';

DROP TABLE IF EXISTS `charge_record`;
CREATE TABLE `charge_record` (
  `id` int NOT NULL AUTO_INCREMENT,
  `vip_id` int DEFAULT NULL,
  `charge_amount` int DEFAULT NULL COMMENT '充值金额',
  `give_amount` int DEFAULT '0' COMMENT '赠送金额',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `creator` int DEFAULT NULL COMMENT '创建人,同user_id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='充值记录表';

DROP TABLE IF EXISTS `color`;
CREATE TABLE `color` (
  `id` int NOT NULL AUTO_INCREMENT,
  `item_name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `item_name_UNIQUE` (`item_name`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='所有颜色';

DROP TABLE IF EXISTS `consume_record`;
CREATE TABLE `consume_record` (
  `id` int NOT NULL AUTO_INCREMENT,
  `vip_id` int DEFAULT NULL,
  `consume_amount` int DEFAULT NULL COMMENT '消费金额',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `creator` int DEFAULT NULL COMMENT '创建人,同user_id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='消费记录表';

DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `mobile` varchar(50) DEFAULT NULL COMMENT '手机号',
  `address` json DEFAULT NULL COMMENT '地址',
  `address_detail` varchar(255) DEFAULT NULL,
  `open_id` varchar(255) DEFAULT NULL COMMENT '微信openid',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `debt` int DEFAULT '0' COMMENT '欠款',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='批发客户表';

DROP TABLE IF EXISTS `dept`;
CREATE TABLE `dept` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `stock_count` int DEFAULT NULL,
  `goods_count` int DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `modify_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='门店表';

DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods` (
  `id` int NOT NULL AUTO_INCREMENT,
  `sku` varchar(50) NOT NULL COMMENT '编码',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `size_group` int DEFAULT NULL COMMENT '尺码组',
  `img_url` varchar(145) DEFAULT NULL COMMENT '图片地址',
  `sale_price` int NOT NULL COMMENT '销售单价',
  `cost_price` int DEFAULT NULL COMMENT '成本单价',
  `pre_sku` varchar(50) DEFAULT NULL COMMENT '上家货号',
  `discount` int DEFAULT NULL COMMENT '折扣',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `input_user` int DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0' COMMENT '删除标志 0：未删除 1已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sku_UNIQUE` (`sku`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=195 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品表';

DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `id` int NOT NULL AUTO_INCREMENT,
  `total` int DEFAULT NULL COMMENT '总计',
  `total_money` int DEFAULT NULL COMMENT '总计金额',
  `order_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
  `buyer` int DEFAULT NULL COMMENT '客户表id',
  `input_user` int DEFAULT NULL COMMENT '录入员，user主键',
  `deleted` tinyint DEFAULT '0',
  `dept_id` varchar(45) DEFAULT NULL COMMENT '门店id',
  `category` tinyint DEFAULT NULL COMMENT '1:批发\n2:零售',
  `payment_status` tinyint DEFAULT '1' COMMENT '0:赊账\n1:结清',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=658 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单表';

DROP TABLE IF EXISTS `order_goods`;
CREATE TABLE `order_goods` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` int NOT NULL COMMENT '订单id',
  `stock_id` int DEFAULT NULL COMMENT '商品库存id',
  `amount` int DEFAULT NULL COMMENT '数量',
  `sale_price` int DEFAULT NULL COMMENT '销售单价',
  `subtotal_money` int DEFAULT NULL COMMENT '金额小计',
  `status` tinyint(1) DEFAULT NULL,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1590 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单商品明细表';

DROP TABLE IF EXISTS `print_template`;
CREATE TABLE `print_template` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `data` json DEFAULT NULL,
  `width` int DEFAULT NULL,
  `height` int DEFAULT NULL,
  `modify_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `purchase`;
CREATE TABLE `purchase` (
  `id` int NOT NULL AUTO_INCREMENT,
  `stock_id` int DEFAULT NULL,
  `purchase_amount` int DEFAULT NULL COMMENT '进货数量',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '录入时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='进货记录表';

DROP TABLE IF EXISTS `sale`;
CREATE TABLE `sale` (
  `id` int NOT NULL AUTO_INCREMENT,
  `goods_id` int DEFAULT NULL COMMENT '商品id',
  `sale_amount` int DEFAULT NULL COMMENT '销售金额',
  `sale_count` int DEFAULT NULL COMMENT '销售数量',
  `sale_date` date NOT NULL COMMENT '销售日期',
  `sale_user_id` int DEFAULT NULL COMMENT '销售人员',
  `dept_id` int DEFAULT NULL COMMENT '所属部门id',
  `remake` varchar(100) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='销售记录表';

DROP TABLE IF EXISTS `size`;
CREATE TABLE `size` (
  `id` int NOT NULL AUTO_INCREMENT,
  `item_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `item_name_UNIQUE` (`item_name`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='所有尺码';

DROP TABLE IF EXISTS `size_group`;
CREATE TABLE `size_group` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `data` json DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='尺码组';

DROP TABLE IF EXISTS `stock`;
CREATE TABLE `stock` (
  `id` int NOT NULL AUTO_INCREMENT,
  `dept_id` int NOT NULL,
  `goods_id` int NOT NULL COMMENT '商品id',
  `color` int NOT NULL COMMENT '颜色',
  `size` int NOT NULL COMMENT '尺码',
  `current_stock` int NOT NULL COMMENT '当前库存',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modify_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志 0：未删除 1已删除',
  `input_user` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1070 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='库存表';

DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` int NOT NULL AUTO_INCREMENT,
  `dict_name` varchar(45) DEFAULT NULL,
  `data` json DEFAULT NULL,
  `more_option` tinyint(1) DEFAULT '0',
  `modify_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统字典表';

DROP TABLE IF EXISTS `sys_version`;
CREATE TABLE `sys_version` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `v` int DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='数据版本表';

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_name` varchar(45) DEFAULT NULL,
  `account_name` varchar(45) DEFAULT NULL,
  `password` char(32) DEFAULT '534d3821b45399172b086cdd3795e864',
  `avatar` varchar(100) DEFAULT NULL,
  `dept_id` int DEFAULT NULL,
  `roles` json DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `modify_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `status` varchar(45) DEFAULT 'Enabled',
  `phone_number` char(11) DEFAULT NULL COMMENT '手机号',
  `phone_code` char(6) DEFAULT NULL COMMENT '手机验证码',
  `code_exp_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `account_name_UNIQUE` (`account_name`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统用户表';

DROP TABLE IF EXISTS `vip`;
CREATE TABLE `vip` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL COMMENT '''姓名''',
  `phone` varchar(45) DEFAULT NULL COMMENT '''电话''',
  `birthday` date DEFAULT NULL COMMENT '''生日''',
  `cert_no` varchar(45) DEFAULT NULL COMMENT '''身份证号''',
  `birth_discount` int DEFAULT NULL COMMENT '''生日折扣''',
  `vip_discount` int DEFAULT NULL COMMENT '终身折扣',
  `balance` int DEFAULT '0' COMMENT '当前余额',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `dept_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='会员表';
