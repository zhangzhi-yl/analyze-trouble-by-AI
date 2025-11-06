package org.yy.util;

/**
 * 说明：常量
 * 作者：YuanYes Q356703572
 * 官网：356703572@qq.com
 */
public class Const {
	
	public static final String SESSION_USER = "SESSION_USER";						//session用的用户
	public static final String SESSION_USERROL = "SESSION_USERROL";					//用户对象(包含角色信息)
	public static final String SESSION_ROLE_RIGHTS = "SESSION_ROLE_RIGHTS";			//角色菜单权限
	public static final String SHIROSET = "SHIROSET";								//菜单权限标识
	public static final String SESSION_USERNAME = "USERNAME";						//用户名
	public static final String SESSION_U_NAME = "SESSION_U_NAME";					//用户姓名
	public static final String SESSION_USERID = "USER_ID";							//用户ID
	public static final String SESSION_STCODE = "FSTATION_CODE";						//工作站NUM
	public static final String SESSION_STNAME = "FSTATION_NAME";						//工作站名称
	public static final String SESSION_ROLE = "SESSION_ROLE";						//主职角色信息
	public static final String SESSION_RNUMBERS = "RNUMBERS";						//角色编码数组
	public static final String SESSION_ALLMENU = "SESSION_ALLMENU";					//全部菜单
	public static final String SKIN = "SKIN";										//用户皮肤
	
	public static final String SYSSET = "config/sysSet.ini";						//系统设置配置文件路径
	public static final String SYSNAME = "sysName";									//系统名称
	public static final String SHOWCOUNT = "showCount";								//每页条数
	
	public static final String FILEPATHFILE = "uploadFiles/file/";					//文件上传路径
	public static final String APPFILEPATHFILE = "uploadFiles/app_ver/";
	public static final String FILEPATHFILELOGO = "uploadFiles/logo/";					//文件上传路径

	public static final String FILEPATHIMG = "uploadFiles/imgs/";					//图片上传路径
	
	public static final String FILEACTIVITI = "uploadFiles/activitiFile/";			//工作流生成XML和PNG目录
	
	public static final String DEPARTMENT_IDS = "DEPARTMENT_IDS";					//当前用户拥有的最高部门权限集合
	public static final String DEPARTMENT_ID = "DEPARTMENT_ID";						//当前用户拥有的最高部门权限
	public static final double TOKEN_PEROID=60;                                     //App TOKEN 失效时长
	public static final String K3SERVERIPANDPORTJK= "172.188.10.63:8080"; 			//自己系统访问K3数据库接口配置

	public static final String OPC_HOST="172.16.30.11";
	public static final String OPC_DOMAIN="172.16.30.11";
	public static final String OPC_USER="OPCServer";
	public static final String OPC_PASWORD="yl_0323";
	public static final String PROG_ID="";
	public static final String CLSZ_ID = "7BC0CC8E-482C-47CA-ABDC-0FE7F9C6E729";// KEPServer的注册表ID，可以在“组件服务”里看到
	public static final String TIME_OUT= "50000";
	public static final String HEART_BEAT= "900000";
	public static final String TOPIC ="opc";
	public static final String code1 ="_8code1";
	public static final String code2 ="_8code2";
	public static final String MISSIONNUM ="_MISSIONNUM";
	public static final String PARTCODE ="_PARTCODE";
	public static final String PLCREQUEST ="_PLCREQUEST";
	public static final String SENDOK="_SENDOK";
	public static final String SIGN="_SIGN";
}
