package com.est.sysinit.sysauthority.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.est.common.ext.util.classutil.StringUtil;
import com.est.sysinit.sysauthority.dao.ISysDeptModuleDao;
import com.est.sysinit.sysauthority.vo.SysDeptmodule;
import com.est.sysinit.sysmodule.vo.SysModule;


public class SysDeptModuleServiceImp implements ISysDeptModuleService {

	private ISysDeptModuleDao sysDeptModuleDao;
	

	public void setSysDeptModuleDao(ISysDeptModuleDao sysDeptModuleDao) {
		this.sysDeptModuleDao = sysDeptModuleDao;
	}


	
	@SuppressWarnings("unchecked")
	public String getDeptmoduleList(Long deptId) {
		StringBuffer buf = new StringBuffer(200);
		buf.append("select {a.*},{b.*} from SYS_MODULE a left join ");
		buf.append(" (select * from SYS_DEPTMODULE t where t.deptid = " + deptId + ") b");
		buf.append(" on a.MODULEID = b.MODULEID");
		buf.append(" order by a.MODULECODE");
		
		HashMap<String,Class> entities = new HashMap<String,Class>();
		entities.put("a", SysModule.class);
		entities.put("b", SysDeptmodule.class);
		List<Object[]> lst = sysDeptModuleDao.sqlQuery(buf.toString(), entities, null);
		
		return makeupDepAuthorityString(lst);
	}

	/**
	 * 
	 *@description 构建部门授权树字符串
	 *@date May 30, 2009
	 *@author jingpj
	 *10:56:32 AM
	 *@param lst
	 *@return
	 */
	private String makeupDepAuthorityString(List<Object[]> lst){
		
		Document document=DocumentHelper.createDocument();
		
		Element rootElement=document.addElement("ul");	//根节点
		
		//暂存<UL>节点map
		Map<Long,Element> ulMap = new HashMap<Long,Element>();
		
		//<LI>节点临时变量
		Element liElement = null;
		
		//遍历模块
		for(Object[] obj : lst) {
			SysModule module = null;
			SysDeptmodule deptModule = null;
			//模块对象
			if (obj[0] != null) {
				if (obj[0] instanceof SysModule) {
					module = (SysModule)obj[0];
				}
				if (obj[0] instanceof SysDeptmodule) {
					deptModule = (SysDeptmodule)obj[0];
				}
			}
			if (obj[1] != null) {
				if (obj[1] instanceof SysModule) {
					module = (SysModule)obj[1];
				}
				if (obj[1] instanceof SysDeptmodule) {
					deptModule = (SysDeptmodule)obj[1];
				}
			}
			//要将<LI>添加到的<UL>
			Element ulAddToElement = null;
			if (module == null) {
				continue;
			}
			
			if(module.getLevelnum()==1){
				//如果模块为第一级，添加到根节点下
				ulAddToElement = rootElement;
			} else {
				//否则，添加到模块父级节点下
				ulAddToElement = ulMap.get(module.getSysModule().getModuleid());
				if(ulAddToElement == null) {
					continue; //如果没有找到父节点，跳出本次循环
				}
//				  List ls=rootElement.selectNodes("//ul[@id=\"ul"+ module.getSysModule().getModuleid() +"\"]");  
//				  if(ls!=null  && ls.size()>0) {  
//				      //存在  
//				      Element  ele=(Element)ls.get(0);
//				      ulAddToElement = ele;
//				  } else {
//					  continue;
//				  }
				
			}
			
			liElement = ulAddToElement.addElement("li");
			if(module.getSysModules().size()>0){
				//不是叶节点
				Element inputElement = liElement.addElement("input");
				inputElement.addAttribute("type", "checkbox");
				inputElement.addAttribute("name", "chk");
				inputElement.addAttribute("id", module.getModuleid()+"");
				if(deptModule!=null) {
					//如果已经授权，checkbox设置为已选择
					inputElement.addAttribute("checked", "checked");
				}
				inputElement.addAttribute("onclick", "javascript:clkbox('ul"+module.getModuleid()+"',this.checked)");
				
				Element aElement = liElement.addElement("a");
				aElement.addAttribute("href", "javascript:toggleLi('ul"+module.getModuleid()+"')");
				aElement.addAttribute("name", "link");
				aElement.addText(module.getModulename());
				
				Element ulElement = liElement.addElement("ul");
				ulElement.addAttribute("id", "ul"+module.getModuleid());
				ulMap.put(module.getModuleid(), ulElement);
				
			} else {
				//叶节点
				Element checkElement = liElement.addElement("input");
				checkElement.addAttribute("type", "checkbox");
				checkElement.addAttribute("name", "chk");
				if(deptModule!=null) {
					//如果已经授权，checkbox设置为已选择
					checkElement.addAttribute("checked", "checked");
				}
				checkElement.addAttribute("id", ""+module.getModuleid());
				checkElement.addAttribute("onclick", "javascript:clkbox(this)");
				liElement.addText(module.getModulename());
			}
		}
		
		//返回构建好的html
		String deptAuthorityString = document.asXML();
		deptAuthorityString = deptAuthorityString.substring(deptAuthorityString.indexOf(">")+1);
		return deptAuthorityString;
	}
	
	public void saveDeptModuleList(Long deptId,String data) throws Exception {
		
		
		try{
			//删除该部门原来授权
			StringBuilder sb = new StringBuilder();
			sb.append("delete from SysDeptmodule t where t.deptid = ?");
			sysDeptModuleDao.updateByHql(sb.toString(), deptId);
			
			//添加部门授权
			String[] lst = data.split(",");
			List<SysDeptmodule> saveLst = new ArrayList<SysDeptmodule>();
			for(String moduleIdStr : lst) {
				SysDeptmodule saveObj = new SysDeptmodule();
				saveObj.setDeptid(deptId);
				SysModule module = new SysModule();
				module.setModuleid(StringUtil.parseLong(moduleIdStr));
				saveObj.setSysModule(module);
				saveLst.add(saveObj);
			}
			
			sysDeptModuleDao.saveAll(saveLst);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		
	}
	
	
	
	

}
