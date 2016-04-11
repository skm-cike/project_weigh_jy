package com.est.weigh.cfginfo.action;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;

import com.est.common.base.BaseAction;
import com.est.common.exception.BaseBussinessException;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;
import com.est.sysinit.sysgroup.vo.SysGroup;
import com.est.weigh.cfginfo.service.IWeighCompanyService;
import com.est.weigh.cfginfo.service.IWeighPriceService;
import com.est.weigh.cfginfo.vo.WeighCompany;
import com.est.weigh.cfginfo.vo.WeighPrice;
import com.est.weigh.cfginfo.vo.WeighWeighprice;

public class WeighPriceAction extends BaseAction {
	@Autowired
	private IWeighPriceService priceService;
	@Autowired
	private IWeighCompanyService companyService;
	private WeighPrice entity = new WeighPrice();

	@Override
	public Object getModel() {
		return entity;
	}

	public String fwdWeighPrice() {
		return toJSP("price");
	}

	/**
	 * @描述: 获取所有单位列表
	 * @return
	 */
	public String getAllWeighPriceList() {
		this.getPage().setRowPerPage(Integer.MAX_VALUE);
		return getWeighPriceList();
	}

	/**
	 * @描述: 获取价格列表
	 * @return
	 */
	public String getWeighPriceList() {
		Result<WeighPrice> rst = priceService.getWeighPriceList(this.params, this.getPage());
		return toJSON(rst);
	}
	

	/**
	 * @描述: 保存价格
	 * @return
	 */
	public String savWeighPrice() {
		if (entity.getPriceid() != null) {
			entity.setPriceid(entity.getPriceid().trim());
			if (entity.getPriceid().endsWith(",")) {
				entity.setPriceid(entity.getPriceid().substring(0, entity.getPriceid().length()-1));
			}
		}
		if ("".equals(entity.getPriceid())||",".equals(entity.getPriceid())) {
			entity.setPriceid(null);
		}
		try {
			String companycode = entity.getCompanycode();// 实际传过来的是companyid
            if ("huizha".equals(entity.getBreedtype())) {
                entity.setGrade("灰渣");
            }
			WeighCompany weighCompany = companyService.getCompanybyCompanycode(companycode, entity.getBreedtype());
			entity.setCompanyname(weighCompany.getCompanyname());
			entity.setCompanycode(weighCompany.getCompanycode());
			priceService.savWeighPrice(entity);
			return toJSON(entity, "{success:true,data:", "}");
		} catch (RuntimeException e) {
			e.printStackTrace();
			if (e instanceof BaseBussinessException) {
				return toSTR("{success:false,error:'" + e.getMessage() + "'}");
			}
			return toSTR("{success:false,error:'保存错误，请联系管理员'}");
		}
	}

	/**
	 * @描述: 获得价额
	 * @return
	 */
	public String getWeighPrice() {
		WeighPrice weighPrice = priceService.getWeighPrice(entity.getPriceid());
		return toJSON(weighPrice, "{success:true,data:", "}");
	}

	/**
	 * 获取单价
	 * 
	 * @return
	 */
	public String getUnit_Price() {
		WeighPrice weighPrice = priceService.getWeighPriceByCondition(this.params);
		return toSTR(weighPrice.getUnit_price() + "");
	}

	/**
	 * @描述: 删除价格
	 * @return
	 */
	public String delWeighPrice() {
		try {
			priceService.delWeighPrice(entity);
			return toJSON("{success:true}");
		} catch (RuntimeException e) {
			e.printStackTrace();
			if (e instanceof BaseBussinessException) {
				return toSTR("{success:false,error:'" + e.getMessage() + "'}");
			}
			return toSTR("{success:false,error:'保存错误，请联系管理员'}");
		}
	}
	
	/**
	 * @描述: 获得重量价格配置
	 * @return
	 */
	public String getWeighWeightPriceList() {
		Result<WeighWeighprice> rst = priceService.getWeighWeightPriceList(this.params, this.getPage());
		return toJSON(rst);
	}
	
	/**
	 * @描述: 保存重量价格配置
	 * @return
	 */
	public String savWeighWeightPriceList() {
		String data = req.getParameter("data");
		try {
			data = java.net.URLDecoder.decode(data,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		EditableGridDataHelper editGridData = EditableGridDataHelper.data2bean(data, WeighWeighprice.class);
		try {
			priceService.savWeighWeightPriceList(editGridData);
			return toSTR("{success:true}");
		} catch (RuntimeException e) {
			e.printStackTrace();
			return toSTR("{success:false, error:'" + e.getMessage() + "'}");
		}
	}
}
