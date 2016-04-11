package com.est.weigh.report.service;

import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.weigh.report.vo.RemainMoney;

import java.util.List;

/**
 * Created by Administrator on 2015-11-09.
 */
public interface IRemainMoneyService {
    List<RemainMoney> getRemainMoneyCompanys(SearchCondition params);
}
