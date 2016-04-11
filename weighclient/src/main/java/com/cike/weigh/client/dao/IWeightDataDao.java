package com.cike.weigh.client.dao;

import java.util.List;

import com.cike.weigh.client.vo.Weight;
import com.est.common.base.IBaseDao;

public interface IWeightDataDao extends IBaseDao<Object> {
	List<Weight> getWeightList(String startdate);
}
