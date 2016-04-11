package com.est.sysinit.sysfilemanage.action;

import java.util.ArrayList;
import java.util.List;

import com.est.common.base.BaseAction;
import com.est.common.ext.util.TreeObject;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.sysinit.sysfilemanage.service.ISysDirService;
import com.est.sysinit.sysfilemanage.vo.SysDir;

/**
 *@desc 文件目录管理ACTION
 *@author zhanglk
 *@date Jun 24, 2009
 *@path com.est.sysinit.sysfilemanage.action.SysDirAction
 *@corporation Enstrong S&T 
 */
@SuppressWarnings("serial")
public class SysDirAction extends BaseAction {
	
	private SysDir sysDir = new SysDir();
	
	public void setDir(SysDir sysDir){
		this.sysDir = sysDir;
	}
	@Override
	public Object getModel() {
		return sysDir;
	}
	/**
	 * 
	 *@desc 生成文件类别树 
	 *@date Jun 24, 2009
	 *@author zhanglk
	 *@return
	 */
	public String getSysDirTree(){
		ISysDirService dirService = (ISysDirService) getBean("sysDirService");
		String node = req.getParameter("node"); 
		String dirType = req.getParameter("dirType"); //目录类型(RPT:存放报表,FILE:存放文件)
		List<SysDir> typeList = dirService.getSysDirTree(StringUtil.parseLong(node),dirType);
		List<TreeObject> treeList = this.parseSysDirTreeList(typeList);
		if(treeList.size()>0) {
			return toJSON(treeList);
		} else {
			return toSTR("[]");
		}
	}
	/**
	 * 
	 *@desc 获取所有文件类型 
	 *@date Jun 25, 2009
	 *@author zhanglk
	 *@return
	 */
	public String getAllSysDirs(){
		ISysDirService dirService = (ISysDirService) getBean("sysDirService");
		List<SysDir> dirList = dirService.getAllSysDir();
		if(dirList.size()>0) {
			return toJSON(dirList ,"{success:true,rows:","}");
		} else {
			return toSTR("{success:true,rows:[]}");
		}
	}
	/**
	 * 
	 *@desc 将文件类别列表转换成Ext tree json 列表 
	 *@date Jun 24, 2009
	 *@author zhanglk
	 *@param dirList
	 *@return
	 */
	private List<TreeObject> parseSysDirTreeList(List<SysDir> dirList){
		 List<TreeObject> treeList = new ArrayList<TreeObject>();
		 for(SysDir dir : dirList) {
			 TreeObject treeobj = new TreeObject();
			 treeobj.setId(String.valueOf(dir.getDirid()));
			 treeobj.setText(dir.getDirname());
			 treeobj.setQtip(dir.getDirname());
			 treeList.add(treeobj);
		 }
		 return treeList;
	}
	/**
	 * 
	 *@desc 删除文件类别对象
	 *@date Jun 24, 2009
	 *@author zhanglk
	 *@return
	 */
	public String delSysDir(){
		ISysDirService dirService = (ISysDirService) getBean("sysDirService");
		try{
			dirService.delSysDir(sysDir.getDirid());
			return toSTR("{success:true}");
		} catch (Exception ex) {
			return toSTR("{success:false}");
		}
	}
	/**
	 * 
	 *@desc 保存文件类别对象 
	 *@date Jun 24, 2009
	 *@author zhanglk
	 *@return
	 */
	public String savSysDir(){
		ISysDirService dirService = (ISysDirService) getBean("sysDirService");
		
		SysDir parentSysDir= null;
		if(sysDir.getSysDir()!=null) {
			parentSysDir=dirService.getSysDirById(sysDir.getSysDir().getDirid());
		}
		
		String strParentDir="";
		if(parentSysDir!=null){
			strParentDir=parentSysDir.getDirpath();
		}else{
			sysDir.setSysDir(null);
		}
		String dirpath=strParentDir+"/"+req.getParameter("dirname");
		try{
			sysDir.setDirpath(dirpath);
			dirService.savSysDir(sysDir);
			return toSTR("{success:true}");
		} catch (Exception ex) {
			ex.printStackTrace();
			return toSTR("{success:false}");
		}
	}
	/**
	 * 
	 *@desc 获取文件类别对象 
	 *@date Jun 24, 2009
	 *@author zhanglk
	 *@return
	 */
	public String getSysDir(){
		ISysDirService dirService = (ISysDirService) getBean("sysDirService");
		Long dirId = sysDir.getDirid();
		SysDir dir = dirService.getSysDirById(dirId);
		return toJSON(dir,"{success:true,data:","}");
	}
}
