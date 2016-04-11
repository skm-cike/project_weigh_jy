package com.est.weigh.report.action;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.est.common.base.BaseAction;
import com.est.common.exception.BaseBussinessException;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.classutil.DateUtil;
import com.est.weigh.report.service.IDayReportService;
import com.est.weigh.report.vo.Dayreport;

public class DayReportAction extends BaseAction{
	@Autowired
	private IDayReportService reportService;
	@Override
	public Object getModel() {
		return null;
	}

	public String fwdDayReport() {
		return toJSP("dayreport");
	}
	
	public String delReport() {
		try {
			reportService.delReport(this.params);
			return toSTR("{success:true}");
		} catch (RuntimeException e) {
			e.printStackTrace();
			if (e instanceof BaseBussinessException) {
				return toSTR("{success:false, error:'" + e.getMessage() + "'}");
			}
			return toSTR("{success:false, error:'生成日报失败！'}");
		}
	}
	
	public String createReport() {
		try {
			String startreportdate = (String)params.get("startreportdate");
			String endreportdate = (String)params.get("endreportdate");
			Set<String> reportdates = new HashSet();
			reportdates.add(startreportdate);
			reportdates.add(endreportdate);
			Date _pred = DateUtil.parse(startreportdate, "yyyy-MM-dd");
			Date _endd = DateUtil.parse(endreportdate, "yyyy-MM-dd");
			while (_pred.before(_endd)) {
				reportdates.add(DateUtil.format(_pred, "yyyy-MM-dd"));
				_pred = DateUtil.add(_pred, DateUtil.DATE, 1);
			}
			for (String str: reportdates) {
				params.set("reportdate", str);
				reportService.savCreateReport(this.params);
			}
			return toSTR("{success:true}");
		} catch (RuntimeException e) {
			e.printStackTrace();
			if (e instanceof BaseBussinessException) {
				return toSTR("{success:false, error:'" + e.getMessage() + "'}");
			}
			return toSTR("{success:false, error:'生成日报失败！'}");
		}
	}
	
	public String getReport() {
		reportService.updateToReport();
		Result<Dayreport> rst = reportService.getReport(this.params, this.getPage());
		for (Dayreport r: rst.getContent()) {
			if (r.getCompanyname() != null && r.getCompanyname().contains("合计")) {
				r.setCompanyname("<font color='green'>" + r.getCompanyname() + "</font>");
			}
			if (r.getCompanyname() != null && r.getCompanyname().contains("总计")) {
				r.setCompanyname("<font color='red'>" + r.getCompanyname() + "</font>");
			}
			if (r.getGrade()!= null && r.getGrade().contains("合计")) {
				r.setGrade("<font color='green'>" + r.getGrade() + "</font>");
			}
			if (r.getGrade()!= null && r.getGrade().contains("总计")) {
				r.setGrade("<font color='red'>" + r.getGrade() + "</font>");
			}
		}
		return toJSON(rst);
	}
	
	/**
	 * @描述: 获取日期
	 * @return
	 */
	public String getDays() {
		List<Map<String, String>> list = reportService.getDays(params);
		return toJSON(list, "{total:" + list.size() + ",rows:", "}");
	}

	/**
	 * @描述: 导出日报
	 * @return
	 */
	public String exportDayReport() {
		try {
			String day = req.getParameter("day");
			params.set("fileName", "日报" + day);
			params.set("username", this.getCurrentUser().getUsername());
			reportService.exportMonthReport(params, res);
			return NONE;
		} catch (Exception e) {
			e.printStackTrace();
			return toSTR("{success:false}");
		}
	}

	/**
	 * @描述: 重新计算价格
	 * @return
	 */
	public String reCalculate() {
		try {
			reportService.savReCalculate(params);
			return toSTR("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof BaseBussinessException) {
				return toSTR("{success:false, error:'" + e.getMessage() + "'}");
			}
			return toSTR("{success:false, error:'计算失败！'}");
		}
	}

    public String reCalculateByWeight() {
        try {
            reportService.reCalculateByWeight(params);
            return toSTR("{success:true}");
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof BaseBussinessException) {
                return toSTR("{success:false, error:'" + e.getMessage() + "'}");
            }
            return toSTR("{success:false, error:'计算失败！'}");
        }
    }
}
