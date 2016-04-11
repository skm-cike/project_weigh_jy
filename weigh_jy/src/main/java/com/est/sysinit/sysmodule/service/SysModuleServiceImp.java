package com.est.sysinit.sysmodule.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.businessutil.SerialNumGeneratorUtil;
import com.est.sysinit.sysmodule.dao.ISysModuleDao;
import com.est.sysinit.sysmodule.vo.SysModule;

/**
 * 
 * @corporation Enstrong S&T
 * @author jingpj
 * @date May 4, 2009
 * @path com.est.sysinit.sysuser.service
 * @name com.est.sysinit.sysuser.service.SysUserServiceImp
 * @description
 */
public class SysModuleServiceImp implements ISysModuleService {

	private final Log log = LogFactory.getLog(SysModuleServiceImp.class);
	private ISysModuleDao sysModuleDao;
	
	/**编号每级递增位数*/
	public static final int SERIAL_LEN = 3;	

	
	
	@SuppressWarnings("unchecked")
	public Result<SysModule> getModuleList(Page page, SearchCondition condition) {
		
		String moduleName = (String) condition.get("moduleName");	//模块名称
		Long parentModuleId = (Long) condition.get("parentModuleId"); //父模块id
		
		
		
		StringBuffer buf = new StringBuffer(200);
		List<Object> paramList = new ArrayList<Object>();
		buf.append("from SysModule t where 1=1");
		if (parentModuleId != null && parentModuleId != 0L) {
			buf.append(" and t.sysModule.moduleid = ?");
			paramList.add(parentModuleId);
		} else {
			buf.append(" and t.sysModule is null");
		}
		if (moduleName != null) {
			buf.append(" and t.modulename like '"+ moduleName +"%' ");
		}
		buf.append(" order by t.orderindex");
		return sysModuleDao.findByPage(page, buf.toString(), paramList.toArray());
	}

	public void setSysModuleDao(ISysModuleDao sysModuleDao) {
		this.sysModuleDao = sysModuleDao;
	}

	public void delModule(Serializable moduleId) {
		sysModuleDao.delById(moduleId);
	}

	public SysModule getModuleById(Serializable moduleId) {
		return sysModuleDao.findById(moduleId);
	}

	@SuppressWarnings("unchecked")
	public List<SysModule> getModuleTree(Serializable moduleId) {
		Long id = (Long) moduleId;
		StringBuffer buf = new StringBuffer(200);
		List<Object> paramList = new ArrayList<Object>();
		buf.append("from SysModule t where 1=1 ");
		if (id != null && id != 0L) {
			buf.append(" and t.sysModule.moduleid = ?");
			paramList.add(id);
		}
		else{
			buf.append(" and t.sysModule is null");
		}
		buf.append(" order by t.orderindex");
		return sysModuleDao.findByHql(buf.toString(), paramList.toArray());
	}

	public void savModule(SysModule sysModule) throws Exception {
		boolean isNew = false;
		if(sysModule == null ){
			return ;
		} else if(sysModule.getModuleid()==null || sysModule.getModuleid()==0){
			isNew = true;
		}
		
		//修改父模块为空时，拦截器将新建一个内容全部为null的对象，导致保存失败，需要手动将父模块设置成null
		if(sysModule.getSysModule() != null && (sysModule.getSysModule().getModuleid() == null||sysModule.getSysModule().getModuleid()==0 )){
			sysModule.setSysModule(null);
		}
		
		//原来的模块
		SysModule oldModule = null; 
		if(sysModule.getModuleid() !=null && sysModule.getModuleid()!=0) {
			oldModule = sysModuleDao.findById(sysModule.getModuleid());
		}
		//新的父模块
		SysModule parentModule = sysModule.getSysModule(); 
		if(parentModule!=null) {
			parentModule = sysModuleDao.findById(parentModule.getModuleid());
		}
		
		
		//修改后，如果父模块id等于自身id，不进行保存
		if(sysModule.getModuleid()!=null && sysModule.getSysModule()!=null) {
			if(sysModule.getModuleid().equals(sysModule.getSysModule().getModuleid())){
				throw new RuntimeException("父模块id等于自身id");
			}
		}
		//修改后，如果将其子模块设置为父模块，不进行保存
		if(oldModule!=null && parentModule!=null && parentModule.getModulecode().startsWith(oldModule.getModulecode())){
			throw new RuntimeException("不能将其子模块设置为父模块");
			
		}
		
		//保存模块时，设置模块的level：父模块为null，设置为1，否则设置为父模块level+1
		
		if(parentModule == null ) {
			sysModule.setLevelnum(1L);
		} else {
			Long parLevel = parentModule.getLevelnum();
			parLevel = parLevel==null ? 0 : parLevel;
			sysModule.setLevelnum(parLevel+1);
		}
		
		
		
		//修改后，为其添加编号（父模块编号＋流水号）
		String parentCode = null;	//父模块编号
		String parentFileurl = null;
		
		if(parentModule != null ) {
			parentModule = sysModuleDao.findById(parentModule.getModuleid());
			parentCode = parentModule.getModulecode();
			parentFileurl = parentModule.getFileurl();
		} 
		//需要修改编码情况：
		if(      // 1 ：新增
				isNew
				 	//2：第一级模块，但编码长度不为SERIAL_LEN
				|| (parentModule == null && sysModule.getModulecode().length() != SERIAL_LEN )
					//3：非一级模块,但编码长度不等于该级长度，或者编码不是以父级编码开始
				|| (parentModule != null &&  (sysModule.getModulecode().length() != SERIAL_LEN * sysModule.getLevelnum() || !sysModule.getModulecode().startsWith(parentCode)))) 
		{
			try{	
				String newCode = new SerialNumGeneratorUtil().getSerialNum(sysModuleDao, "SysModule", "modulecode", parentCode, SERIAL_LEN);
				sysModule.setModulecode(newCode);
				//在不是新增时，修改所有子模块编码、层数
				if(sysModule.getModuleid()!=null && sysModule.getModuleid()!=0){
					setAllSubCode(oldModule.getModulecode(),newCode);
				}
				
			} catch (Exception ex) {
				log.info("修改所有子模块编码、层数错误！");
				throw ex;
			}
		}

		if(oldModule == null || (oldModule != null && oldModule.getFileurl()==null)) {
			if(parentFileurl == null) {
				sysModule.setFileurl("/"+sysModule.getModulename());
			} else {
				sysModule.setFileurl(parentFileurl+"/"+sysModule.getModulename());
			}
		}
		
		if(oldModule!=null) {
			//因为已经从数据库中读取了修改前的模块，为避免hibernate验证错误，这里保存读取的对象
			BeanUtils.copyProperties(oldModule, sysModule);
		} else {
			oldModule = sysModule;
		}
		
		sysModuleDao.save(oldModule);
	}

	
	@SuppressWarnings("unchecked")
	public List<SysModule> getAllModule() {
		StringBuffer buf = new StringBuffer(200);
		List<Object> paramList = new ArrayList<Object>();
		buf.append("from SysModule t ");
		buf.append(" order by t.modulecode");	
		return sysModuleDao.findByHql(buf.toString(), paramList.toArray());
	}
	
	/**
	 * 
	 *@description 在父模块编码改变的时候，修改所有子模块的编码、层数
	 *@date May 29, 2009
	 *@author jingpj
	 *8:18:04 PM
	 *@param oldCode 原编码
	 *@param newCode 新编码
	 * @throws Exception 
	 */
	private void setAllSubCode(String oldCode,String newCode) throws Exception{
		StringBuffer buf = new StringBuffer();
		int oldCodeLen = oldCode.length();
		buf.append("update SysModule set modulecode = '"+newCode+"'||substr(modulecode,"+(oldCodeLen+1)+") ");
		buf.append(",levelnum = length('"+newCode+"'||substr(modulecode,"+(oldCodeLen+1)+"))/"+SERIAL_LEN);
		buf.append(" where modulecode!='"+oldCode+"' and modulecode like '"+oldCode+"%'");
		sysModuleDao.updateByHql(buf.toString());
	}
	
	/**
	 *@desc 通过模块id得到模块的完整路径(用于上方toolbar显示)
	 *@date Aug 15, 2009
	 *@author jingpj
	 *@param moduleId
	 *@return
	 */
	public String getModuleFullPathById(Serializable moduleId){
		SysModule module =  getModuleById(moduleId);
		if(module==null){
			return "";
		}
		StringBuilder buf = new StringBuilder(100);
		buf.append(module.getModulename());
		while(module.getSysModule()!=null){
			module = module.getSysModule();
			buf.insert(0, module.getModulename()+"->");
		}
		return buf.toString();
	}

	/**
	 *@desc 通过模块名称得到模块
	 *@date Sep 6, 2009
	 *@author jingpj
	 *@param modulename
	 *@return
	 */
	@SuppressWarnings("unchecked")
	public SysModule getModuleByModulename(String modulename) {
		StringBuffer buf = new StringBuffer(200);
		buf.append("from SysModule t ");
		buf.append(" where t.modulename = ?");
		buf.append(" order by t.modulecode");	
		List<SysModule>  lst = sysModuleDao.findByHql(buf.toString(), modulename);
		if(lst.size()>0) {
			return lst.get(0);
		} else {
			return null;
		}
	}
	
	
	

}
