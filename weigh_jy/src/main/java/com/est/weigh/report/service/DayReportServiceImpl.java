package com.est.weigh.report.service;

import java.math.BigDecimal;
import java.util.*;

import com.est.weigh.cfginfo.dao.IWeighPriceDao;
import com.est.weigh.cfginfo.vo.WeighPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.est.common.exception.BaseBussinessException;
import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.classutil.DateUtil;
import com.est.common.ext.util.classutil.NumberUtil;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.weigh.cfginfo.dao.IWeighDataDao;
import com.est.weigh.cfginfo.vo.WeighData;
import com.est.weigh.cfginfo.vo.WeighWeighprice;
import com.est.weigh.report.dao.IMonthReportDao;
import com.est.weigh.report.vo.Dayreport;
import com.est.weigh.report.vo.Monthreport;

import javax.servlet.http.HttpServletResponse;

@Service
public class DayReportServiceImpl implements IDayReportService {
	@Autowired
	private IWeighDataDao weighDao;
	@Autowired
	private IMonthReportDao monthDao;
	@Autowired
	private IWeighPriceDao weighPriceDao;
	public Result<Dayreport> getReport(SearchCondition params, Page page) {
		page.setRowPerPage(10000);
		String searchDate = (String)params.get("searchDate");
		String pz = (String)params.get("pz");
		String jizu = (String)params.get("jizu");
		if (!StringUtil.isEmpty(jizu)) {
            if ("全部".equals(jizu)) {
                jizu = null;
            } else if (jizu.contains("31#")) {
				String[] strs = jizu.split("_");
				jizu = "'" + strs[0] + "','" + strs[1]+"'";
			} else {
				jizu = "'" + jizu + "'";
			}
		}
		String dateFlag = "gross_time";
		if ("fenmeihui".equals(pz) || "shigao".equals(pz) || "huizha".equals(pz)) {
			dateFlag = "vehicle_time";
		}
		String hql = "from WeighData where to_char("+dateFlag+",'yyyy-mm-dd')=? and type=? and dayreportMark=1 ";
		if (!StringUtil.isEmpty(jizu)) {
			hql += " and jizu in ("+jizu+")";
		}
		hql += " order by grade,outno,companyname,pounds_number";
		Result rst = weighDao.findByPage(page, hql, searchDate, pz);
		List<WeighData> weighlist = rst.getContent();
		if (weighlist.size() == 0) {
			return rst;
		}
		
		List<Dayreport> list = new ArrayList(weighlist.size());
		long index = 0;
		for (int i = 0; i < weighlist.size(); i++) {
			WeighData weighdate = weighlist.get(i);
			Dayreport report = new Dayreport();
			list.add(report);
			report.setReportid(++index + "");
			report.setBreedtype(weighdate.getType());
			report.setCompanycode(weighdate.getCompanyname());
			report.setCompanyname(weighdate.getCompanyname());
			report.setGrade(weighdate.getGrade());
			report.setNetWeight(weighdate.getNet_weight());
			report.setPoundsNumber(weighdate.getPounds_number());
			report.setReportDate(DateUtil.format(weighdate.getGross_time()));
			report.setVehicleNo(weighdate.getVehicle_no());
			report.setOutNumber(weighdate.getOutno());
			report.setVehicleNum(1l);
			report.setJizu(weighdate.getJizu());
            report.setUnitPrice(weighdate.getUnit_price());
            report.setTotalMoney(weighdate.getTotal_price());
		}
		Map<String, List<Dayreport>> companyGroup = new HashMap();
		Map<String, List<Dayreport>> gradeGroup = new HashMap();
		for (int i = 0; i < list.size(); i++) {
			Dayreport report = list.get(i);
			List<Dayreport> companyList = companyGroup.get(report.getCompanycode());
			if (companyList == null) {
				companyList = new ArrayList();
				companyGroup.put(report.getCompanycode(), companyList);
			}
			companyList.add(report);
			if ("fenmeihui".equals(pz) || "shigao".equals(pz) || "huizha".equals(pz)) {
				List<Dayreport> gradeList = gradeGroup.get(report.getGrade());
				if (gradeList == null) {
					gradeList = new ArrayList();
					gradeGroup.put(report.getGrade(), gradeList);
				}
				gradeList.add(report);
			}
		}
		
		
		if ("fenmeihui".equals(pz) || "shigao".equals(pz)) {
			String preGrade = list.get(0).getGrade();
			List<Dayreport> last = new ArrayList(list.size() + gradeGroup.size() + 1);
			for (int i = 0; i < list.size(); i++) {
				Dayreport dp = list.get(i);
				boolean isAdd = false;
				if (preGrade!=null && !preGrade.equals(dp.getGrade())) {
					Dayreport t = new Dayreport();
					List<Dayreport> group = gradeGroup.get(preGrade);
					t.setGrade(preGrade + " 合计");
					t.setVehicleNum(new Long(group.size()));
					double weigh = 0d;
                    double totalmoney = 0d;
					for (Dayreport d: group) {
						weigh = NumberUtil.add(weigh, d.getNetWeight());
                        totalmoney = NumberUtil.add(totalmoney, d.getTotalMoney());
					}
					t.setNetWeight(weigh);
                    t.setTotalMoney(totalmoney);
					last.add(t);
					last.add(dp);
					isAdd = true;
					preGrade = dp.getGrade();
				} 
				if (i == list.size() - 1) {
					Dayreport t = new Dayreport();
					List<Dayreport> group = gradeGroup.get(dp.getGrade());
					if (group != null && preGrade != null) {
						t.setGrade(preGrade + " 合计");
						t.setVehicleNum(new Long(group.size()));
						double weigh = 0d;
                        double totalmoney = 0d;
						for (Dayreport d: group) {
							weigh = NumberUtil.add(weigh, d.getNetWeight());
                            totalmoney = NumberUtil.add(totalmoney, d.getTotalMoney());
						}
						t.setNetWeight(weigh);
                        t.setTotalMoney(totalmoney);
						if (!isAdd) {
							last.add(dp);
							isAdd = true;
						}
						
						last.add(t);
						
						Dayreport total = new Dayreport();
						total.setGrade("总计");
						total.setVehicleNum(new Long(list.size()));
						weigh = 0d;
                        totalmoney = 0d;
						for (Dayreport d: list) {
							weigh = NumberUtil.add(weigh, d.getNetWeight());
                            totalmoney = NumberUtil.add(totalmoney, d.getTotalMoney());
						}
						total.setNetWeight(weigh);
                        total.setTotalMoney(totalmoney);
						last.add(total);
					}
				}
				if (!isAdd) {
					last.add(dp);
				}
			}
			rst.setContent(last);
			rst.getPage().setTotalRows(last.size());
			return rst;
		} else {
			List<Dayreport> last = new ArrayList(list.size() + companyGroup.size() + 1);
			String preCompany = list.get(0).getCompanycode();
			for (int i = 0; i < list.size(); i++) {
				Dayreport dp = list.get(i);
				if (!preCompany.equals(dp.getCompanycode())) {
					List<Dayreport> t = companyGroup.get(preCompany);
					Dayreport xiaoji = new Dayreport();
					xiaoji.setCompanycode(preCompany + " 合计");
					xiaoji.setCompanyname(preCompany + " 合计");
					xiaoji.setVehicleNum(new Long(t.size()));
					double weight = 0d;
                    double totalmoney = 0d;
					for (Dayreport r: t) {
						weight = NumberUtil.add(weight, r.getNetWeight());
                        totalmoney = NumberUtil.add(totalmoney, r.getTotalMoney());
					}
					xiaoji.setNetWeight(weight);
                    xiaoji.setTotalMoney(totalmoney);
					last.add(xiaoji);
					preCompany = dp.getCompanycode();
				}
				
				boolean hasAdd = false;
				if (i == list.size() - 1) {
					last.add(dp);
					hasAdd = true;
					List<Dayreport> t = companyGroup.get(preCompany);
					Dayreport xiaoji = new Dayreport();
					xiaoji.setCompanycode(preCompany + " 合计");
					xiaoji.setCompanyname(preCompany + " 合计");
					xiaoji.setVehicleNum(new Long(t.size()));
					double weight = 0d;
                    double totalmoney = 0d;
					for (Dayreport r: t) {
						weight = NumberUtil.add(weight, r.getNetWeight());
                        totalmoney = NumberUtil.add(totalmoney, r.getTotalMoney());
					}
					xiaoji.setNetWeight(weight);
                    xiaoji.setTotalMoney(totalmoney);
					last.add(xiaoji);
					
					//添加总计
					Dayreport zongji = new Dayreport();
					zongji.setCompanycode("总计");
					zongji.setCompanyname("总计");
					zongji.setVehicleNum(new Long(list.size()));
					weight = 0d;
                    totalmoney = 0d;
					for (Dayreport r: list) {
						weight = NumberUtil.add(weight,r.getNetWeight());
                        totalmoney = NumberUtil.add(totalmoney,r.getTotalMoney());
					}
					zongji.setNetWeight(weight);
                    zongji.setTotalMoney(totalmoney);
					last.add(zongji);
				}
				if (!hasAdd) {
					last.add(dp);
				}
			}
			rst.setContent(last);
			rst.getPage().setTotalRows(last.size());
			return rst;
		}
	}

	/**
	 * @描述：生成日报
	 */
	public void savCreateReport(SearchCondition params) {
		String pz = (String)params.get("pz");
		String reportdate = (String) params.get("reportdate");
		//查看是否有当月月报
		Date _reportdate = DateUtil.parse(reportdate, "yyyy-MM-dd");
		String preDate = DateUtil.format(DateUtil.add(_reportdate, DateUtil.MONTH, -1));
		String afterDate = DateUtil.format(DateUtil.add(_reportdate, DateUtil.MONTH, 1));
		List<Monthreport> monthlist = monthDao.findByHql("from Monthreport where yearmonth in (?,?,?) and breedtype=?", reportdate, preDate, afterDate, pz);
		//如果有之前一个月月报和之后一个月月报，则不能再生成
		for (Monthreport m: monthlist) {
			if (DateUtil.parse(m.getStartdate(), "yyyy-MM-dd").before(_reportdate) && DateUtil.parse(m.getEnddate(), "yyyy-MM-dd").after(_reportdate)) {
				throw new BaseBussinessException("已有该月月报，不能生成！");
			}
		}
		
		String timeFlag = "gross_time";
		if ("fenmeihui".equals(pz) || "shigao".equals(pz) || "huizha".equals(pz)) {
			timeFlag = "vehicle_time";
		}
		Long count = (Long)weighDao.findByHql("select count(*) from WeighData where dayreportMark=1 and  type=? and to_char("+timeFlag+",'yyyy-mm-dd')=?", pz, reportdate).get(0);
		if (count != 0) {
			return;
			//throw new BaseBussinessException("已存在" + reportdate + "的日报!");
		}
		count = (Long)weighDao.findByHql("select count(*) from WeighData where dayreportMark=0 and  type=? and to_char("+timeFlag+",'yyyy-mm-dd')=?", pz, reportdate).get(0);
		if (count == 0) {
			return;
		}
		if ("shigao".equals(pz)) {
			count = (Long)weighDao.findByHql("select count(*) from WeighData where grade is null and  type=? and to_char("+timeFlag+",'yyyy-mm-dd')=?", pz, reportdate).get(0);
			if (count != null) {
				throw new BaseBussinessException(reportdate+"的数据尚存在没有设置水份的数据!");
			}
		}
		
		try {
			//根据重量设置价格
			if ("fenmeihui".equals(pz)) {
				List<Object[]> list = weighDao.findByHql("select distinct companycode,grade,sum(net_weight) from WeighData where type=? and to_char("+timeFlag+",'yyyy-mm-dd')=? group by companycode,grade", pz, reportdate);
				for (int i = 0; i < list.size(); i++) {
					Object[] objs = list.get(i);
					String companycode = (String)objs[0];
					String grade = (String)objs[1];
					double sumweight = (Double)objs[2];
					List<WeighWeighprice> prices = weighDao.findByHql("from WeighWeighprice t1 where exists (select 1 from WeighPrice t2 where" +
							" t1.priceid=t2.priceid and t2.companycode=? and t2.breedtype=? and t2.grade=? and t1.limitWeight<=? and t2.enableweighcfg=1)" +
							" order by weightPrice desc"
							, companycode,pz,grade, sumweight);
					if (prices.size() == 0) {
						continue;
					}
					Double unit = prices.get(0).getWeightPrice();
					weighDao.updateByHql("update WeighData set unit_price=?,total_price=(net_weight*?) where type=?" +
										" and to_char("+timeFlag+",'yyyy-mm-dd')=? and grade=? and companycode=?", unit, unit, pz, reportdate, grade, companycode);
				}
			}
		
			weighDao.updateByHql("update WeighData set dayreportMark=1 where type=? and to_char("+timeFlag+",'yyyy-mm-dd')=?", pz, reportdate);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseBussinessException("日报生成失败!");
		}
	}
	
	/**
	 * @描述: 删除日报
	 */
	public void delReport(SearchCondition params) {
		String delDate = (String)params.get("delDate");
		String pz = (String)params.get("pz");
		if (StringUtil.isEmpty(delDate)) {
			throw new BaseBussinessException("请指定删除日期!");
		}
		Date _delDate = DateUtil.parse(delDate, "yyyy-MM-dd");
		String preDate = DateUtil.format(DateUtil.add(_delDate, DateUtil.MONTH, -1));
		String afterDate = DateUtil.format(DateUtil.add(_delDate, DateUtil.MONTH, 1));
		List<Monthreport> list = monthDao.findByHql("from Monthreport where yearmonth in (?,?,?) and breedtype=?", delDate, preDate, afterDate, pz);
		for (Monthreport m: list) {
			if (DateUtil.parse(m.getStartdate(), "yyyy-MM-dd").before(_delDate) && DateUtil.parse(m.getEnddate(), "yyyy-MM-dd").after(_delDate)) {
				throw new BaseBussinessException("已有该月月报，不能删除！");
			}
		}
		String timeFlag = "gross_time";
		if ("fenmeihui".equals(pz) || "shigao".equals(pz) || "huizha".equals(pz)) {
			timeFlag = "vehicle_time";
		}
		try {
			weighDao.updateByHql("update WeighData set dayreportMark=0 where type=? and to_char("+timeFlag+",'yyyy-mm-dd')=?", pz, delDate);;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseBussinessException("删除失败!");
		}
	}

	public void updateToReport() {
		try {
			weighDao.updateByHql("update WeighData set dayreportMark=1 where dayreportMark=0");
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseBussinessException("更新失败!");
		}
	}

	public List<Map<String, String>> getDays(SearchCondition params) {
		String startTime = (String)params.get("startTime");
		String endTime = (String)params.get("endTime");
		String pz = (String)params.get("pz");
		String timeFlag = "gross_time";
		if ("fenmeihui".equals(pz) || "shigao".equals(pz) || "huizha".equals(pz)) {
			timeFlag = "vehicle_time";
		}
		String sql = "select to_char("+timeFlag+", 'yyyy-mm-dd'),count(*) from weigh_data "
				+ " where to_char("+timeFlag+", 'yyyy-mm-dd')>=? and to_char("+timeFlag+", 'yyyy-mm-dd')<=?"
				+ " and type=? group by to_char("+timeFlag+", 'yyyy-mm-dd') order by to_char("+timeFlag+", 'yyyy-mm-dd')";
		List list = weighDao.sqlQuery(sql, startTime, endTime, pz);
		List rst = new ArrayList();
		for (Object obj: list) {
			Map<String, String> map = new HashMap();
			Object[] objs = (Object[])obj;
			map.put("day", (String)objs[0]);
			map.put("vehiclenums", objs[1]+"");
			rst.add(map);
		}
		return rst;
	}



	/**
	 * @描述: 导出日报
	 * @param params
	 * @param res
	 */
	public void exportMonthReport(SearchCondition params, HttpServletResponse res) {
		String pz = (String)params.get("pz");
		String day = (String)params.get("day");
		String jizu = (String)params.get("jizu");
		String fileName = (String)params.get("fileName");
		params.set("searchDate", day);
		Page page = new Page();
		page.setRowPerPage(10000);
		List<Dayreport> lst = this.getReport(params, page).getContent();
		String[] needFields = null;
		String[] tableHead = null;
		String title = null;
		String year = day.substring(0, 4);
		title = year + "年度";
		if ("fenmeihui".equals(pz)) {
			title += jizu+"机组粉煤灰销售日报";
			needFields = new String[]{"grade","companyname", "outNumber", "poundsNumber", "vehicleNo", "netWeight", "vehicleNum"};
			tableHead = new String[]{"品种等级","单位名称", "出门证号", "磅单号", "车号", "重量", "车数"};
		} else if ("huizha".equals(pz)) {
			title += jizu+"机组灰渣销售日报";
			needFields = new String[]{"companyname", "outNumber", "poundsNumber", "vehicleNo", "netWeight", "vehicleNum"};
			tableHead = new String[]{"单位名称", "出门证号", "磅单号","车号", "重量", "车数"};
		} else if ("shigao".equals(pz)) {
			title += "石膏销售日报";
			needFields = new String[]{"grade","companyname", "outNumber", "poundsNumber", "vehicleNo", "netWeight", "vehicleNum"};
			tableHead = new String[]{"水份","单位名称", "出门证号", "磅单号","车号", "重量", "车数"};
		} else if ("suan".equals(pz)) {
			title += "酸进厂日报";
			needFields = new String[]{"companyname", "outNumber", "poundsNumber", "vehicleNo", "netWeight", "vehicleNum"};
			tableHead = new String[]{"单位名称", "出门证号", "磅单号", "车号", "重量", "车数"};
		} else if ("jian".equals(pz)) {
			title += "碱进厂日报";
			needFields = new String[]{"companyname", "outNumber", "poundsNumber", "vehicleNo", "netWeight", "vehicleNum"};
			tableHead = new String[]{"单位名称", "出门证号", "磅单号", "车号", "重量", "车数"};
		} else if ("yean".equals(pz)) {
			title += "液氨进厂日报";
			needFields = new String[]{"companyname", "outNumber", "poundsNumber", "vehicleNo", "netWeight", "vehicleNum"};
			tableHead = new String[]{"单位名称", "出门证号", "磅单号", "车号", "重量", "车数"};
		} else if ("shihuishi".equals(pz)) {
			title += "石灰石进厂日报";
			needFields = new String[]{"companyname", "outNumber", "poundsNumber", "vehicleNo", "netWeight", "vehicleNum"};
			tableHead = new String[]{"单位名称", "出门证号", "磅单号", "车号", "重量", "车数"};
		}
		String date = "日期:" + day;
		DayReportExcel dre = new DayReportExcel(res, fileName, lst, title, needFields, tableHead);
		dre.createExcel();
	}

	/**
	 * @描述: 重新处理过磅数据
	 */
	public void savReCalculate(SearchCondition params) {
		String day = (String)params.get("day");
		String pz = (String)params.get("pz");
		String jizu = (String)params.get("jizu");
		String hql = "from WeighData where type=? and to_char(vehicle_time, 'yyyy-mm-dd')=?";
		List par = new ArrayList();
		par.add(pz);
		par.add(day);
		if (!StringUtil.isEmpty(jizu)) {
            if (jizu.contains("31#")) {
                String[] strs = jizu.split("_");
                hql += "and jizu in (?,?)";
                par.add(strs[0]);
                par.add(strs[1]);
            } else {
                hql += " and jizu=?";
                par.add(jizu);
            }
		}
		List<WeighData> lst = weighDao.findByHql(hql,par.toArray());
		Set<String> companycode = new HashSet();
        Set<WeighData> resetcompanynameData = new HashSet();                //重设单位代码
		for (WeighData w: lst) {
            if (!StringUtil.isEmpty(w.getCompanyname()) && w.getCompanyname().equals(w.getCompanycode())) {
                resetcompanynameData.add(w);
            }
		}
        //重设单位代码
        //根据名称找到单位代码
        if (resetcompanynameData.size() != 0) {
            Set<String> con = new HashSet();
            for (WeighData w : resetcompanynameData) {
                con.add(w.getCompanyname());
            }
            String str = "";
            for (String s : con) {
                str += "'" + s + "',";
            }
            if (!"".equals(str)) {
                str = str.substring(0, str.length() - 1);
                List companycodeByName = weighPriceDao.sqlQuery("select companycode,companyname from WEIGH_COMPANY where companyname in (" + str + ") and type='" + pz + "'");
                Map<String, String> nameandcodeMap = new HashMap();
                for (Object obj : companycodeByName) {
                    Object[] os = (Object[]) obj;
                    nameandcodeMap.put((String) os[1], (String) os[0]);
                }
                for (WeighData w : resetcompanynameData) {
                    w.setCompanycode(nameandcodeMap.get(w.getCompanyname()));
                }
                weighPriceDao.saveAll(new ArrayList(resetcompanynameData));
            }
        }

        for (WeighData w: lst) {
            if (!StringUtil.isEmpty(w.getCompanycode())) {
                companycode.add(w.getCompanycode());
            } else {
                throw new BaseBussinessException("有无法找到单位代码的数据,名称为:<font color=\"green\">" + w.getCompanyname() + "</font>");
            }
        }

		String ccs = "";
		par.clear();
		for (String str: companycode) {
			ccs += "?,";
			par.add(str);
		}

		if (!"".equals(ccs)) {
			ccs = ccs.substring(0, ccs.length() - 1);
		} else {
            return;
        }

		par.add(pz);
		hql = "from WeighPrice where (companycode in ("+ccs+")) and breedtype=?";
		List<WeighPrice> prices = weighPriceDao.findByHql(hql, par.toArray());
		Map<String, WeighPrice> map = new HashMap();   //根据单位代码，品种，品种等级，机组 获取单价
		for (WeighPrice p: prices) {
			if (StringUtil.isEmpty(p.getGrade())) {
				p.setGrade(null);
			}
			if (StringUtil.isEmpty(p.getJizu())) {
				p.setJizu(null);
			}
			map.put(p.getCompanycode() + "_" + p.getBreedtype() + "_" + p.getGrade() + "_" + p.getJizu(), p);
		}
        for (WeighData w: lst) {
            if (StringUtil.isEmpty(w.getJizu())) {
                w.setJizu(null);
            }
            if (StringUtil.isEmpty(w.getGrade())) {
                w.setGrade(null);
            }
            String _jizu = w.getJizu();
            if (w.getJizu()!=null && (w.getJizu().contains("31#") || w.getJizu().contains("32#"))) {
                _jizu = "31#_32#";
            }
            WeighPrice wp = map.get(w.getCompanycode() + "_" + w.getType() + "_" +w.getGrade() + "_" +_jizu);
            if (wp == null) {
                String msg = "没有找到商家的价格配置,名称为:<font color=\"green\">" + w.getCompanyname() + "</font>";
                if (!StringUtil.isEmpty(w.getGrade())) {
                    msg += ",等级为:<font color=\"green\">"+w.getGrade()+"</font>";
                }
                if (!StringUtil.isEmpty(w.getJizu())) {
                    msg += ",机组为:<font color=\"green\">"+w.getJizu()+"</font>";
                }
                throw new BaseBussinessException(msg);
            }
            Double price = wp.getUnit_price();
			w.setNet_weight(NumberUtil.sub(w.getGross_weight(), NumberUtil.add(w.getDe_weight(), w.getVehicle_weight())));
            w.setUnit_price(price);
            w.setTotal_price(NumberUtil.mul(w.getNet_weight(), w.getUnit_price()));
        }
        weighDao.saveAll(lst);
	}

    /**
     * @描述: 按重量计算价格
     * @param params
     */
    public void reCalculateByWeight(SearchCondition params) {
        String day = (String)params.get("day");
        String pz = (String)params.get("pz");
        String jizu = (String)params.get("jizu");
        String hql = "from WeighData where type=? and to_char(vehicle_time, 'yyyy-mm-dd')=?";
        List par = new ArrayList();
        par.add(pz);
        par.add(day);
        if (!StringUtil.isEmpty(jizu)) {
            if (jizu.contains("31#")) {
                String[] strs = jizu.split("_");
                hql += "and jizu in (?,?)";
                par.add(strs[0]);
                par.add(strs[1]);
            } else {
                hql += " and jizu=?";
                par.add(jizu);
            }
        }
        List<WeighData> lst = weighDao.findByHql(hql,par.toArray());
        Set<String> companycode = new HashSet();
        for (WeighData w: lst) {
            if (!StringUtil.isEmpty(w.getCompanycode())) {
                companycode.add(w.getCompanycode());
            } else {
                throw new BaseBussinessException("有无法找到单位代码的数据,名称为:<font color=\"green\">" + w.getCompanyname() + "</font>");
            }
        }

        //分组计算重量
        String sql = "select sum(t.net_weight), t.companycode, t.grade, t.tjizu " +
                    "  from (select t.*, " +
                    "               case " +
                    "                 when t.jizu = '31#' then " +
                    "                  '31#_32#' " +
                    "                 when t.jizu = '32#' then " +
                    "                  '31#_32#' " +
                    "                 else " +
                    "                  t.jizu " +
                    "               end tjizu " +
                    "          from weigh_data t " +
                    "         where to_char(t.vehicle_time, 'yyyy-mm-dd') = ? " +
                    "           and t.type = ?) t " +
                    " where t.tjizu = ? " +
                    " group by t.companycode, t.grade, t.tjizu";
        List groupWeight = weighDao.sqlQuery(sql, day, pz, jizu);
        Map<String, Double> groupWeightMap = new HashMap();
        for (Object obj: groupWeight) {
            Object[] objs = (Object[])obj;
            if (objs[0] == null) {
                objs[0]= new BigDecimal(0d);
            }
            if (StringUtil.isEmpty((String)objs[1])) {
                objs[1] = null;
            }
            if (StringUtil.isEmpty((String)objs[2])) {
                objs[2] = null;
            }
            if (StringUtil.isEmpty((String)objs[3])) {
                objs[3] = null;
            }
            String grs = objs[1] + "_" + pz + "_" + objs[2] + "_" + objs[3];
            groupWeightMap.put(grs, ((BigDecimal)objs[0]).doubleValue());
        }

        //找到价格
        String ccs = "";
        par.clear();
        for (String str: companycode) {
            ccs += "?,";
            par.add(str);
        }
        if (!"".equals(ccs)) {
            ccs = ccs.substring(0, ccs.length() - 1);
        } else {
            return;
        }
        par.add(pz);
        hql = "from WeighPrice where companycode in ("+ccs+") and breedtype=?";
        List<WeighPrice> prices = weighPriceDao.findByHql(hql, par.toArray());
        Map<String, List<WeighWeighprice>> mapForWeight = new HashMap();   //根据单位代码，品种，品种等级，机组 获取单价
		Map<String, String> mapForCompany = new HashMap();
        for (WeighPrice p: prices) {
            if (StringUtil.isEmpty(p.getGrade())) {
                p.setGrade(null);
            }
            if (StringUtil.isEmpty(p.getJizu())) {
                p.setJizu(null);
            }
            //获取按重量计算的价格
            hql = "from WeighWeighprice where priceid=? order by limitWeight";
            List<WeighWeighprice> wwps = weighPriceDao.findByHql(hql, p.getPriceid());
			mapForCompany.put(p.getCompanycode(), p.getCompanyname());
			mapForWeight.put(p.getCompanycode() + "_" + p.getBreedtype() + "_" + p.getGrade() + "_" + p.getJizu(), wwps);
        }

		//查找单价
		Map<String, Double> unitMap = new HashMap();
		for (String key: groupWeightMap.keySet()) {
			List<WeighWeighprice> wwps = mapForWeight.get(key);
			if (wwps == null) {
				String[] strs = key.split("_");
				String msg = "单位:" + mapForCompany.get(strs[0]) + ",等级:" + strs[2] + ",机组:" + strs[3] + "没有找到其按重量配置的价格!";
				throw new BaseBussinessException(msg);
			}
			Double weights = groupWeightMap.get(key);
			for (int i = 0; i < wwps.size(); i++) {
				if (i != wwps.size() - 1) {
					WeighWeighprice current = wwps.get(i);
					WeighWeighprice next = wwps.get(i+1);
					if (weights >= current.getLimitWeight() && weights < next.getLimitWeight()) {
						unitMap.put(key, current.getWeightPrice());
						break;
					}
				} else {
					unitMap.put(key, wwps.get(i).getWeightPrice());
					break;
				}
			}
		}

		//配置单价
		for (WeighData d: lst) {
			d.setNet_weight(NumberUtil.sub(d.getGross_weight(), NumberUtil.add(d.getDe_weight(), d.getVehicle_weight())));
			String _jizu = null;
			if (d.getJizu().contains("31#") || d.getJizu().contains("32#")) {
				_jizu = "31#_32#";
			} else {
				_jizu = d.getJizu();
			}
			String key = d.getCompanycode() + "_" + d.getType() + "_" + d.getGrade() + "_" + _jizu;
			Double price = unitMap.get(key);
			if (price == null) {
				String msg = "单位:<font color=\"green\">" + d.getCompanyname() + "</font>,等级:<font color=\"green\">" + d.getGrade()
						+ "</font>,机组:<font color=\"green\">" + _jizu + "</font>没有找到其按重量配置的价格!";
				throw new BaseBussinessException(msg);
			}
			d.setUnit_price(price);
			d.setTotal_price(NumberUtil.mul(d.getUnit_price(), d.getNet_weight()));
		}
		weighDao.saveAll(lst);
    }
}
