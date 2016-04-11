package com.cike.weigh.client.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.est.common.ext.util.classutil.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.cike.weigh.client.vo.Weight;
import com.est.common.base.BaseDaoImp;
import com.est.common.ext.util.classutil.DateUtil;
@Repository
public class WeightDataDaoImp extends BaseDaoImp<Object> implements IWeightDataDao {
	private static Log log = LogFactory.getLog(WeightDataDaoImp.class);
	public List<Weight> getWeightList(String startdate) {
        if (StringUtil.isEmpty(startdate)) {
            startdate="2012-01-01 00:00:00";
        }
        Date _date = null;
        _date = DateUtil.parse(startdate, DateUtil.YMDHMS);
        if (_date == null) {
            startdate="2012-01-01 00:00:00";
            _date = DateUtil.parse(startdate, DateUtil.YMDHMS);
        }

		Connection con = this.getSessionObj().connection();
		List<Weight> list = new ArrayList();
		try {
			PreparedStatement ps = con.prepareStatement("select 序号,流水号,车号,过磅类型,发货单位,收货单位,货名,规格,毛重,皮重,净重,扣重,毛重司磅员,皮重司磅员,毛重时间,皮重时间,备注,打印次数,更新人,更新时间,备用1,备用2,备用3  from 称重信息 where 打印次数<>0 and 上传否=0  and 更新时间>=?");
            ps.setDate(1, new java.sql.Date(_date.getTime()));
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Weight weight = new Weight();
				list.add(weight);
				int i = 1;
				weight.setSequence(rs.getLong(i++));
				weight.setWater_no(rs.getString(i++));
				weight.setVehicle_no(rs.getString(i++));
				weight.setWeight_type(rs.getString(i++));
				weight.setSend_company(rs.getString(i++));
				weight.setRecive_company(rs.getString(i++));
				weight.setType(rs.getString(i++));
				weight.setGuige(rs.getString(i++));
				weight.setGross_weight(rs.getDouble(i++));
				weight.setVehicle_weight(rs.getDouble(i++));
				weight.setNet_weight(rs.getDouble(i++));
				weight.setDe_weight(rs.getDouble(i++));
				weight.setGross_man(rs.getString(i++));
				weight.setTare_man(rs.getString(i++));
				weight.setGross_date(rs.getTimestamp(i++));
				weight.setTare_date(rs.getTimestamp(i++));
				weight.setRemark(rs.getString(i++));
				weight.setPrintCount(rs.getInt(i++));
				weight.setUpdateMan(rs.getString(i++));
				weight.setUpdateDate(rs.getTimestamp(i++));
				weight.setBanbie(rs.getString(i++));
				weight.setOutno(rs.getString(i++));          //出门证号
				weight.setJizu(rs.getString(i++));
			}
			
			rs.close();
			ps.close();
			log.debug("提取数据成功!");
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		}
		return list;
	}
}
