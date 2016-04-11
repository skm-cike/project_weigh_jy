package com.est.weigh.report.service;

import java.util.List;
import java.util.Map;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.weigh.report.vo.Dayreport;

import javax.servlet.http.HttpServletResponse;

public interface IDayReportService {

	void savCreateReport(SearchCondition params);

	Result<Dayreport> getReport(SearchCondition params, Page page);

	void delReport(SearchCondition params);

	void updateToReport();

	List<Map<String, String>> getDays(SearchCondition params);

	void exportMonthReport(SearchCondition params, HttpServletResponse res);

	void savReCalculate(SearchCondition params);

    void reCalculateByWeight(SearchCondition params);
}
