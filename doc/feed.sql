use feed;

-- 用户发的所有微博内容
DROP TABLE if EXISTS origin_feed;
CREATE TABLE `origin_feed` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id,用做feed_id',
  `userid` bigint(20) UNSIGNED NOT NULL COMMENT '谁发的',
  `feed_type` VARCHAR(32) NOT NULL COMMENT '这个feed的类型',
  `content` TEXT NOT NULL COMMENT 'feed内容',
  `ref_feed_id` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0' COMMENT '引用的feed id,没有引用为0',
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  INDEX `idx_user` (`userid`,`id` desc)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分享内容表';

DROP TABLE if EXISTS feed_timeline;
CREATE TABLE `feed_timeline` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id,用做feed_id',
  `userid` bigint(20) UNSIGNED NOT NULL COMMENT '谁的feed timeline',
  `feed_id` bigint(20) UNSIGNED NOT NULL COMMENT 'feedid',
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  INDEX `idx_user` (`userid` ,`id` desc)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分享内容表';
