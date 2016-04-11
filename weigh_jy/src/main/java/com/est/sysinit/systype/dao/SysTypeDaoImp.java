package com.est.sysinit.systype.dao;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.est.common.base.BaseDaoImp;
import com.est.sysinit.systype.vo.SysType;

/**
 * @desc 合同类型的实现类
 * @author hxing
 * @date 2013-4-1上午09:13:21
 * @path com.est.sysinit.systype.dao
 * @corporation Enstrong S&T
 */
@Repository
public class SysTypeDaoImp extends BaseDaoImp<SysType> implements ISysTypeDao {
	private final Log log = LogFactory.getLog(getClass());
	public void updateBySql(String sql, Object... params) throws Exception {
		try { 
			Query query = this.getSession().createSQLQuery(sql);
			for(int i=0; i<params.length; i++ ){
				query.setParameter(i, params[i]);
			}
			query.executeUpdate();
		} catch(Exception ex) {
			log.info("update failed！");
			throw ex;
		}
	}

}
