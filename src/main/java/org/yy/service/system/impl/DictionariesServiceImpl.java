package org.yy.service.system.impl;

import java.util.List;

import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.entity.system.Dictionaries;
import org.yy.mapper.dsno1.system.DictionariesMapper;
import org.yy.service.system.DictionariesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 说明：按钮权限服务接口实现类
 * 作者：YuanYes Q356703572
 * 官网：356703572@qq.com
 */
@Service
@Transactional //开启事物
public class DictionariesServiceImpl implements DictionariesService {
	
	@Autowired
	private DictionariesMapper dictionariesMapper;
	
	/**
	 * 获取所有数据并填充每条数据的子级列表(递归处理)
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<Dictionaries> listAllDict(String parentId) throws Exception {
		List<Dictionaries> dictList = this.listSubDictByParentId(parentId);
		for(Dictionaries dict : dictList){
			dict.setTreeurl("dictionaries_list.html?DICTIONARIES_ID="+dict.getDICTIONARIES_ID());
			dict.setSubDict(this.listAllDict(dict.getDICTIONARIES_ID()));
			dict.setTarget("treeFrame");
		}
		return dictList;
	}
	
	/**
	 * 通过ID获取其子级列表
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public List<Dictionaries> listSubDictByParentId(String parentId) throws Exception {
		return dictionariesMapper.listSubDictByParentId(parentId);
	}
	
	/**
	 * 获取所有数据并填充每条数据的子级列表(递归处理)用于代码生成器引用数据字典
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<Dictionaries> listAllDictToCreateCode(String parentId) throws Exception {
		List<Dictionaries> dictList = this.listSubDictByParentId(parentId);
		for(Dictionaries dict : dictList){
			dict.setTreeurl("setDID('"+dict.getDICTIONARIES_ID()+"');");
			dict.setSubDict(this.listAllDictToCreateCode(dict.getDICTIONARIES_ID()));
			dict.setTarget("treeFrame");
		}
		return dictList;
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return dictionariesMapper.datalistPage(page);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return dictionariesMapper.findById(pd);
	}
	
	/**通过编码获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByBianma(PageData pd)throws Exception{
		return dictionariesMapper.findByBianma(pd);
	}

	/**通过名称获取数据 
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByName(PageData pd)throws Exception{
		return dictionariesMapper.findByName(pd);
	}
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dictionariesMapper.save(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dictionariesMapper.edit(pd);
	}
	
	/**排查表检查是否被占用
	 * @param pd
	 * @throws Exception
	 */
	public PageData findFromTbs(PageData pd)throws Exception{
		return dictionariesMapper.findFromTbs(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dictionariesMapper.delete(pd);
	}

	/**
	 * 根据传过来的父ID参数查询是用途分类还是只是分类的下拉列表
	 * @param PARENT_ID
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<PageData> listByParentID(PageData pd) throws Exception {
		return dictionariesMapper.listByParentID(pd);
	}

	@Override
	public List<PageData> getBadnessMX(PageData pd) throws Exception {
		// TODO 自动生成的方法存根
		return dictionariesMapper.getBadnessMX(pd);
	}
	
	/**通过数据字典名称获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findByDICTIONARIESId(PageData pd)throws Exception{
		return dictionariesMapper.findByDICTIONARIESId(pd);
	}
}
