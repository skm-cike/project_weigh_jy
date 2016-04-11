package com.est.weigh.cfginfo.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.est.common.base.BaseAction;
import com.est.common.exception.BaseBussinessException;
import com.est.weigh.cfginfo.service.IWeighVehicleService;
import com.est.weigh.cfginfo.vo.WeighVehicle;

public class WeighVehicleAction extends BaseAction{
	private WeighVehicle vehicle = new WeighVehicle();
	@Autowired
	private IWeighVehicleService vehicleService;
	@Override
	public Object getModel() {
		return vehicle;
	}
	
	public String fwdVehicle() {
		return toJSP("vehicle");
	}
	
	public String getWeighVehicleList() {
		return toJSON(vehicleService.getWeighVehicleList(this.params, this.getPage()));
	}
	
	public String savWeighVehicle() {
		try {
			vehicleService.saveVehicle(vehicle, this.getCurrentUser());
			return toJSON(vehicle,"{success:true,data:","}");
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage().contains("ORA-00001") || e.getMessage().contains("org.hibernate.exception.ConstraintViolationException")) {
				throw new BaseBussinessException("已有该车号!");
			}
			return toSTR("{success:false,error:'"+e.getMessage()+"'}");
		}
	}
	
	public String getWeighVehicle() {
		String s_vehicleid = req.getParameter("vehicleid");
		Long vehicleid = Long.parseLong(s_vehicleid);
		WeighVehicle vehicle = vehicleService.getVehicle(vehicleid);
		return toJSON(vehicle, "{success: true, data:", "}");
	}
	
	public String delWeighVehicle() {
		try {
			vehicleService.delWeighVehicle(vehicle, this.getCurrentUser());
			return toSTR("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof BaseBussinessException) {
				return toSTR("{success:false,error:'"+e.getMessage()+"!'}");
			}
			return toSTR("{success:false,error:'删除失败!'}");
		}
	}
}
