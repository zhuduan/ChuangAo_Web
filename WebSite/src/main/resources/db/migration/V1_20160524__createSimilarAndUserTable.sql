

DROP TABLE IF EXISTS `similarmartingale`;
CREATE TABLE `similarmartingale` (
  `accountid` int(11) NOT NULL,
  `expiretime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `netinputflag` tinyint(3) unsigned zerofill NOT NULL,
  `inputparams` text CHARACTER SET utf8 NOT NULL,
  PRIMARY KEY (`accountid`)
);



DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 NOT NULL,
  `group` tinyint(4) NOT NULL,
  `authority` text CHARACTER SET utf8 NOT NULL,
  `pass` varchar(100) CHARACTER SET utf8 NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
);


INSERT INTO user VALUES ('1', 'admin', '51', '{\"similarMartingale\":1}', '*6BB4837EB74329105EE4568DDA7DC67ED2CA2AD9');



