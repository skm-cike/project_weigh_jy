package com.est.sysinit.sysdept.action;

import java.util.ArrayList;
import java.util.List;

import com.est.common.base.BaseAction;
import com.est.common.ext.util.TreeObject;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.sysinit.sysdept.service.ISysDeptService;
import com.est.sysinit.sysdept.vo.SysDept;
/**
 * 
 * @corporation Enstrong S&T
 * @author jingpj
 * @date May 5, 2009
 * @path com.est.sysinit.sysdept.action
 * @name com.est.sysinit.sysdept.action.SysDeptAction
 * @description 系统模块->用户管理->系统部门树Action
 */
public class SysDeptAction extends BaseAction{
	
	private SysDept dept = new SysDept();
	
	public void setDept(SysDept dept) {
		this.dept = dept;
	}

	@Override
	public Object getModel() {
		return dept;
	}
	
	/**
	 * 
	 *@description 得到部门树，返回结果为参数部门的所有子部门
	 *@date May 5, 2009
	 *@author jingpj
	 *3:50:59 PM
	 *@return
	 */
	public String getDepartTree(){
		ISysDeptService deptService = (ISysDeptService) getBean("sysDeptService");
		String node = req.getParameter("node"); 
		List<SysDept> deptList = deptService.getDepartTree(StringUtil.parseLong(node));
		List<TreeObject> treeList = this.parseDeptTreeList(deptList);
		if(treeList.size()>0) {
			return toJSON(treeList);
		} else {
			return toSTR("[]");
		}
	}
	
	
	/**
	 * 
	 *@description 得到所有部门，combo使用
	 *@date May 22, 2009
	 *@author jingpj
	 *11:25:31 AM
	 *@return 
	 */
	public String getAllDepart(){
		ISysDeptService deptService = (ISysDeptService) getBean("sysDeptService");
		List<SysDept> deptList = deptService.getAllDepart();
		if(deptList.size()>0) {
			return toJSON(deptList ,"{success:true,rows:","}");
		} else {
			return toSTR("{success:true,rows:[]}");
		}
	}
	
	/**
	 * 
	 *@description 删除部门
	 *@date May 5, 2009
	 *@author jingpj
	 *3:52:29 PM
	 *@return
	 */
	public String delDept(){
		ISysDeptService deptService = (ISysDeptService) getBean("sysDeptService");
		try{
			deptService.delDept(dept.getDeptid());
			return toSTR("{success:true}");
		} catch (Exception ex) {
			return toSTR("{success:false,error:'"+ex.getMessage()+"'}");
		}
	}
	
	/**
	 * 
	 *@description 保存部门
	 *@date May 5, 2009
	 *@author jingpj
	 *3:52:48 PM
	 *@return
	 */
	public String savDept(){
		ISysDeptService deptService = (ISysDeptService) getBean("sysDeptService");
		try{
			deptService.savDept(dept);
			return toSTR("{success:true}");
		} catch (Exception ex) {
			ex.printStackTrace();
			return toSTR("{success:false}");
		}
	}
	
	/**
	 * 
	 *@description 得到部门
	 *@date May 22, 2009
	 *@author jingpj
	 *11:01:34 AM
	 *@return
	 */
	public String getDept(){
		ISysDeptService deptService = (ISysDeptService) getBean("sysDeptService");
		Long deptId = dept.getDeptid();
		SysDept sysdept = deptService.getDeptById(deptId);
		return toJSON(sysdept,"{success:true,data:","}");
	}
	
	
	/**
	 * 
	 *@description 将部门列表转换成Ext tree json 列表
	 *@date May 5, 2009
	 *@author jingpj
	 *3:53:05 PM
	 *@param deptList
	 *@return
	 */
	private List<TreeObject> parseDeptTreeList(List<SysDept> deptList){
		 List<TreeObject> treeList = new ArrayList<TreeObject>();
		 for(SysDept dept : deptList) {
			 TreeObject treeobj = new TreeObject();
			 treeobj.setId(String.valueOf(dept.getDeptid()));
			 //treeobj.setLeaf(false);
			 treeobj.setText(dept.getDeptname());
			 treeobj.setQtip(dept.getDeptname());
			 treeList.add(treeobj);
		 }
		 return treeList;
	}
	 
}
