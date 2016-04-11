package com.est.weigh.report.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;
import com.est.weigh.report.vo.MonthReportList;

public interface IMonthReportService {

	void createReport(SearchCondition params);

	void delReport(SearchCondition params);

	Result<MonthReportList> getReport(SearchCondition params);

	void exportMonthReport(SearchCondition params,HttpServletResponse rep);

	void exportQuarterReport(SearchCondition params, HttpServletResponse res);

	void exportYearReport(SearchCondition params, HttpServletResponse res);

	void savTrimJzje(EditableGridDataHelper editData, SearchCondition params);

	List<String> getYearListByPz(SearchCondition params);

	List<Map<String, String>> getMonthReportByYear(SearchCondition params);

	void createReports(SearchCondition params);
}
