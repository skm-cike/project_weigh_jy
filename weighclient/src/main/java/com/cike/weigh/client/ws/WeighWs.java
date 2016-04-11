package com.cike.weigh.client.ws;

import com.cike.weigh.client.common.CfgUtil;
import com.est.webservice.server.service.DownloadForClient;
import com.est.webservice.server.service.IWeighTrans;
import com.est.webservice.server.service.IWeighTransService;
import com.est.webservice.server.service.Weight;

import javax.xml.namespace.QName;
import java.net.URL;
import java.util.List;

public class WeighWs {
	private IWeighTransService port = null;
	public WeighWs() {
		init();
	}
	
	void init() {
		QName SERVICE_NAME = IWeighTrans.SERVICE;
		try {
			String url = CfgUtil.getKey("wsurl");
			URL wsdlURL = new URL(url);
			IWeighTrans ss = new IWeighTrans(wsdlURL, SERVICE_NAME);
			port = ss.getWeighTransServiceImpPort();
		} catch (Exception e) {
			e.printStackTrace();
		}  
	}
	
	public String savUploadData(String mac, List<Weight> weightDatas) {
		if (port==null) {
			init();
		}
		return port.savUploadData(mac, weightDatas);
	}

    public List<DownloadForClient> getDownload(String mac, Long startTime) {
        if (port==null) {
            init();
        }
        return port.getDownloads(mac, startTime);
    }
}
