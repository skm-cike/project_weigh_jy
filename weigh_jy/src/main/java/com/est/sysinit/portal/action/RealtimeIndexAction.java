package com.est.sysinit.portal.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpSession;

import com.est.common.base.BaseAction;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.sysinit.sysauthority.service.ISysUserModuleService;
import com.est.sysinit.sysdept.service.ISysDeptService;
import com.est.sysinit.sysdept.vo.SysDept;
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
public class RealtimeIndexAction extends BaseAction {
	private static final long serialVersionUID = -5236565446298628901L;

	
	@Override
	public Object getModel() {
		return null;
	}


	public String fwdRealtime() {
		return toJSP("realtime");
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
		
		//ISysUserService userService = (ISysUserService) getBean("sysUserService");
		
		//return toCHART(userService.getChartXml());
		
		return toSTR("<graph showShadow='1' divLineColor='aaaaaa' divLineIsDashed='1'  imageSave='1' adjustDiv='1' bgAlpha='20'   staggerLines='3' decimals='2' divLineColor='ff0000'   yAxisValueDecimals='2'  canvasBgAlpha='80' labeldisplay='WRAP' slantLabels='1' staggerLines='2' anchorAlpha='0' yaxisminvalue='0' yaxismaxvalue='63' baseFontSize='12' baseFont='宋体' showValues='0' labelstep='1' numdivlines='5' ><set label='人力资源部' name='人力资源部'  value='1.0'  color='6092B3'  isSliced='1'/><set label='党群工作部' name='党群工作部'  value='1.0'  color='E24E24' /><set label='公司领导' name='公司领导'  value='9.0'  color='A24EE2'  isSliced='1'/><set label='化水' name='化水'  value='17.0'  color='8AAF52' /><set label='化试' name='化试'  value='6.0'  color='EDCC49'  isSliced='1'/><set label='发电部' name='发电部'  value='11.0'  color='6092B3' /><set label='外委单位' name='外委单位'  value='1.0'  color='E24E24'  isSliced='1'/><set label='安全监察部' name='安全监察部'  value='2.0'  color='A24EE2' /><set label='审计监察部' name='审计监察部'  value='1.0'  color='8AAF52'  isSliced='1'/><set label='总经理工作部' name='总经理工作部'  value='1.0'  color='EDCC49' /><set label='机务一队' name='机务一队'  value='24.0'  color='6092B3'  isSliced='1'/><set label='机务二队' name='机务二队'  value='6.0'  color='E24E24' /><set label='江油工程公司' name='江油工程公司'  value='1.0'  color='A24EE2'  isSliced='1'/><set label='热工队' name='热工队'  value='23.0'  color='8AAF52' /><set label='燃煤质检部' name='燃煤质检部'  value='11.0'  color='EDCC49'  isSliced='1'/><set label='物资部' name='物资部'  value='16.0'  color='6092B3' /><set label='生产技术部' name='生产技术部'  value='17.0'  color='E24E24'  isSliced='1'/><set label='电气队' name='电气队'  value='24.0'  color='A24EE2' /><set label='系统管理员' name='系统管理员'  value='3.0'  color='8AAF52'  isSliced='1'/><set label='脱硫' name='脱硫'  value='16.0'  color='EDCC49' /><set label='计划经营部' name='计划经营部'  value='4.0'  color='6092B3'  isSliced='1'/><set label='设备维修部' name='设备维修部'  value='5.0'  color='E24E24' /><set label='输煤' name='输煤'  value='29.0'  color='A24EE2'  isSliced='1'/><set label='除灰' name='除灰'  value='10.0'  color='8AAF52' /><set label='集控' name='集控'  value='54.0'  color='EDCC49'  isSliced='1'/></graph>");
	}
}
