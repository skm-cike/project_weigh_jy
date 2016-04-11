package com.est.sysinit.systype.action;

import java.util.ArrayList;
import java.util.List;

import com.est.common.base.BaseAction;
import com.est.common.ext.util.TreeObject;
import com.est.sysinit.systype.service.ITypeForTreeService;
import com.est.sysinit.systype.vo.SysType;
/**
 * 
 * @描述: 用以生成类型树
 * @作者: 陆华
 * @时间: 2013-4-9 上午05:19:33
 */
public class TypeForTreeAction extends BaseAction{
	private ITypeForTreeService typeForTreeService;
	@Override
	public Object getModel() {
		return null;
	}
	
	public void setTypeForTreeService(ITypeForTreeService typeForTreeService) {
		this.typeForTreeService = typeForTreeService;
	}

	/**
	 * 
	 * @描述: 获得子类型节点
	 * @作者: 陆华
	 * @时间: 2013-4-9 上午05:31:23
	 * @return
	 */
	public String getNode() {
		List<SysType> typeList = typeForTreeService.getSubType(this.params);
		List<TreeObject> treeList = this.creatTreeList(typeList);
		if(treeList.size()>0) {
			return toJSON(treeList);
		} else {
			return toSTR("[]");
		}
	}
	
	/**
	 * 
	 * @描述: 获得当前登录人员所属部门拥有的一级类型代码
	 * @作者: 陆华
	 * @时间: 2013-4-15 上午03:57:00
	 * @return
	 */
	public String getTopTypeStr() {
		List<SysType> list = typeForTreeService.getTopTypeStr(this.getCurrentUser());
		if(list.size()>0) {
			return toJSON(list);
		} else {
			return toSTR("[]");
		}
	}
	
	/**
	 * 
	 * @描述: 获得分配给相关部门的子类型
	 * @作者: 陆华
	 * @时间: 2013-4-15 上午03:55:30
	 * @return
	 */
	public String getNodeByDept() {
		List<SysType> typeList = typeForTreeService.getNodeByDept(this.getCurrentUser().getSysDept().getDeptid(),
				(String)this.params.get("node"));
		List<TreeObject> treeList = this.creatTreeList(typeList);
		if(treeList.size()>0) {
			return toJSON(treeList);
		} else {
			return toSTR("[]");
		}
	}

	/**
	 * 
	 * @描述: 生成节点
	 * @作者: 陆华
	 * @时间: 2013-4-9 上午05:31:37
	 * @param typeList
	 * @return
	 */
	private List<TreeObject> creatTreeList(List<SysType> typeList) {
		List<TreeObject> treeList = new ArrayList();
		for (int i = 0; i < typeList.size(); i++) {
			TreeObject to = new TreeObject();
			SysType st = typeList.get(i);
			to.setId(st.getTypecode());
			to.setText(st.getTypename());
			to.setQtip(st.getTypename());
			treeList.add(to);
		}
		return treeList;
	}
}
