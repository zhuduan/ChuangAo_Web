



DROP TABLE IF EXISTS `follow`;
CREATE TABLE `follow` (
  `accountid` int(11) NOT NULL,
  `accounttype` tinyint(3) NOT NULL,
  `followaccount` text CHARACTER SET utf8 NOT NULL,
  `expiretime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `offline` tinyint(3) NOT NULL DEFAULT '0',
  `feedback` tinyint(3) NOT NULL DEFAULT '0',
  `datastatistic` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`accountid`)
);