package com.est.sysinit.sysfilemanage.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.sysinit.sysfilemanage.dao.ISysTemplateDao;
import com.est.sysinit.sysfilemanage.vo.SysTemplate;

/**
 * @desc 模板管理service
 * @author zhanglk
 * @date Jun 25, 2009
 * @path com.est.sysinit.sysfilemanage.service.SysTemplateServiceImp
 * @corporation Enstrong S&T
 */
@Service
@Transactional
public class SysTemplateServiceImp implements ISysTemplateService {
	private static final String RUNMODE = "runmode"; // 运行方式默认dircode
	private ISysTemplateDao sysTemplateDao;
    private ISysTemplateService sysTemplateService;
	public void setSysTemplateService(ISysTemplateService sysTemplateService) {
		this.sysTemplateService = sysTemplateService;
	}

	public void setSysTemplateDao(ISysTemplateDao sysTemplateDao) {
		this.sysTemplateDao = sysTemplateDao;
	}

	/**
	 * 
	 * @desc 分页查询模板列表
	 * @date Jun 25, 2009
	 * @author zhanglk
	 * @param page
	 * @param condition
	 * @return
	 */
	public Result<SysTemplate> getTemplateList(Page page,
			SearchCondition condition) {
		String templateName = (String) condition.get("templateName");
		String strDirId = (String) condition.get("dirId");
		String dirCode = (String) condition.get("dirCode");
		Long dirId = StringUtil.parseLong(strDirId);

		StringBuffer buf = new StringBuffer(200);
		List<Object> paramList = new ArrayList<Object>();

		buf.append("from SysTemplate t where 1=1");
		if (dirId != null && dirId != 0) {
			buf.append(" and t.sysDir.dirid = ?");
			paramList.add(dirId);
		}
		if (templateName != null && !"".equals(templateName)) {
			buf.append(" and t.templatename like ? ");
			paramList.add(templateName + "%");
		}
		if (dirCode != null && !"".equals(dirCode)) {
			buf.append(" and t.sysDir.dircode like ?");
			paramList.add(dirCode + "%");
		}
		buf.append(" order by t.templateid");
		return sysTemplateDao.findByPage(page, buf.toString(), paramList
				.toArray());
	}

	/**
	 * 
	 * @desc 删除模板
	 * @date Jun 25, 2009
	 * @author zhanglk
	 * @param templateId
	 */
	public void delTemplate(Serializable templateId) {
		sysTemplateDao.delById(templateId);
	}

	/**
	 * 
	 * @desc 通过ID获取模板
	 * @date Jun 25, 2009
	 * @author zhanglk
	 * @param templateId
	 * @return
	 */
	public SysTemplate getTemplateById(Serializable templateId) {
		return sysTemplateDao.findById(templateId);
	}

	/**
	 * 
	 * @desc 保存模板
	 * @date Jun 25, 2009
	 * @author zhanglk
	 * @param template
	 */
	public void savTemplate(SysTemplate template) {
		sysTemplateDao.save(template);
	}

	/**
	 * 
	 * @desc 通过Code获取模板
	 * @date Jul 14, 2009
	 * @author jingpj
	 * @param templateCode
	 * @return
	 * @see com.est.sysinit.sysfilemanage.service.ISysTemplateService#getTemplateByCode(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public SysTemplate getTemplateByCode(String templateCode) {
		String hql = "from SysTemplate t where t.templatecode = ?";
		Query query = sysTemplateDao.createQuery(hql, templateCode);
		List<SysTemplate> lst = query.list();
		if (lst == null || lst.size() == 0) {
			return null;
		} else {
			return lst.get(0);
		}

	}

	/**
	 * 
	 * @desc 获取运行方式报表模板
	 * @date Jul 31, 2009
	 * @author hebo
	 * @return
	 */
	public List<SysTemplate> getRunModeTemplate() {
		String hql = "from SysTemplate t where t.sysDir.sysDir.dircode=?";
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(SysTemplateServiceImp.RUNMODE);
		return sysTemplateDao.findByHql(hql.toString(), paramList.toArray());
	}
	/**
	 * 
	 *@desc 根据文件编码获取模板
	 *@date Jul 31, 2009
	 *@author Administrator
	 *@return
	 */
	public SysTemplate getSysTemplateByFileNo(String fileno){
		String hql="from SysTemplate t where t.fileno=?";
		return (SysTemplate) sysTemplateDao.findUniqueByHql(hql, fileno);
	}
	
}
