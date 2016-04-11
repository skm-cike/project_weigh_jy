package com.est.weigh.cfginfo.service;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.weigh.cfginfo.vo.WeighCompany;
import com.est.weigh.download.vo.Download;

import java.util.Date;
import java.util.List;

public interface IWeighCompanyService {
    List<Download> getDownloads(Date date);

	Result<WeighCompany> getCompanyList(SearchCondition params, Page page);

	void savCompany(WeighCompany entity);

	WeighCompany getCompany(String companyid);

	//WeighCompany getCompanybyCompanyname(String companyname);

	void delCompany(WeighCompany entity);

	Result<WeighCompany> getCompanyListByPz(SearchCondition params, Page page);

	WeighCompany getCompanybyCompanycode(String companycode, String breedtype);
}
