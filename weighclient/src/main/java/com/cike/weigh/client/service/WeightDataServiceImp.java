package com.cike.weigh.client.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cike.weigh.client.dao.IWeightDataDao;
import com.cike.weigh.client.vo.Weight;

@Service
public class WeightDataServiceImp implements IWeightDataService {
    @Autowired
    private IWeightDataDao weightDao;

    public List<Weight> getWeightList(String startdate) {
        List<Weight> datas = weightDao.getWeightList(startdate);
        return datas;
    }

    /**
     * @param s water1,water2...
     * @描述: 根据流水号更新重量信息为已上传
     */
    @Override
    public void updateUploadWeighByWater(String s) {
        String[] waternos = s.split(",");
        if (waternos.length == 0) {
            return;
        }
        StringBuilder waternos_str = new StringBuilder("");
        for (String str : waternos) {
            waternos_str.append("'" + str + "',");
        }
        String condition  =  waternos_str.substring(0, waternos_str.length() - 1);
        Connection con = weightDao.getSessionObj().connection();
        try {
            Statement sm = con.createStatement();
            sm.execute("update 称重信息 set 上传否=-1 where 流水号 in ("+condition+")");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
