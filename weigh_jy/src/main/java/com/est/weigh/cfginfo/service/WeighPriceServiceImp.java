package com.est.weigh.cfginfo.service;

import java.util.List;

import com.est.weigh.cfginfo.vo.WeighCompany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.est.common.exception.BaseBussinessException;
import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.classutil.ObjectUtil;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;
import com.est.weigh.cfginfo.dao.IWeighPriceDao;
import com.est.weigh.cfginfo.vo.WeighPrice;
import com.est.weigh.cfginfo.vo.WeighWeighprice;

@Service
public class WeighPriceServiceImp implements IWeighPriceService {
	@Autowired
	private IWeighPriceDao priceDao;

	public void delWeighPrice(WeighPrice entity) {
		if (entity.getPriceid() != null) {
			entity.setPriceid(entity.getPriceid().replaceAll(",", "").trim());
		}
		priceDao.del(entity);
	}

	public WeighPrice getWeighPrice(String priceid) {
		return priceDao.findById(priceid);
	}

	public Result<WeighPrice> getWeighPriceList(SearchCondition params, Page page) {
		String pz = (String)params.get("pz");
        String companyname = (String)params.get("companyname");
		StringBuilder hql = new StringBuilder("from WeighPrice t where t.priceid is not null");
		if (!StringUtil.isEmpty(pz)) {
			hql.append(" and t.breedtype='" + pz + "'");
		}
        if (!StringUtil.isEmpty(companyname)) {
            hql.append(" and exists (select 1 from WeighCompany t2 where t2.companycode=t.companycode" +
                    " and t2.companyname like '"+companyname+"%')");
        }
		hql.append(" order by t.companyname, t.breedtype, t.grade");
		Result<WeighPrice> rst = priceDao.findByPage(page, hql.toString());
		return rst;
	}

	public void savWeighPrice(WeighPrice entity) {
		String hql = "select count(*) from WeighPrice where companycode='" + entity.getCompanycode()
				+ "' and breedtype='" + entity.getBreedtype() + "' and grade='" + entity.getGrade()
				+ "'";
		if (!StringUtil.isEmpty(entity.getJizu())) {
			hql += " and jizu='"+entity.getJizu()+"'";
		}
		List<WeighPrice> list = priceDao.findByHql(hql);
		if (list != null || list.size() == 0) {
			hql = "from WeighPrice where companyname='" + entity.getCompanyname()
					+ "' and breedtype='" + entity.getBreedtype() + "' and grade='" + entity.getGrade()
					+ "'";
			if (!StringUtil.isEmpty(entity.getJizu())) {
				hql += " and jizu='"+entity.getJizu()+"'";
			}
			list = priceDao.findByHql(hql);
		}
		if (list.size() != 0) {
			WeighPrice obj = list.get(0);
			if (entity.getPriceid()!=null && entity.getPriceid().endsWith(",")) {
				entity.setPriceid(entity.getPriceid().substring(0, entity.getPriceid().length()-1));
			}
			entity = (WeighPrice)ObjectUtil.objcetMerge(obj, entity);
		}
		priceDao.save(entity);
	}

	public WeighPrice getWeighPriceByCondition(SearchCondition condition) {
		String fenmeihui = condition.get("fenmeihui") == null ? "" : condition.get("fenmeihui").toString();
		String grade = condition.get("fenmeihui") == null ? "" : condition.get("grade").toString();
		String companyname = condition.get("companyname").toString();
		String hql;
		if ("fenmeihui".equals(fenmeihui)) {
			hql = "from WeighPrice t where t.companyname='" + companyname + "' and t.grade='" + grade + "'";
		} else {
			hql = "from WeighPrice t where t.companyname='" + companyname + "'";
		}
		List<WeighPrice> list = priceDao.findByHql(hql);
		return list.size() > 0 == true ? list.get(0) : new WeighPrice();
	}

	/**
	 * @描述: 获得重量匹配的价格
	 */
	public Result<WeighWeighprice> getWeighWeightPriceList(SearchCondition params,
			Page page) {
		String priceid = (String)params.get("priceid");
		return priceDao.findByPage(page, "from WeighWeighprice where priceid=? order by limitWeight", priceid);
	}

	public void savWeighWeightPriceList(EditableGridDataHelper editGridData) {
		List savObj = editGridData.getSaveObjects();
		List delObj = editGridData.getDelObjects();
		for (Object o: savObj) {
			WeighWeighprice p = (WeighWeighprice)o;
			if (StringUtil.isEmpty(p.getId())) {
				p.setId(null);
			}
		}
		priceDao.saveAll(savObj);
		priceDao.delAll(delObj);
	}
}
