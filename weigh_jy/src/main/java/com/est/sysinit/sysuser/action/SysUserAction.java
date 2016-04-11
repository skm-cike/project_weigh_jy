package com.est.sysinit.sysuser.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.servlet.http.HttpSession;

import com.est.common.base.BaseAction;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.common.ext.util.jsonlibhelper.propertyfilter.JSONPropertyFilter;
import com.est.sysinit.sysauthority.service.ISysUserModuleService;
import com.est.sysinit.sysdept.service.ISysDeptService;
import com.est.sysinit.sysdept.vo.SysDept;
import com.est.sysinit.syslog.service.ISysLogService;
import com.est.sysinit.syslog.vo.SysLog;
import com.est.sysinit.sysuser.service.ISysUserService;
import com.est.sysinit.sysuser.vo.SysUser;

/**
 * 
 * @corporation Enstrong S&T
 * @author jingpj
 * @date May 5, 2009
 * @path com.est.sysinit.sysuser.action
 * @name com.est.sysinit.sysuser.action.SysUserAction
 * @description 系统模块->用户管理->用户管理Action
 */
public class SysUserAction extends BaseAction {
	private static final long serialVersionUID = -5236565446298628901L;
	
	// model object
	private SysUser user = new SysUser();

	public void setUser(SysUser user) {
		this.user = user;
	}

	@Override
	public Object getModel() {
		return user;
	}

	/**
	 * 
	 * @description 分页查询用户列表
	 * @date May 5, 2009
	 * @author jingpj 4:05:06 PM
	 * @return
	 */
	public String getUserList() {
		String userName = req.getParameter("userName"); // 查询条件:用户名
		String deptId = req.getParameter("deptId"); // 查询条件：部门id
		String includeSubDept = req.getParameter("includeSubDept"); // 是否包括子部门用户

		SearchCondition condition = new SearchCondition();
		condition.set("userName", userName);
		condition.set("deptId", deptId);
		condition.set("includeSubDept", includeSubDept);

		ISysUserService userService = (ISysUserService) getBean("sysUserService");
		Result<SysUser> result = null;
		try {
			result = userService.getUserList(getPage(), condition);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return toJSON(result);
	}

	/**
	 * 
	 * @description 分页查询用户列表
	 * @date May 5, 2009
	 * @author jingpj 4:05:06 PM
	 * @return
	 */
	public String getFirstLevelDepUserList() {
		SearchCondition condition = new SearchCondition();
		SysUser sysUser = (SysUser) req.getSession().getAttribute("loginUser");
		String strDeptId = req.getParameter("deptId");
		if (!"000".equals(sysUser.getSysDept().getDeptcode())) { // 000-代表管理员
			Long deptId = sysUser.getSysDept().getDeptid();
			// 当前用户的第一级部门
			ISysDeptService deptService = (ISysDeptService) getBean("sysDeptService");

			SysDept firstLevelDept = deptService.findFirstLevelDept(deptId);
			condition.set("deptId", firstLevelDept.getDeptid() + "");
			condition.set("includeSubDept", "true");
		}

		condition.set("userName", req.getParameter("userName"));
		if(strDeptId != null) {
			condition.set("deptId", strDeptId);
		}
		
		
		ISysUserService userService = (ISysUserService) getBean("sysUserService");
		Result<SysUser> result = null;
		try {
			result = userService.getUserList(getPage(), condition);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return toJSON(result);
	}

	public String getUser() {
		String userid = req.getParameter("userid");
		ISysUserService userService = (ISysUserService) getBean("sysUserService");
		SysUser user = userService.getUserById(StringUtil.parseLong(userid));
		user.setSysGroupusers(null);
		user.setSysUsermodules(null);
		user.getSysDept().setSysDept(null);
		user.getSysDept().setSysDepts(null);
		user.getSysDept().setSysUsers(null);
		this.setJsonPropertyFilter(JSONPropertyFilter.BLACKLIST, new String[]{"sysDept.sysUsers","sysDept.sysDepts","sysDept.sysDept","sysUsermodules","sysGroupusers"});
		return toJSON(user, "{success: true, data:", "}");
	}

	/**
	 * 
	 * @description 删除用户
	 * @date May 5, 2009
	 * @author jingpj 4:05:21 PM
	 * @return
	 */
	public String delUser() {
		ISysUserService userService = (ISysUserService) getBean("sysUserService");
		try {
			userService.delUser(user.getUserid());
			return toSTR("{success:true}");
		} catch (Exception ex) {
			ex.printStackTrace();
			return toSTR("{success:false,error:'此用户存在关联,不能删除。'}");
		}
	}

	/**
	 * 
	 * @description 保存用户
	 * @date May 5, 2009
	 * @author jingpj 4:05:33 PM
	 * @return
	 */
	public String savUser() {
		ISysUserService userService = (ISysUserService) getBean("sysUserService");
		try {
			userService.savUser(user);
			return toJSON(user,"{success:true,data:","}");
		} catch (Exception ex) {
			return toSTR("{success:false}");
		}
	}

	/**
	 * @description 验证用户
	 * @date June 1,2009
	 * @author hebo 9:54 AM
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String vefUser() throws UnsupportedEncodingException {
		String login = req.getParameter("login");// 验证条件：用户名
		String password = req.getParameter("password");// 验证条件：密码
		String ipaddress = req.getRemoteAddr();
		SearchCondition condition = new SearchCondition();
		condition.set("login", login);
		condition.set("password", password);
		ISysUserService userService = (ISysUserService) getBean("sysUserService");
		ISysLogService sysLogService = (ISysLogService) getBean("sysLogService");
		SysUser sysUser = userService.vefUser(condition);
		if (sysUser != null) {// 用户验证成功
//			if (!StringUtil.isEmpty(sysUser.getIpaddress())) {
//				String[] ips = sysUser.getIpaddress().split(",");
//				boolean valSuccess = false;
//				for (String s: ips) {
//					if (s.equals(ipaddress)) {
//						valSuccess = true;
//					}
//				}
//				if (!valSuccess && !"系统管理员".equals(sysUser.getSysDept().getDeptname())) {
//					return toSTR("{success:false,error:'请在正确的电脑上登录!'}");
//				}
//			} else {
//				sysUser.setIpaddress(ipaddress);
//				userService.savUser(sysUser);
//			}
			HttpSession session = req.getSession();
			session.setAttribute("loginUser", sysUser);// 保存用户登陆信息
//			if (sysUser.getSysDept() != null) {
//				sysUser.getSysDept().getDeptname();
//			}
			try {
				// 保存用户菜单
				ISysUserModuleService service = (ISysUserModuleService) getBean("userModuleService");
				String menuString = service.getUserModuleMenu(sysUser);
				session.setAttribute("menu", menuString);
			} catch (Exception e) {
				session.setAttribute("menu", "[]");
				e.printStackTrace();
			}
			
			SysLog sysLog = new SysLog(sysUser.getUsername(), "用户登录", new Date(), login, "登录成功");
			sysLogService.saveSysLog(sysLog);
			return toJSON(sysUser,"{success:true,data:","}"); //modified by jingpj 返回用户的详细信息
		} else {// 用户验证失败
			
			SysLog sysLog = new SysLog(login, "用户登录", new Date(), login, "登录失败");
			sysLogService.saveSysLog(sysLog);
			
			return toSTR("{success:false, error:'用户名或密码错误!'}");
		}

	}

	/**
	 * @description 用户名登录名检测
	 * @date June 3,2009
	 * @author hebo 9:54 AM
	 * @return
	 * @throws UnsupportedEncodingException
	 */

	public String chkUser() throws UnsupportedEncodingException {
		String login = new String(req.getParameter("login").getBytes(
				"ISO_8859_1"), "utf-8");
		SearchCondition condition = new SearchCondition();
		condition.set("login", login);
		ISysUserService userService = (ISysUserService) getBean("sysUserService");
		if (userService.chkUser(condition) != null) {// 如果此登录名存在
			return toSTR("{success:false}");
		} else {// 如果此登录名不存在,则登录名可用
			return toSTR("{success:true}");
		}
	}

	/**
	 * @description 用户注销
	 * @date June 8,2009
	 * @author hebo 10:23 AM
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	public String cancelUser() {
		ISysLogService sysLogService = (ISysLogService) getBean("sysLogService");
		SysLog sysLog = new SysLog(this.getCurrentUser().getUsername(), "用户登出", new Date(), this.getCurrentUser().getLogin(), "登出成功");
		sysLogService.saveSysLog(sysLog);
		
		
		HttpSession session = req.getSession();
		session.invalidate();// 清除session
		// System.out.println("cancel User");
		// res.sendRedirect("../../../../index.jsp");
		
		
		return toINDEX();
	}

	/**
	 * 
	 * @desc 修改用户密码
	 * @date Sep 21, 2009
	 * @author hebo
	 * @return
	 */
	public String modifyUserPwd() {

		try {
			HttpSession session = req.getSession();
			SysUser sysUser = (SysUser) session.getAttribute("loginUser");
			String login = req.getParameter("login");
			String oldpassword = req.getParameter("oldpassword");
			String password = req.getParameter("password");

			SearchCondition condition = new SearchCondition();
			condition.set("login", login);
			condition.set("password", oldpassword);
			ISysUserService userService = (ISysUserService) getBean("sysUserService");
			sysUser = userService.vefUser(condition);
			if (sysUser == null) {
				return toSTR("{success:false,error:'原始密码错误，请重新输入!'}");
			} else {
				sysUser.setPassword(password);
				userService.savModifiedUserPwd(sysUser);
				return toSTR("{success:true}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return toSTR("{success:false,error:'密码修改错误!'}");
		}
	}

	/**
	 * 登录成功转向页面
	 */
	public String login() {
		return toJSP("layout");
	}
	/**
	 * 转向到页头
	 * 
	 * @return
	 */
	public String fwdTop() {
		return toJSP("top");
	}

	/**
	 * 转向到底部
	 * 
	 * @return
	 */
	public String fwdBottom() {
		return toJSP("bottom");
	}

	/**
	 * 转向到空白页面
	 * 
	 * @return
	 */
	public String fwdBlank() {
		return toJSP("blank");
	}

	/**
	 * 转向到用户管理页面
	 * 
	 * @return
	 */
	public String fwdUser() {
		return toJSP("user");
	}

	/**
	 * 转向用户注册页
	 * 
	 * @return
	 */
	public String fwdReg() {
		return toJSP("reg");
	}
	/**
	 * 转向登录页面
	 * 
	 * @return
	 */
	public String toINDEX() {
		return super.toINDEX();
	}
	
	/**
	 * 修改密码页面
	 * 
	 * @return
	 */
	public String toChangPwd() {
		return toJSP("changepwd");
	}
	
	
	/**
	 *@desc 待办事项 
	 *@date Mar 11, 2010
	 *@author jingpj
	 *@return
	 */
	public String fwdTodoWork(){
		return toJSP("todowork");
	}
	
	public String fwdCardguide() {
		return toJSP("cardguide");
	}
	
	public String fwdBody() {
		return toJSP("body");
	}
	
	
	public String fwdChart(){ 
		String content = " <chart caption='月度收入' xAxisName='月' yAxisName='收入' numberPrefix='￥' rotateYAxisName='0' showValues='0'>"
			+"   <set label='Jan' value='420000' />                                                                   "
			+"   <set label='Feb' value='910000' />                                                                   "
			+"   <set label='Mar' value='720000' />                                                                   "
			+"   <set label='Apr' value='550000' />                                                                   "
			+"   <set label='May' value='810000' />                                                                   "
			+"   <set label='Jun' value='510000' />                                                                   "
			+"   <set label='Jul' value='680000' />                                                                   "
			+"   <set label='Aug' value='620000' />                                                                   "
			+"   <set label='Sep' value='610000' />                                                                   "
			+"   <set label='Oct' value='490000' />                                                                   "
			+"   <set label='Nov' value='530000' />                                                                   "
			+"   <set label='Dec' value='330000' />                                                                   "
			+"                                                                                                        "
			+"   <trendLines>                                                                                         "
			+"      <line startValue='700000' color='009933' displayvalue='Target' />                                 "
			+"   </trendLines>                                                                                        "
			+"                                                                                                        "
			+"   <styles>                                                                                             "
			+"                                                                                                        "
			+"      <definition>                                                                                      "
			+"         <style name='CanvasAnim' type='animation' param='_xScale' start='0' duration='1' />            "
			+"      </definition>                                                                                     "
			+"                                                                                                        "
			+"      <application>                                                                                     "
			+"         <apply toObject='Canvas' styles='CanvasAnim' />                                                "
			+"      </application>                                                                                    "
			+"                                                                                                        "
			+"   </styles>                                                                                            "
			+"                                                                                                        "
			+"</chart>";
		
		ISysUserService userService = (ISysUserService) getBean("sysUserService");
		
		return toCHART(userService.getChartXml());
		
		//return toCHART("<graph showShadow='1' divLineColor='aaaaaa' divLineIsDashed='1'  imageSave='1' adjustDiv='1' bgAlpha='20'   staggerLines='3' decimals='2' divLineColor='ff0000'   yAxisValueDecimals='2'  canvasBgAlpha='80' labeldisplay='WRAP' slantLabels='1' staggerLines='2' anchorAlpha='0' yaxisminvalue='0' yaxismaxvalue='63' baseFontSize='12' baseFont='宋体' showValues='0' labelstep='1' numdivlines='5' ><set label='人力资源部' name='人力资源部'  value='1.0'  color='6092B3'  isSliced='1'/><set label='党群工作部' name='党群工作部'  value='1.0'  color='E24E24' /><set label='公司领导' name='公司领导'  value='9.0'  color='A24EE2'  isSliced='1'/><set label='化水' name='化水'  value='17.0'  color='8AAF52' /><set label='化试' name='化试'  value='6.0'  color='EDCC49'  isSliced='1'/><set label='发电部' name='发电部'  value='11.0'  color='6092B3' /><set label='外委单位' name='外委单位'  value='1.0'  color='E24E24'  isSliced='1'/><set label='安全监察部' name='安全监察部'  value='2.0'  color='A24EE2' /><set label='审计监察部' name='审计监察部'  value='1.0'  color='8AAF52'  isSliced='1'/><set label='总经理工作部' name='总经理工作部'  value='1.0'  color='EDCC49' /><set label='机务一队' name='机务一队'  value='24.0'  color='6092B3'  isSliced='1'/><set label='机务二队' name='机务二队'  value='6.0'  color='E24E24' /><set label='江油工程公司' name='江油工程公司'  value='1.0'  color='A24EE2'  isSliced='1'/><set label='热工队' name='热工队'  value='23.0'  color='8AAF52' /><set label='燃煤质检部' name='燃煤质检部'  value='11.0'  color='EDCC49'  isSliced='1'/><set label='物资部' name='物资部'  value='16.0'  color='6092B3' /><set label='生产技术部' name='生产技术部'  value='17.0'  color='E24E24'  isSliced='1'/><set label='电气队' name='电气队'  value='24.0'  color='A24EE2' /><set label='系统管理员' name='系统管理员'  value='3.0'  color='8AAF52'  isSliced='1'/><set label='脱硫' name='脱硫'  value='16.0'  color='EDCC49' /><set label='计划经营部' name='计划经营部'  value='4.0'  color='6092B3'  isSliced='1'/><set label='设备维修部' name='设备维修部'  value='5.0'  color='E24E24' /><set label='输煤' name='输煤'  value='29.0'  color='A24EE2'  isSliced='1'/><set label='除灰' name='除灰'  value='10.0'  color='8AAF52' /><set label='集控' name='集控'  value='54.0'  color='EDCC49'  isSliced='1'/></graph>");
	}
}
