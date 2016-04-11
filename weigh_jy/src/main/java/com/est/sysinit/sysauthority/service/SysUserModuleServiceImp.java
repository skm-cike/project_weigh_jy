package com.est.sysinit.sysauthority.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.sysinit.sysauthority.dao.ISysUserModuleDao;
import com.est.sysinit.sysauthority.vo.SysUsermodule;
import com.est.sysinit.sysmodule.dao.ISysModuleDao;
import com.est.sysinit.sysmodule.vo.SysModule;
import com.est.sysinit.sysuser.vo.SysUser;


public class SysUserModuleServiceImp implements ISysUserModuleService {

	private ISysUserModuleDao sysUserModuleDao;
	private ISysModuleDao sysModuleDao;
	
	public void setSysUserModuleDao(ISysUserModuleDao sysUserModuleDao) {
		this.sysUserModuleDao = sysUserModuleDao;
	}
	

	public void setSysModuleDao(ISysModuleDao sysModuleDao) {
		this.sysModuleDao = sysModuleDao;
	}

	/**
	 * 
	 *@desc  根据模块路径查询模块路径ID
	 *@date Dec 5, 2012
	 *@author heb
	 *@param url
	 *@return
	 *@see com.est.sysinit.sysauthority.service.ISysUserModuleService#querySysModuleId(java.lang.String)
	 */
	public Long querySysModuleId(String url) {
		
		if(url!=null){
			String hql = "select moduleid from SysModule t where t.url like '%"+url+"%'";
			
			List<Long> lst =sysModuleDao.findByHql(hql);
			
			if(lst!=null && lst.size()!=0){
				return lst.get(0);
			}
		}
		
		return 0L;
	}
	
	/**
	 * 
	 *@desc 查询用户已分配模块
	 *@date Dec 5, 2012
	 *@author heb
	 *@param user
	 *@return
	 *@see com.est.sysinit.sysauthority.service.ISysUserModuleService#getSysModuleList(com.est.sysinit.sysuser.vo.SysUser)
	 */
	@SuppressWarnings("unchecked")
	public List<SysModule> getSysModuleList(Long userid) {

		//查询 
		StringBuilder sb = new StringBuilder();
		
		sb.append("FROM SysModule t WHERE (t.publicflag = '是' OR t.moduleid IN (SELECT n.sysModule.moduleid FROM SysUsermodule n WHERE n.sysUser.userid = ?) )");
		sb.append(" AND (t.isvalidity != '否' OR t.isvalidity  IS NULL)");
		sb.append(" ORDER BY t.modulecode");
		
		
		List<SysModule> moduleList = sysUserModuleDao.findByHql(sb.toString(), userid);
		//sysUserModuleDao.flushSession();
		//List<SysModule> moduleList = new ArrayList<SysModule>();
		return moduleList;
		
	}


	@SuppressWarnings("unchecked")
	public String getUsermoduleList(Long userId,Long deptId) {
		StringBuffer buf = new StringBuffer(200);
		buf.append("select {a.*},{b.*} from ");
		buf.append(" (select c.* from SYS_MODULE c where c.MODULEID in (select d.MODULEID from SYS_DEPTMODULE d where d.deptid = "+deptId+")) a ");
		buf.append(" left join ");
		buf.append(" (select t.* from SYS_UserMODULE t where t.userid = " + userId + ") b");
		buf.append(" on a.MODULEID = b.MODULEID");
		
		buf.append(" order by a.MODULECODE");
		
		HashMap<String,Class> entities = new HashMap<String,Class>();
		entities.put("a", SysModule.class);
		entities.put("b", SysUsermodule.class);
		List<Object[]> lst = sysUserModuleDao.sqlQuery(buf.toString(), entities, null);
		
		return makeupUserAuthorityString(lst);
	}

	/**
	 * 
	 *@description 构建用户授权树字符串
	 *@date May 30, 2009
	 *@author jingpj
	 *10:56:32 AM
	 *@param lst
	 *@return
	 */
	private String makeupUserAuthorityString(List<Object[]> lst){
		
		Document document=DocumentHelper.createDocument();
		
		Element rootElement=document.addElement("ul");	//根节点
		
		//暂存<UL>节点map
		Map<Long,Element> ulMap = new HashMap<Long,Element>();
		
		//<LI>节点临时变量
		Element liElement = null;
		try{
			//遍历模块
			for(Object[] obj : lst) {
				SysModule module = null;
				SysUsermodule userModule = null;
				//模块对象
				//人员模块授权对象
//				SysDeptmodule deptModule = (SysDeptmodule) obj[1];
				
				if (obj[0] != null) {
					if (obj[0] instanceof SysModule) {
						module = (SysModule)obj[0];
					}
					if (obj[0] instanceof SysUsermodule) {
						userModule = (SysUsermodule)obj[0];
					}
				}
				
				if (obj[1] != null) {
					if (obj[1] instanceof SysModule) {
						module = (SysModule)obj[1];
					}
					if (obj[1] instanceof SysUsermodule) {
						userModule = (SysUsermodule)obj[1];
					}
				}
				
				//要将<LI>添加到的<UL>
				Element ulAddToElement = null;
				if(module == null) {
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
//					  List ls=rootElement.selectNodes("//ul[@id=\"ul"+ module.getSysModule().getModuleid() +"\"]");  
//					  if(ls!=null  && ls.size()>0) {  
//					      //存在  
//					      Element  ele=(Element)ls.get(0);
//					      ulAddToElement = ele;
//					  } else {
//						  continue;
//					  }
					
				}
				
				liElement = ulAddToElement.addElement("li");
				if(module.getSysModules().size()>0){
					//不是叶节点
					Element inputElement = liElement.addElement("input");
					inputElement.addAttribute("type", "checkbox");
					inputElement.addAttribute("name", "chk");
					inputElement.addAttribute("id", module.getModuleid()+"");
					if(userModule!=null) {
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
					checkElement.addAttribute("onclick", "javascript:clkbox(this)");
					if(userModule!=null) {
						//如果已经授权，checkbox设置为已选择
						checkElement.addAttribute("checked", "checked");
					}
					liElement.addText(module.getModulename());
					checkElement.addAttribute("id", ""+module.getModuleid());
					
					Element saveElement = liElement.addElement("input");
					saveElement.addAttribute("type", "checkbox");
					saveElement.addAttribute("class", "save");
					if(userModule==null) {
						//saveElement.addAttribute("disabled","disabled");
					} else {
						if(userModule.getRwflag()!=null &&  userModule.getRwflag().contains("M")) {
							saveElement.addAttribute("checked", "checked");
						}
					}
					liElement.addText("新增/保存");
					Element delElement = liElement.addElement("input");
					delElement.addAttribute("type", "checkbox");
					delElement.addAttribute("class", "delte");
					if(userModule==null) {
						//saveElement.addAttribute("disabled","disabled");
					} else {
						if(userModule.getRwflag()!=null &&  userModule.getRwflag().contains("D")) {
							delElement.addAttribute("checked", "checked");
						}
					}
					liElement.addText("删除");
					
					
				}
			}
			
			//返回构建好的html
			String deptAuthorityString = document.asXML();
			deptAuthorityString = deptAuthorityString.substring(deptAuthorityString.indexOf(">")+1);
			return deptAuthorityString;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	
	
	
	/**
	 * 
	 *@desc 保存用户权限
	 *@date Jun 5, 2009
	 *@author jingpj
	 *@parameter
	 *@see com.est.sysinit.sysauthority.service.ISysUserModuleService#saveUserModuleList(java.lang.Long, java.lang.Long, java.lang.String)
	 */
	public void saveUserModuleList(Long userId,Long deptId,String data) throws Exception {
		
		
		try{
			
			StringBuilder sb = new StringBuilder();
			//删除用户属于所在部门权限的授权
			//sb.append("delete from SysUsermodule t where t.sysModule in (select sysModule from SysDeptmodule m where m.deptid = ? )  and t.sysUser.userid = ?");
			//sysUserModuleDao.updateByHql(sb.toString(), deptId,userId);
			//修改为 删除该用户原来的所有权限
			sb.append("delete from SysUsermodule t where t.sysUser.userid = ?");
			sysUserModuleDao.updateByHql(sb.toString(), userId);
			
			
			//添加用户授权
			JSONArray jsonArr = JSONArray.fromObject(data);
			List<SysUsermodule> saveLst = new ArrayList<SysUsermodule>();
			//jsonArr.iterator();
			Iterator<?> it = jsonArr.iterator();
			while (it.hasNext()) {
				JSONObject jsonObj = (JSONObject) it.next();
				
				
				Long moduleId = StringUtil.parseLong((String) jsonObj.get("moduleid"));
				//Long userId =  StringUtil.parseLong((String) propsMap.get("userid"));
				String rwflag = (String) jsonObj.get("rwflag");
				String modulecode = null;
				
				if(moduleId == 0 || userId == 0) {
					continue;
				}
				SysUsermodule saveObj = new SysUsermodule();
				SysModule module = sysModuleDao.findById(moduleId);
				if(module==null){
					continue;
				} else {
					modulecode = module.getModulecode();
				}
				
				SysUser user = new SysUser();
				user.setUserid(userId);
				
				saveObj.setSysModule(module);
				saveObj.setSysUser(user);
				saveObj.setModulecode(modulecode);
				saveObj.setRwflag(rwflag);
				saveLst.add(saveObj);
				
				
			}
			
			sysUserModuleDao.saveAll(saveLst);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		
	}

	/**
	 * 
	 *@description 查询登录用户可使用模块，并组装成下拉菜单
	 *@date Jun 2, 2009
	 *@author jingpj
	 *4:51:11 PM
	 *@param user
	 *@return
	 */
	@SuppressWarnings("unchecked")
	public String getUserModuleMenu(SysUser user) throws Exception {
		
		//查询 
		StringBuilder sb = new StringBuilder();
		
		sb.append("FROM SysModule t WHERE (t.publicflag = '是' OR t.moduleid IN (SELECT n.sysModule.moduleid FROM SysUsermodule n WHERE n.sysUser = ?) )");
		sb.append(" AND (t.isvalidity != '否' OR t.isvalidity  IS NULL)");
		sb.append(" ORDER BY t.modulecode");
		
		//sb.append("from SysUsermodule t where t.sysUser = ? and (t.sysModule.isvalidity != '否' or t.sysModule.isvalidity  is null) order by t.sysModule.modulecode");
		//List<SysUsermodule> userModuleList =  sysUserModuleDao.findByHql(sb.toString(), user);
		List<SysModule> moduleList = sysUserModuleDao.findByHql(sb.toString(), user);
		
		//拼装
		List<Map<String,Object>> menu = new ArrayList<Map<String,Object>>(); 
		Map<Long,List<Map<String,Object>>> itemContainerMap = new HashMap<Long,List<Map<String,Object>>>();
		
		//for(SysUsermodule userModule : userModuleList) { 
			//SysModule module = userModule.getSysModule();
			//模块
		for(SysModule module : moduleList) {
			
			SysUsermodule userModule = getUserModule(user,module);
			if(userModule == null) {
				userModule = new SysUsermodule();
				userModule.setSysUser(user);
				userModule.setSysModule(module);
			}
			
			Map<String,Object> modulePropMap = new HashMap<String, Object>();
			modulePropMap.put("text",  module.getModulename());
			modulePropMap.put("order", module.getOrderindex());
			
			if(module.getSysModules().size()>0) {
				//不是叶节点
				Map<String,Object> subModuleMap = new HashMap<String, Object>();
				List<Map<String,Object>> subItems = new ArrayList<Map<String,Object>>();
				
				//如果菜单设置了url
				if(module.getUrl()!=null && module.getUrl().trim().length() != 0) {
					String handler = getBtnClickEvent(userModule);
					modulePropMap.put("handler", handler);
				}
				
				//如果设置模块设置为不显示子菜单
				if(!"否".equals(module.getShowsubmenu())){
					modulePropMap.put("menu", subModuleMap);
				}
				subModuleMap.put("items", subItems);
				
				
				itemContainerMap.put(module.getModuleid(), subItems);
				
			} else {
				//是叶节点
				String handler = getBtnClickEvent(userModule);
				modulePropMap.put("handler", handler);
			}
			
			
			if(module.getSysModule() == null) {
				//第一级模块，加到根目录
				int i = 0; //插入位置根据indexorder判断
				for(; i< menu.size(); i++){
					if((Long)menu.get(i).get("order") > module.getOrderindex()){
						break;
					}
				}
				menu.add(i,modulePropMap);
			} else {
				//不是第一级，加到父级
				List<Map<String,Object>> itemContainer = itemContainerMap.get(module.getSysModule().getModuleid());
				
				if(itemContainer != null) {
					int i = 0; //插入位置根据indexorder判断
					for(; i< itemContainer.size(); i++){
						if((Long)itemContainer.get(i).get("order") > module.getOrderindex()){
							break;
						}
					}
					itemContainer.add(i,modulePropMap);
				} else {
					continue;
				}
			}
			
		}
		return JSONArray.fromObject(menu).toString();
	}


	@SuppressWarnings("unchecked")
	private SysUsermodule findSysUsermoduleByModuleAndUser(SysModule module,SysUser user) {
		String hql = "FROM SysUsermodule t WHERE t.sysModule=? AND t.sysUser=?";
		SysUsermodule sysuserModule = (SysUsermodule) sysUserModuleDao.findUniqueByHql(hql, module, user);
		return sysuserModule;
		
	}

	
	/**
	 *@desc 构造菜单中按钮的点击事件方法
	 *@date Nov 25, 2009
	 *@author jingpj
	 *@param userModule
	 *@param module
	 *@return
	 */
	public String getBtnClickEvent(SysUsermodule userModule) {
		StringBuilder handler = new StringBuilder();
		SysModule module = userModule.getSysModule();
		handler.append("function(){");
		handler.append("fwdmodule('");
		handler.append(module.getUrl());
		if(module.getUrl()!=null) {
			if(module.getUrl().contains("?")){
				handler.append("&");
			} else {
				handler.append("?");
			}
			handler.append("_moduleID="+module.getModuleid());
			if(userModule==null) {
				handler.append("&rw=");
			} else {
				handler.append("&rw="+userModule.getRwflag());
			}
		}
		handler.append("');}");
		return handler.toString();
	}
	

	/**
	 *@desc 构造菜单中按钮的点击事件方法
	 *@date Nov 25, 2009
	 *@author jingpj
	 *@param userModule
	 *@param module
	 *@return
	 */
	public String getFrameBtnClickEvent(SysUsermodule userModule) {
		StringBuilder handler = new StringBuilder();
		SysModule module = userModule.getSysModule();
		handler.append("function(){");
		handler.append("framefwdmodule('");
		handler.append(module.getUrl());
		if(module.getUrl()!=null){
			if(module.getUrl().contains("?")){
				handler.append("&");
			} else {
				handler.append("?");
			}
			handler.append("_moduleID="+module.getModuleid());
			if(userModule==null) {
				handler.append("&rw=");
			} else {
				handler.append("&rw="+userModule.getRwflag());
			}
		}
		handler.append("');}");
		return handler.toString();
	}
	
	
	/**
	 *@desc 得到工作流处理任务路径
	 *@date Oct 21, 2009
	 *@author jingpj
	 *@param userId
	 *@param moduleId
	 *@return
	 */
	public String getWfHandleUrl(SearchCondition condition) {
		StringBuilder sb = new StringBuilder();
		
		//Long taskid = (Long) condition.get("taskId");
		Long taskDefinitionId = (Long) condition.get("taskDefinitionId");
		//String masterid = (String) condition.get("masterId");
		Long userId = (Long) condition.get("userId");
		Long moduleId = (Long) condition.get("moduleId");
		//Long wfProcessInstanceId = (Long) condition.get("wfProcessInstanceId");
		
		String hql = "from SysUsermodule t where t.sysModule.moduleid=? and t.sysUser.userid = ?";
		SysUsermodule userModule = (SysUsermodule) sysUserModuleDao.findUniqueByHql(hql, moduleId,userId);
		SysModule module = userModule.getSysModule();
		
		sb.append(module.getUrl());
		if(module.getUrl()!=null){
			if(module.getUrl().contains("?")){
				sb.append("&");
			} else {
				sb.append("?");
			}
			sb.append("_moduleID="+module.getModuleid());
			sb.append("&rw="+userModule.getRwflag());
			sb.append("&_isWf=1");
			//sb.append("&_taskId="+taskid);
			//sb.append("&_masterid="+masterid);
			sb.append("&_taskDefid="+taskDefinitionId);
			//sb.append("&_wfProcessInstanceId="+wfProcessInstanceId);
		}
		return sb.toString();
	}
	

	/**
	 *@desc 得到用户的模块按钮
	 *@date Sep 1, 2009
	 *@author jingpj
	 *@param user 用户
	 *@param rootModuleName 作为根模块的模块名称
	 *@return
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	public List<SysUsermodule> getUserModuleButtons(SysUser user, String rootModuleName) {
		String hql = "select m.modulecode from SysModule m where m.modulename = ? )";
		String modulecode = (String) sysUserModuleDao.findUniqueByHql(hql, rootModuleName);
		modulecode = modulecode + "%";
		hql = "from SysUsermodule t where t.sysUser = ? and t.sysModule.modulecode like ? and not exists(from SysModule n where n.sysModule = t.sysModule) order by t.sysModule.orderindex";
		List<SysUsermodule> userModuleList =  sysUserModuleDao.findByHql(hql, user,modulecode);
		return userModuleList;
	}

	/**
	 *@desc 得到用户的模块按钮
	 *@date Sep 1, 2009
	 *@author jingpj
	 *@param user 用户
	 *@param rootModuleName 作为根模块的模块名称
	 *@return
	 */
	public List<SysUsermodule> getUserModuleButtons(SysUser user, Long moduleId) {
		String hql = "select m.modulecode from SysModule m where m.moduleid = ? )";
		String modulecode = (String) sysUserModuleDao.findUniqueByHql(hql, moduleId);
		modulecode = modulecode + "%";
		hql = "from SysUsermodule t where t.sysUser = ? and t.sysModule.modulecode like ? and not exists(from SysModule n where n.sysModule = t.sysModule) order by t.sysModule.orderindex";
		List<SysUsermodule> userModuleList =  sysUserModuleDao.findByHql(hql, user,modulecode);
		return userModuleList;
	}
	
	private SysUsermodule getUserModule(SysUser user, SysModule module) {
		String hql = "FROM SysUsermodule WHERE sysUser=? AND sysModule=?";
		SysUsermodule usermodule = (SysUsermodule) sysUserModuleDao.findUniqueByHql(hql, user, module);
		return usermodule;
	}


	public String getGuideModuleForAccordion(Long userid) {
		String sql = "select distinct t1.MODULENAME,t1.URL,t1.MODULEID,t1.PARENTID,t1.\"ORDERINDEX\"," +
				" (select t3.moduleid from SYS_MODULE t3 where t3.moduleid!=t1.moduleid and t3.parentid is null start with t3.moduleid=t1.moduleid " +
				" connect by prior t3.parentid=t3.moduleid) as topid " + 
				" from SYS_MODULE t1,SYS_USERMODULE t2 where t1.MODULEID=t2.MODULEID and (t2.USERID="+userid + " or t1.PUBLICFLAG='是')" +
				" and (t1.ISVALIDITY!='否' or t1.ISVALIDITY is null) order by t1.\"ORDERINDEX\"";
		List list = sysUserModuleDao.sqlQuery(sql);
		List onLevelList = new ArrayList();
		Map<Long, List> map = new HashMap();
		//获取一级菜单
		for (int i = 0; i < list.size(); i++) {
			Object[] objs = (Object[])list.get(i);
			if (objs[5] == null) {
				onLevelList.add(objs);
				map.put(((BigDecimal)objs[2]).longValue(), new ArrayList());
			}
		}
		list.removeAll(onLevelList);
		
		//为一级菜单添加二级菜单
		for (int i = 0; i < list.size(); i++) {
			Object[] objs = (Object[])list.get(i);
			if (map.get(((BigDecimal)objs[5]).longValue()) != null) {
				map.get(((BigDecimal)objs[5]).longValue()).add(objs);
			}
		}
		
		//组合成为EXTJS 使用的 手风琴菜单 字符串
		StringBuilder buffer = new StringBuilder(1000);
		for (int i = 0; i < onLevelList.size(); i++) {
			Object[] objs = (Object[])onLevelList.get(i);
			//一级菜单
			buffer.append(",{title:'" + (String)objs[0] + "',autoScroll:true,html:'");
			
			//二级菜单
			List subList = map.get(((BigDecimal)objs[2]).longValue());
			for (int j = 0; j < subList.size(); j++) {
				Object[] subObj = (Object[])subList.get(j);
				if (!StringUtil.isEmpty((String)subObj[1])) {
					buffer.append("<div class=\"submodulebtn\"" + " onclick=fwdPage(\"" + (String)subObj[1] + "?_moduleID=" + ((BigDecimal)subObj[2]).longValue() + "\")><img src=\"/fuel_jt/img/portal/cog.png\"></img>" + (String)subObj[0] + "</div>");
				}
			}
			buffer.append("'}");
		}
		if (onLevelList.size() != 0) {
			buffer.replace(0, 1, "");
		}
		buffer.insert(0, "[").append("]");
		return buffer.toString();
	}
}
