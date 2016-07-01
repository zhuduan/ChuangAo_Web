-- ----------------------------
-- Table structure for `follow`
-- ----------------------------
DROP TABLE IF EXISTS `follow`;
CREATE TABLE `follow` (
  `accountid` int(11) NOT NULL,
  `accounttype` tinyint(3) NOT NULL,
  `followaccount` text CHARACTER SET utf8 NOT NULL,
  `expiretime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `as_sender` int(11) NOT NULL DEFAULT '0',
  `as_receiver` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`accountid`)
);

-- ----------------------------
-- Records of follow
-- ----------------------------
INSERT INTO follow VALUES ('7767180', '3', '{\"ids\":[{\"id\":7767180}]}', '2017-07-14 18:11:15', '0', '0');
INSERT INTO follow VALUES ('7793251', '1', '{\"ids\":[{\"id\":7767180}]}', '2017-06-21 18:55:52', '0', '0');


-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 NOT NULL,
  `group` tinyint(4) NOT NULL,
  `authority` text CHARACTER SET utf8 NOT NULL,
  `pass` varchar(100) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `ownaccounts` text CHARACTER SET utf8 NOT NULL,
  `email` varchar(100) NOT NULL,
  `telephone` varchar(18) CHARACTER SET utf8 NOT NULL,
  `noticetype` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) AUTO_INCREMENT=17;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO user VALUES ('1', 'admin', '51', '{\"similarmartingale\":1,\"onlyBolling\":0}', '*6BB4837EB74329105EE4568DDA7DC67ED2CA2AD9', '{\"7767180\":7,\"7767170\":2,\"7767160\":4,\"7793251\":7}', '745316206@qq.com', '18080496761', '1');
INSERT INTO user VALUES ('2', 'test1', '31', '{\"similarmartingale\":1}', '*6BB4837EB74329105EE4568DDA7DC67ED2CA2AD9', '{\"7767180\":7}', '', '0', '0');
INSERT INTO user VALUES ('3', 'test3', '21', '{\"similarmartingale\":1}', '*6BB4837EB74329105EE4568DDA7DC67ED2CA2AD9', '{\"7767180\":2}', '', '0', '0');
INSERT INTO user VALUES ('4', 'test4', '31', '{\"similarmartingale\":1}', '*6BB4837EB74329105EE4568DDA7DC67ED2CA2AD9', '{\"7767180\":3}', '', '0', '0');
INSERT INTO user VALUES ('5', 'test5', '41', '{\"similarmartingale\":1}', '*6BB4837EB74329105EE4568DDA7DC67ED2CA2AD9', '{\"7767180\":4}', '', '0', '0');
INSERT INTO user VALUES ('6', 'test6', '1', '{\"similarmartingale\":1}', '*6BB4837EB74329105EE4568DDA7DC67ED2CA2AD9', '{\"7767180\":5}', '', '0', '0');
INSERT INTO user VALUES ('7', 'test7', '11', '{\"similarmartingale\":1}', '*6BB4837EB74329105EE4568DDA7DC67ED2CA2AD9', '{\"7767180\":6}', '', '0', '0');
INSERT INTO user VALUES ('8', 'test8', '21', '{\"similarmartingale\":1}', '*6BB4837EB74329105EE4568DDA7DC67ED2CA2AD9', '{\"7767180\":7}', '', '0', '0');
INSERT INTO user VALUES ('9', 'test9', '31', '{\"similarmartingale\":1}', '*6BB4837EB74329105EE4568DDA7DC67ED2CA2AD9', '{\"7767180\":0}', '', '0', '0');
INSERT INTO user VALUES ('10', 'test10', '41', '{\"similarmartingale\":1}', '*6BB4837EB74329105EE4568DDA7DC67ED2CA2AD9', '{\"7767180\":1}', '', '0', '0');
INSERT INTO user VALUES ('11', 'test11', '21', '{\"similarmartingale\":1}', '*6BB4837EB74329105EE4568DDA7DC67ED2CA2AD9', '{\"7767180\":2}', '', '0', '0');
INSERT INTO user VALUES ('12', 'test12', '11', '{\"similarmartingale\":1}', '*6BB4837EB74329105EE4568DDA7DC67ED2CA2AD9', '{\"7767180\":3}', '', '0', '0');
INSERT INTO user VALUES ('13', 'test13', '21', '{\"similarmartingale\":1}', '*6BB4837EB74329105EE4568DDA7DC67ED2CA2AD9', '{\"7767180\":4}', '', '0', '0');
INSERT INTO user VALUES ('14', 'test14', '41', '{\"similarmartingale\":1}', '*6BB4837EB74329105EE4568DDA7DC67ED2CA2AD9', '{\"7767180\":5}', '', '0', '0');
INSERT INTO user VALUES ('15', 'test15', '31', '{\"similarmartingale\":1}', '*6BB4837EB74329105EE4568DDA7DC67ED2CA2AD9', '{\"7767180\":6}', '', '0', '0');
INSERT INTO user VALUES ('16', 'test16', '31', '{\"similarmartingale\":1}', '*6BB4837EB74329105EE4568DDA7DC67ED2CA2AD9', '{\"7767180\":7}', '', '0', '0');
