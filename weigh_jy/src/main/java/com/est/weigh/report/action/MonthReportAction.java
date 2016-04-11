package com.est.weigh.report.action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;

import org.springframework.beans.factory.annotation.Autowired;

import com.est.common.base.BaseAction;
import com.est.common.exception.BaseBussinessException;
import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.weigh.report.service.IMonthReportService;
import com.est.weigh.report.vo.MonthReportList;
/**
 * @描述: 月报
 * @author 陆华
 *
 */
public class MonthReportAction extends BaseAction{
	@Autowired
	private IMonthReportService monthService;
	@Override
	public Object getModel() {
		return null;
	}
	
	/**
	 * @描述: 月报
	 * @return
	 */
	public String fwdMonthReport() {
		return toJSP("monthreport");
	}
	
	
	public String createReport() {
		try {
			monthService.createReports(this.params);
			return toSTR("{success:true}");
		} catch (RuntimeException e) {
			e.printStackTrace();
			if (e instanceof BaseBussinessException) {
				return toSTR("{success:false, error:'" + e.getMessage() + "'}");
			}
			return toSTR("{success:false, error:'生成月报失败！'}");
		}
	}
	
	public String delReport() {
		try {
			monthService.delReport(this.params);
			return toSTR("{success:true}");
		} catch (RuntimeException e) {
			e.printStackTrace();
			if (e instanceof BaseBussinessException) {
				return toSTR("{success:false, error:'" + e.getMessage() + "'}");
			}
			return toSTR("{success:false, error:'删除月报失败！'}");
		}
	}
	
	public String getReport() {
		params.set("page", this.getPage());
		params.set("jizu", params.get("jizu"));
		Result<MonthReportList> result;
		try {
			result = monthService.getReport(this.params);
			return toJSON(result);
		} catch (RuntimeException e) {
			e.printStackTrace();
			return toSTR("{success:false,msg:'" + e.getMessage() + "'}");
		}
	}
	
	/**
	 * @描述: 导出月报
	 * @return
	 */
	public String exportMonthReport() {
		try {
			String yearmonth = req.getParameter("yearmonth");
			params.set("fileName", "月报" + yearmonth);
			params.set("username", this.getCurrentUser().getUsername());
			monthService.exportMonthReport(params, res);
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			return toSTR("{success:false}");
		}
	}
	
	/**
	 * @描述: 导出季报
	 * @return
	 */
	public String exportQuarterReport() {
		try {
			String reportyear = req.getParameter("reportyear");  //年
			String reporseason = req.getParameter("reporseason");  //季
			String zh_reporseason = null;
			if ("1".equals(reporseason)) {zh_reporseason="第一季";}
			if ("2".equals(reporseason)) {zh_reporseason="第二季";}
			if ("3".equals(reporseason)) {zh_reporseason="第三季";}
			if ("4".equals(reporseason)) {zh_reporseason="第四季";}
			Page page = this.getPage();
			page.setRowPerPage(Integer.MAX_VALUE);
			params.set("page", page);
			params.set("fileName", reportyear + "年" + zh_reporseason + "季报");
			params.set("username", this.getCurrentUser().getUsername());
			monthService.exportQuarterReport(params, res);
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			return toSTR("{success:false}");
		}
	}
	
	/**
	 * @描述: 导出年报
	 * @return
	 */
	public String exportYearReport() {
		try {
			String reportyear = req.getParameter("reportyear");  //年
			Page page = this.getPage();
			page.setRowPerPage(Integer.MAX_VALUE);
			params.set("page", page);
			params.set("fileName", reportyear + "年年报");
			params.set("username", this.getCurrentUser().getUsername());
			monthService.exportYearReport(params, res);
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			return toSTR("{success:false}");
		}
	}
	
	/**
	 * @描述: 调整结转金额
	 * @return
	 */
	public String trimJzje() {
		try {
            String data = req.getParameter("data");
            try {
    			data = java.net.URLDecoder.decode(data,"utf-8");
    		} catch (UnsupportedEncodingException e) {
    			e.printStackTrace();
    		}
            EditableGridDataHelper editData = EditableGridDataHelper.data2bean(data,MonthReportList.class);
			monthService.savTrimJzje(editData, params);
			return toSTR("{success:true}");
		} catch (RuntimeException e) {
			e.printStackTrace();
			return toSTR("{success:false,msg:'" + e.getMessage() + "'}");
		}
	}

	/**
	 * @描述: 根据年份获取其已生成月报的月份
	 * @return
	 */
	public String getMonthReportByYear() {
		List<Map<String, String>> rst = monthService.getMonthReportByYear(params);
		if (rst != null && rst.size() != 0) {
			return toJSON(rst ,"{success:true,rows:","}");
		} else {
			return toSTR("{success:true,rows:[]}");
		}
	}
	
	public String getYearListByPz() {
		List<String> years = monthService.getYearListByPz(params);
		List<Map<String, String>> lst = new ArrayList();
		for (int i = 0; i < years.size(); i++) {
			Map<String, String> map = new HashMap();
			map.put("year", years.get(i));
			lst.add(map);
		}
		return toJSON(lst ,"{success:true,rows:","}");
	}
}
