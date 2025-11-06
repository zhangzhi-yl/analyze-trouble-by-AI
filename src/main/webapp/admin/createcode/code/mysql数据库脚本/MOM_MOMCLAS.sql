
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `MOM_MOMCLAS`
-- ----------------------------
DROP TABLE IF EXISTS `MOM_MOMCLAS`;
CREATE TABLE `MOM_MOMCLAS` (
 		`MOMCLAS_ID` varchar(100) NOT NULL,
		`FNUMBER` varchar(100) DEFAULT NULL COMMENT '代码',
		`FNAME` varchar(50) DEFAULT NULL COMMENT '名称',
		`FDATA` varchar(32) DEFAULT NULL COMMENT '日期',
		`FNUM` int(11) NOT NULL COMMENT '数量',
		`FPIC` double(11,2) DEFAULT NULL COMMENT '金额',
		`FDIQU` varchar(100) DEFAULT NULL COMMENT '地区',
		`FYINCANG` int(11) NOT NULL COMMENT '隐藏值',
  		PRIMARY KEY (`MOMCLAS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
