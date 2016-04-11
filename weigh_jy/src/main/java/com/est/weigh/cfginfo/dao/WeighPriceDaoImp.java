package com.est.weigh.cfginfo.dao;

import com.est.common.base.BaseDaoImp;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.weigh.cfginfo.vo.WeighPrice;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class WeighPriceDaoImp extends BaseDaoImp<WeighPrice> implements IWeighPriceDao {

	public Double getUnitPrice(String companyName, String pz, String grade, String jizu) {
		if (StringUtil.isEmpty(companyName) || StringUtil.isEmpty(pz) || StringUtil.isEmpty(grade)) {
			return null;
		}
		String hql = "from WeighPrice t where t.companyname=? and t.breedtype=? and t.grade=?";
		if (!StringUtil.isEmpty(jizu)) {
			hql += " and t.jizu like '%"+jizu+"%'";
		}
		WeighPrice wp = (WeighPrice) this.findUniqueByHql(hql, companyName, pz, grade);
		if (wp == null) {
			return null;
		}
		if (wp.getEnableweighcfg() != null && wp.getEnableweighcfg() == 1) {   //�������������ü۸����ݲ�����
			return null;
		}
		return wp.getUnit_price();
	}

	public String getGradeByShigao(String companyname, String type, Double water) {
		List<WeighPrice> list = this.findByHql("from WeighPrice where companyname=? and breedtype=?", companyname, type);
		List<Water> watercfg = new ArrayList<Water>();
		for (WeighPrice p: list) {
			if (StringUtil.isEmpty(p.getGrade())) {
				continue;
			}
			if (p.getGrade().contains("~")) {
				Water w = new Water();
				String[] strs = p.getGrade().split("~");
				if (strs.length != 2) {
					continue;
				}
				w.min = Double.parseDouble(strs[0]);
				w.max = Double.parseDouble(strs[1]);
				w.grade = p.getGrade();
				watercfg.add(w);
			} else if (p.getGrade().contains("<")) {
				Water w = new Water();
				w.min = 0d;
				w.max = Double.parseDouble(p.getGrade().replace("<", ""));
				w.grade = p.getGrade();
				watercfg.add(w);
			} else if (p.getGrade().contains(">")) {
				Water w = new Water();
				w.min = Double.parseDouble(p.getGrade().replace(">", ""));
				w.max = 100000d;
				w.grade = p.getGrade();
				watercfg.add(w);
			}
		}
		
		for (Water w: watercfg) {
			if (water >= w.min && water<w.max) {
				return w.grade;
			}
		}
		return null;
	}
	private class Water {
		public String grade;
		public double max;
		public double min;
	}
}
