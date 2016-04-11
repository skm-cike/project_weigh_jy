package com.est.weigh.download.dao;

import com.est.common.base.IBaseDao;
import com.est.weigh.download.vo.Download;

import java.util.Date;
import java.util.List;

/**
 * Created by 陆华 on 15-10-26 下午4:58
 */
public interface IDownloadDao extends IBaseDao<Download>{
    List<Download> getDownloads(Date startDate);
    void delDownload(String id);
    void savDownload(String accesssql);
}
