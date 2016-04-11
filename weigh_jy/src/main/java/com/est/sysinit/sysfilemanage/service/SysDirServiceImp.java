package com.est.sysinit.sysfilemanage.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.est.sysinit.sysfilemanage.dao.ISysDirDao;
import com.est.sysinit.sysfilemanage.vo.SysDir;


/**
 *@desc 文件夹service
 *@author zhanglk
 *@date Jun 24, 2009
 *@path com.est.sysinit.sysfilemanage.service.SysDirServiceImp
 *@corporation Enstrong S&T 
 */
public class SysDirServiceImp implements ISysDirService{
	private ISysDirDao sysDirDao;
	public void setSysDirDao(ISysDirDao sysDirDao) {
		this.sysDirDao = sysDirDao;
	}
	/**
	 * 
	 *@desc 删除 
	 *@date Jun 24, 2009
	 *@author zhanglk
	 *@param dirId
	 */
	public void delSysDir(Serializable dirId){
		sysDirDao.delById(dirId);
	}
	/**
	 * 
	 *@desc 生成文件目录类别树
	 *@date Jun 24, 2009
	 *@author zhanglk
	 *@param dirId
	 *@return
	 */
	public List<SysDir> getSysDirTree(Serializable dirId,String dirType){
		Long id = (Long)dirId;
		StringBuffer buf = new StringBuffer(200);
		List<Object> paramList = new ArrayList<Object>();
		buf.append("from SysDir t where ");
		if(id!=null && id!=0L) {
			buf.append(" t.sysDir.dirid = ?");
			paramList.add(id);
		} else {
			buf.append(" t.sysDir is null");
			if(dirType!=null) {
				buf.append(" and t.dirType = ?");
				paramList.add(dirType);
			}
		}
		buf.append(" order by t.dirid");
		return sysDirDao.findByHql(buf.toString(), paramList.toArray());
	}
	/**
	 * 
	 *@desc 保存文件目录类别 
	 *@date Jun 24, 2009
	 *@author zhanglk
	 *@param sysDir
	 */
	public void savSysDir(SysDir sysDir){
		//修改父类别为空时，拦截器将新建一个内容全部为null的对象，导致保存失败，需要手动将父类别设置成null
		if(sysDir.getSysDir()!=null && 
				sysDir.getSysDir().getDirid()==null){
			sysDir.setSysDir(null);
		}
		sysDirDao.save(sysDir);
	}
	/**
	 * 通过ID获取文件目录类别
	 *@desc 
	 *@date Jun 24, 2009
	 *@author zhanglk
	 *@param dirId
	 *@return
	 */
	public SysDir getSysDirById(Serializable dirId){
		if(dirId==null || (Long)dirId==0L){
			return null;
		}
		return sysDirDao.findById(dirId);
	}
	/**
	 * 
	 *@desc 获取所有文件目录类型 
	 *@date Jun 25, 2009
	 *@author zhanglk
	 *@return
	 */
	public List<SysDir> getAllSysDir(){
		return sysDirDao.findAll();
	}
}
