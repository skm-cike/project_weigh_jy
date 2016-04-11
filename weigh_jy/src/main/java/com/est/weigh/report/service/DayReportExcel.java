package com.est.weigh.report.service;

import com.est.common.ExcelToFile;
import com.est.weigh.report.vo.Dayreport;
import com.est.weigh.report.vo.MonthReportList;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2015-11-10.
 */
public class DayReportExcel extends ExcelToFile<Dayreport> {
    public DayReportExcel() {
        super();
    }

    public DayReportExcel(HttpServletResponse response, String fileName, List<Dayreport> datas, String title, String[] needFields, String[] tableHead) {
        super(response, fileName, datas, title, needFields, tableHead);
    }

    @Override
    protected void creatRoot() {

    }

    @Override
    protected void creatHead() {

    }

    @Override
    protected void formatTable() {

    }
}
