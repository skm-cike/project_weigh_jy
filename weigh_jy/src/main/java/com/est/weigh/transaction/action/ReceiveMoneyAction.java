package com.est.weigh.transaction.action;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.est.common.base.BaseAction;
import com.est.common.exception.BaseBussinessException;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.weigh.cfginfo.service.IWeighCompanyService;
import com.est.weigh.transaction.service.IReceiveMoneyService;
import com.est.weigh.transaction.vo.WeighReceivemoney;

/**
 * @描述: 预收货款
 * @author skm
 *
 */
public class ReceiveMoneyAction extends BaseAction{
	@Autowired
	private IReceiveMoneyService receiveService;
	@Autowired
	private IWeighCompanyService companyService;
	private WeighReceivemoney entity = new WeighReceivemoney();
	@Override
	public Object getModel() {
		return entity;
	}
	
	public String fwdReceiveMoney() {
		return toJSP("receivemoney");
	}
	
	public String fwdReceiveMoneyAudit() {
		return toJSP("receivemoneyaudit");
	}
	
	public String getReceiveMoneyList() {
		Result<WeighReceivemoney> rst = receiveService.getWeighPriceList(this.params, this.getPage());
		return toJSON(rst);
	}
	
	public String auditAccept() {
		String data = this.req.getParameter("data");
		try {
			receiveService.auditAccept(data, this.getCurrentUser().getUsername());
			return toSTR("{success:true}");
		} catch (RuntimeException e) {
			e.printStackTrace();
			if (e instanceof BaseBussinessException) {
				return toSTR("{success:false,error:'" + e.getMessage() + "'}");
			}
			return toSTR("{success:false,error:'保存错误，请联系管理员'}");
		}
	}
	
	/**
	 * @描述: 保存预收货款
	 * @return
	 */
	public String savReceiveMoney() {
		if ("".equals(entity.getId())) {
			entity.setId(null);
		}
		try {
			if (StringUtil.isEmpty(entity.getId())) {
				entity.setOperator(this.getCurrentUser().getUsername());
				entity.setReceivedate(new Date());
			}
			receiveService.savReceiveMoney(entity);
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
	 * @描述: 获取预收货款
	 * @return
	 */
	public String getReceiveMoney() {
		WeighReceivemoney receivemoney = receiveService.getReceiveMoney(entity.getId());
		return toJSON(receivemoney, "{success:true,data:", "}");
	}
	
	/**
	 * @描述: 删除预收货款
	 * @return
	 */
	public String delReceiveMoney() {
		try {
			receiveService.delReceiveMoney(entity);
			return toJSON("{success:true}");
		} catch (RuntimeException e) {
			e.printStackTrace();
			if (e instanceof BaseBussinessException) {
				return toSTR("{success:false,error:'" + e.getMessage() + "'}");
			}
			return toSTR("{success:false,error:'删除失败，请联系管理员'}");
		}
	}
}
