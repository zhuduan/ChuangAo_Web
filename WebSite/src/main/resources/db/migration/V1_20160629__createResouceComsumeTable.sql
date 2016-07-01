

DROP TABLE IF EXISTS `resource_comsume_user`;
CREATE TABLE `resource_comsume_user` (
  `userid` int(11) NOT NULL,
  `mail_used_times` bigint(20) NOT NULL DEFAULT '0',
  `text_used_times` bigint(20) NOT NULL DEFAULT '0',
  `onlinemonitor_time` bigint(20) NOT NULL,
  PRIMARY KEY (`userid`)
);