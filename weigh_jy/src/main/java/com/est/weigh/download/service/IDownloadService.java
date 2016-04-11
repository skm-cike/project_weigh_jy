package com.est.weigh.download.service;

import com.est.weigh.download.vo.Download;

import java.util.Date;
import java.util.List;

/**
 * Created by 陆华 on 15-10-27 上午9:36
 */
public interface IDownloadService {
    /**
     * @描述: 下载更新操作
     * @param startDate
     * @return
     */
    List<Download> getDownloads(Date startDate);

    /**
     * @描述: 保存商家 及 商家和品种对应关系
     * @param companyname
     * @param pz
     */
    void insertCompanyAndVehicle(String companyname, String pz);

    /**
     * @描述: 保存车辆 及 车辆和商家对应关系
     * @param vehicleNo
     * @param companyname
     */
    void insertVehicle(String vehicleNo, String remark, String companyname, String pz);

    /**
     * @描述: 更新商家及商家和品种对应关系
     * @param oldCompany
     * @param newCompany
     * @param pz
     */
    void updateCompany(String oldCompany, String newCompany, String pz);

    /**
     * @描述: 更新车辆 及车辆和商家对应关系
     * @param oldVehicle
     * @param newVehicle
     * @param companyname
     */
    void updateVehicle(String oldVehicle, String newVehicle, String remark, String companyname, String pz);

    /**
     * @描述: 删除商家 及 商家和品种对应关系
     * @param companyname
     * @param pz
     */
    void delCompanyAndVehicle(String companyname, String pz);

    /**
     * @描述: 删除车辆 及 车辆和商家对应关系
     * @param vehicle
     * @param companyname
     */
    void delVehicle(String vehicle, String companyname, String pz);

	void updateClientInfo();
}
