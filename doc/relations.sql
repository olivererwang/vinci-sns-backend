use relations;

-- 用户关注关系数据
DROP TABLE if EXISTS relation;
CREATE TABLE `relation` (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `source_user` bigint(20) UNSIGNED NOT NULL COMMENT '关注人',
  `dst_user` bigint(20) UNSIGNED NOT NULL COMMENT '被关注人',
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `unique_source_user` (`source_user`,dst_user),
  INDEX `idx_source_user`(`source_user`,`id` desc),
  INDEX `idx_dst_user` (`dst_user`,`id` desc)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='关注关系表';