package com.est.weigh.download.dao;

import com.est.common.base.BaseDaoImp;
import com.est.weigh.download.vo.Download;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by 陆华 on 15-10-26 下午5:00
 */
@Repository
public class DownloadDaoImp extends BaseDaoImp<Download> implements IDownloadDao {
    public List<Download> getDownloads(Date startDate) {
        return this.findByHql("from Download where operatDate>? order by operatDate asc", startDate);
    }

    public void delDownload(String id) {
        this.delById(id);
    }

    public void savDownload(String accesssql) {
        Download download = new Download();
        download.setOperat(accesssql);
        download.setOperatDate(new Date());
        this.save(download);
    }
}
