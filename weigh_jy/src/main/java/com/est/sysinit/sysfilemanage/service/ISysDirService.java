package com.est.sysinit.sysfilemanage.service;

import java.io.Serializable;
import java.util.List;

import com.est.sysinit.sysfilemanage.vo.SysDir;

/**
 *@desc 实现文件目录类别service接口
 *@author zhanglk
 *@date Jun 24, 2009
 *@path com.est.sysinit.sysfilemanage.service.ISysDirService
 *@corporation Enstrong S&T 
 */
public interface ISysDirService {

	/**
	 * 
	 *@desc 删除 
	 *@date Jun 24, 2009
	 *@author zhanglk
	 *@param dirId
	 */
	public void delSysDir(Serializable dirId);
	/**
	 * 
	 *@desc 生成文件目录类别树
	 *@date Jun 24, 2009
	 *@author zhanglk
	 *@param dirId
	 *@return
	 */
	public List<SysDir> getSysDirTree(Serializable dirId,String dirType);
	/**
	 * 
	 *@desc 保存文件目录类别 
	 *@date Jun 24, 2009
	 *@author zhanglk
	 *@param sysDir
	 */
	public void savSysDir(SysDir sysDir);
	/**
	 * 通过ID获取文件目录类别
	 *@desc 
	 *@date Jun 24, 2009
	 *@author zhanglk
	 *@param dirId
	 *@return
	 */
	public SysDir getSysDirById(Serializable dirId);
	/**
	 * 
	 *@desc 获取所有文件目录类型 
	 *@date Jun 25, 2009
	 *@author zhanglk
	 *@return
	 */
	public List<SysDir> getAllSysDir();
}
