package com.cike.weigh.client.process;

import com.cike.weigh.client.common.CfgUtil;
import com.cike.weigh.client.common.DownCommon;
import com.cike.weigh.client.common.SystemUtil;
import com.cike.weigh.client.service.IBaseDataService;
import com.cike.weigh.client.service.IWeightDataService;
import com.cike.weigh.client.vo.Weight;
import com.cike.weigh.client.ws.WeighWs;
import com.est.common.ext.util.classutil.DateUtil;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.webservice.server.service.DownloadForClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
/**
 * @描述: 上传重量数据任务
 * @author Administrator
 *
 */
public class UploadTask extends TimerTask {
	private static Log log = LogFactory.getLog(UploadTask.class);
	@Autowired
	private IBaseDataService baseDataService;
	@Autowired
	private IWeightDataService weightDataService;
	@Autowired
	private WeighWs weighWs;
	private String mac = SystemUtil.getLocalMac();
	private String cfgKey = "weightDataDate";
	private String downloadTimeTarget = "downloadTime";

//    public void setWeighWs(WeighWs weighWs) {
//        this.weighWs = weighWs;
//    }

    @Override
	public void run() {
		try {
			//清除没有绑定货名的商家和车辆信息
	    	baseDataService.savCleanInfo();
			//=============================上传数据====================================
	    	
	    	//上传数据
			List<Weight> weightList = weightDataService.getWeightList(CfgUtil.getKey(cfgKey));
			List<com.est.webservice.server.service.Weight> rst = new ArrayList(weightList.size());
			Date maxDate = null;
			for (Weight w: weightList) {
				if (maxDate == null) {
					maxDate = w.getUpdateDate();
				}
				if (w.getUpdateDate().after(maxDate)) {
					maxDate = w.getUpdateDate();
				}
				com.est.webservice.server.service.Weight wd = new com.est.webservice.server.service.Weight();
				wd.setDeWeight(w.getDe_weight());
				wd.setGrossDate(DateUtil.convertToXmlGreCal(w.getGross_date()));
				wd.setGrossMan(w.getGross_man());
				wd.setGrossWeight(w.getGross_weight());
				wd.setGuige(w.getGuige());
				wd.setNetWeight(w.getNet_weight());
				wd.setPrintCount(w.getPrintCount());
				wd.setReciveCompany(w.getRecive_company());
				wd.setRemark(w.getRemark());
				wd.setSendCompany(w.getSend_company());
				wd.setSequence(w.getSequence());
				wd.setTareDate(DateUtil.convertToXmlGreCal(w.getTare_date()));
				wd.setTareMan(w.getTare_man());
				wd.setType(w.getType());
				wd.setVehicleNo(w.getVehicle_no());
				wd.setVehicleWeight(w.getVehicle_weight());
				wd.setWaterNo(w.getWater_no());
				wd.setWeightType(w.getWeight_type());
				wd.setUpdateMan(w.getUpdateMan());
				wd.setUpdateDate(DateUtil.convertToXmlGreCal(w.getUpdateDate()));

				wd.setOutno(w.getOutno());
				wd.setBanbie(w.getBanbie());
				wd.setJizu(w.getJizu());
				rst.add(wd);
			}
			String waterNo = "";                            //结构:  成功的流水号1,成功的流水号2_失败的流水号3,失败的流水号4
			if (rst.size() != 0) {
				log.debug("开始上传数据!");
                waterNo = weighWs.savUploadData(mac, rst);
                if (waterNo == null || "".equals(waterNo)) {
    				log.debug("上传数据失败!");
    			} else {
                    weightDataService.updateUploadWeighByWater(waterNo);
                    CfgUtil.write(cfgKey, DateUtil.format(maxDate, DateUtil.YMDHMS));
                    log.debug("上传过磅数据成功!");
                }
			}



    //		baseDataService.saveGoods();
            //下载更新数据
            String downloadTime = CfgUtil.getKey(downloadTimeTarget);
            if (StringUtil.isEmpty(downloadTime)) {
                Long val = new Date().getTime();
                try {
                    CfgUtil.write(downloadTimeTarget, val.toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                downloadTime = CfgUtil.getKey(downloadTimeTarget);
            }
            Long _downloadTime = Long.parseLong(downloadTime);
            List<DownloadForClient> downloads = weighWs.getDownload(mac, _downloadTime);
            if (downloads != null) {
                for (int i = 0; i < downloads.size(); i++) {
                    DownCommon.put(downloads.get(i));
                }
            }
            DownloadForClient dc = null;
            while ((dc = DownCommon.get()) != null) {
                if(baseDataService.updateData(dc)) {
                    try {
                        CfgUtil.write(downloadTimeTarget, dc.getOperatDate().toString());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
	}
}
