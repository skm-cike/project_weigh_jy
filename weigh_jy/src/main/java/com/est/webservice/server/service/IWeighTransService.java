package com.est.webservice.server.service;

import java.util.Date;
import java.util.List;

import javax.jws.WebService;

import com.est.webservice.server.vo.DownloadForClient;
import com.est.webservice.server.vo.Weight;
@WebService
public interface IWeighTransService {
//	List<KindGrade> getKindGrade(String mac);
//	List<WeighVockind> getWeighVockind(String mac);
//	List<WeighCompany> getWeighCompany(String mac);
//	List<SysUser> getSysUser(String mac);

    /**
     * @描述: 保存上传的重量数据
     * @param mac
     * @param weightDatas
     * @return  成功的流水号1，成功的流水号2_失败的流水号1,失败的流水号2
     */
	String savUploadData(String mac, Weight[] weightDatas);

    List<DownloadForClient> getDownloads(String mac, Long startDate);
//	List<WeighVehicleWs> getBuyVehicles();
//	List<WeighVehicleWs> getSaleVehicles();
}
