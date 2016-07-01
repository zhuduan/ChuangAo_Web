

DROP TABLE IF EXISTS `similarmartingale`;
CREATE TABLE `similarmartingale` (
  `accountid` int(11) NOT NULL,
  `expiretime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `netinputflag` tinyint(3) unsigned zerofill NOT NULL,
  `inputparams` text CHARACTER SET utf8 NOT NULL,
  PRIMARY KEY (`accountid`)
);

INSERT INTO similarmartingale VALUES ('7767180', '2017-06-14 18:12:01', '001', '{\"recoverDepthNet\":4,\"windowSizeNet\":200,\"recoverProfitNet\":65,\"reverseTendNet\":true,\"availiableLotsNet\":21,\"recoverBootDifferenceNet\":90,\"minDifferenceNet\":25,\"betterPointModeNet\":true,\"recoverStopLoseNet\":300,\"stopLoseNet\":8000,\"maxCloseProfitStepNet\":4,\"positiveSectionUpNet\":100000.03,\"maxCloseProfitReverseDepthNet\":8,\"recoverMinDifferenceNet\":90,\"positiveSectionLowNet\":1.5E-4,\"negativeSectionUpNet\":-10000.03,\"maxCloseProfitBaseNet\":0,\"windowApplifyNet\":50,\"closeProfitPointNet\":45,\"lotsGroupNet\":3,\"accmulationMinNet\":-1,\"amplificationNet\":100000,\"minDifferenceStartOrderNet\":7,\"takeProfitNet\":8000,\"minDifferenceStepNet\":10,\"recoverStopLevelNet\":6,\"negativeSectionLowNet\":-1.5E-4}');



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



