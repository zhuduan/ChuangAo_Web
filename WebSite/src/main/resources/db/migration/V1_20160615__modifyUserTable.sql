


DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 NOT NULL,
  `group` tinyint(4) NOT NULL,
  `authority` text CHARACTER SET utf8 NOT NULL,
  `pass` varchar(100) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `ownaccounts` text CHARACTER SET utf8 NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO user VALUES ('1', 'admin', '51', '{\"similarmartingale\":1}', '*6BB4837EB74329105EE4568DDA7DC67ED2CA2AD9', '{\"7767180\":1}');
INSERT INTO user VALUES ('2', 'test1', '31', '{\"similarmartingale\":1}', '*6BB4837EB74329105EE4568DDA7DC67ED2CA2AD9', '{\"7767180\":1}');