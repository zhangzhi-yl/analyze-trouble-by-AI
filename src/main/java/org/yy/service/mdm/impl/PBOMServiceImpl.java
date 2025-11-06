package org.yy.service.mdm.impl;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.entity.mdm.PBOM;
import org.yy.entity.mdm.PBOMNew;
import org.yy.mapper.dsno1.mdm.PBOMMapper;
import org.yy.service.mdm.PBOMService;

/** 
 * 说明： 配方管理接口实现类
 * 作者：YuanYe Q356703572
 * 时间：2020-01-13
 * 
 * @version
 */
@Service
@Transactional //开启事物
public class PBOMServiceImpl implements PBOMService{

	@Autowired
	private PBOMMapper pbomMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		pbomMapper.save(pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		pbomMapper.delete(pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		pbomMapper.edit(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return pbomMapper.datalistPage(page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return pbomMapper.listAll(pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return pbomMapper.findById(pd);
	}

	/**
	 * 通过ID获取其子级列表
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public List<PBOM> listByParentId(PageData pd) throws Exception {
		return pbomMapper.listByParentId(pd);
	}
	/**
	 * 鑾峰彇鎵�鏈夋暟鎹苟濉厖姣忔潯鏁版嵁鐨勫瓙绾у垪琛�(閫掑綊澶勭悊)
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<PBOM> listTree(PageData pd) throws Exception {
		List<PBOM> valueList = this.listByParentId(pd);
		for(PBOM fhentity : valueList){
			if("0".equals(fhentity.getFSUBNUM()) && !"0".equals(fhentity.getFFLEVEL())) {
				fhentity.setTreeurl("pbom_list.html?PBOM_ID="+fhentity.getPBOM_ID()+"&SHOW="+fhentity.getSHOW()+"&FLAST="+"FLAST");
			}else {
				fhentity.setTreeurl("pbom_list.html?PBOM_ID="+fhentity.getPBOM_ID()+"&SHOW="+fhentity.getSHOW());
			}
			PageData pdNew=new PageData();
			pdNew.put("parentId", fhentity.getPBOM_ID());
			fhentity.setSubPBOM(this.listTree(pdNew));
			fhentity.setTarget("treeFrame");
		}
		return valueList;
	}
	/**
	 * 获取所有数据并填充每条数据的子级列表(递归处理)
	 * @param MENU_ID
	 * @return
	 * @throws Exception
	 */
	public List<PBOMNew> listTreeNew(PageData pd) throws Exception {
		List<PBOMNew> valueList = this.listByParentIdNew(pd);
		System.out.println(valueList.size());
		//System.out.println(pd+"KEYWORDS");
		for(PBOMNew fhentity : valueList){
			fhentity.setTreeurl("pbom_list.html?PBOM_ID="+fhentity.getPBOM_ID());
			pd.put("parentId", fhentity.getPBOM_ID());
			pd.put("KEYWORDS","VUENULL");
			fhentity.setOpen(true);
			fhentity.setSubPBOM(this.listTreeNew(pd));
			fhentity.setTarget("treeFrame");
		}
		return valueList;
	}
	/**
	 * 通过ID获取其子级列表
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public List<PBOMNew> listByParentIdNew(PageData pd) throws Exception {
		return pbomMapper.listByParentIdNew(pd);
	}
	
	/**更新配方版本号
	 * @param pd
	 * @throws Exception
	 */
	public void editVersions(PageData pd)throws Exception{
		pbomMapper.editVersions(pd);
	}
	
	/**更新配方发布状态
	 * @param pd
	 * @throws Exception
	 */
	public void editState(PageData pd)throws Exception{
		pbomMapper.editState(pd);
	}
	
	/**通过顶级ID查询列表
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAllId(PageData pd)throws Exception{
		return pbomMapper.listAllId(pd);
	}
	
	/**归档配方
	 * @param pd
	 * @throws Exception
	 */
	public void saveGD(PageData pd)throws Exception{
		pbomMapper.saveGD(pd);
	}

	/**根据根节点id查询所有
	 * @param pd
	 * @return
	 */
	@Override
	public List<PageData> listAllCopy(PageData pd) throws Exception {
		return pbomMapper.listAllCopy(pd);
	}

	/**临时表插入数据
	 * @param copypd
	 */
	@Override
	public void savecopy(PageData copypd) throws Exception {
		pbomMapper.savecopy(copypd);
	}

	/**更新临时表主键
	 * @param copypd
	 */
	@Override
	public void updateBomIdAndPid(PageData copypd) throws Exception {
		pbomMapper.updateBomIdAndPid(copypd);
	}

	/**获取临时表数据
	 * @param pd
	 */
	@Override
	public List<PageData> copylistAll(PageData pd) throws Exception {
		return pbomMapper.copylistAll(pd);
	}

	/**删除临时表数据
	 * @param pd
	 */
	@Override
	public void deletecopylistAll(PageData pd) throws Exception {
		pbomMapper.deletecopylistAll(pd);
	}

	/**更换母件-更新本级父级节点和层次
	 * @param pdUpNow
	 */
	@Override
	public void updateParent(PageData pdUpNow) throws Exception {
		pbomMapper.updateParent(pdUpNow);
	}

	/**更新子级根节点和层次
	 * @param pd
	 * @return
	 */
	@Override
	public void updateRoot(PageData pd) throws Exception {
		List<PBOMNew> varList=listTreeNew(pd);
		for(PBOMNew pbom :varList) {
			pd.put("PBOM_ID", pbom.getPBOM_ID());		
			int DIFFLEVEL=Integer.parseInt(pd.get("DIFFLEVEL").toString());
			int FLEVELNow=Integer.parseInt(pbom.getFFLEVEL());
			int FLEVEL=FLEVELNow-DIFFLEVEL;
			pd.put("FLEVEL", FLEVEL);		
			pbomMapper.updateRoot(pd);
		}
		
	}
}

