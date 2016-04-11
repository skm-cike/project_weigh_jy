package com.cike.weigh.client.service;

import com.cike.weigh.client.dao.IBaseDataDao;
import com.est.webservice.server.service.DownloadForClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Service
public class BaseDataServiceImp implements IBaseDataService {
    @Autowired
    private IBaseDataDao baseDataDao;

	public void saveGoods() {
		baseDataDao.saveGoods();
	}

    public boolean updateData(DownloadForClient download) {
        Connection con = baseDataDao.getSessionObj().connection();
        Statement sm = null;
        try {
            sm = con.createStatement();
            String sqlstr = download.getOperat();
            String[] sqls = sqlstr.split(";");
            for (int i = 0; i < sqls.length; i++) {
                if (sqls[i].contains("称重信息")) {
                    continue;
                }
                sm.addBatch(sqls[i]);
            }
            sm.executeBatch();
            System.out.println("更新成功!");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (sm != null) {
                try {
                    sm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

	public void savCleanInfo() {
		String sql1 = "delete from [车号] where [发货单位] is null and [收货单位] is null";
		String sql2 = "delete from [收货单位] t1 where not exists (select 1 from tbl_goods_shouHuo t2 where t1.[收货单位]=t2.shouHuo)";
		Connection con = baseDataDao.getSessionObj().connection();
        Statement sm = null;
        try {
            sm = con.createStatement();
            sm.addBatch(sql1);
            sm.addBatch(sql2);
            sm.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (sm != null) {
                try {
                    sm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
	}
}
