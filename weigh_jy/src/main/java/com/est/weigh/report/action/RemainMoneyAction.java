package com.est.weigh.report.action;

import com.est.common.base.BaseAction;
import com.est.common.ext.util.Result;
import com.est.weigh.report.service.IRemainMoneyService;
import com.est.weigh.report.vo.RemainMoney;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Administrator on 2015-11-09.
 */
public class RemainMoneyAction extends BaseAction{
    @Autowired
    private IRemainMoneyService remainMoneyService;
    @Override
    public Object getModel() {
        return null;
    }

    /**
     * @√Ë ˆ: ªıøÓ”‡∂Ó
     * @return
     */
    public String fwdRemainmoney() {
        return toJSP("remainmoney");
    }

    public String getRemainMoneyCompanys() {
        List<RemainMoney> rst = remainMoneyService.getRemainMoneyCompanys(params);
        return toJSON(rst, "{total:" + rst.size() + ",rows:", "}");
    }
}
