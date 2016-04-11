package com.est.weigh.cfginfo.service;

import java.util.List;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.sysinit.sysuser.vo.SysUser;
import com.est.webservice.server.vo.WeighVehicleWs;
import com.est.weigh.cfginfo.vo.WeighVehicle;

public interface IWeighVehicleService {

	Result<WeighVehicle> getWeighVehicleList(SearchCondition condition, Page page);
	void saveVehicle(WeighVehicle vehicle, SysUser user);
	WeighVehicle getVehicle(Long vehicleid);
	void delWeighVehicle(WeighVehicle vehicle, SysUser currentUser);
	
	/**
	 * @描述: 获取买方车辆数据
	 * @return
	 */
	List<WeighVehicleWs> getBuyVehicle();
	
	/**
	 * @描述: 获取卖方车辆数据
	 * @return
	 */
	List<WeighVehicleWs> getSaleVehicle();
}
