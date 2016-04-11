package com.est.weigh.cfginfo.service;

import java.util.ArrayList;
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
import com.est.weigh.cfginfo.common.GoodsType;
import com.est.weigh.cfginfo.dao.IWeighCompanyDao;
import com.est.weigh.cfginfo.vo.WeighCompany;
import com.est.weigh.download.service.IDownloadService;
import com.est.weigh.download.vo.Download;

@Service
public class WeighCompanyServiceImp implements IWeighCompanyService {
	@Autowired
	private IWeighCompanyDao tcompanyDao;

	public void delCompany(WeighCompany entity) {
        //检查该商家下是否还有车辆存在
        List list = tcompanyDao.findByHql("select count(*) from WeighVehicle where companyid=?", entity.getCompanyid());
        if ((Long)list.get(0) != 0) {
            throw new BaseBussinessException("");
        }
//        downloadService.delCompanyAndVehicle(entity.getCompanyname(), entity.getType());
        //删除商家
		tcompanyDao.del(entity);
		//删除其下所有车辆
		try {
			tcompanyDao.updateByHql("delete from WeighVehicle where companyid=?", entity.getCompanyid());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseBussinessException(e);
		}
	}

	public WeighCompany getCompany(String transcompanyid) {
		return tcompanyDao.findById(transcompanyid);
	}

    public List<Download> getDownloads(Date date) {
        return null;
    }

    public Result<WeighCompany> getCompanyList(SearchCondition params, Page page) {
		String status = (String) params.get("status");
		String pz = (String) params.get("pz");
		String inouttype = (String) params.get("inouttype");
		String companyname = (String) params.get("companyname");
		StringBuilder hql = new StringBuilder("from WeighCompany t where t.companyid is not null and t.companycode is not null");
		if (!StringUtil.isEmpty(pz)) {
			hql.append(" and t.type like '%" + pz + "%'");
		}
		if (!StringUtil.isEmpty(status)) {
			hql.append(" and t.status=" + status);
		}
		if (!StringUtil.isEmpty(inouttype)) {
			hql.append(" and t.inouttype=" + inouttype);
		}
        if (!StringUtil.isEmpty(companyname)) {
            hql.append(" and t.companyname like '" + companyname + "%'");
        }
		hql.append(" order by t.status desc,t.companyname asc");
		Result<WeighCompany> rst = tcompanyDao.findByPage(page, hql.toString());
		for (WeighCompany c: rst.getContent()) {
			c.setTypename(GoodsType.getName(c.getType()));
		}
		return rst;
	}

	public void savCompany(WeighCompany entity) {
		if (entity.getStatus() == null) {
			entity.setStatus(1l);
		}
		String hql = "select count(*) from WeighCompany where companycode='" + entity.getCompanycode() + "' and type=?";
		if (!StringUtil.isEmpty(entity.getCompanyid())) {
			hql += " and companyid!='" + entity.getCompanyid() + "'";
		}
		List list = tcompanyDao.findByHql(hql.toString(), entity.getType());
		if (((Long) list.get(0)) != 0) {
			throw new BaseBussinessException("已存在该单位编码!");
		}

		hql = "select count(*) from WeighCompany where companyname='" + entity.getCompanyname() + "' and type=?";
		if (!StringUtil.isEmpty(entity.getCompanyid())) {
			hql += " and companyid!='" + entity.getCompanyid() + "'";
		}
		list = tcompanyDao.findByHql(hql.toString(), entity.getType());
		if (((Long) list.get(0)) != 0) {
			throw new BaseBussinessException("已存在该单位名称!");
		}
		String newcompany = entity.getCompanyname();
		String pz = entity.getType();
		String oldcompany = null;
		String operat = null;
        if (!StringUtil.isEmpty(entity.getCompanyid())) {
            WeighCompany old = tcompanyDao.findById(entity.getCompanyid());
            oldcompany = old.getCompanyname();
            if (entity.getStatus() == 0) {
            	operat = "delete";
            } else {
                if (old.getStatus() == 0 && entity.getStatus() == 1) {
                	operat = "insert";
                } else {
                    if (!old.getCompanyname().equals(entity.getCompanyname())) {
                    	operat = "update";
                    }
                }
            }
            if (StringUtil.isEmpty(old.getBeforename())) {
                if (old.getCompanyname()!= null && !old.getCompanyname().equals(entity.getCompanyname())) {
                    old.setBeforename(entity.getCompanyname());
                }
            } else {
                if (old.getCompanyname()!=null && !old.getCompanyname().equals(entity.getCompanyname())) {
                    String[] strs = old.getBeforename().split(",");
                    List<String> beforeNames = new ArrayList(strs.length + 1);
                    boolean hasName = false;
                    for (String str: strs) {
                        if (str.equals(entity.getCompanyname())) {
                            hasName = true;
                            break;
                        }
                    }
                    if (!hasName) {
                        beforeNames.add(entity.getCompanyname());
                    }
                    String str = "";
                    for (int i = 0; i < beforeNames.size(); i++) {
                        str += beforeNames.get(i) + ",";
                    }
                    if (!"".equals(str)) {
                        entity.setBeforename(str.substring(0, str.length() - 1));
                    }
                }
            }
            ObjectUtil.objcetMerge(old, entity);
            entity = old;
        } else {
            if (entity.getStatus() == 1) {
            	operat = "insert";
            }
        }

		tcompanyDao.save(entity);
		tcompanyDao.flushSession();
//		if ("insert".equals(operat)) {
//			downloadService.insertCompanyAndVehicle(newcompany, pz);
//		} else if ("delete".equals(operat)) {
//			downloadService.delCompanyAndVehicle(newcompany, pz);
//		} else if ("update".equals(operat)) {
//			downloadService.updateCompany(oldcompany, newcompany, pz);
//		}
	}

	public WeighCompany getCompanybyCompanyname(String companyname) {
		String hql = "from WeighCompany where companyname='" + companyname + "'";
		List<WeighCompany> list = tcompanyDao.findByHql(hql);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public Result<WeighCompany> getCompanyListByPz(SearchCondition params, Page page) {
		page.setCurPage(1);
		page.setRowPerPage(Integer.MAX_VALUE);
		String pz = params.get("pz") + "";
		String hql = "from WeighCompany t where 1=1 and t.type='" + pz + "'";
		Result<WeighCompany> res = tcompanyDao.findByPage(page, hql);
		return res;
	}

	public WeighCompany getCompanybyCompanycode(String companycode,
			String breedtype) {
		String hql = "from WeighCompany where companycode='" + companycode + "' " +
								" and type='" + breedtype + "'";
		List<WeighCompany> list = tcompanyDao.findByHql(hql);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
}
