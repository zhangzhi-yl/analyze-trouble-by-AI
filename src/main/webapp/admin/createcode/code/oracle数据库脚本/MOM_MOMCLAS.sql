-- ----------------------------
-- Table structure for "C##FHBOOT"."MOM_MOMCLAS"
-- ----------------------------
-- DROP TABLE "C##FHBOOT"."MOM_MOMCLAS";
CREATE TABLE "C##FHBOOT"."MOM_MOMCLAS" (
	"FNUMBER" VARCHAR2(100 BYTE) NULL ,
	"FNAME" VARCHAR2(50 BYTE) NULL ,
	"FDATA" VARCHAR2(32 BYTE) NULL ,
	"FNUM" NUMBER(11) NULL ,
	"FPIC" NUMBER(11,2) NULL ,
	"FDIQU" VARCHAR2(100 BYTE) NULL ,
	"FYINCANG" NUMBER(11) NULL ,
	"MOMCLAS_ID" VARCHAR2(100 BYTE) NOT NULL 
)
LOGGING
NOCOMPRESS
NOCACHE
;

COMMENT ON COLUMN "C##FHBOOT"."MOM_MOMCLAS"."FNUMBER" IS '代码';
COMMENT ON COLUMN "C##FHBOOT"."MOM_MOMCLAS"."FNAME" IS '名称';
COMMENT ON COLUMN "C##FHBOOT"."MOM_MOMCLAS"."FDATA" IS '日期';
COMMENT ON COLUMN "C##FHBOOT"."MOM_MOMCLAS"."FNUM" IS '数量';
COMMENT ON COLUMN "C##FHBOOT"."MOM_MOMCLAS"."FPIC" IS '金额';
COMMENT ON COLUMN "C##FHBOOT"."MOM_MOMCLAS"."FDIQU" IS '地区';
COMMENT ON COLUMN "C##FHBOOT"."MOM_MOMCLAS"."FYINCANG" IS '隐藏值';
COMMENT ON COLUMN "C##FHBOOT"."MOM_MOMCLAS"."MOMCLAS_ID" IS 'ID';

-- ----------------------------
-- Indexes structure for table MOM_MOMCLAS
-- ----------------------------

-- ----------------------------
-- Checks structure for table "C##FHBOOT"."MOM_MOMCLAS"

-- ----------------------------

ALTER TABLE "C##FHBOOT"."MOM_MOMCLAS" ADD CHECK ("MOMCLAS_ID" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table "C##FHBOOT"."MOM_MOMCLAS"
-- ----------------------------
ALTER TABLE "C##FHBOOT"."MOM_MOMCLAS" ADD PRIMARY KEY ("MOMCLAS_ID");
