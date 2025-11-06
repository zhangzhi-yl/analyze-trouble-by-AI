package org.yy.service.system.impl;

import java.util.Date;
import java.util.List;

import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.system.FHlogMapper;
import org.yy.service.system.FHlogService;
import org.yy.util.DateUtil;
import org.yy.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 说明：操作日志记录接口实现类
 * 作者：YuanYe Q356703572
 * 
 */
@Service
@Transactional //开启事物
public class FHlogServiceImpl implements FHlogService {
	
	@Autowired
	private FHlogMapper fHlogMapper;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(String USERNAME,String MODEL,String TABLENAME,String TABLEID, String CONTENT)throws Exception{
		PageData pd = new PageData();
		pd.put("USERNAME", USERNAME);						//用户名
		pd.put("MODEL", MODEL);//功能模块
		pd.put("TABLENAME", TABLENAME);//表名
		pd.put("TABLEID", TABLEID);//表id
		pd.put("CONTENT", CONTENT);							//事件
		pd.put("FHLOG_ID", UuidUtil.get32UUID());			//主键
		pd.put("CZTIME", DateUtil.date2Str(new Date()));	//操作时间
		fHlogMapper.save(pd);
	}
	public void save(String USERNAME, String CONTENT)throws Exception{
		PageData pd = new PageData();
		pd.put("USERNAME", USERNAME);						//用户名
		pd.put("CONTENT", CONTENT);							//事件
		pd.put("FHLOG_ID", UuidUtil.get32UUID());			//主键
		pd.put("CZTIME", DateUtil.date2Str(new Date()));	//操作时间
		fHlogMapper.save(pd);
	}
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		fHlogMapper.delete(pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return fHlogMapper.datalistPage(page);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		fHlogMapper.deleteAll(ArrayDATA_IDS);
	}

}
