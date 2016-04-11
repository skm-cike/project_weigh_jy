package com.est.webservice.server.service;

import com.est.common.ext.util.classutil.NumberUtil;
import com.est.sysinit.syslog.dao.ISysLogDao;
import com.est.webservice.server.vo.DownloadForClient;
import com.est.webservice.server.vo.Weight;
import com.est.weigh.cfginfo.service.IWeighDataService;
import com.est.weigh.cfginfo.vo.WeighData;
import com.est.weigh.download.service.IDownloadService;
import com.est.weigh.download.vo.Download;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
@WebService(endpointInterface = "com.est.webservice.server.service.IWeighTransService", serviceName = "IWeighTrans")  
public class WeighTransServiceImp implements IWeighTransService{
	@Autowired
	private ISysLogDao logDao;
	@Autowired
	private IWeighDataService weightDataService;
    @Autowired
    private IDownloadService downloadService;
	public WeighTransServiceImp() {
	}
	
	private boolean validateMac(String mac) {
		return true;
	}

//	public List<KindGrade> getKindGrade(String mac) {
//		if (!validateMac(mac)) {
//			return null;
//		}
//		List<SysProperty> fenmeihui = propertyService.getPropertiesByCode("PZDJ");
//		List<SysProperty> jizu = propertyService.getPropertiesByCode("FMHJZ");
//		List<KindGrade> list = new ArrayList();
//		for (int i = 0; i < jizu.size(); i++) {
//			for (int j = 0; j < fenmeihui.size(); j++) {
//				KindGrade g = new KindGrade();
//				String val = jizu.get(i).getPropertyname() + "-" + fenmeihui.get(j).getPropertyname();
//				g.setKindCode(val);
//				g.setKindName(val);
//				list.add(g);
//			}
//		}
//		return list;
//	}
//
//	public List<WeighVockind> getWeighVockind(String mac) {
//		if (!validateMac(mac)) {
//			return null;
//		}
//		SearchCondition params = new SearchCondition();
//		Page page = new Page();
//		page.setRowPerPage(Integer.MAX_VALUE);
////		List<WeighVockind> list = kindService.getKindsByPage(params, page).getContent();
//		List<WeighVockind> list = new ArrayList();
//		WeighVockind w1 = new WeighVockind();
//		w1.setKindCode("粉煤灰");
//		w1.setKindName("粉煤灰");
//		WeighVockind w2 = new WeighVockind();
//		w2.setKindCode("石膏");
//		w2.setKindName("石膏");
//		WeighVockind w3 = new WeighVockind();
//		w3.setKindCode("石灰石");
//		w3.setKindName("石灰石");
//		WeighVockind w4 = new WeighVockind();
//		w4.setKindCode("液氨");
//		w4.setKindName("液氨");
//		WeighVockind w5 = new WeighVockind();
//		w5.setKindCode("酸");
//		w5.setKindName("酸");
//		WeighVockind w6 = new WeighVockind();
//		w6.setKindCode("酸");
//		w6.setKindName("碱");
//		WeighVockind w7 = new WeighVockind();
//		w6.setKindCode("灰渣");
//		w6.setKindName("灰渣");
//		list.add(w1);
//		list.add(w2);
//		list.add(w3);
//		list.add(w4);
//		list.add(w5);
//		list.add(w6);
//		list.add(w7);
//		return list;
//	}
//
//	public List<WeighCompany> getWeighCompany(String mac) {
//		if (!validateMac(mac)) {
//			return null;
//		}
//		return tcompanyService.getDownloadDelCompanyList();
//	}
//
//	public List<SysUser> getSysUser(String mac) {
//		if (!validateMac(mac)) {
//			return null;
//		}
//		List<SysUser> users = logDao.findByHql("from SysUser");
//		for (SysUser u: users) {
//			logDao.getSessionObj().evict(u);
//		}
//
//		for (SysUser u: users) {
//			u.setSysDept(null);
//			u.setSysGroupusers(null);
//			u.setSysUsermodules(null);
//		}
//		return users;
//	}
//
//
//
//	public List<WeighVehicleWs> getBuyVehicles() {
//		return vehicleService.getBuyVehicle();
//	}
//
//	public List<WeighVehicleWs> getSaleVehicles() {
//		return vehicleService.getSaleVehicle();
//	}



    public String savUploadData(String mac, Weight[] weightDatas) {
        if (!validateMac(mac)) {
            return null;
        }
        List<WeighData> list = new ArrayList();
        for (Weight w: weightDatas) {
            Long inouttype = 1l;
            String companyname = null;
            String type = w.getType();
            if ("石膏".equals(w.getType()) || "shigao".equals(w.getType()) || "脱硫石膏".equals(w.getType())) {
                type = "shigao";
                inouttype = 2l;
            } else if ("粉煤灰".equals(w.getType()) || "fenmeihui".equals(w.getType())) {
                type = "fenmeihui";
                inouttype = 2l;
            } else if ("石灰石".equals(w.getType()) || "shihuishi".equals(w.getType())) {
                type = "shihuishi";
            } else if ("液氨".equals(w.getType()) || "yean".equals(w.getType())) {
                type = "yean";
            } else if ("酸".equals(w.getType()) || "suan".equals(w.getType())) {
                type = "suan";
            } else if ("碱".equals(w.getType()) || "jian".equals(w.getType())) {
                type = "jian";
            } else if ("钙粉".equals(w.getType()) || "gaifen".equals(w.getType())){
                type = "gaifen";
                //inouttype = 2l;
            } else if ("灰渣".equals(w.getType()) || "huizha".equals(w.getType())) {
                type = "huizha";
                inouttype = 2l;
            } else {

            }

            if (inouttype == 1l) {
                companyname = w.getSend_company();
            } else {
                companyname = w.getRecive_company();
            }

            WeighData wd = new WeighData();
            wd.setCompanycode(null);
            wd.setCompanyname(companyname);
            wd.setDayreportMark(0);
            wd.setDe_weight(w.getDe_weight());
            wd.setGrade(w.getGuige());
            wd.setGross_time(w.getGross_date());
            wd.setGross_weight(w.getGross_weight());
            wd.setInouttype(inouttype);
            wd.setIsclosed("N");
            wd.setJizu(w.getJizu());
            wd.setMonthreportMark(0);
            wd.setOperatTime(w.getUpdateDate());
            wd.setPounds_number(w.getWater_no());
            wd.setRemark(w.getRemark());
            wd.setTotal_price(0d);
            wd.setTransferman(w.getUpdateMan());
            wd.setType(type);
            wd.setUnit_price(0d);
            wd.setVehicle_no(w.getVehicle_no());
            wd.setVehicle_time(w.getTare_date());
            wd.setVehicle_weight(w.getVehicle_weight());
            wd.setWater(0d);
            wd.setWeighman(w.getGross_man());
            wd.setTareweightman(w.getTare_man());
            wd.setOutno(w.getOutno());
            wd.setBanbie(w.getBanbie());
            wd.setNet_weight(NumberUtil.sub(wd.getGross_weight(), NumberUtil.add(wd.getDe_weight(), wd.getVehicle_weight())));
            if ("huizha".equals(type)) {
                wd.setGrade("灰渣");
            }
            list.add(wd);
        }

        try {
            String strs = weightDataService.savWeightDats(list);
            return strs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @描述: 获取更新
     * @param startDate
     * @return
     */
    public List<DownloadForClient> getDownloads(String mac, Long startDate) {
        if (!validateMac(mac)) {
            return null;
        }
        List<Download> list = downloadService.getDownloads(new Date(startDate));
        if (list != null && list.size()>0) {
            List<DownloadForClient> rst = new ArrayList(list.size());
            for (int i = 0; i < list.size(); i++) {
                DownloadForClient c = new DownloadForClient();
                c.setOperat(list.get(i).getOperat());
                c.setOperatDate(list.get(i).getOperatDate().getTime());
                rst.add(c);
            }
            if (rst.size() != 0) {
                return rst;
            }
        }
        return null;
    }
}
