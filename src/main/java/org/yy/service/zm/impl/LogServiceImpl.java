package org.yy.service.zm.impl;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yy.entity.Page;
import org.yy.entity.PageData;
import org.yy.mapper.dsno1.zm.LogMapper;
import org.yy.service.zm.LogService;
import org.yy.util.IPUtil;
import org.yy.util.Jurisdiction;
import org.yy.util.Tools;
import org.yy.util.UuidUtil;

/**
 * 说明： 日志接口实现类
 * 作者：YuanYes Q356703572
 * 时间：2021-10-12
 * 官网：356703572@qq.com
 * @version
 */
@Service
@Transactional //开启事物
public class LogServiceImpl implements LogService{

	@Autowired
	private LogMapper logMapper;

	/**新增
	 * @param Content 消息
	 * @throws Exception
	 */
	public void save(String Content,String obj,String plc)throws Exception{
		PageData pd = new PageData();
		pd.put("LOG_ID", UuidUtil.get32UUID());
		pd.put("Content",Content);
		pd.put("PLC",plc);
		pd.put("OperationTime", Tools.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
		pd.put("Operator", Jurisdiction.getName());
		pd.put("IP", IPUtil.getIpAddress());
		pd.put("Extend2", obj);
		logMapper.save(pd);
	}

	public void appSave(String USERNAME,String Content,String obj,String plc)throws Exception{
		PageData pd = new PageData();
		pd.put("LOG_ID", UuidUtil.get32UUID());
		pd.put("Content",Content);
		pd.put("PLC",plc);
		pd.put("OperationTime", Tools.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
		pd.put("Operator", USERNAME);
		pd.put("IP", IPUtil.getIpAddress());
		pd.put("Extend2", obj);
		logMapper.save(pd);
	}

	public void autoSave(String Content,String obj,String plc)throws Exception{
		PageData pd = new PageData();
		pd.put("LOG_ID", UuidUtil.get32UUID());
		pd.put("Content",Content);
		pd.put("PLC",plc);
		pd.put("OperationTime", Tools.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
		pd.put("Operator", "系统控制");
		pd.put("IP", IPUtil.getIpAddress());
		pd.put("Extend2", obj);
		logMapper.save(pd);
	}

	public void plcControlSave(String Content,String obj,String plc)throws Exception{
		PageData pd = new PageData();
		pd.put("LOG_ID", UuidUtil.get32UUID());
		pd.put("Content",Content);
		pd.put("PLC",plc);
		pd.put("OperationTime", Tools.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
		pd.put("Operator", "断路器控制");
		pd.put("IP", IPUtil.getIpAddress());
		pd.put("Extend2", obj);
		logMapper.save(pd);
	}

	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		logMapper.delete(pd);
	}

	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		logMapper.edit(pd);
	}

	/**列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return logMapper.datalistPage(page);
	}

	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	public List<PageData> listAll(PageData pd)throws Exception{
		return logMapper.listAll(pd);
	}

	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return logMapper.findById(pd);
	}

	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		logMapper.deleteAll(ArrayDATA_IDS);
	}
}

