package com.est.sysinit.systype.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.est.common.base.BaseAction;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.TreeObject;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.common.ext.util.jsonlibhelper.propertyfilter.JSONPropertyFilter;
import com.est.sysinit.sysdept.service.ISysDeptService;
import com.est.sysinit.sysdept.vo.SysDept;
import com.est.sysinit.systype.service.ISysTypeService;
import com.est.sysinit.systype.vo.SysType;
import com.est.sysinit.sysuser.service.ISysUserService;
import com.est.sysinit.sysuser.vo.SysUser;

/**
 * 
 * 
 * @desc 合同类型Action
 * @author hxing
 * @date 2013-4-2上午08:01:25
 * @path com.est.sysinit.systype.action
 * @corporation Enstrong S&T
 */
public class SysTypeAction extends BaseAction {

//	private SysType sysType = new SysType();
//
//	public SysType getSysType() {
//		return sysType;
//	}
//
//	public void setSysType(SysType sysType) {
//		this.sysType = sysType;
//	}

	@Override
	public Object getModel() {
		return null;
	}

	/**
	 * 转向合同类型管理界面
	 * @return
	 */
	public String fwdContractType() {
		return toJSP("typeManage");
	}
	
	/**
	 * 转向部门与合同类型关系管理界面
	 * @return
	 */
	public String fwdContractTypeDeptGroup(){
		return toJSP("typeDeptManage");
	}
	/**
	 *获取类型树
	 * @return
	 */
	public String findTreeSysTypes(){
		ISysTypeService typeService = (ISysTypeService) getBean("sysTypeService");
		String sysTypeId = req.getParameter("node");
		System.out.println("--------sysTypeId------------"+sysTypeId);
		List<SysType> typeList = typeService.getTreeSysType(StringUtil.parseLong(sysTypeId));
		List<TreeObject> treeList = this.parseSysTypeTreeList(typeList);
		if(treeList.size()>0) {
			return toJSON(treeList);
		} else {
			return toSTR("[]");
		}
	}
	
	
	/**
	 * 获取除了自身及子类外的所有的合同类型
	 * @return
	 */
	public String findAllSysTypes(){
		String typeid = req.getParameter("typeid");
		SearchCondition condition = new SearchCondition();
		condition.set("typeid", typeid);
		ISysTypeService typeService = (ISysTypeService) getBean("sysTypeService");
		List<SysType> typeList = typeService.getAllSysType(condition);
		if(typeList.size()>0) {
			return toJSON(typeList ,"{success:true,rows:","}");
		} else {
			return toSTR("{success:true,rows:[]}");
		}
	}
	
	/**
	 * 分页查询所有的合同类型
	 * @return
	 */
	public String getSysTypesList() {
//		String typename = req.getParameter("typename"); // 查询条件:类型名
		String typeid = req.getParameter("typeid"); // 查询条件：类型id
//		String parenttypeid = req.getParameter("parenttypeid"); // 父合同类型ID

//		System.out.println("--------typeid------->"+typeid);
		SearchCondition condition = new SearchCondition();
//		condition.set("typename", typename);
		condition.set("typeid", typeid);
//		condition.set("parenttypeid", parenttypeid);

		ISysTypeService typeService = (ISysTypeService) getBean("sysTypeService");
		Result<SysType> result = null;
		try {
			result = typeService.getTypeList(getPage(), condition);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return toJSON(result);
	}
	/**保存合同类型
	 * 
	 * @return
	 */
	public String savType(){
		ISysTypeService typeService = (ISysTypeService) getBean("sysTypeService");
		String typeidStr = req.getParameter("typeid");
		String typename = req.getParameter("typename");
		String typecode = req.getParameter("typecode");
		String parenttypeidStr = req.getParameter("parenttypeid");
		String typerule = req.getParameter("typerule");
		String typeremark = req.getParameter("typeremark");
		//实例化类型对象
		SysType sysType = new SysType();
		Long typeid = StringUtil.parseLong(typeidStr);
		Long parenttypeid = StringUtil.parseLong(parenttypeidStr);
//		System.out.println("-----parenttypeid----"+parenttypeid);
		SysType type = new SysType();
		//  修改选中的类型   修改是typeid 不为空
		if(0L != typeid && null !=typeid){
//			System.out.println("--修改原来类型----typeid != 0L---------类型ID是："+typeid);
			List<Object> paramslist = new ArrayList<Object>();
			paramslist.add(typecode);
			paramslist.add(typename);
			if(parenttypeid==0){
				paramslist.add(null);
			}else{
				paramslist.add(parenttypeid);
			}
			paramslist.add(typerule);
			paramslist.add(typeremark);
			paramslist.add(typeid);
			try{
				typeService.updateSysType(paramslist);
				return toSTR("{success:true}");
			}catch (Exception e){
				return toSTR("{success:false}");
			}
		}else{
			//添加新类型
//			System.out.println("----添加新类型---typeid==0L-------------");
			if(0L != parenttypeid && null != parenttypeid){
				//添加有父类型的子类型
//				System.out.println("-------parenttypeid !=0L-------------"+parenttypeid);
				type.setTypeid(parenttypeid);
				sysType.setSysType(type);
			}
			//添加根类型
			sysType.setTypename(typename);
			sysType.setTypecode(typecode);
			sysType.setTyperule(typerule);
			sysType.setTyperemark(typeremark);
			try {
				typeService.savSysType(sysType);
				return toSTR("{success:true}");
			} catch (Exception ex) {
				return toSTR("{success:false}");
			}
		}
		
	}
	
	/**
	 * 删除合同类型
	 * @return
	 */
	public String delType(){
		String typeidStr = req.getParameter("typeid");
		Long typeid = StringUtil.parseLong(typeidStr);
//		System.out.println("----------当前的typeid-------->:" +typeid);
		ISysTypeService typeService = (ISysTypeService) getBean("sysTypeService");
		try {
			typeService.delSysType(typeid);
			return toSTR("{success:true}");
		} catch (Exception ex) {
			return toSTR("{success:false}");
		}
	}
	
	/**
	 * 选中加载一行数据
	 * @return
	 */
	public String getType() {
		String typeid = req.getParameter("typeid");
		ISysTypeService typeService = (ISysTypeService) getBean("sysTypeService");
		SysType type = typeService.getTypeById(StringUtil.parseLong(typeid));
//		this.setJsonPropertyFilter(JSONPropertyFilter.BLACKLIST, new String[]{"SysDept.sysUsers","SysDept.sysDepts","SysDept.sysDept","sysUsermodules"});
		return toJSON(type, "{success: true, data:", "}");
	}
	
	/**
	 * 修改类型
	 * @return
	 */
	public String modifyType(){
		ISysTypeService typeService = (ISysTypeService) getBean("sysTypeService");
		try {
//			typeService.updateSysType(sysType);
			return toSTR("{success:true}");
		} catch (Exception ex) {
			return toSTR("{success:false}");
		}
	}
	/**
	 * 
	 *@description 把类型列表转换成Ext tree json 列表
	 *@date 2013-4-2上午08:01:25
	 *@author hxing
	 *@param typeList
	 *@return
	 */
	private List<TreeObject> parseSysTypeTreeList(List<SysType> typeList){
		 List<TreeObject> treeList = new ArrayList<TreeObject>();
		 for(SysType type : typeList) {
			 TreeObject treeobj = new TreeObject();
			 treeobj.setId(String.valueOf(type.getTypeid()));
			 treeobj.setText(type.getTypename());
			 treeobj.setQtip(type.getTypename());
			 treeList.add(treeobj);
		 }
		 return treeList;
	}
	
	
}
