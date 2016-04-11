package com.est.weigh.cfginfo.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.est.common.base.BaseAction;
import com.est.common.exception.BaseBussinessException;
import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.weigh.cfginfo.service.IWeighCompanyService;
import com.est.weigh.cfginfo.vo.WeighCompany;
import com.est.weigh.download.service.IDownloadService;

public class WeighCompanyAction extends BaseAction {
	@Autowired
	private IWeighCompanyService tcompanyService;
	@Autowired
	private IDownloadService downloadService;

	private WeighCompany entity = new WeighCompany();

	@Override
	public Object getModel() {
		return entity;
	}

	public String fwdTransCompany() {
		return toJSP("company");
	}

	/**
	 * @描述: 获取所有转运单位列表
	 * @return
	 */
	// public String getAllTransferCompanyList() {
	// this.getPage().setRowPerPage(Integer.MAX_VALUE);
	// return getCompanyList();
	// }
	/**
	 * @描述: 获取单位列表
	 * @return
	 */
	public String getCompanyList() {
		Result<WeighCompany> rst = tcompanyService.getCompanyList(this.params, this.getPage());
		return toJSON(rst);
	}
	
	/**
	 * @描述: 获取单位列表(combox)
	 * @return
	 */
	public String getCompanyListForCombox() {
		Result<WeighCompany> rst = tcompanyService.getCompanyList(this.params, this.getPage());
//		WeighCompany company = new WeighCompany();
//		company.setCompanycode("全部");
//		company.setCompanyname("全部");
//		rst.getContent().add(0, company);
		return toJSON(rst);
	}
	
	public String getCompanyListAll() {
		Page page = this.getPage();
		page.setRowPerPage(10000);
		params.set("status", "1");
		Result<WeighCompany> rst = tcompanyService.getCompanyList(this.params, page);
		return toJSON(rst.getContent() ,"{success:true,rows:","}");
	}
	
	public String getCompanyListByPz() {
		String pagesiz = req.getParameter("pagesiz");
		Page page = this.getPage();
		if ("max".equals(pagesiz)) {
			page.setRowPerPage(Integer.MAX_VALUE);
		}
		Result<WeighCompany> res = tcompanyService.getCompanyListByPz(params, page);
		return toJSON(res);
	}

	/**
	 * @描述: 保存单位
	 * @return
	 */
	public String savWeighCompany() {
		if ("".equals(entity.getCompanyid())) {
			entity.setCompanyid(null);
		}
		try {
			tcompanyService.savCompany(entity);
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
	 * @描述: 获得单位
	 * @return
	 */
	public String getWeighCompany() {
		WeighCompany company = tcompanyService.getCompany(entity.getCompanyid());
		return toJSON(company, "{success:true,data:", "}");
	}

	/**
	 * @描述: 删除单位
	 * @return
	 */
	public String delWeighCompany() {
		try {
			tcompanyService.delCompany(entity);
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
	 * @描述: 更新客户端信息
	 * @return
	 */
	public String updateClientInfo() {
		try {
			downloadService.updateClientInfo();
			return toJSON("{success:true}");
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof BaseBussinessException) {
				return toSTR("{success:false,error:'" + e.getMessage() + "'}");
			}
			return toSTR("{success:false,error:'保存错误，请联系管理员'}");
		}
	}
}
