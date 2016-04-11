package com.cike.weigh.client.service;

import com.est.webservice.server.service.DownloadForClient;

public interface IBaseDataService {
	void saveGoods();
    boolean updateData(DownloadForClient download);
	void savCleanInfo();
}
