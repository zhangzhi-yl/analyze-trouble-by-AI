package org.yy.entity.mdm;

import java.util.List;

 /** 
 * 璇存槑锛氶厤鏂圭鐞嗗疄浣撶被
 * 浣滆�咃細YuanYe
 * 鏃堕棿锛�2020-01-13
 * 
 */
public class PBOM{ 
	
	private String PBOM_ID;	//涓婚敭
	//private String NAME;					//鍚嶇О
	private String PARENT_ID;				//鐖剁被ID
	private String target;
	private String SHOW;
	private PBOM pbom;
	private List<PBOM> subPBOM;
	private boolean hasPBOM = false;
	private String treeurl;
	private String FSUBNUM;
	private String PRODUCT_NAME;			//浜у搧鍚嶇О
	public String getFPRODUCT_NAME() {
		return PRODUCT_NAME;
	}
	public void setFPRODUCT_NAME(String PRODUCT_NAME) {
		this.PRODUCT_NAME = PRODUCT_NAME;
	}
	private String AUXILIARY_UNIT;			//杈呭崟浣�
	public String getFAUXILIARY_UNIT() {
		return AUXILIARY_UNIT;
	}
	public void setFAUXILIARY_UNIT(String AUXILIARY_UNIT) {
		this.AUXILIARY_UNIT = AUXILIARY_UNIT;
	}
	private String FNAME;			//鍚嶇О
	public String getFFNAME() {
		return FNAME;
	}
	public void setFFNAME(String FNAME) {
		this.FNAME = FNAME;
	}
	private String FLEVEL;			//灞傛
	public String getFFLEVEL() {
		return FLEVEL;
	}
	public void setFFLEVEL(String FLEVEL) {
		this.FLEVEL = FLEVEL;
	}
	private String MAT_CODE;			//鐗╂枡浠ｇ爜
	public String getFMAT_CODE() {
		return MAT_CODE;
	}
	public void setFMAT_CODE(String MAT_CODE) {
		this.MAT_CODE = MAT_CODE;
	}
	private String MAT_NAME;			//鐗╂枡鍚嶇О
	public String getFMAT_NAME() {
		return MAT_NAME;
	}
	public void setFMAT_NAME(String MAT_NAME) {
		this.MAT_NAME = MAT_NAME;
	}
	private Double FQTY;			//鏁伴噺
	public Double getFFQTY() {
		return FQTY;
	}
	public void setFFQTY(Double FQTY) {
		this.FQTY = FQTY;
	}
	private String FUNIT;			//鍗曚綅
	public String getFFUNIT() {
		return FUNIT;
	}
	public void setFFUNIT(String FUNIT) {
		this.FUNIT = FUNIT;
	}
	private Double QTY_H;			//鏁伴噺涓婇檺
	public Double getFQTY_H() {
		return QTY_H;
	}
	public void setFQTY_H(Double QTY_H) {
		this.QTY_H = QTY_H;
	}
	private Double QTY_L;			//鏁伴噺涓嬮檺
	public Double getFQTY_L() {
		return QTY_L;
	}
	public void setFQTY_L(Double QTY_L) {
		this.QTY_L = QTY_L;
	}
	private String FSPECS;			//瑙勬牸
	public String getFFSPECS() {
		return FSPECS;
	}
	public void setFFSPECS(String FSPECS) {
		this.FSPECS = FSPECS;
	}
	/*private String PARENT_ID;			//姣嶄欢ID
	public String getFPARENT_ID() {
		return PARENT_ID;
	}
	public void setFPARENT_ID(String PARENT_ID) {
		this.PARENT_ID = PARENT_ID;
	}*/
	private String PARENT_CODE;			//姣嶄欢浠ｇ爜
	public String getFPARENT_CODE() {
		return PARENT_CODE;
	}
	public void setFPARENT_CODE(String PARENT_CODE) {
		this.PARENT_CODE = PARENT_CODE;
	}
	private String ROOT_NODE_CODE;			//鏍硅妭鐐逛唬鐮�
	public String getFROOT_NODE_CODE() {
		return ROOT_NODE_CODE;
	}
	public void setFROOT_NODE_CODE(String ROOT_NODE_CODE) {
		this.ROOT_NODE_CODE = ROOT_NODE_CODE;
	}
	private String FVERSIONS;			//鐗堟湰鍙�
	public String getFFVERSIONS() {
		return FVERSIONS;
	}
	public void setFFVERSIONS(String FVERSIONS) {
		this.FVERSIONS = FVERSIONS;
	}
	private String FSTATE;			//浣跨敤鐘舵��
	public String getFFSTATE() {
		return FSTATE;
	}
	public void setFFSTATE(String FSTATE) {
		this.FSTATE = FSTATE;
	}
	private String STANDARD_NUMBER;			//鏍囧噯鍙�
	public String getFSTANDARD_NUMBER() {
		return STANDARD_NUMBER;
	}
	public void setFSTANDARD_NUMBER(String STANDARD_NUMBER) {
		this.STANDARD_NUMBER = STANDARD_NUMBER;
	}
	private String FORWARD_NO;			//杞彂鍙�
	public String getFFORWARD_NO() {
		return FORWARD_NO;
	}
	public void setFFORWARD_NO(String FORWARD_NO) {
		this.FORWARD_NO = FORWARD_NO;
	}
	private String FCREATOR;			//鍒涘缓浜�
	public String getFFCREATOR() {
		return FCREATOR;
	}
	public void setFFCREATOR(String FCREATOR) {
		this.FCREATOR = FCREATOR;
	}
	private String CREATE_TIME;			//鍒涘缓鏃堕棿
	public String getFCREATE_TIME() {
		return CREATE_TIME;
	}
	public void setFCREATE_TIME(String CREATE_TIME) {
		this.CREATE_TIME = CREATE_TIME;
	}
	private String RECIPE_EXECUTION_DATE;			//閰嶆柟鎵ц鏃ユ湡
	public String getFRECIPE_EXECUTION_DATE() {
		return RECIPE_EXECUTION_DATE;
	}
	public void setFRECIPE_EXECUTION_DATE(String RECIPE_EXECUTION_DATE) {
		this.RECIPE_EXECUTION_DATE = RECIPE_EXECUTION_DATE;
	}
	private String FAPPROVER;			//鎵瑰噯浜�
	public String getFFAPPROVER() {
		return FAPPROVER;
	}
	public void setFFAPPROVER(String FAPPROVER) {
		this.FAPPROVER = FAPPROVER;
	}
	private String BOM_KPI1;			//閰嶆柟鎸囨爣1
	public String getFBOM_KPI1() {
		return BOM_KPI1;
	}
	public void setFBOM_KPI1(String BOM_KPI1) {
		this.BOM_KPI1 = BOM_KPI1;
	}
	private String BOM_KPI2;			//閰嶆柟鎸囨爣2
	public String getFBOM_KPI2() {
		return BOM_KPI2;
	}
	public void setFBOM_KPI2(String BOM_KPI2) {
		this.BOM_KPI2 = BOM_KPI2;
	}
	private String BOM_KPI3;			//閰嶆柟鎸囨爣3
	public String getFBOM_KPI3() {
		return BOM_KPI3;
	}
	public void setFBOM_KPI3(String BOM_KPI3) {
		this.BOM_KPI3 = BOM_KPI3;
	}
	private String BOM_KPI4;			//閰嶆柟鎸囨爣4
	public String getFBOM_KPI4() {
		return BOM_KPI4;
	}
	public void setFBOM_KPI4(String BOM_KPI4) {
		this.BOM_KPI4 = BOM_KPI4;
	}
	private String FEXTEND1;			//鎵╁睍瀛楁1
	public String getFFEXTEND1() {
		return FEXTEND1;
	}
	public void setFFEXTEND1(String FEXTEND1) {
		this.FEXTEND1 = FEXTEND1;
	}
	private String FEXTEND2;			//鎵╁睍瀛楁2
	public String getFFEXTEND2() {
		return FEXTEND2;
	}
	public void setFFEXTEND2(String FEXTEND2) {
		this.FEXTEND2 = FEXTEND2;
	}
	private String FEXTEND3;			//鎵╁睍瀛楁3
	public String getFFEXTEND3() {
		return FEXTEND3;
	}
	public void setFFEXTEND3(String FEXTEND3) {
		this.FEXTEND3 = FEXTEND3;
	}
	private String FEXTEND4;			//鎵╁睍瀛楁4
	public String getFFEXTEND4() {
		return FEXTEND4;
	}
	public void setFFEXTEND4(String FEXTEND4) {
		this.FEXTEND4 = FEXTEND4;
	}
	private String FEXTEND5;			//鎵╁睍瀛楁5
	public String getFFEXTEND5() {
		return FEXTEND5;
	}
	public void setFFEXTEND5(String FEXTEND5) {
		this.FEXTEND5 = FEXTEND5;
	}

	public String getPBOM_ID() {
		return PBOM_ID;
	}
	public void setPBOM_ID(String PBOM_ID) {
		this.PBOM_ID = PBOM_ID;
	}
	/*public String getNAME() {
		return NAME;
	}
	public void setNAME(String NAME) {
		this.NAME = NAME;
	}*/
	public String getPARENT_ID() {
		return PARENT_ID;
	}
	public void setPARENT_ID(String PARENT_ID) {
		this.PARENT_ID = PARENT_ID;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public PBOM getPBOM() {
		return pbom;
	}
	public void setPBOM(PBOM pbom) {
		this.pbom = pbom;
	}
	public List<PBOM> getSubPBOM() {
		return subPBOM;
	}
	public void setSubPBOM(List<PBOM> subPBOM) {
		this.subPBOM = subPBOM;
	}
	public boolean isHasPBOM() {
		return hasPBOM;
	}
	public void setHasPBOM(boolean hasPBOM) {
		this.hasPBOM = hasPBOM;
	}
	public String getTreeurl() {
		return treeurl;
	}
	public void setTreeurl(String treeurl) {
		this.treeurl = treeurl;
	}
	public String getSHOW() {
		return SHOW;
	}
	public void setSHOW(String sHOW) {
		SHOW = sHOW;
	}
	public String getFSUBNUM() {
		return FSUBNUM;
	}
	public void setFSUBNUM(String FSUBNUM) {
		this.FSUBNUM = FSUBNUM;
	}
}
