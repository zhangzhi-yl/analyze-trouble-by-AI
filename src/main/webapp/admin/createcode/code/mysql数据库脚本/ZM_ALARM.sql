
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `ZM_ALARM`
-- ----------------------------
DROP TABLE IF EXISTS `ZM_ALARM`;
CREATE TABLE `ZM_ALARM` (
 		`ALARM_ID` varchar(100) NOT NULL,
		`LOOP_ID` varchar(100) DEFAULT NULL COMMENT '回路主键',
		`ALARM_CONTENT` varchar(255) DEFAULT NULL COMMENT '报警内容',
		`ALARM_TIME` varchar(100) DEFAULT NULL COMMENT '报警时间',
  		PRIMARY KEY (`ALARM_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
