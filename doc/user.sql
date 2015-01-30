use users;

-- 存储了所有出厂设备的唯一标识
DROP TABLE if EXISTS device;
create table `device` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `imei` varchar(64) NOT NULL COMMENT '设备唯一id',
  `mac_addr` VARCHAR(64) NOT NULL COMMENT '设备wifi mac地址',
  `userid` bigint(20) UNSIGNED NOT NULL DEFAULT '0' COMMENT '反查用的userid',
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '此次操作的时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_imei` (`imei`),
  INDEX `idx_userid` (`userid`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='设备表';

-- 用户数据存储
DROP TABLE if EXISTS user;
CREATE TABLE `user` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id,兼做用户id',
  `nick_name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '用户昵称，唯一的',
  `device_imei` varchar(64) NOT NULL DEFAULT '' COMMENT '绑定的设备唯一id',
  `extra` TEXT NOT NULL COMMENT '存储用户其他信息',
  `version` int(11) UNSIGNED NOT NULL DEFAULT '0' COMMENT '更新时的版本号',
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '此次操作的时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_nick_name` (`nick_name`)
) ENGINE=InnoDB AUTO_INCREMENT=100000 DEFAULT CHARSET=utf8mb4 COMMENT='用户基本信息表';