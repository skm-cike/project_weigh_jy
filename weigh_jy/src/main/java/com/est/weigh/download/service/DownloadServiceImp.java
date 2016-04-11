package com.est.weigh.download.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.est.common.ext.util.classutil.DateUtil;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.weigh.cfginfo.dao.IWeighCompanyDao;
import com.est.weigh.cfginfo.dao.IWeighVehicleDao;
import com.est.weigh.cfginfo.vo.WeighCompany;
import com.est.weigh.cfginfo.vo.WeighVehicle;
import com.est.weigh.download.dao.IDownloadDao;
import com.est.weigh.download.vo.Download;

/**
 * Created by 陆华 on 15-10-27 上午9:37
 */
@Service
public class DownloadServiceImp implements  IDownloadService{
    @Autowired
    private IDownloadDao downloadDao;
    @Autowired
    private IWeighCompanyDao companyDao;
    @Autowired
    private IWeighVehicleDao vehicleDao;
    /**
     * @描述: 下载更新操作
     * @param startDate
     * @return
     */
    public List<Download> getDownloads(Date startDate) {
        return downloadDao.getDownloads(startDate);
    }

    /**
     * @描述: 商家商家 及 商家和品种对应关系,同时插入车辆信息
     * @param companyname
     * @param pz
     */
    public void insertCompanyAndVehicle(String companyname, String pz) {
        if (StringUtil.isEmpty(pz) || StringUtil.isEmpty(companyname)) {
            return;
        }
        //保存商家
        String mainTable = null;
        String middleTable = null;
        String daima = "[代码]";
        String goodRs = null;
        String goods = "goods";
        if ("shigao".equals(pz) || "fenmeihui".equals(pz) || "huizha".equals(pz)) {
            mainTable = "[收货单位]";
            middleTable = "tbl_goods_shouHuo";
            goodRs = "shouHuo";
        } else {
            mainTable = "[发货单位]";
            middleTable = "tbl_goods_faHuo";
            goodRs = "faHuo";
        }

        String sql = "insert into " + mainTable + " ("+daima+","+mainTable+") select a1,a2 from (select '"+companyname+"' as a1,'"+companyname+"' as a2,count(*) as a3 from "+mainTable+" where "+mainTable+"='"+companyname+"') where a3=0";
        String sql1 = "insert into " + middleTable + " ("+goods+","+goodRs+") select a1,a2 from (select '"+convertPz(pz)+"' as a1,'"+companyname+"' as a2,count(*) as a3 from "+middleTable+" where "+goods+"='"+convertPz(pz)+"' and "+goodRs+"='"+companyname+"') where a3=0" ;
        downloadDao.savDownload(sql + ";" + sql1);
        //插入车辆信息
        List<WeighVehicle> vehicles = vehicleDao.findByHql("from WeighVehicle where companyid in (select companyid from WeighCompany where companyname=? and type=?)", companyname, pz);
        for (WeighVehicle v: vehicles) {
            insertVehicle(v.getVehicleno(), v.getRemark(), companyname, pz);
        }
    }

    /**
     * @描述: 保存车辆 及 车辆和商家对应关系
     * @param vehicleNo
     * @param companyname
     */
    public void insertVehicle(String vehicleNo, String remark, String companyname, String pz) {
        if (StringUtil.isEmpty(pz) || StringUtil.isEmpty(companyname) || StringUtil.isEmpty(vehicleNo)) {
            return;
        }
        String companyRs = null;
        if ("shigao".equals(pz) || "fenmeihui".equals(pz) || "huizha".equals(pz)) {
            companyRs = "[收货单位]";
        } else {
            companyRs = "[发货单位]";
        }
        String sql = "insert into [车号] ([车号],[备注],"+companyRs+") select a1,a2,a3 from (select '"+vehicleNo+"' as a1,'"+remark+"' as a2,'"+companyname+"' as a3,count(*) as a4 from [车号] where [车号]='"+vehicleNo+"' and "+companyRs+"='"+companyname+"') where a4=0";
        downloadDao.savDownload(sql);
        //更新备注
//        this.updateVehicle(vehicleNo, vehicleNo, remark, companyname, pz);
    }

    /**
     * @描述: 更新商家及商家和品种对应关系,商家和车辆对应关系
     * @param oldCompany
     * @param newCompany
     * @param pz
     */
    public void updateCompany(String oldCompany, String newCompany, String pz) {
        if (StringUtil.isEmpty(pz) || StringUtil.isEmpty(oldCompany) || StringUtil.isEmpty(newCompany)) {
            return;
        }
        //检查是否有未更名的同名商家,若有，则删除新旧商家信息，同时更新商家，及其下面车辆信息
        String checkHql = "from WeighCompany where companyname=? and type!=? and status=1";
        List<WeighCompany> list = companyDao.findByHql(checkHql, oldCompany, pz);
        if (list.size() != 0) {
            //删除旧商家和车辆信息
            for (WeighCompany c: list) {
                delCompanyAndVehicle(c.getCompanyname(), c.getType());
            }
            delCompanyAndVehicle(oldCompany, pz);
            //重新插入商家和车辆信息
            for (WeighCompany c: list) {
                insertCompanyAndVehicle(c.getCompanyname(), c.getType());
            }
            insertCompanyAndVehicle(newCompany, pz);
            return;
        }

        //保存商家
        delCompanyAndVehicle(oldCompany, pz);
        insertCompanyAndVehicle(newCompany, pz);
//        String mainTable = null;
//        String middleTable = null;
//        String daima = "[代码]";
//        String goodRs = null;
//        String goods = "goods";
//        String companyRs = null;
//        if ("shigao".equals(pz) || "fenmeihui".equals(pz) || "huizha".equals(pz)) {
//            mainTable = "[收货单位]";
//            middleTable = "tbl_goods_shouHuo";
//            goodRs = "shouHuo";
//            companyRs = "[收货单位]";
//        } else {
//            mainTable = "[发货单位]";
//            middleTable = "tbl_goods_faHuo";
//            goodRs = "faHuo";
//            companyRs = "[发货单位]";
//        }
//        String sql = "update " + mainTable + " set " + mainTable + "='"+newCompany+"' where " +mainTable+ "='"+oldCompany+"'";  //更新单位
//        String sql1 = "update " + middleTable + " set " + goodRs + "='"+newCompany+"' where "+goods+"='"+convertPz(pz)+"' and " + goodRs + "='"+oldCompany+"'"; //更新单位，品种中间表
//        String sql2 ="update [车号] set "+companyRs+"='"+newCompany+"' where " + companyRs + "='"+oldCompany+"'";//更新单位,车辆中间表
//        downloadDao.savDownload(sql+";"+sql1+";"+sql2);
    }

    /**
     * @描述: 更新车辆 及车辆和商家对应关系
     * @param oldVehicle
     * @param newVehicle
     * @param companyname
     */
    public void updateVehicle(String oldVehicle, String newVehicle, String remark, String companyname, String pz) {
        if (StringUtil.isEmpty(pz) || StringUtil.isEmpty(oldVehicle) || StringUtil.isEmpty(newVehicle) || StringUtil.isEmpty(companyname)) {
            return;
        }

        //若有其他同名商家信息,拥有该车辆，则更新此车辆信息
        Long inouttype = null;
        if ("shigao".equals(pz) || "fenmeihui".equals(pz) || "huizha".equals(pz)) {
            inouttype = 1l;
        } else {
            inouttype = 2l;
        }
        String checkSql = "select t1.remark from WEIGH_VEHICLE t1 where t1.VEHICLENO=?";
        List list = companyDao.sqlQuery(checkSql, newVehicle);
        Set<String> remarkSet = new HashSet();
        if (list.size() != 0) {
            //拼接其他品种的车辆备注信息
        	for (Object obj: list) {
        		remarkSet.add((String)obj);
        	}
        }
        
        if (!StringUtil.isEmpty(remark)) {
        	remarkSet.add(remark);
        }
        remark = "";
        for (String str: remarkSet) {
        	remark += str + ",";
        }
        if (!"".equals(remark)) {
        	remark = remark.substring(0, remark.length() - 1);
        }

        String companyRs = null;
        if ("shigao".equals(pz) || "fenmeihui".equals(pz) || "huizha".equals(pz)) {
            companyRs = "[收货单位]";
        } else {
            companyRs = "[发货单位]";
        }
        if (!oldVehicle.equals(newVehicle)) {
	        String sql = "update [车号] set [车号]='"+newVehicle+"',[备注]='"+remark+"' where [车号]='"+oldVehicle+"' and " + companyRs + "='"+companyname+"'";
	        downloadDao.savDownload(sql);
	        updateVehicle(newVehicle, newVehicle, null, companyname, pz);
	        updateVehicle(oldVehicle, oldVehicle, null, companyname, pz);
        } else {
        	String sql = "update [车号] set [车号]='"+newVehicle+"',[备注]='"+remark+"' where [车号]='"+oldVehicle+"'";
	        downloadDao.savDownload(sql);
        }
    }

    /**
     * @描述: 删除商家 及 商家和品种对应关系,商家和车辆对应关系
     * @param companyname
     * @param pz
     */
    public void delCompanyAndVehicle(String companyname, String pz) {
        if (StringUtil.isEmpty(pz) || StringUtil.isEmpty(companyname)) {
            return;
        }
        delCompanyAndVehicleByPz(companyname, pz);
        //检查是否有未更名的同名商家,若有，则删除新旧商家信息，同时更新商家，及其下面车辆信息
        String checkHql = "from WeighCompany where companyname=? and type!=? and status=1";
        List<WeighCompany> list = companyDao.findByHql(checkHql, companyname, pz);
        if (list.size() != 0) {
            //删除旧商家信息
            for (WeighCompany c: list) {
            	delCompanyAndVehicleByPz(c.getCompanyname(), c.getType());
            }
            //重新插入商家信息
            for (WeighCompany c: list) {
            	insertCompanyAndVehicle(c.getCompanyname(), c.getType());
            }
            return;
        }
    }
    
    private void delCompanyAndVehicleByPz(String companyname, String pz) {
    	if (StringUtil.isEmpty(pz) || StringUtil.isEmpty(companyname)) {
            return;
        }
    	 String mainTable = null;
         String middleTable = null;
         String daima = "[代码]";
         String goodRs = null;
         String goods = "goods";
         String companyRs = null;
         if ("shigao".equals(pz) || "fenmeihui".equals(pz) || "huizha".equals(pz)) {
             mainTable = "[收货单位]";
             middleTable = "tbl_goods_shouHuo";
             goodRs = "shouHuo";
             companyRs = "[收货单位]";
         } else {
             mainTable = "[发货单位]";
             middleTable = "tbl_goods_faHuo";
             goodRs = "faHuo";
             companyRs = "[发货单位]";
         }
         String sql = "delete from [车号] where "+companyRs+"='"+companyname+"'";
         String sql1 = "delete from " + middleTable + " where " + goods + "='"+convertPz(pz)+"' and " + goodRs + "='"+companyname+"'";
         String sql2 = "delete from " + mainTable + " where " + mainTable + "='"+companyname+"'";
         downloadDao.savDownload(sql+";"+sql1+";"+sql2);
    }

    /**
     * @描述: 删除车辆 及 车辆和商家对应关系
     * @param vehicle
     * @param companyname
     */
    public void delVehicle(String vehicle, String companyname, String pz) {
        if (StringUtil.isEmpty(pz) || StringUtil.isEmpty(companyname) || StringUtil.isEmpty(vehicle)) {
            return;
        }
        String companyRs = null;
        long inouttype = 1;
        if ("shigao".equals(pz) || "fenmeihui".equals(pz) || "huizha".equals(pz)) {
            companyRs = "[收货单位]";
            inouttype = 1l;
        } else {
            companyRs = "[发货单位]";
            inouttype = 2l;
        }
        //查找是否有其它货物的同名商家，拥有该车号。 若有，则不删除,若没有，则更新备注
        String checksql = "select count(1) from WEIGH_VEHICLE t1 left join WEIGH_COMPANY t2 on t1.COMPANYID=t2.COMPANYID where t2.COMPANYNAME=? and t2.TYPE!=? and t1.VEHICLENO=? and t2.INOUTTYPE=?";
        List list = companyDao.sqlQuery(checksql, companyname, pz, vehicle, inouttype);
        if (((BigDecimal)list.get(0)).intValue() == 0) {
        	String sql = "delete from [车号] where "+companyRs+"='"+companyname+"' and [车号]='"+vehicle+"'";
            downloadDao.savDownload(sql);
        } else {
        	//更新备注
            this.updateVehicle(vehicle, vehicle, null, companyname, pz);
        }
    }

    /**
     * @描述: 转换客户端为可以识别的品种信息
     * @param pz
     * @return
     */
    private String convertPz(String pz) {
        if ("fenmeihui".equals(pz)) {
            return "粉煤灰";
        } else if ("shigao".equals(pz)) {
            return "石膏";
        } else if ("huizha".equals(pz)) {
            return "灰渣";
        } else if ("shihuishi".equals(pz)) {
            return "石灰石";
        } else if ("yean".equals(pz)) {
            return "液氨";
        } else if ("suan".equals(pz)) {
            return "酸";
        } else if ("jian".equals(pz)) {
            return "碱";
        } else if ("gaifen".equals(pz)) {
            return "钙粉";
        }
        return pz;
    }

	public void updateClientInfo() {
        try {
            downloadDao.updateByHql("delete from Download");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //删除所有商家和车辆信息
		String sql1 = "delete from tbl_goods_shouHuo";
		String sql2 = "delete from [收货单位]";
		String sql3 = "delete from [车号] where [收货单位] is not null";
		downloadDao.savDownload(sql1);
		downloadDao.savDownload(sql2);
		downloadDao.savDownload(sql3);
		List<WeighCompany>list = this.companyDao.findByHql("from WeighCompany where status=1");
		//添加所有商家
		for (WeighCompany c: list) {
			insertCompanyAndVehicle(c.getCompanyname(), c.getType());
		}
	}
}
