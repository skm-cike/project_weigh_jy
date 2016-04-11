package com.est.weigh.cfginfo.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.est.common.exception.BaseBussinessException;
import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.classutil.ObjectUtil;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.sysinit.sysuser.vo.SysUser;
import com.est.webservice.server.vo.WeighVehicleWs;
import com.est.weigh.cfginfo.dao.IWeighCompanyDao;
import com.est.weigh.cfginfo.dao.IWeighVehicleDao;
import com.est.weigh.cfginfo.vo.WeighCompany;
import com.est.weigh.cfginfo.vo.WeighVehicle;
import com.est.weigh.download.service.IDownloadService;

@Service
public class WeighVehicleServiceImp implements IWeighVehicleService {
	@Autowired
	private IWeighVehicleDao vehicleDao;
    @Autowired
    private IWeighCompanyDao companyDao;

    public Result<WeighVehicle> getWeighVehicleList(SearchCondition condition,Page page) {
		String companyid = (String)condition.get("companyid");
		String companycode = (String)condition.get("companycode");
		StringBuilder sb = new StringBuilder("from WeighVehicle t1 where 1=1");
		List params = new ArrayList();
		if (!StringUtil.isEmpty(companyid)) {
			sb.append(" and t1.companyid=?");
			params.add(companyid);
		} else if (!StringUtil.isEmpty(companycode)) {
			sb.append(" and t1.companycode=?");
			params.add(companycode);
		}
		sb.append(" order by t1.vehicleno");
		Result<WeighVehicle> rst = vehicleDao.findByPage(page, sb.toString(), params.toArray());
		return rst;
	}
	
	
	public void saveVehicle(WeighVehicle vehicle, SysUser user) {
		vehicle.setInputman(user.getUsername());
		vehicle.setInputtime(new Date());
		//vehicle.setLasttime(new Date());
		vehicle.setSysDept(user.getSysDept());
        WeighCompany company = companyDao.findById(vehicle.getCompanyid());
		if (vehicle.getVehicleid() != null && vehicle.getVehicleid() != 0) {
			WeighVehicle old = vehicleDao.findById(vehicle.getVehicleid());
			if (old != null && !old.getSysDept().getDeptid().equals(vehicle.getSysDept().getDeptid())) {
				throw new BaseBussinessException("非本部门人员无法修改!");
			}
			//直接更新车辆信息
            String oldno = old.getVehicleno();
            String newno = vehicle.getVehicleno();
            String remark = vehicle.getRemark();
            String companyname = company.getCompanyname();
            String pz = company.getType();
			ObjectUtil.objcetMerge(old, vehicle);
			vehicleDao.save(old);
			vehicleDao.flushSession();
//			downloadService.updateVehicle(oldno, newno, remark, companyname, pz);
		} else {
			String[] vehicles = vehicle.getVehicleno().split(" +");
			List<WeighVehicle> list = new ArrayList(vehicles.length);
			//查找相同品种，相同商家之前的车号
			String strnos = "";
			for (String str: vehicles) {
				strnos += "'" + str + "',";
			}
			Map<String, WeighVehicle> map = new HashMap();
			if (!"".equals(strnos)) {
				strnos = strnos.substring(0, strnos.length() - 1);
				List<WeighVehicle> vehicleList = vehicleDao.findByHql("from WeighVehicle where vehicleno in ("+strnos+") and companyid='"+vehicle.getCompanyid()+"'");
				for (WeighVehicle v: vehicleList) {
					map.put(v.getVehicleno(), v);
				}
			}
			Set<String> vehiclenos = new HashSet();
			for (String str: vehicles) {
				WeighVehicle v = map.get(str);
				boolean hasOtherVehicle = false;
				if (v == null) {
					v = new WeighVehicle();
				} else {
					hasOtherVehicle = true;
				}
				v.setCompanyid(vehicle.getCompanyid());
				v.setCompanyname(vehicle.getCompanyname());
				v.setCompanycode(vehicle.getCompanycode());
				v.setInputman(vehicle.getInputman());
				v.setInputtime(vehicle.getInputtime());
				if (StringUtil.isEmpty(v.getRemark())) {
					v.setRemark(vehicle.getRemark());
				} else {
					v.setRemark(v.getRemark() + "," + vehicle.getRemark());
				}
				v.setSysDept(vehicle.getSysDept());
				v.setVehicleno(str);
				v.setVehicleweight(vehicle.getVehicleweight());
				if (!hasOtherVehicle) {
					vehiclenos.add(v.getVehicleno() + "_" + company.getCompanyname() + "_" + company.getType());
				} else {
				}
				list.add(v);
			}
			if (list.size()!=0) {
				vehicleDao.saveAll(list);
			}
			vehicleDao.flushSession();
			for (int i = 0; i < list.size(); i++) {
				WeighVehicle v = list.get(i);
				String str = v.getVehicleno() + "_" + company.getCompanyname() + "_" + company.getType();
//				if (vehiclenos.contains(str)) {
//					downloadService.insertVehicle(v.getVehicleno(), v.getRemark(), company.getCompanyname(), company.getType());
//				} else {
//					downloadService.updateVehicle(v.getVehicleno(), v.getVehicleno(), v.getRemark(), v.getCompanyname(), company.getType());
//				}
			}
		}
	}

	public WeighVehicle getVehicle(Long vehicleid) {
		return vehicleDao.findById(vehicleid);
	}

	public void delWeighVehicle(WeighVehicle vehicle, SysUser currentUser) {
		WeighVehicle old = vehicleDao.findById(vehicle.getVehicleid());
		if (!old.getSysDept().getDeptid().equals(currentUser.getSysDept().getDeptid())) {
			throw new BaseBussinessException("非本部门人员无法删除!");
		}
        WeighCompany company = companyDao.findById(vehicle.getCompanyid());
        String oldvehicleno = old.getVehicleno();
		try {
			vehicleDao.updateByHql("delete from WeighVehicle where vehicleid=?", old.getVehicleid());
//			downloadService.delVehicle(oldvehicleno, company.getCompanyname(), company.getType());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseBussinessException(e);
		}
	}


	/**
	 * @描述: 获取买方车车辆数据
	 */
	public List<WeighVehicleWs> getBuyVehicle() {
		StringBuilder sb = new StringBuilder("from WeighVehicle t1 where exists (select 1 from WeighCompany t2 where t1.companycode=t2.companycode and t2.inouttype=1 and t2.status=1 and rownum=1) and t1.vehicleno is not null");
		List<WeighVehicle> rst = vehicleDao.findByHql(sb.toString());
		Set<String> companyids = new HashSet();
		for (WeighVehicle v: rst) {
			companyids.add(v.getCompanyid());
		}
		List<String> companyidList = new ArrayList(companyids);
		List<String> params = new ArrayList(1000);
		Map<String, String> map = new HashMap();
		int index = 0, max = 1000;
		Iterator<String> it = companyidList.iterator();
		while (it.hasNext()) {
			index++;
			params.add(it.next());
			it.remove();
			if (index == max) {
				StringBuilder str_param = new StringBuilder();
				for (String s: params) {
					str_param.append("?,");
				}
				List<WeighCompany> lst = vehicleDao.findByHql("from WeighCompany where companyid in (" + str_param.substring(0, str_param.length() - 1) + ")", params.toArray());
				for (WeighCompany c: lst) {
					map.put(c.getCompanyid(), c.getCompanycode() + "_" + c.getCompanyname());
				}
				params.clear();
				index = 0;
			}
		}
		if (params.size() != 0) {
			StringBuilder str_param = new StringBuilder();
			for (String s: params) {
				str_param.append("?,");
			}
			List<WeighCompany> lst = vehicleDao.findByHql("from WeighCompany where companyid in (" + str_param.substring(0, str_param.length() - 1) + ")", params.toArray());
			for (WeighCompany c: lst) {
				map.put(c.getCompanyid(), c.getCompanycode() + "_" + c.getCompanyname());
			}
		}
		for (WeighVehicle v: rst) {
			String codenames = map.get(v.getCompanyid());
			if (codenames != null) {
				String[] codename = codenames.split("_");
				v.setCompanycode(codename[0]);
				v.setCompanyname(codename[1]);
			}
		}
		List<WeighVehicleWs> rstWs = new ArrayList(rst.size());
		for (int i = 0; i < rst.size(); i++) {
			WeighVehicle v = rst.get(i);
			WeighVehicleWs w = new WeighVehicleWs();
			w.setCompanycode(v.getCompanycode());
			w.setCompanyname(v.getCompanyname());
			w.setInputman(v.getInputman());
			w.setInputtime(v.getInputtime());
			w.setRemark(v.getRemark());
			w.setVehicleid(v.getVehicleid());
			w.setVehicleno(v.getVehicleno());
			w.setVehicleweight(v.getVehicleweight());
			rstWs.add(w);
		}
		return rstWs;
	}

	/**
	 * @描述: 获取卖方车车辆数据
	 */
	public List<WeighVehicleWs> getSaleVehicle() {
		StringBuilder sb = new StringBuilder("from WeighVehicle t1 where exists (select 1 from WeighCompany t2 where t1.companycode=t2.companycode and t2.inouttype=2 and t2.status=1 and rownum=1) and t1.vehicleno is not null");
		List<WeighVehicle> rst = vehicleDao.findByHql(sb.toString());
		Set<String> companyids = new HashSet();
		for (WeighVehicle v: rst) {
			companyids.add(v.getCompanyid());
		}
		List<String> companyidList = new ArrayList(companyids);
		List<String> params = new ArrayList(1000);
		Map<String, String> map = new HashMap();
		int index = 0, max = 1000;
		Iterator<String> it = companyidList.iterator();
		while (it.hasNext()) {
			index++;
			params.add(it.next());
			it.remove();
			if (index == max) {
				StringBuilder str_param = new StringBuilder();
				for (String s: params) {
					str_param.append("?,");
				}
				List<WeighCompany> lst = vehicleDao.findByHql("from WeighCompany where companyid in (" + str_param.substring(0, str_param.length() - 1) + ")", params.toArray());
				for (WeighCompany c: lst) {
					map.put(c.getCompanyid(), c.getCompanycode()+"_"+c.getCompanyname());
				}
				params.clear();
				index = 0;
			}
		}
		if (params.size() != 0) {
			StringBuilder str_param = new StringBuilder();
			for (String s: params) {
				str_param.append("?,");
			}
			List<WeighCompany> lst = vehicleDao.findByHql("from WeighCompany where companyid in (" + str_param.substring(0, str_param.length() - 1) + ")", params.toArray());
			for (WeighCompany c: lst) {
				map.put(c.getCompanyid(), c.getCompanycode()+"_"+c.getCompanyname());
			}
		}
		for (WeighVehicle v: rst) {
			String codenames = map.get(v.getCompanyid());
			if (codenames != null) {
				String[] codename = codenames.split("_");
				v.setCompanycode(codename[0]);
				v.setCompanyname(codename[1]);
			}
		}
		List<WeighVehicleWs> rstWs = new ArrayList(rst.size());
		for (int i = 0; i < rst.size(); i++) {
			WeighVehicle v = rst.get(i);
			WeighVehicleWs w = new WeighVehicleWs();
			w.setCompanycode(v.getCompanycode());
			w.setCompanyname(v.getCompanyname());
			w.setInputman(v.getInputman());
			w.setInputtime(v.getInputtime());
			w.setRemark(v.getRemark());
			w.setVehicleid(v.getVehicleid());
			w.setVehicleno(v.getVehicleno());
			w.setVehicleweight(v.getVehicleweight());
			rstWs.add(w);
		}
		return rstWs;
	}
}
