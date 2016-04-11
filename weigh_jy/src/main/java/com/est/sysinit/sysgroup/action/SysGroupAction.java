package com.est.sysinit.sysgroup.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.est.common.base.BaseAction;
import com.est.common.exception.BaseBussinessException;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.TreeObject;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;
import com.est.sysinit.sysgroup.service.ISysGroupService;
import com.est.sysinit.sysgroup.vo.SysGroup;

@SuppressWarnings("serial")
public class SysGroupAction extends BaseAction {

	@Override
	public Object getModel() {
		return null;
	}
	
	/**
	 * 
	 *@description 得到角色列表
	 *@date Jun 18, 2009
	 *@author jingpj
	 *12:03:24 AM
	 *@return
	 */
	public String getGroupList(){
		ISysGroupService groupService = (ISysGroupService) getBean("sysGroupService");
		SearchCondition condition = new SearchCondition();
		condition.set("grouptype", req.getParameter("grouptype"));
		condition.set("groupname", req.getParameter("groupname"));
		Result<SysGroup> result = groupService.getGroupList(getPage(), condition);
		return toJSON(result);
	}
	
	/**
	 * 
	 *@description 得到所有角色，combo使用
	 *@date May 22, 2009
	 *@author jingpj
	 *@return 
	 */
	public String getAllGroup(){
		ISysGroupService groupService = (ISysGroupService) getBean("sysGroupService");
		List<SysGroup> groupList = groupService.getAllGroup();
		if(groupList.size()>0) {
			return toJSON(groupList ,"{success:true,rows:","}");
		} else {
			return toSTR("{success:true,rows:[]}");
		}
	}
	
	public String savGroupList(){
		ISysGroupService groupService = (ISysGroupService) getBean("sysGroupService");
		String data = req.getParameter("data");
		try {
			data = java.net.URLDecoder.decode(data,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		EditableGridDataHelper editGridData = EditableGridDataHelper.data2bean(data, SysGroup.class);
		
		try{
			groupService.savChanges(editGridData);
			return toSTR("{success:true}");
		}catch(Exception ex) {
			throw new BaseBussinessException("删除失败，请先删除角色下分配的用户！");
		}
		
	}
	
	/**
	 * 
	 *@description 得到角色树
	 *@date May 5, 2009
	 *@author jingpj
	 *3:50:59 PM
	 *@return
	 */
	public String getGroupTree(){
		ISysGroupService groupService = (ISysGroupService) getBean("sysGroupService");
		String node = req.getParameter("node"); 
		List<SysGroup> groupList = groupService.getGroupTree(StringUtil.parseLong(node));
		List<TreeObject> treeList = this.parseGroupTreeList(groupList);
		return toJSON(treeList);
	}
	
	/**
	 * 
	 *@description 将角色列表转换成Ext tree json 列表
	 *@date May 5, 2009
	 *@author jingpj
	 *3:53:05 PM
	 *@param deptList
	 *@return
	 */
	private List<TreeObject> parseGroupTreeList(List<SysGroup> groupList){
		 List<TreeObject> treeList = new ArrayList<TreeObject>();
		 for(SysGroup group : groupList) {
			 TreeObject treeobj = new TreeObject();
			 treeobj.setId(String.valueOf(group.getGroupid()));
			 treeobj.setText(group.getGroupname());
			 treeobj.setQtip(group.getGroupdesc());
			 treeList.add(treeobj);
		 }
		 return treeList;
	}	
	
	/**
	 * 
	 *@description 跳转到group主界面
	 *@date Jun 12, 2009
	 *@author jingpj
	 *@return
	 */
	public String fwdGroupMain(){
		String grouptype = req.getParameter("grouptype");
		req.setAttribute("grouptype", grouptype==null?"":grouptype);
		return toJSP("group");
	}

}
