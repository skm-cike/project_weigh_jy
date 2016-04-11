package com.est.weigh.transaction.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.est.common.exception.BaseBussinessException;
import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.classutil.ObjectUtil;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.weigh.transaction.dao.IReceiveMoneyDao;
import com.est.weigh.transaction.vo.WeighReceivemoney;

@Service
public class ReceiveMoneyServiceImp implements IReceiveMoneyService {
	@Autowired
	private IReceiveMoneyDao receiveDao;
	/**
	 * @描述: 删除预收货款
	 */
	public void delReceiveMoney(WeighReceivemoney entity) {
		if (!StringUtil.isEmpty(entity.getAuditor())) {
			throw new BaseBussinessException("已经审核,不能删除!");
		}
		receiveDao.del(entity);
	}

	/**
	 * @描述: 获取预收货款
	 */
	public WeighReceivemoney getReceiveMoney(String id) {
		return receiveDao.findById(id);
	}

	/**
	 * @描述: 获取预收货款列表
	 */
	public Result<WeighReceivemoney> getWeighPriceList(SearchCondition params,Page page) {
		String pz = (String)params.get("pz");
		Result<WeighReceivemoney> rst = receiveDao.findByPage(page, "from WeighReceivemoney where breedtype=? order by receivedate desc", pz);
		return rst;
	}

	/**
	 * @描述: 保存预收货款
	 */
	public void savReceiveMoney(WeighReceivemoney entity) {
		if ("".equals(entity.getId())) {
			entity.setId(null);
		}

		if (!StringUtil.isEmpty(entity.getId())) {
			WeighReceivemoney old = receiveDao.findById(entity.getId());
			if (!StringUtil.isEmpty(old.getAuditor())) {
				throw new BaseBussinessException("已经审核,不能修改!");
			}
			ObjectUtil.objcetMerge(old, entity);
			entity = old;
		}
		receiveDao.save(entity);
	}

	/**
	 * @描述: 审核通过
	 */
	public void auditAccept(String data, String username) {
		String[] ids = data.split(",");
		String str = "";
		for (int i = 0; i < ids.length ; i++) {
			str+="?,";
		}
		if ("".equals(str)) {
			throw new BaseBussinessException("没有要审核的数据!");
		}
		str = str.substring(0, str.length() - 1);
		
		List<WeighReceivemoney> list =	receiveDao.findByHql("from WeighReceivemoney  where id in (" + str + ")", ids);
		Date date = new Date();
		for (WeighReceivemoney receivemoney: list) {
			if (!StringUtil.isEmpty(receivemoney.getAuditor())) {
				throw new BaseBussinessException("已审核过的数据不能再审核!");
			}
			receivemoney.setAuditor(username);
			receivemoney.setAudittime(date);
		}
		receiveDao.saveAll(list);
	}
}
