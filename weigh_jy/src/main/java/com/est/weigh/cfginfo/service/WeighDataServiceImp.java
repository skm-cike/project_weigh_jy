package com.est.weigh.cfginfo.service;

import java.util.*;

import com.est.weigh.cfginfo.dao.IWeighCompanyDao;
import com.est.weigh.cfginfo.vo.WeighCompany;

import com.est.weigh.report.service.IDayReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.est.common.exception.BaseBussinessException;
import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.classutil.DateUtil;
import com.est.common.ext.util.classutil.NumberUtil;
import com.est.common.ext.util.classutil.ObjectUtil;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.weigh.cfginfo.dao.IWeighDataDao;
import com.est.weigh.cfginfo.dao.IWeighPriceDao;
import com.est.weigh.cfginfo.vo.WeighData;

@Service
public class WeighDataServiceImp implements IWeighDataService {
	@Autowired
	private IWeighDataDao dataDao;
	@Autowired
	private IWeighPriceDao priceDao;
    @Autowired
    private IWeighCompanyDao companyDao;

	public void delWeighData(WeighData entity) {
//		if (!StringUtil.isEmpty(entity.getId()) && ("fenmeihui".equals(entity.getType()) || "shigao".equals(entity.getType())) ) {
//			//冲销
//			WeighData old = dataDao.findById(entity.getId());
//			if (old.getUnit_price() != null && old.getUnit_price() != 0) {
//				accountListDao.savKoukuanchongxiao(old.getType(), old.getOperatTime(), old.getId(), old.getCompanyname(), old.getGrade(), old.getRemark());
//			}
//			dataDao.getSessionObj().evict(old);
//		}
		dataDao.del(entity);
	}

	public WeighData getWeighData(String id) {
		return dataDao.findById(id);
	}

	public Result<WeighData> getWeighDataList(SearchCondition params, Page page) {
		String pz = (String)params.get("pz");
		String startTime = (String) params.get("startTime");
		String endTime = (String) params.get("endTime");
		String companyName = (String) params.get("companyCombox");
        String isexportorder = (String) params.get("isexporder");
		
		if (!StringUtil.isEmpty(startTime)) {
			startTime = startTime.substring(0, 10) + " 00:00:00";
		}
		
		if (!StringUtil.isEmpty(endTime)) {
			endTime = endTime.substring(0, 10) + " 23:59:59";
		}
		
		StringBuilder hql = new StringBuilder("from WeighData t where 1=1 ");
		List param = new ArrayList();

        String timeFlag = "gross_time";
        if ("fenmeihui".equals(pz) || "shigao".equals(pz) || "huizha".equals(pz)) {
            timeFlag = "vehicle_time";
        }
		
		if (!StringUtil.isEmpty(pz)) {
			hql.append(" and type=?");
			param.add(pz);
		}
		if (!StringUtil.isEmpty(startTime)) {
			hql.append("  and t." + timeFlag + ">=?");
			param.add(DateUtil.parse(startTime, "yyyy-MM-dd HH:mm:ss"));
		}
		if (!StringUtil.isEmpty(endTime)) {
			hql.append("  and t."+timeFlag+"<=?");
			param.add(DateUtil.parse(endTime, "yyyy-MM-dd HH:mm:ss"));
		}
		if (!StringUtil.isEmpty(companyName)) {
			hql.append(" and t.companyname=?");
			param.add(companyName);
		}
        if ("yes".equals(isexportorder)) {
            if ("fenmeihui".equals(pz)) {
                hql.append("  order by t.grade, t.jizu, t." + timeFlag + " asc");
            } else if ("shigao".equals(pz)) {
                hql.append("  order by t.grade, t." + timeFlag + " asc");
            } else {
                hql.append("  order by t.jizu, t." + timeFlag + " asc");
            }

        } else {
            hql.append("  order by t." + timeFlag + " desc");
        }
		
		Result<WeighData> res = dataDao.findByPage(page, hql.toString(), param.toArray());
		return res;
	}

	public void savWeighData(WeighData entity) {
        try {
            dataDao.updateByHql("update WeighData set grade='灰渣' where type='huizha' and grade is null");
        } catch (Exception e) {
            e.printStackTrace();
        }
		//如果值没有变化，则不保存
		if (!StringUtil.isEmpty(entity.getId())) {
			
		}
		entity.setOperatTime(new Date());
		
		WeighData old = null;
		if (entity.getId() != null && !"".equals(entity.getId())) {
			old = dataDao.findById(entity.getId());
			if (old.getMonthreportMark() == 1) {
				throw new BaseBussinessException("该数据已生成月报!");
			}
			if (old.getDayreportMark() == 1) {
//				throw new BaseBussinessException("该数据已生成日报!");
			}
		}
		
		entity.setDayreportMark(0);
		entity.setMonthreportMark(0);
		
		//净重
		entity.setNet_weight(NumberUtil.sub(entity.getGross_weight(), NumberUtil.add(entity.getDe_weight(), entity.getVehicle_weight())));
		
		//获取单价
		if ("fenmeihui".equals(entity.getType()) || "shigao".equals(entity.getType()) || "huizha".equals(entity.getType())) {
			entity.setInouttype(2l);
			if ("shigao".equals(entity.getType())) {
//				entity.setGrade(priceDao.getGradeByShigao(entity.getCompanyname(), entity.getType(), entity.getWater()));
			}
			if (entity.getUnit_price() == null || entity.getUnit_price() == 0d) {
				Double unitPrice = priceDao.getUnitPrice(entity.getCompanyname(), entity.getType(), entity.getGrade(), entity.getJizu());
				if (unitPrice != null) {
					entity.setUnit_price(unitPrice);
				}
			}
			if (entity.getUnit_price() != null && entity.getUnit_price() != 0d) {
				entity.setTotal_price(NumberUtil.mul(entity.getUnit_price(), entity.getNet_weight()));
			}
		} else {
			entity.setInouttype(1l);
		}
		
		if ("fenmeihui".equals(entity.getType()) || "shigao".equals(entity.getType())) {
			if (!StringUtil.isEmpty(entity.getId()) && entity.getUnit_price() != null && entity.getUnit_price() != 0) {
				//冲销
				if (old.getUnit_price() != null && old.getUnit_price() != 0) {
//					accountListDao.savKoukuanchongxiao(old.getType(), old.getOperatTime(), old.getId(), old.getCompanyname(), old.getGrade(), old.getRemark());
				}
			}
		}
		if (old != null) {
            ObjectUtil.objcetMerge(old, entity);
        } else {
            old = entity;
        }
		//保存过磅数据
		dataDao.save(old);
		
		//保存扣款记录
		if ("fenmeihui".equals(entity.getType()) || "shigao".equals(entity.getType())) {
			if (entity.getUnit_price() != null && entity.getUnit_price() != 0) {
//				accountListDao.savKoukuan(entity.getCompanyname(), entity.getType(), entity.getGrade(), entity.getRemark(), entity.getOperatTime(), entity.getTotal_price(), entity.getId());
			}
		}
	}

	public String savWeightDats(List<WeighData> list) {
        if (list.size() == 0) {
            return null;
        }

        //根据磅单号查找已经存在的数据
        StringBuilder waterNo = new StringBuilder("");
        for (WeighData d: list) {
            waterNo.append("'" + d.getPounds_number() + "',");
        }
        try {
            dataDao.updateByHql("update WeighData set grade='灰渣' where type='huizha' and grade is null");
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<WeighData> olds = dataDao.findByHql("from WeighData where pounds_number in (" + waterNo.substring(0, waterNo.length()-1) + ")");
        List<WeighData> nonUpdate = new ArrayList();
        for (WeighData n: list) {
            for (WeighData o: olds) {
                if (n.getPounds_number().equals(o.getPounds_number()) && o.getOutno()!=null && o.getOutno().equals(n.getOutno())) {
                    nonUpdate.add(n);
                    break;
                }
            }
        }
        list.removeAll(nonUpdate);
        for (WeighData o: olds) {
            dataDao.getSessionObj().evict(o);
        }
		//设置商家编码
        if (list.size() != 0) {
            StringBuilder sb = new StringBuilder("");
            Set<String> companynames = new HashSet();
            Map<String, String> map = new HashMap();
            for (WeighData e: list) {
                companynames.add(e.getCompanyname());
            }
            for (String s: companynames) {
                sb.append("'" + s + "',");
            }
            List<WeighCompany> companies = companyDao.findByHql("from WeighCompany where companyname in (" + sb.substring(0, sb.length() - 1) + ") and status=1");
            for (WeighCompany c: companies) {
                map.put(c.getCompanyname(), c.getCompanycode());
            }
            companynames.removeAll(map.keySet());
            if (companynames.size() != 0) {
                for (String s: companynames) {
                    companies = companyDao.findByHql("from WeighCompany where beforename like '%" + s + "%' and status=1");
                    if (companies.size() != 0) {
                        map.put(s, companies.get(0).getCompanycode());
                    }
                }
            }
            for (WeighData entity: list) {
                entity.setCompanycode(map.get(entity.getCompanyname()));
            }

            //设置价格
            for (WeighData entity: list) {
                //计算价格
                if ("fenmeihui".equals(entity.getType()) || "shigao".equals(entity.getType()) || "huizha".equals(entity.getType())) {
                    entity.setInouttype(2l);
//                    if ("shigao".equals(entity.getType())) {
//                        entity.setGrade(priceDao.getGradeByShigao(entity.getCompanyname(), entity.getType(), entity.getWater()));
//                    }
                    if (entity.getUnit_price() == null || entity.getUnit_price() == 0d) {
                        Double unitPrice = priceDao.getUnitPrice(entity.getCompanyname(), entity.getType(), entity.getGrade(), entity.getJizu());
                        if (unitPrice != null) {
                            entity.setUnit_price(unitPrice);
                        }
                    }
                    if (entity.getUnit_price() != null && entity.getUnit_price() != 0d) {
                        entity.setTotal_price(NumberUtil.mul(entity.getUnit_price(), entity.getNet_weight()));
                    }
                }
            }
            dataDao.saveAll(list);
        }
        String rst = "";
        for (WeighData n: list) {
            rst += n.getPounds_number() + ",";
        }
        for (WeighData non: nonUpdate) {
            rst += non.getPounds_number() + ",";
        }
        if (rst.endsWith(",")) {
            rst = rst.substring(0, rst.length() - 1);
        }
        return rst;
	}

    public void batchSetWater(SearchCondition params) {
        String day = (String)params.get("day");
        String grade = (String)params.get("grade");
        String timeFlag = "vehicle_time";
        List<WeighData> wds = dataDao.findByHql("from WeighData where to_char("+timeFlag+",'yyyy-mm-dd')=? and type=?",day, "shigao");
        for (WeighData d: wds) {
            d.setGrade(grade);
            savWeighData(d);
        }
    }
}
