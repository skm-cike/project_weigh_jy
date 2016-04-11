package com.est.sysinit.sysfilemanage.service;

import java.io.Serializable;
import java.util.List;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.sysinit.sysfilemanage.vo.SysTemplate;

/**
 *@desc 模板管理service接口
 *@author zhanglk
 *@date Jun 25, 2009
 *@path com.est.sysinit.sysfilemanage.service.ISysTemplateService
 *@corporation Enstrong S&T 
 */
public interface ISysTemplateService {
	/**
	 * 
	 *@desc 分页查询模板列表
	 *@date Jun 25, 2009
	 *@author zhanglk
	 *@param page
	 *@param condition
	 *@return
	 */
	public Result<SysTemplate> getTemplateList(Page page, SearchCondition condition);
	/**
	 * 
	 *@desc 删除模板 
	 *@date Jun 25, 2009
	 *@author zhanglk
	 *@param templateId
	 */
	public void delTemplate(Serializable templateId);
	/**
	 * 
	 *@desc  通过ID获取模板
	 *@date Jun 25, 2009
	 *@author zhanglk
	 *@param templateId
	 *@return
	 */
	public SysTemplate getTemplateById(Serializable templateId);
	/**
	 * 
	 *@desc 保存模板 
	 *@date Jun 25, 2009
	 *@author zhanglk
	 *@param template
	 */
	public void savTemplate(SysTemplate template);
	/**
	 * 
	 *@desc  通过code获取模板
	 *@date Jul 14, 2009
	 *@author Administrator
	 *@param templateCode
	 *@return
	 */
	public SysTemplate getTemplateByCode(String templateCode);
	/**
	 * 
	 *@desc 获取运行方式报表模板
	 *@date Jul 31, 2009
	 *@author hebo
	 *@return
	 */
	public List<SysTemplate> getRunModeTemplate();
	/**
	 * 
	 *@desc 根据文件编码获取模板
	 *@date Jul 31, 2009
	 *@author Administrator
	 *@return
	 */
	public SysTemplate getSysTemplateByFileNo(String fileno);

}
