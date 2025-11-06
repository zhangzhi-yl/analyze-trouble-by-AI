package org.yy.mapper.dsno1.fhoa;

import java.util.List;

import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.entity.fhoa.Department;

/** 
 * 说明： 组织机构接Mapper
 * 创建人：FH Q356703572
 * 官网：356703572@qq.com
 */
public interface DepartmentMapper{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception;
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception;
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception;
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> datalistPage(Page page)throws Exception;
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception;

	/**通过编码获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByBianma(PageData pd)throws Exception;
	
	/**
	 * 通过ID获取其子级列表
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public List<Department> listSubDepartmentByParentId(String parentId) throws Exception;
	
	/**
	 * 获取所有数据并填充每条数据的子级列表(递归处理)
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<Department> listAllDepartment(String parentId) throws Exception;
	
	/**
	 * 获取所有数据并填充每条数据的子级列表(递归处理)下拉ztree用
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listAllDepartmentToSelect(String parentId, List<PageData> zdepartmentPdList) throws Exception;
	
	/**获取某个部门所有下级部门ID(返回拼接字符串 in的形式)
	 * @param DEPARTMENT_ID
	 * @return
	 * @throws Exception
	 */
	public String getDEPARTMENT_IDS(String DEPARTMENT_ID) throws Exception;
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception;

	/**
	 * @param dept1
	 * @return
	 */
	public PageData findByIdx(String dept1)throws Exception;

	/**
	 * @param pd
	 * @return
	 */
	public PageData findByName(PageData pd)throws Exception;
}

