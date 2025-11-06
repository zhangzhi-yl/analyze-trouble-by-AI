package org.yy.entity.mdm;

import java.util.List;

 /** 
 * 说明：配方管理实体类
 * 作者：YuanYe
 * 时间：2020-01-13
 * 
 */
public class PBOMNew{ 
	
	private String PBOM_ID;	//主键
	//private String NAME;					//名称
	private String PARENT_ID;				//父类ID
	private String target;
	private PBOMNew pbom;
	private List<PBOMNew> subPBOM;
	private boolean hasPBOM = false;
	private String treeurl;
	private boolean open;
	
	private String PRODUCT_NAME;			//产品名称
	public String getFPRODUCT_NAME() {
		return PRODUCT_NAME;
	}
	public void setFPRODUCT_NAME(String PRODUCT_NAME) {
		this.PRODUCT_NAME = PRODUCT_NAME;
	}
	private String AUXILIARY_UNIT;			//辅单位
	public String getFAUXILIARY_UNIT() {
		return AUXILIARY_UNIT;
	}
	public void setFAUXILIARY_UNIT(String AUXILIARY_UNIT) {
		this.AUXILIARY_UNIT = AUXILIARY_UNIT;
	}
	private String FNAME;			//名称
	public String getFFNAME() {
		return FNAME;
	}
	public void setFFNAME(String FNAME) {
		this.FNAME = FNAME;
	}
	private String FLEVEL;			//层次
	public String getFFLEVEL() {
		return FLEVEL;
	}
	public void setFFLEVEL(String FLEVEL) {
		this.FLEVEL = FLEVEL;
	}
	private String MAT_CODE;			//物料代码
	public String getFMAT_CODE() {
		return MAT_CODE;
	}
	public void setFMAT_CODE(String MAT_CODE) {
		this.MAT_CODE = MAT_CODE;
	}
	private String MAT_NAME;			//物料名称
	public String getFMAT_NAME() {
		return MAT_NAME;
	}
	public void setFMAT_NAME(String MAT_NAME) {
		this.MAT_NAME = MAT_NAME;
	}
	private Double FQTY;			//数量
	public Double getFFQTY() {
		return FQTY;
	}
	public void setFFQTY(Double FQTY) {
		this.FQTY = FQTY;
	}
	private String FUNIT;			//单位
	public String getFFUNIT() {
		return FUNIT;
	}
	public void setFFUNIT(String FUNIT) {
		this.FUNIT = FUNIT;
	}
	private Double QTY_H;			//数量上限
	public Double getFQTY_H() {
		return QTY_H;
	}
	public void setFQTY_H(Double QTY_H) {
		this.QTY_H = QTY_H;
	}
	private Double QTY_L;			//数量下限
	public Double getFQTY_L() {
		return QTY_L;
	}
	public void setFQTY_L(Double QTY_L) {
		this.QTY_L = QTY_L;
	}
	private String FSPECS;			//规格
	public String getFFSPECS() {
		return FSPECS;
	}
	public void setFFSPECS(String FSPECS) {
		this.FSPECS = FSPECS;
	}
	/*private String PARENT_ID;			//母件ID
	public String getFPARENT_ID() {
		return PARENT_ID;
	}
	public void setFPARENT_ID(String PARENT_ID) {
		this.PARENT_ID = PARENT_ID;
	}*/
	private String PARENT_CODE;			//母件代码
	public String getFPARENT_CODE() {
		return PARENT_CODE;
	}
	public void setFPARENT_CODE(String PARENT_CODE) {
		this.PARENT_CODE = PARENT_CODE;
	}
	private String ROOT_NODE_CODE;			//根节点代码
	public String getFROOT_NODE_CODE() {
		return ROOT_NODE_CODE;
	}
	public void setFROOT_NODE_CODE(String ROOT_NODE_CODE) {
		this.ROOT_NODE_CODE = ROOT_NODE_CODE;
	}
	private String FVERSIONS;			//版本号
	public String getFFVERSIONS() {
		return FVERSIONS;
	}
	public void setFFVERSIONS(String FVERSIONS) {
		this.FVERSIONS = FVERSIONS;
	}
	private String FSTATE;			//使用状态
	public String getFFSTATE() {
		return FSTATE;
	}
	public void setFFSTATE(String FSTATE) {
		this.FSTATE = FSTATE;
	}
	private String STANDARD_NUMBER;			//标准号
	public String getFSTANDARD_NUMBER() {
		return STANDARD_NUMBER;
	}
	public void setFSTANDARD_NUMBER(String STANDARD_NUMBER) {
		this.STANDARD_NUMBER = STANDARD_NUMBER;
	}
	private String FORWARD_NO;			//转发号
	public String getFFORWARD_NO() {
		return FORWARD_NO;
	}
	public void setFFORWARD_NO(String FORWARD_NO) {
		this.FORWARD_NO = FORWARD_NO;
	}
	private String FCREATOR;			//创建人
	public String getFFCREATOR() {
		return FCREATOR;
	}
	public void setFFCREATOR(String FCREATOR) {
		this.FCREATOR = FCREATOR;
	}
	private String CREATE_TIME;			//创建时间
	public String getFCREATE_TIME() {
		return CREATE_TIME;
	}
	public void setFCREATE_TIME(String CREATE_TIME) {
		this.CREATE_TIME = CREATE_TIME;
	}
	private String RECIPE_EXECUTION_DATE;			//配方执行日期
	public String getFRECIPE_EXECUTION_DATE() {
		return RECIPE_EXECUTION_DATE;
	}
	public void setFRECIPE_EXECUTION_DATE(String RECIPE_EXECUTION_DATE) {
		this.RECIPE_EXECUTION_DATE = RECIPE_EXECUTION_DATE;
	}
	private String FAPPROVER;			//批准人
	public String getFFAPPROVER() {
		return FAPPROVER;
	}
	public void setFFAPPROVER(String FAPPROVER) {
		this.FAPPROVER = FAPPROVER;
	}
	private String BOM_KPI1;			//配方指标1
	public String getFBOM_KPI1() {
		return BOM_KPI1;
	}
	public void setFBOM_KPI1(String BOM_KPI1) {
		this.BOM_KPI1 = BOM_KPI1;
	}
	private String BOM_KPI2;			//配方指标2
	public String getFBOM_KPI2() {
		return BOM_KPI2;
	}
	public void setFBOM_KPI2(String BOM_KPI2) {
		this.BOM_KPI2 = BOM_KPI2;
	}
	private String BOM_KPI3;			//配方指标3
	public String getFBOM_KPI3() {
		return BOM_KPI3;
	}
	public void setFBOM_KPI3(String BOM_KPI3) {
		this.BOM_KPI3 = BOM_KPI3;
	}
	private String BOM_KPI4;			//配方指标4
	public String getFBOM_KPI4() {
		return BOM_KPI4;
	}
	public void setFBOM_KPI4(String BOM_KPI4) {
		this.BOM_KPI4 = BOM_KPI4;
	}
	private String FEXTEND1;			//扩展字段1
	public String getFFEXTEND1() {
		return FEXTEND1;
	}
	public void setFFEXTEND1(String FEXTEND1) {
		this.FEXTEND1 = FEXTEND1;
	}
	private String FEXTEND2;			//扩展字段2
	public String getFFEXTEND2() {
		return FEXTEND2;
	}
	public void setFFEXTEND2(String FEXTEND2) {
		this.FEXTEND2 = FEXTEND2;
	}
	private String FEXTEND3;			//扩展字段3
	public String getFFEXTEND3() {
		return FEXTEND3;
	}
	public void setFFEXTEND3(String FEXTEND3) {
		this.FEXTEND3 = FEXTEND3;
	}
	private String FEXTEND4;			//扩展字段4
	public String getFFEXTEND4() {
		return FEXTEND4;
	}
	public void setFFEXTEND4(String FEXTEND4) {
		this.FEXTEND4 = FEXTEND4;
	}
	private String FEXTEND5;			//扩展字段5
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
	public PBOMNew getPBOM() {
		return pbom;
	}
	public void setPBOM(PBOMNew pbom) {
		this.pbom = pbom;
	}
	public List<PBOMNew> getSubPBOM() {
		return subPBOM;
	}
	public void setSubPBOM(List<PBOMNew> subPBOM) {
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
	public boolean getOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}

	
}
