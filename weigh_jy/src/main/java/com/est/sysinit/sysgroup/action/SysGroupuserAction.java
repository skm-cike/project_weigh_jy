package com.est.sysinit.sysgroup.action;

import com.est.common.base.BaseAction;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.sysinit.sysgroup.service.ISysGroupuserService;
import com.est.sysinit.sysgroup.vo.SysGroupuser;

@SuppressWarnings("serial")
public class SysGroupuserAction extends BaseAction {

	@Override
	public Object getModel() {
		return null;
	}
	/**
	 * 
	 *@desc 分页获取已分配权限用户 
	 *@date Jul 6, 2009
	 *@author zhanglk
	 *@return
	 */
	public String getGroupuserList(){
		String groupId = req.getParameter("groupid");
		String deptId = req.getParameter("deptid");
		String userName = req.getParameter("username");
		SearchCondition sc = new SearchCondition();
		sc.set("groupid", groupId);
		sc.set("deptid", deptId);
		sc.set("username", userName);
		ISysGroupuserService groupUserService = (ISysGroupuserService) getBean("sysGroupuserService");
		Result<SysGroupuser> result = null;
		try{
			result = groupUserService.getGroupuserList(getPage(), sc);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return toJSON(result);
	}
	/**
	 *@desc 批量删除已分配权限用户 
	 *@date Jul 7, 2009
	 *@author zhanglk
	 *@return
	 */
	public String delGroupuserList(){
		String strGroupuser = req.getParameter("groupuserid");//主建id串
		SearchCondition sc = new SearchCondition();
		sc.set("groupuserid", strGroupuser);
		ISysGroupuserService groupUserService = (ISysGroupuserService) getBean("sysGroupuserService");
		try{
			groupUserService.delGroupuserList(sc);
			return toSTR("{success:true}");
		} catch (Exception ex) {
			return toSTR("{success:false}");
		}
	}
	/**
	 *@desc 分配用户组权限  
	 *@date Jul 7, 2009
	 *@author zhanglk
	 *@return
	 */
	public String savGroupuserList(){
		String groupId = req.getParameter("groupid");
		String strUserid = req.getParameter("userid");
		SearchCondition sc = new SearchCondition();
		sc.set("groupid", groupId);
		sc.set("userid", strUserid);
		ISysGroupuserService groupUserService = (ISysGroupuserService) getBean("sysGroupuserService");
		try{
			groupUserService.savGroupuserList(sc);
			return toSTR("{success:true}");
		}catch(Exception ex){
			return toSTR("{success:false}");
		}
	}
	
	
	/**
	 *@desc 是否定期工作用户 
	 *@date Aug 25, 2009
	 *@author jingpj
	 *@return
	 */
	public String isCycleworkUser(){
		String strUserid = req.getParameter("userid");
		Long userid = StringUtil.parseLong(strUserid);
		if(userid == 0) {
			return toSTR("{success:false}");
		}
		ISysGroupuserService groupUserService = (ISysGroupuserService) getBean("sysGroupuserService");
		try {
			boolean isTrue = groupUserService.isCycleworkUser(userid);
			if(isTrue){
				return toSTR("{success:true}");
			} else {
				return toSTR("{success:false}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return toSTR("{success:false}");
		}
	}
}
