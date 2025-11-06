package org.yy.entity.system;

/**
 * 说明：角色实体类
 * 作者：YuanYe Q356703572
 * 
 */
public class Role {
	private String ROLE_ID;		//ID
	private String ROLE_NAME;	//名称
	private String RIGHTS;		//权限(存放的除权后的菜单ID)控制菜单显示
	private String PARENT_ID;	//上级ID
	private String ADD_QX;		//新增权限(存放的除权后的菜单ID)有新增权限的菜单ID
	private String DEL_QX;		//删除权限(存放的除权后的菜单ID)有删除权限的菜单ID
	private String EDIT_QX;		//修改权限(存放的除权后的菜单ID)有修改权限的菜单ID
	private String CHA_QX;		//查看权限(存放的除权后的菜单ID)有查看权限的菜单ID
	private String YL1_QX;		//预留1
	private String YL2_QX;		//预留2
	private String YL3_QX;		//预留3
	private String RNUMBER;		//编号(在处理类中新增的时候自动生成)
	
	public String getROLE_ID() {
		return ROLE_ID;
	}
	public void setROLE_ID(String rOLE_ID) {
		ROLE_ID = rOLE_ID;
	}
	public String getROLE_NAME() {
		return ROLE_NAME;
	}
	public void setROLE_NAME(String rOLE_NAME) {
		ROLE_NAME = rOLE_NAME;
	}
	public String getRIGHTS() {
		return RIGHTS;
	}
	public void setRIGHTS(String rIGHTS) {
		RIGHTS = rIGHTS;
	}
	public String getPARENT_ID() {
		return PARENT_ID;
	}
	public void setPARENT_ID(String pARENT_ID) {
		PARENT_ID = pARENT_ID;
	}
	public String getADD_QX() {
		return ADD_QX;
	}
	public void setADD_QX(String aDD_QX) {
		ADD_QX = aDD_QX;
	}
	public String getDEL_QX() {
		return DEL_QX;
	}
	public void setDEL_QX(String dEL_QX) {
		DEL_QX = dEL_QX;
	}
	public String getEDIT_QX() {
		return EDIT_QX;
	}
	public void setEDIT_QX(String eDIT_QX) {
		EDIT_QX = eDIT_QX;
	}
	public String getCHA_QX() {
		return CHA_QX;
	}
	public void setCHA_QX(String cHA_QX) {
		CHA_QX = cHA_QX;
	}
	public String getRNUMBER() {
		return RNUMBER;
	}
	public void setRNUMBER(String rNUMBER) {
		RNUMBER = rNUMBER;
	}
	public String getYL1_QX() {
		return YL1_QX;
	}
	public void setYL1_QX(String yL1_QX) {
		YL1_QX = yL1_QX;
	}
	public String getYL2_QX() {
		return YL2_QX;
	}
	public void setYL2_QX(String yL2_QX) {
		YL2_QX = yL2_QX;
	}
	public String getYL3_QX() {
		return YL3_QX;
	}
	public void setYL3_QX(String yL3_QX) {
		YL3_QX = yL3_QX;
	}
	
	
}
