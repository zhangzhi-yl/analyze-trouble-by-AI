-- ----------------------------
-- Table structure for "C##FHBOOT"."ZM_ALARM"
-- ----------------------------
-- DROP TABLE "C##FHBOOT"."ZM_ALARM";
CREATE TABLE "C##FHBOOT"."ZM_ALARM" (
	"LOOP_ID" VARCHAR2(100 BYTE) NULL ,
	"ALARM_CONTENT" VARCHAR2(255 BYTE) NULL ,
	"ALARM_TIME" VARCHAR2(100 BYTE) NULL ,
	"ALARM_ID" VARCHAR2(100 BYTE) NOT NULL 
)
LOGGING
NOCOMPRESS
NOCACHE
;

COMMENT ON COLUMN "C##FHBOOT"."ZM_ALARM"."LOOP_ID" IS '回路主键';
COMMENT ON COLUMN "C##FHBOOT"."ZM_ALARM"."ALARM_CONTENT" IS '报警内容';
COMMENT ON COLUMN "C##FHBOOT"."ZM_ALARM"."ALARM_TIME" IS '报警时间';
COMMENT ON COLUMN "C##FHBOOT"."ZM_ALARM"."ALARM_ID" IS 'ID';

-- ----------------------------
-- Indexes structure for table ZM_ALARM
-- ----------------------------

-- ----------------------------
-- Checks structure for table "C##FHBOOT"."ZM_ALARM"

-- ----------------------------

ALTER TABLE "C##FHBOOT"."ZM_ALARM" ADD CHECK ("ALARM_ID" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table "C##FHBOOT"."ZM_ALARM"
-- ----------------------------
ALTER TABLE "C##FHBOOT"."ZM_ALARM" ADD PRIMARY KEY ("ALARM_ID");
