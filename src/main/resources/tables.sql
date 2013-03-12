CREATE TABLE IF NOT EXISTS `players` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(16) NOT NULL,
  `pvp-optout` tinyint(1) NOT NULL DEFAULT '0',
  `home-world` varchar(64) DEFAULT NULL,
  `home-x` double NOT NULL,
  `home-y` double NOT NULL,
  `home-z` double NOT NULL,
  `home-yaw` float NOT NULL,
  `home-pitch` float NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;
