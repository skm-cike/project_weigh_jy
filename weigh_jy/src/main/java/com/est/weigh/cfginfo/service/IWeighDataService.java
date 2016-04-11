package com.est.weigh.cfginfo.service;

import java.util.List;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.weigh.cfginfo.vo.WeighData;

public interface IWeighDataService {

	Result<WeighData> getWeighDataList(SearchCondition params, Page page);

	void savWeighData(WeighData entity);

	WeighData getWeighData(String id);

	void delWeighData(WeighData entity);

	String savWeightDats(List<WeighData> list);

    void batchSetWater(SearchCondition params);
}
