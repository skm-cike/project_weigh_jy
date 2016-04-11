package com.est.weigh.transaction.service;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.weigh.transaction.vo.WeighReceivemoney;

public interface IReceiveMoneyService {

	Result<WeighReceivemoney> getWeighPriceList(SearchCondition params,
			Page page);

	void savReceiveMoney(WeighReceivemoney entity);

	WeighReceivemoney getReceiveMoney(String id);

	void delReceiveMoney(WeighReceivemoney entity);

	void auditAccept(String data, String username);

}
