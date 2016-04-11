package com.est.weigh.cfginfo.service;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;
import com.est.weigh.cfginfo.vo.WeighPrice;
import com.est.weigh.cfginfo.vo.WeighWeighprice;

public interface IWeighPriceService {

	Result<WeighPrice> getWeighPriceList(SearchCondition params, Page page);

	void savWeighPrice(WeighPrice entity);

	WeighPrice getWeighPrice(String priceid);

	void delWeighPrice(WeighPrice entity);

	WeighPrice getWeighPriceByCondition(SearchCondition condition);

	Result<WeighWeighprice> getWeighWeightPriceList(SearchCondition params, Page page);

	void savWeighWeightPriceList(EditableGridDataHelper editGridData);

}
