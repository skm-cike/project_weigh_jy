package com.cike.weigh.client.dao;


import com.cike.weigh.client.common.GoodsType;
import com.est.common.base.BaseDaoImp;
import com.est.common.ext.util.classutil.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Repository
public class BaseDataDaoImp extends BaseDaoImp<Object> implements IBaseDataDao{
	private static Log log = LogFactory.getLog(BaseDataDaoImp.class);


	public void saveGoods() {
		Map<String, String> map = new HashMap();
		map.put(GoodsType.fenmeihui, GoodsType.getName(GoodsType.fenmeihui));
		map.put(GoodsType.gaifen, GoodsType.getName(GoodsType.gaifen));
		map.put(GoodsType.huizha, GoodsType.getName(GoodsType.huizha));
		map.put(GoodsType.jian, GoodsType.getName(GoodsType.jian));
		map.put(GoodsType.shigao, GoodsType.getName(GoodsType.shigao));
		map.put(GoodsType.shihuishi, GoodsType.getName(GoodsType.shihuishi));
		map.put(GoodsType.suan, GoodsType.getName(GoodsType.suan));
		map.put(GoodsType.yean, GoodsType.getName(GoodsType.yean));
		Set<String> set = map.keySet();
		String codes = "";
		for (String str: set) {
			codes += "'" + str + "',";
		}
		codes = codes.substring(0, codes.length() - 1);
		
		Connection con = this.getSessionObj().connection();
		try {
			Statement sm = con.createStatement();
			Statement smquery = con.createStatement();
			ResultSet rs = smquery.executeQuery("select [代码],[货名] from [货名]");
			boolean hasCode = true;
			while (rs.next()) {
				if (StringUtil.isEmpty(rs.getString(1))) {
					hasCode = false;
				}
			}
			if (!hasCode) {
				sm.execute("delete from [货名]");
				con.commit();
				for (String key: set) {
					sm.addBatch("insert into [货名] ([代码],[货名]) values ('"+key+"','"+map.get(key)+"')");
				}
				sm.executeBatch();
			}
			sm.close();
			smquery.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
