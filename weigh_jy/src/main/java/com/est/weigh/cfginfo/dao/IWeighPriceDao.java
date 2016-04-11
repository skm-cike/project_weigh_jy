package com.est.weigh.cfginfo.dao;

import com.est.common.base.IBaseDao;
import com.est.weigh.cfginfo.vo.WeighPrice;

public interface IWeighPriceDao  extends IBaseDao<WeighPrice>{
	Double getUnitPrice(String companyName, String pz, String grade, String jizu);

	/**
	 * @描述: 根据石膏水份获取其等级
	 * @param water
	 */
	//String getGradeByShigao(String companyname, String type, Double water);
}
