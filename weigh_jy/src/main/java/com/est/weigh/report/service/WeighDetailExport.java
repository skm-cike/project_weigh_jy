package com.est.weigh.report.service;

import com.est.common.ExcelToFile;
import com.est.weigh.cfginfo.vo.WeighData;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by 陆华 on 16-2-22 下午3:56
 */
public class WeighDetailExport extends ExcelToFile<WeighData> {
    public WeighDetailExport(HttpServletResponse response, String fileName, List<WeighData> datas, String title, String[] needFields, String[] tableHead) {
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
