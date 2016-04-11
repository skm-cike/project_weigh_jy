package com.est.weigh.report.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;
import org.kohsuke.rngom.parse.host.Base;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.est.common.exception.BaseBussinessException;
import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.classutil.DateUtil;
import com.est.common.ext.util.classutil.NumberUtil;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.weigh.cfginfo.dao.IWeighCompanyDao;
import com.est.weigh.cfginfo.dao.IWeighDataDao;
import com.est.weigh.cfginfo.vo.WeighCompany;
import com.est.weigh.cfginfo.vo.WeighData;
import com.est.weigh.report.dao.IMonthReportDao;
import com.est.weigh.report.vo.GradeForMonthReport;
import com.est.weigh.report.vo.MonthReportList;
import com.est.weigh.report.vo.Monthreport;
import com.est.weigh.report.vo.UnitForMonthReport;
import com.est.weigh.report.vo.WeighJzje;
import com.est.weigh.transaction.dao.IReceiveMoneyDao;

@Service
public class MonthReportServiceImpl implements IMonthReportService {
	@Autowired
	private IMonthReportDao monthDao;
	@Autowired
	private IWeighDataDao weighDao;
	@Autowired
	private IReceiveMoneyDao receiveMoneyDao;
	@Autowired
	private IWeighCompanyDao companyDao;
	@Autowired
	private IDayReportService dayReportService;
	public void createReports(SearchCondition params) {
		String pz = (String)params.get("pz");
		if ("fenmeihui".equals(pz) || "huizha".equals(pz)) {
			params.set("jizu", "31#_32#");
			createReport(params);
			params.set("jizu", "33#_34#");
			createReport(params);
		} else {
			createReport(params);
		}
	}

	/**
	 * @描述: 创建月报
	 */
	public void createReport(SearchCondition params) {
		String pz = (String) params.get("pz");
		String jizu = (String)params.get("jizu");
		String startdate = (String) params.get("startdate");
		String enddate = (String) params.get("enddate");
		String reportdate = (String) params.get("reportdate");
		Date _startdate = DateUtil.parse(startdate);
		Date _enddate = DateUtil.add(DateUtil.parse(enddate), DateUtil.DATE, 1);

		// 获取上期月报
//		String pre_date = DateUtil.format(DateUtil.add(DateUtil.parse(reportdate, "yyyy-MM"), DateUtil.MONTH, -1), "yyyy-MM");
//		StringBuilder hql = new StringBuilder("from Monthreport where breedtype=? and (yearmonth,companycode) in" +
//                " (select max(yearmonth) as maxym, companycode from Monthreport group by companycode)");
//		List params_str = new ArrayList();
//		params_str.add(pz);
//		if (!StringUtil.isEmpty(jizu)) {
//			hql.append(" and jizu=?");
//			params_str.add(jizu);
//		}
//		hql.append(" order by companyname");
//		List<Monthreport> months = monthDao.findByHql(hql.toString(), params_str.toArray());
//		if (months.size() != 0) {
//			Monthreport preMonth = months.get(0);
//			if (DateUtil.add(DateUtil.parse(preMonth.getEnddate()), DateUtil.DATE, 1).compareTo(DateUtil.parse(startdate)) != 0) {
//				throw new BaseBussinessException("本期月报请紧接上期月报的最后一天!");
//			}
//		}
        List params_str = new ArrayList();
		String timeFlag = "gross_time";
		if ("fenmeihui".equals(pz) || "shigao".equals(pz) || "huizha".equals(pz)) {
			timeFlag = "vehicle_time";
		}

		// 设置所有数据都已生成日报
        StringBuilder hql = new StringBuilder("update WeighData set dayreportMark=1 where dayreportMark is null or dayreportMark!=1");
		try {
			monthDao.updateByHql(hql.toString());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		// 验证是否已经生成月报
		hql = new StringBuilder("select count(*) from Monthreport where breedtype=? and yearmonth=?");
		params_str.clear();
		params_str.add(pz);
		params_str.add(reportdate);
		if (!StringUtil.isEmpty(jizu)) {
			hql.append(" and jizu=?");
			params_str.add(jizu);
		}
		Long alreadyMonth = (Long) (this.monthDao.findByHql(hql.toString(),params_str.toArray()).get(0));
		if (alreadyMonth != 0) {
			throw new BaseBussinessException("已有该月月报!");
		}
		// 生成月报
		hql = new StringBuilder("from WeighData where type=? and "+timeFlag+">=? and "+timeFlag+"<?");
		params_str.clear();
		params_str.add(pz);
		params_str.add(_startdate);
		params_str.add(_enddate);
		if (!StringUtil.isEmpty(jizu)) {
			if (jizu.contains("31#")) {
				String[] strs = jizu.split("_");
				hql.append(" and jizu in (?,?)");
				params_str.add(strs[0]);
				params_str.add(strs[1]);
			} else {
				hql.append(" and jizu=?");
				params_str.add(jizu);
			}
		}
		List<WeighData> weights = weighDao.findByHql(hql.toString(),params_str.toArray());
		if ("fenmeihui".equals(pz) || "shigao".equals(pz) || "huizha".equals(pz)) {
			for (WeighData w: weights) {
				if (w.getUnit_price() == null || w.getUnit_price() == 0d) {
					throw new BaseBussinessException("有未设置单价的数据:日期:" + DateUtil.format(w.getVehicle_time()) + ",车号:" + w.getVehicle_no());
				}
			}
		}
		processCompanycode(weights);  //如果companyname=companycode，则需要找到其真实的companycode
		Map<String, List<WeighData>> groupMap = new HashMap(); // 按单位,品种等级和单价分组
		Map<String, List<WeighData>> groupForCompany = new HashMap(); // 按单位分组
		Map<String, List<WeighData>> groupForCompanyGrade = new HashMap(); // 按单位,品种等级分组
		for (WeighData data : weights) {
			// 按单位,品种等级和单价划分
			String key = data.getCompanycode() + "_" + data.getGrade() + "_"
					+ data.getUnit_price();
			List<WeighData> datas = groupMap.get(key);
			if (datas == null) {
				datas = new ArrayList();
				groupMap.put(key, datas);
			}
			datas.add(data);
			// 按单位划分
			key = data.getCompanycode();
			datas = groupForCompany.get(key);
			if (datas == null) {
				datas = new ArrayList();
				groupForCompany.put(key, datas);
			}
			datas.add(data);

			// 按单位,品种等级分组
			key = data.getCompanycode() + "_" + data.getGrade();
			datas = groupForCompanyGrade.get(key);
			if (datas == null) {
				datas = new ArrayList();
				groupForCompanyGrade.put(key, datas);
			}
			datas.add(data);
		}
		List<Monthreport> rst = new ArrayList();
		for (String key : groupMap.keySet()) {
			List<WeighData> datas = groupMap.get(key);
			if (datas == null || datas.size() == 0) {
				continue;
			}
			Monthreport report = new Monthreport();
			String companycode = datas.get(0).getCompanycode();
			String companyname = datas.get(0).getCompanyname();
			String grade = datas.get(0).getGrade();
			Double unitPrice = datas.get(0).getUnit_price();
			double weight = 0d;
			// 出货量
			for (WeighData data : datas) {
				weight = NumberUtil.add(weight, data.getNet_weight());
			}
			report.setCompanycode(companycode);
			report.setCompanyname(companyname);
			report.setGrade(grade);
			report.setChl(weight);
			report.setYearmonth(reportdate);
			report.setStartdate(startdate);
			report.setEnddate(enddate);
			report.setBreedtype(pz);
			report.setDanjia(unitPrice);
			report.setJizu(jizu);
			report.setVehicles((long)datas.size());
			rst.add(report);
		}

		// 获取本期结转金额, 本期货款余额, 本期预收货款,本期销售金额总计
		Map<String, Double[]> vals = processSale(groupForCompany, reportdate,startdate, enddate, pz, jizu);
		for (Monthreport r : rst) {
			Double[] val = vals.get(r.getCompanycode());
			double jzje = val[0];
			double bqye = val[1];
			double bqyshk = val[2];
			double xsjezj = val[3];
			r.setJzje(jzje);
			r.setHkye(bqye);
			r.setYshk(bqyshk);
			r.setXsjexj(xsjezj);
		}

		// 设置按单位,品种等级划分的销售金额
		Map<String, Double> xsjeMap = new HashMap<String, Double>();
		for (String key : groupForCompanyGrade.keySet()) {
			List<WeighData> list = groupForCompanyGrade.get(key);
			double val = 0d;
			for (WeighData data : list) {
				val = NumberUtil.add(val, data.getTotal_price());
			}
			xsjeMap.put(key, val);
		}
		for (Monthreport r : rst) {
			double val = xsjeMap.get(r.getCompanycode() + "_" + r.getGrade());
			r.setXsje(val);
		}

		// 保存月报
		monthDao.saveAll(rst);
		try {
			hql = new StringBuilder("update WeighData set monthreportMark=1 where dayreportMark=1 and type=? and "+timeFlag+">=? and "+timeFlag+"<?");
			params_str.clear();
			params_str.add(pz);
			params_str.add(_startdate);
			params_str.add(_enddate);
			if (!StringUtil.isEmpty("pz")) {
				hql.append("  and jizu=?");
				params_str.add(jizu);
			}
			weighDao.updateByHql(hql.toString(),params_str.toArray());
			weighDao.flushSession();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseBussinessException(e.getMessage());
		}
		// 保存本期结转金额
//		for (String key : vals.keySet()) {
//			Double[] val = vals.get(key);
//			double jzje = val[0]; // 本期结转金额
//			this.accountListDao.savJiezhuanjine(key, pz, reportdate, jzje);
//		}
	}

	private void processCompanycode(List<WeighData> weights) {
		Set<String> names = new HashSet();
		for (WeighData w: weights) {
			if (w.getCompanyname().equals(w.getCompanycode())) {
				names.add(w.getCompanyname());
			}
		}
		if (names.size() == 0) {
			return;
		}
		String str = "";
		for (String s: names) {
			str += "?,";
		}
		str = str.substring(0, str.length() - 1);
		List<WeighCompany> list = companyDao.findByHql("from WeighCompany where companyname in ("+str+")", names.toArray());
		Map<String, String> map = new HashMap();
		for (WeighCompany c: list) {
			map.put(c.getCompanyname(), c.getCompanycode());
		}
		for (WeighData w: weights) {
			if (map.get(w.getCompanyname()) != null) {
				w.setCompanycode(map.get(w.getCompanyname()));
			}
		}
	}

	/**
	 * 
	 * @param groupForCompany
	 * @param reportdate
	 * @param startdate
	 * @param enddate
	 * @return 按公司分组, 顺序获取 [本期结转金额, 本期货款余额, 本期预收货款,本期销售金额总计]
	 */
	private Map<String, Double[]> processSale(
			Map<String, List<WeighData>> groupForCompany, String reportdate,
			String startdate, String enddate, String pz, String jizu) {
		Date _startdate = DateUtil.parse(startdate);
		Date _enddate = DateUtil.add(DateUtil.parse(enddate), DateUtil.DATE, 1);
		Map<String, Double[]> map = new HashMap<String, Double[]>();
		StringBuilder hql = new StringBuilder(200);
		List params = new ArrayList();
		for (String key : groupForCompany.keySet()) {
			List<WeighData> datas = groupForCompany.get(key);
			double money = 0d;
			for (WeighData d : datas) {
				money = NumberUtil.add(money, d.getTotal_price());
			}
			// 找到预收货款,上期结转金额 生成本期结转金额
			// ============本期预收货款============
			hql.delete(0, hql.length());
			params.clear();
			hql.append("select nvl(sum(money),0) from WEIGH_RECEIVEMONEY where BREEDTYPE=? and RECEIVEDATE>=? and RECEIVEDATE<? and COMPANYCODE=? and AUDITTIME is not null");
			params.add(pz);params.add(_startdate);params.add(_enddate);params.add(key);
			if (!StringUtil.isEmpty(jizu)) {
				hql.append(" and jizu=?");
				params.add(jizu);
			}
			double yushouhuokuan = ((BigDecimal)receiveMoneyDao.sqlQuery(hql.toString(),params.toArray()).get(0)).doubleValue();
			
			//=============结转金额=============
			double current_jzje  = 0d;
			//从调整的结转金额中获取
			hql.delete(0, hql.length());
			params.clear();
			hql.append("from WeighJzje where breedtype=? and yearmonth=? and companycode=?");
			params.add(pz);params.add(reportdate);params.add(key);
			if (!StringUtil.isEmpty(jizu)) {
				hql.append(" and jizu=?");
				params.add(jizu);
			}
			hql.append(" order by operdate desc");
			List<WeighJzje> jzjeList = monthDao.findByHql(hql.toString(), params.toArray());
			if (jzjeList.size() != 0) {
				current_jzje = jzjeList.get(0).getMoney();
			}
			// 上期货款余额 作为本期结转金额
			if (current_jzje == 0) {
				String pre_date = DateUtil.format(DateUtil.add(DateUtil.parse(reportdate, "yyyy-MM"), DateUtil.MONTH, -1), "yyyy-MM");
				double pre_ye = 0d;
				hql.delete(0, hql.length());
				params.clear();
				hql.append("select nvl(hkye, 0) from MONTHREPORT where breedtype=? and (yearmonth, companycode) in" +
                        " (select max(yearmonth), companycode from MONTHREPORT where companycode=? ");
                params.add(pz);params.add(key);
				if (!StringUtil.isEmpty(jizu)) {
					hql.append(" and jizu=?");
					params.add(jizu);
				}
                hql.append(" group by companycode)");
                if (!StringUtil.isEmpty(jizu)) {
                    hql.append(" and jizu=?");
                    params.add(jizu);
                }
				List list =  this.monthDao.sqlQuery(hql.toString(),params.toArray());
				if (list.size() != 0) {
					pre_ye = ((BigDecimal)list.get(0)).doubleValue();
				}
				current_jzje = pre_ye;
			}
			// ==========本期余额==================
			double current_ye = NumberUtil.sub(NumberUtil.add(current_jzje, yushouhuokuan), money);
			Double[] vals = new Double[4];
			vals[0] = current_jzje;
			vals[1] = current_ye;
			vals[2] = yushouhuokuan;
			vals[3] = money;
			map.put(key, vals);
		}
		return map;
	}

	public void delReport(SearchCondition params) {
		String pz = (String) params.get("pz");
		String reportdate = (String) params.get("delDate");
		String jizu =  (String) params.get("jizu");
		if (StringUtil.isEmpty(reportdate)) {
			throw new BaseBussinessException("日期不能为空!");
		}
		if (jizu == null) {
			throw new BaseBussinessException("机组不能为空!");
		}
		// 查看是否是最后一月月报,如不是则不能删除
		StringBuilder hql = new StringBuilder("select count(*) from Monthreport where yearmonth>? and breedtype=?");
		List params_str = new ArrayList();
		params_str.add(reportdate);
		params_str.add(pz);
		if (!StringUtil.isEmpty(jizu)) {
			hql.append(" and jizu=?");
			params_str.add(jizu);
		}
		Long count = (Long) this.monthDao.findByHql(hql.toString(),params_str.toArray()).get(0);
		if (count != 0) {
			throw new BaseBussinessException("只能删除最后一个月的月报!");
		}
		//获取本月的月报数据
		hql = new StringBuilder("from Monthreport where yearmonth=? and breedtype=?");
		params_str.clear();
		params_str.add(reportdate);
		params_str.add(pz);
		if (!StringUtil.isEmpty(jizu)) {
			hql.append(" and jizu=?");
			params_str.add(jizu);
		}
		List<Monthreport> list = monthDao.findByHql(hql.toString(), params_str.toArray());
		if (list.size() == 0) {
			throw new BaseBussinessException("没有数据可以删除!");
		}
//		Set<String> company = new HashSet<String>();
//		for (Monthreport report: list) {
//			company.add(report.getCompanycode());
//		}
//		for (String str: company) {
//			this.accountListDao.savJiezhuanjineChongxiao(str, pz, reportdate);
//		}
		String startdate = list.get(0).getStartdate();
		String enddate = list.get(0).getEnddate();
		Date _startdate = DateUtil.parse(startdate);
		Date _enddate = DateUtil.add(DateUtil.parse(enddate), DateUtil.DATE, 1);
		
		try {
            String timeFlag = "gross_time";
            if ("fenmeihui".equals(pz) || "shigao".equals(pz) || "huizha".equals(pz)) {
                timeFlag = "vehicle_time";
            }
			hql = new StringBuilder("update WeighData set monthreportMark=0 where dayreportMark=1 and type=? and "+timeFlag+">=? and "+timeFlag+"<?");
			params_str.clear();
			params_str.add(pz);
			params_str.add(_startdate);
			params_str.add(_enddate);
			if (!StringUtil.isEmpty(jizu)) {
				hql.append(" and jizu=?");
				params_str.add(jizu);
			}
			weighDao.updateByHql(hql.toString(), params_str.toArray());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseBussinessException("删除失败!");
		}
		
		if (list.size() != 0) {
			monthDao.delAll(list);
		}
	}

	public Result<MonthReportList> getReport(SearchCondition params) {
		String yearmonth = (String)params.get("yearmonth");
		if (StringUtil.isEmpty(yearmonth)) {
			throw new BaseBussinessException("请选择要查询的月份!");
		}
		yearmonth = yearmonth.substring(0, 7);
		String pz = (String)params.get("pz");
		String jizu = (String)params.get("jizu");
		Page page = (Page)params.get("page");
		page.setRowPerPage(Integer.MAX_VALUE);
		String hql = "from Monthreport where breedtype=? and yearmonth=? ";
		List<String> params_str = new ArrayList();
		params_str.add(pz);
		params_str.add(yearmonth);
		if (!StringUtil.isEmpty(jizu)) {
			hql += " and jizu=?";
			params_str.add(jizu);
		}
		hql += " order by companycode";
		Result rst = monthDao.findByPage(page, hql, params_str.toArray());
		List<Monthreport> list = rst.getContent();
		if (list.size() == 0) {
			return rst;
		}
		
		//按商家分组
		Map<String, List<Monthreport>> map = new HashMap();
		for (Monthreport report: list) {
			List<Monthreport> reportList = map.get(report.getCompanycode());
			if (reportList == null) {
				reportList = new ArrayList();
				map.put(report.getCompanycode(), reportList);
			}
			reportList.add(report);
		}
		
		List<MonthReportList> rstList = new ArrayList();
		for (String companycode: map.keySet()) {
			List<Monthreport> reportList = map.get(companycode);
			String companyname = reportList.get(0).getCompanyname();
			Double jzje = reportList.get(0).getJzje();
			Double yfk = reportList.get(0).getYshk();
			Double xsjezj = reportList.get(0).getXsjexj();
			Double hkye = reportList.get(0).getHkye();
			String startdate = reportList.get(0).getStartdate();
			String enddate = reportList.get(0).getEnddate();
			double chl = 0d;
			for (Monthreport r: reportList) {
				chl = NumberUtil.add(r.getChl(), chl);
			}
			
			MonthReportList monthrep = new MonthReportList();
			monthrep.setChl(chl);
			monthrep.setCompanycode(companycode);
			monthrep.setCompanyname(companyname);
			monthrep.setHkye(hkye);
			monthrep.setJzje(jzje);
			monthrep.setXsjezj(xsjezj);
			monthrep.setYfk(yfk);
			monthrep.setGradeForMonth(null);
			monthrep.setJizu(jizu);
			monthrep.setPz(pz);
			monthrep.setStartdate(startdate);
			monthrep.setEnddate(enddate);
			rstList.add(monthrep);
		}
		
		
		if ("fenmeihui".equals(pz) || "shigao".equals(pz)) {
			//按商家,品种等级分组
			Map<String, List<Monthreport>> gradMap = new HashMap();
			for (Monthreport report: list) {
				String key = report.getCompanycode() + "_" + report.getGrade();
				List<Monthreport> data = gradMap.get(key);
				if (data == null) {
					data = new ArrayList();
					gradMap.put(key, data);
				}
				data.add(report);
			}
			//按商家,品种等级和价格分组
			Map<String, List<Monthreport>> gradUnitMap = new HashMap();
			for (Monthreport report: list) {
				String key = report.getCompanycode() + "_" + report.getGrade() + "_" + report.getDanjia();
				List<Monthreport> data = gradUnitMap.get(key);
				if (data == null) {
					data = new ArrayList();
					gradUnitMap.put(key, data);
				}
				data.add(report);
			}
			
			//添加等级数据
			Map<String, List<GradeForMonthReport>> gradeData = new HashMap(); //键为 商家
			for (String companygrade: gradMap.keySet()) {
				String companycode = companygrade.split("_")[0];
				 List<Monthreport> data = gradMap.get(companygrade);
				 Double chl = 0d;
				 String  grade = companygrade.split("_")[1];
				 Double xsje = 0d;
				 for (Monthreport r: data) {
					 chl = NumberUtil.add(chl, r.getChl());
					 xsje = NumberUtil.add(r.getXsje(), xsje);
				 }
				 GradeForMonthReport gfmr = new GradeForMonthReport();
				 gfmr.setChl(chl);
				 gfmr.setGrade(grade);
				 gfmr.setXsje(xsje);
				 List<GradeForMonthReport> lis = gradeData.get(companycode);
				 if (lis == null) {lis = new ArrayList();gradeData.put(companycode, lis);}
				 lis.add(gfmr);
			}
			
			//添加单价分组数据
			Map<String, List<UnitForMonthReport>> unitData = new HashMap();  //键为 商家+等级
			for (String companygradeunit:gradUnitMap.keySet()) {
				String[] strs = companygradeunit.split("_");
				String companycode = strs[0];
				String grade = strs[1];
				String unit = strs[2];
				String key = companycode + "_" + grade;
				List<UnitForMonthReport> lis = unitData.get(key);
				if (lis == null) {lis = new ArrayList(); unitData.put(key, lis);}
				for (Monthreport r: gradUnitMap.get(companygradeunit)) {
					UnitForMonthReport ur = new UnitForMonthReport();
					ur.setChl(r.getChl());
					ur.setUnit(r.getDanjia());
					ur.setXsje(r.getXsje());
					lis.add(ur);
				}
			}
			
			//=========整合数据=========
			for (MonthReportList reportl: rstList) {
				//添加等级
				reportl.setGradeForMonth(gradeData.get(reportl.getCompanycode()));
				//添加unit
				if (reportl.getGradeForMonth() != null) {
					for (GradeForMonthReport g: reportl.getGradeForMonth()) {
						String key = reportl.getCompanycode() + "_" + g.getGrade();
						g.setUnitReport(unitData.get(key));
					}
				}
			}
		}
		rst.setContent(rstList);
		rst.getPage().setTotalRows(rstList.size());
		return rst;
	}
	
	/**
	 * @描述: 导出月报
	 */
	public void exportMonthReport(SearchCondition params,HttpServletResponse res) {
		String pz = (String)params.get("pz");
		String yearmonth = (String)params.get("yearmonth");
		String filename = (String)params.get("fileName");
		String username = (String)params.get("username");
		params.set("searchDate", yearmonth);
		Page page = new Page();
		page.setCurPage(0);
		page.setRowPerPage(Integer.MAX_VALUE);
		params.set("page", page);
		params.set("delDate", yearmonth);
		List<MonthReportList> datas = this.getReport(params).getContent();
		if (datas.size() == 0) {
			return;
		}
		for (MonthReportList l: datas) {
			l.setUsername(username);
		}
		String title = "四川巴蜀江油燃煤发电有限公司";
        String jizu = (String)params.get("jizu");
        if (jizu!=null) {
            if (jizu.contains("33#") || jizu.contains("34#")) {
                title = "四川巴蜀江油燃煤发电有限公司";
            } else {
                title = "神华四川能源有限公司江油发电厂";
            }
        }
		if ("fenmeihui".equals(pz)) {
			title += "," + yearmonth.substring(0, 4) + "年度" + params.get("jizu") + "机组粉煤灰销售月报表";
		} else if ("shigao".equals(pz)) {
			title += "," + yearmonth.substring(0, 4) + "年度脱硫石膏销售月报表";
		} else if ("shihuishi".equals(pz)) {
			title += "," + yearmonth.substring(0, 4) + "年度石灰石月报表";
		} else if ("yean".equals(pz)) {
			title += "," + yearmonth.substring(0, 4) + "年度液氨月报表";
		} else if ("suan".equals(pz)) {
			title += "," + yearmonth.substring(0, 4) + "年度酸月报表";
		} else if ("jian".equals(pz)) {
			title += "," + yearmonth.substring(0, 4) + "年度碱月报表";
		} else if ("huizha".equals(pz)) {
			title += "," + yearmonth.substring(0, 4) + "年度灰渣月报表";
		}
		String[] tableHead = new String[2];
		tableHead[0] = "统计时间:" + yearmonth;
		if ("fenmeihui".equals(pz) || "shigao".equals(pz)) {
			tableHead[1] = "编制单位：经营计划部";
		} else {
			tableHead[1] = "编制单位：                                                                                                                                        ";
		}
		MonthReportExcel excel = new MonthReportExcel(res,filename,datas,title,null,tableHead);

		excel.createExcel("yyyy-MM-dd");
	}
	
	/**
	 * @描述: 获取一个连续月份的报表,不对价格分类
	 * @param params
	 * @param yearmonth
	 * @return
	 */
	private Result<MonthReportList> getMonthReportAllowMonth(SearchCondition params, String[] yearmonth) {
		String _yearmonth_str = "";
		for (String s: yearmonth) {
			_yearmonth_str += "?,";
		}
		_yearmonth_str = _yearmonth_str.substring(0, _yearmonth_str.length() - 1);
		String pz = (String)params.get("pz");
		String jizu = (String)params.get("jizu");
		Page page = (Page)params.get("page");
		page.setRowPerPage(Integer.MAX_VALUE);
		String hql = "from Monthreport where breedtype=? and yearmonth in (" + _yearmonth_str + ")";
		List<String> param_str = new ArrayList();
		param_str.add(pz);
		for (int i = 0; i < yearmonth.length; i++) {
			param_str.add(yearmonth[i]);
		}
		if (!StringUtil.isEmpty(jizu)) {
			hql += " and jizu=?";
			param_str.add(jizu);
		}
		hql += " order by startdate,companycode";
		Result rst = monthDao.findByPage(page, hql, param_str.toArray());
		List<Monthreport> list = rst.getContent();
		if (list.size() == 0) {
			return rst;
		}
		
		//按商家分组
		Map<String, List<Monthreport>> map = new HashMap();
		for (Monthreport report: list) {
			List<Monthreport> reportList = map.get(report.getCompanycode());
			if (reportList == null) {
				reportList = new ArrayList();
				map.put(report.getCompanycode(), reportList);
			}
			reportList.add(report);
		}
		
		List<MonthReportList> rstList = new ArrayList();
		for (String companycode: map.keySet()) {
			List<Monthreport> reportList = map.get(companycode);
			String companyname = reportList.get(reportList.size() - 1).getCompanyname();
			Double jzje = reportList.get(0).getJzje();
			Double yfk = 0d;
			Double xsjezj = 0d;
			Double hkye = reportList.get(reportList.size() - 1).getHkye();
			String startdate = reportList.get(0).getStartdate();
			String enddate = reportList.get(reportList.size() - 1).getEnddate();
			double chl = 0d;
			Map<String, Monthreport> map_yfk = new HashMap();  //用来计算预收货款
			for (Monthreport r: reportList) {
//				yfk += r.getYshk();
				String key = r.getCompanycode() + "_" + r.getBreedtype() + "_" + r.getYearmonth();
				if (map_yfk.get(key) == null) {
					map_yfk.put(key, r);
				}
				
				chl += r.getChl();
			}
			
			for (String key: map_yfk.keySet()) {
				yfk += map_yfk.get(key).getYshk();
				xsjezj += map_yfk.get(key).getXsjexj();
			}
			
			MonthReportList monthrep = new MonthReportList();
			monthrep.setChl(chl);
			monthrep.setCompanycode(companycode);
			monthrep.setCompanyname(companyname);
			monthrep.setHkye(hkye);
			monthrep.setJzje(jzje);
			monthrep.setXsjezj(xsjezj);
			monthrep.setYfk(yfk);
			monthrep.setGradeForMonth(null);
			monthrep.setJizu(jizu);
			monthrep.setPz(pz);
			monthrep.setStartdate(startdate);
			monthrep.setEnddate(enddate);
			rstList.add(monthrep);
		}
		
		
		if ("fenmeihui".equals(pz) || "shigao".equals(pz)) {
			//按商家,品种等级分组
			Map<String, List<Monthreport>> gradMap = new HashMap();
			for (Monthreport report: list) {
				String key = report.getCompanycode() + "_" + report.getGrade();
				List<Monthreport> data = gradMap.get(key);
				if (data == null) {
					data = new ArrayList();
					gradMap.put(key, data);
				}
				data.add(report);
			}
			//按商家,品种等级和价格分组
			Map<String, List<Monthreport>> gradUnitMap = new HashMap();
			for (Monthreport report: list) {
				String key = report.getCompanycode() + "_" + report.getGrade() + "_" + report.getDanjia();
				List<Monthreport> data = gradUnitMap.get(key);
				if (data == null) {
					data = new ArrayList();
					gradUnitMap.put(key, data);
				}
				data.add(report);
			}
			
			//添加等级数据
			Map<String, List<GradeForMonthReport>> gradeData = new HashMap(); //键为 商家
			for (String companygrade: gradMap.keySet()) {
				String companycode = companygrade.split("_")[0];
				 List<Monthreport> data = gradMap.get(companygrade);
				 Double chl = 0d;
				 String  grade = companygrade.split("_")[1];
				 Double xsje = 0d;
				 for (Monthreport r: data) {
					 chl += r.getChl();
					 xsje += r.getXsje();
				 }
				 GradeForMonthReport gfmr = new GradeForMonthReport();
				 gfmr.setChl(chl);
				 gfmr.setGrade(grade);
				 gfmr.setXsje(xsje);
				 List<GradeForMonthReport> lis = gradeData.get(companycode);
				 if (lis == null) {lis = new ArrayList();gradeData.put(companycode, lis);}
				 lis.add(gfmr);
			}
			
			//添加单价分组数据
//			Map<String, List<UnitForMonthReport>> unitData = new HashMap();  //键为 商家+等级
//			for (String companygradeunit:gradUnitMap.keySet()) {
//				String[] strs = companygradeunit.split("_");
//				String companycode = strs[0];
//				String grade = strs[1];
//				String unit = strs[2];
//				String key = companycode + "_" + grade;
//				List<UnitForMonthReport> lis = unitData.get(key);
//				if (lis == null) {lis = new ArrayList(); unitData.put(key, lis);}
//				for (Monthreport r: gradUnitMap.get(companygradeunit)) {
//					UnitForMonthReport ur = new UnitForMonthReport();
//					ur.setChl(r.getChl());
//					ur.setUnit(r.getDanjia());
//					ur.setXsje(r.getXsje());
//					lis.add(ur);
//				}
//			}
			
			//=========整合数据=========
			for (MonthReportList reportl: rstList) {
				//添加等级
				reportl.setGradeForMonth(gradeData.get(reportl.getCompanycode()));
				//添加unit
//				for (GradeForMonthReport g: reportl.getGradeForMonth()) {
//					String key = reportl.getCompanycode() + "_" + g.getGrade();
//					g.setUnitReport(unitData.get(key));
//				}
			}
		}
		rst.setContent(rstList);
		rst.getPage().setTotalRows(rstList.size());
		return rst;
	}

	/**
	 * @描述: 导出季报
	 */
	public void exportQuarterReport(SearchCondition params,
			HttpServletResponse res) {
		String pz = (String)params.get("pz");
		String reportyear = (String)params.get("reportyear");
		String filename = (String)params.get("fileName");
		String reporseason = (String)params.get("reporseason");
		String jizu = (String)params.get("jizu");
		String username = (String)params.get("username");
		String[] yearmonth = new String[3];
		String reporseason_str = null;
		if ("1".equals(reporseason)) {
			yearmonth[0] = reportyear + "-01";
			yearmonth[1] = reportyear + "-02";
			yearmonth[2] = reportyear + "-03";
			reporseason_str = "第一季度";
		}
		if ("2".equals(reporseason)) {
			yearmonth[0] = reportyear + "-04";
			yearmonth[1] = reportyear + "-05";
			yearmonth[2] = reportyear + "-06";
			reporseason_str = "第二季度";
		}
		if ("3".equals(reporseason)) {
			yearmonth[0] = reportyear + "-07";
			yearmonth[1] = reportyear + "-08";
			yearmonth[2] = reportyear + "-09";
			reporseason_str = "第三季度";
		}
		if ("4".equals(reporseason)) {
			yearmonth[0] = reportyear + "-10";
			yearmonth[1] = reportyear + "-11";
			yearmonth[2] = reportyear + "-12";
			reporseason_str = "第四季度";
		}
		
		List<MonthReportList> datas = this.getMonthReportAllowMonth(params, yearmonth).getContent();
		if (datas.size() == 0) {
			return;
		}
		for (MonthReportList l: datas) {
			l.setUsername(username);
		}
		String title = "四川巴蜀江油燃煤发电有限公司";
		if ("fenmeihui".equals(pz)) {
			title += "," + reportyear + "年" + reporseason_str + params.get("jizu") + "机组粉煤灰销售季报表";
		} else if ("shigao".equals(pz)) {
			title += "," + reportyear + "年" + reporseason_str + "脱硫石膏销售季报表";
		} else if ("huizha".equals(pz)) {
			title += "," + reportyear + "年" + reporseason_str + "灰渣销售季报表";
		}
		String[] tableHead = new String[2];
		tableHead[0] = "统计时间:" + DateUtil.format(new Date());
		tableHead[1] = "编制单位：经营计划部";
		AllowMonthReportExcel excel = new AllowMonthReportExcel(res,filename,datas,title,null,tableHead);
	
		excel.createExcel("yyyy-MM-dd");
	}

	/**
	 * @描述: 年报
	 */
	public void exportYearReport(SearchCondition params, HttpServletResponse res) {
		String pz = (String)params.get("pz");
		String reportyear = (String)params.get("reportyear");
		String filename = (String)params.get("fileName");
		String jizu = (String)params.get("jizu");
		String username = (String)params.get("username");
		String[] yearmonth = new String[12];
		yearmonth[0] = reportyear + "-01";
		yearmonth[1] = reportyear + "-02";
		yearmonth[2] = reportyear + "-03";
		yearmonth[3] = reportyear + "-04";
		yearmonth[4] = reportyear + "-05";
		yearmonth[5] = reportyear + "-06";
		yearmonth[6] = reportyear + "-07";
		yearmonth[7] = reportyear + "-08";
		yearmonth[8] = reportyear + "-09";
		yearmonth[9] = reportyear + "-10";
		yearmonth[10] = reportyear + "-11";
		yearmonth[11] = reportyear + "-12";
		
		List<MonthReportList> datas = this.getMonthReportAllowMonth(params, yearmonth).getContent();
		if (datas.size() == 0) {
			return;
		}
		for (MonthReportList l: datas) {
			l.setUsername(username);
		}
		String title = "四川巴蜀江油燃煤发电有限公司";
		if ("fenmeihui".equals(pz)) {
			title += "," + reportyear + "年" + params.get("jizu") + "机组粉煤灰销售年报表";
		} else if ("shigao".equals(pz)) {
			title += "," + reportyear + "年" + "脱硫石膏销售年报表";
		} else if ("huizha".equals(pz)) {
			title += "," + reportyear + "年" + "灰渣石膏销售年报表";
		}
		String[] tableHead = new String[2];
		tableHead[0] = "统计时间:" + DateUtil.format(new Date());
		tableHead[1] = "编制单位：经营计划部";
		AllowMonthReportExcel excel = new AllowMonthReportExcel(res,filename,datas,title,null,tableHead);
	
		excel.createExcel("yyyy-MM-dd");
	}

	/**
	 * @描述: 调整结转金额
	 */
	public void savTrimJzje(EditableGridDataHelper editData, SearchCondition params) {
		String pz = (String)params.get("pz");
		String yearmonth = (String)params.get("yearmonth");
        String jizu = (String)params.get("jizu");
        List tlist = editData.getSaveObjects();
        List<MonthReportList> list = new ArrayList(tlist.size());
        if (!validate(yearmonth, pz, jizu)) {
            throw new BaseBussinessException("只能修改最后一月数据!");
        }
        for (Object obj: tlist) {
            list.add((MonthReportList)obj);
        }
        for (MonthReportList r: list) {
            String hql = "from WeighJzje where breedtype=? and yearmonth=? and companycode=? ";
            List par = new ArrayList();
            par.add(pz);
            par.add(yearmonth);
            par.add(r.getCompanycode());
            if (!StringUtil.isEmpty(jizu)) {
                hql += " and jizu=?";
                par.add(jizu);
            }
            hql += " order by operdate desc";
            List<WeighJzje> jzjeList = this.monthDao.findByHql(hql, par.toArray());
            WeighJzje jzje = null;
            if (jzjeList.size() == 0) {
                jzje = new WeighJzje();
            } else {
                jzje = jzjeList.get(0);
            }
            jzje.setCompanycode(r.getCompanycode());
            jzje.setBreedtype(pz);
            jzje.setYearmonth(yearmonth);
            jzje.setMoney(r.getJzje());
            jzje.setOperdate(new Date());
            if (!StringUtil.isEmpty(jizu)) {
				jzje.setJizu(jizu);
            }
            monthDao.save(jzje);

			r.setHkye(NumberUtil.sub(NumberUtil.add(r.getJzje(), r.getYfk()), r.getXsjezj()));
			try {
				hql = "update Monthreport set hkye=?,jzje=? where companycode=? and yearmonth=? and breedtype=?";
				par.clear();
				par.add(r.getHkye());
				par.add(r.getJzje());
				par.add(r.getCompanycode());
				par.add(yearmonth);
				par.add(pz);
				if (!StringUtil.isEmpty(jizu)) {
					hql += " and jizu=?";
					par.add(jizu);
				}
				monthDao.updateByHql(hql, par.toArray());
			} catch (Exception e) {
				e.printStackTrace();
				throw new BaseBussinessException(e);
			}
		}
	}

	public List<String> getYearListByPz(SearchCondition params) {
		String pz = (String)params.get("pz");
		if (StringUtil.isEmpty(pz)) {
			return new ArrayList();
		}
		String dateFlag = "gross_time";
		if ("fenmeihui".equals(pz) || "shigao".equals(pz) || "huizha".equals(pz)) {
			dateFlag = "vehicle_time";
		}
		Map<String, String> map = new HashMap();
		System.out.println("select distinct tochar("+dateFlag+", 'yyyy') from WEIGH_DATA");
		List<String> list = monthDao.sqlQuery("select distinct to_char("+dateFlag+", 'yyyy') from WEIGH_DATA where type=?", pz);
		return list;
	}

	/**
	 * @描述: 获取月份
	 */
	public List<Map<String, String>> getMonthReportByYear(SearchCondition params) {
		String pz = (String)params.get("pz");
		String year = (String)params.get("yearCombox");
		String jizu = (String)params.get("_leftjizu");
		String dateFlag = "gross_time";
		if ("fenmeihui".equals(pz) || "shigao".equals(pz) || "huizha".equals(pz)) {
			dateFlag = "vehicle_time";
		}
		String sql = "select distinct YEARMONTH,sum(VEHICLES),sum(chl),max(STARTDATE),min(ENDDATE) from MONTHREPORT where breedtype='"+pz+"' and YEARMONTH like '"+year+"%'";
		if (!StringUtil.isEmpty(jizu)) {
			sql += " and jizu='"+jizu+"' ";
		}
		sql += " group by YEARMONTH order by YEARMONTH";
		List list = monthDao.sqlQuery(sql);
		List<Map<String, String>> rst = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			Object[] objs = (Object[])list.get(i);
			Map<String, String> map = new HashMap();
			map.put("month", ((String)objs[0]).substring(5,7));
			map.put("year", ((String)objs[0]).substring(0,4));
			map.put("vehicles", objs[1]==null?"":objs[1].toString());
			map.put("weight", objs[2]==null?"":objs[2].toString());
            map.put("date", (objs[3]==null?"":objs[3].toString()) + "~" + (objs[4]==null?"":objs[4].toString()));
			rst.add(map);
		}
		return rst;
	}

    public boolean validate(String reportdate, String pz, String jizu) {
        // 查看是否是最后一月月报
        StringBuilder hql = new StringBuilder("select count(*) from Monthreport where yearmonth>? and breedtype=?");
        List params_str = new ArrayList();
        params_str.add(reportdate);
        params_str.add(pz);
        if (!StringUtil.isEmpty(jizu)) {
            hql.append(" and jizu=?");
            params_str.add(jizu);
        }
        Long count = (Long) this.monthDao.findByHql(hql.toString(),params_str.toArray()).get(0);
        if (count != 0) {
            return false;
        } else {
            return true;
        }
    }
}
