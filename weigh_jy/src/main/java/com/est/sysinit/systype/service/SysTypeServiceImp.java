package com.est.sysinit.systype.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.sysinit.systype.dao.ISysTypeDao;
import com.est.sysinit.systype.vo.SysType;
/**
 * 
 * 
 * @desc 合同类型的Service实现类
 * @author hxing
 * @date 2013-4-2上午03:29:41
 * @path com.est.sysinit.systype.service
 * @corporation Enstrong S&T
 */
@Service
public class SysTypeServiceImp implements ISysTypeService{
	private final Log log = LogFactory.getLog(SysTypeServiceImp.class);
	
	private ISysTypeDao sysTypeDao;
	
	public ISysTypeDao getSysTypeDao() {
		return sysTypeDao;
	}

	public void setSysTypeDao(ISysTypeDao sysTypeDao) {
		this.sysTypeDao = sysTypeDao;
	}

	/**
	 * 删除合同类型
	 * @param typeCode
	 */
	public boolean delSysType(Long typeid) {
		String SQL ="DELETE FROM SYS_TYPE WHERE TYPEID = ? ";
		try{
			sysTypeDao.updateBySql(SQL, typeid);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	/**
	 * 查询出自身及子类外所有的合同类型
	 */
	@SuppressWarnings("unchecked")
	public List<SysType> getAllSysType(SearchCondition condition) {
		String strTypeId = (String)condition.get("typeid");
		Long typeid = StringUtil.parseLong(strTypeId);
		List<Object> params = new ArrayList<Object>();
		params.add(typeid);
//		String sql = " select * from sys_type where typeid not in (select typeid from sys_type start with typeid = ? connect by prior typeid = parenttypeid ) ";
//		List<SysType> sysTypeLists = (List<SysType>)sysTypeDao.sqlQuery(sql, params.toArray());
		String HQL = "from SysType";
		List<SysType> sysTypeLists = sysTypeDao.findByHql(HQL, new Object[0]);
		if(sysTypeLists!=null){
			return sysTypeLists;
		}else{
			return null;
		}
	}

	/**
	 * 获取合同类型树
	 */
	@SuppressWarnings("unchecked")
	public List<SysType> getTreeSysType(Serializable typeid) {
		Long id = (Long)typeid;
		List<SysType> sysTypeList = new ArrayList<SysType>();
		List<Object> list = null;
		if(0L == id || null == id){
			String SQL = "SELECT typeid,typename,typecode FROM SYS_TYPE WHERE PARENTTYPEID IS NULL";
			list = sysTypeDao.sqlQuery(SQL, new Object[0]);
			
		}else{
			String SQL ="SELECT typeid,typename,typecode FROM SYS_TYPE WHERE PARENTTYPEID = ? ";
			list = sysTypeDao.sqlQuery(SQL,id);
		}
		
		for(int i=0;i<list.size();i++){
			Object[] value = (Object[])list.get(i);
			SysType sysType = new SysType();
			sysType.setTypeid(((BigDecimal)value[0]).longValue());
			sysType.setTypename((String)value[1]);
			sysType.setTypecode((String)value[2]);
			sysTypeList.add(sysType);
		}
		return sysTypeList;
		
//		StringBuffer buf = new StringBuffer(200);
//		List<Object> paramList = new ArrayList<Object>();
//		buf.append("from SysType t where ");
//		if(id!=null && id!=0L) {
//			buf.append(" t.parenttypeid = ? ");
//			paramList.add(id);
//		} else {
//			buf.append(" t.parenttypeid is null ");
//		}
//		buf.append(" order by t.typeid asc");
//		String HQL = buf.toString();
//		return (List<SysType>)sysTypeDao.findByHql(HQL, paramList.toArray());
	}
	
	/**
	 * 添加合同类型
	 * @param sysType
	 */
	public void savSysType(SysType sysType) {
		
		sysTypeDao.save(sysType);
	}

   /**
    * 更新合同类型
    * @param sysType
    */
	public void updateSysType(List<Object> paramslist) {
		try {
			String SQL ="update sys_type set typecode = ?, typename = ?, parenttypeid = ? , typerule = ?, typeremark = ? where typeid = ? ";
			sysTypeDao.updateBySql(SQL, paramslist.toArray());
			System.out.println("----修改完成----");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 分页查询合同类型
	 */
	@SuppressWarnings("unchecked")
	public Result<SysType> getTypeList(Page page, SearchCondition condition) {
		String typeidStr = (String) condition.get("typeid");
		Long typeid = StringUtil.parseLong(typeidStr);
//		System.out.println("=======typeid======>"+typeid);
		Result<SysType> resultSysType = new Result<SysType>();
		Result<Object> result = null;
		
		if(null != typeid && 0L != typeid){
			String SQL ="select typeid,typename,typecode,parenttypeid,typerule,typeremark from sys_type where parenttypeid = ? ";
			result = sysTypeDao.sqlQueryByPage(SQL, page, typeid);
			
		}else{
			String SQL = "select typeid,typename,typecode,parenttypeid,typerule,typeremark from sys_type";
			result = sysTypeDao.sqlQueryByPage(SQL, page, new Object[0]);
		}
		
		//获取查询的集合
		List<Object> list = result.getContent();
		//获取查询的Page对象
		Page newpage = result.getPage();
		//设置Page对象
		resultSysType.setPage(newpage);
		//创建装SysType的集合
		List<SysType> content = new ArrayList<SysType>();
		for(int i=0;i<list.size();i++){
			Object[] value = (Object[])list.get(i);
			SysType sysType = new SysType();
			sysType.setTypeid(((BigDecimal)value[0]).longValue());
			sysType.setTypename((String)value[1]);
			sysType.setTypecode((String)value[2]);
//			SysType type = new SysType();
//			type.setTypeid((((BigDecimal)value[3]).longValue()));
//			sysType.setSysType(type);
			sysType.setTyperule((String)value[4]);
			sysType.setTyperemark((String)value[5]);
			content.add(sysType);
		}
		resultSysType.setContent(content);
		return resultSysType;
//
//		
//		buf.append("from SysType t where 1=1");
//		if (typeid_int != null && typeid_int !=0 ) {
//			if (null == parenttypeid_int || 0 == parenttypeid_int) {
////				System.out.println("父类型为空!------>"+parenttypeid);
//				buf.append(" and t.parenttypeid = ?");
//				paramList.add(typeid_int);
//			} else {
////				System.out.println("父类型不为空！为----->"+parenttypeid);
//				buf.append(" and t.parenttypeid = ?");
//				paramList.add(typeid);
//			}
//		}
//
//		if (typename != null && !"".equals(typename)) {
////			System.out.println("----类型名不为空！---->:"+typename);
//			buf.append(" and t.typename like ? ");
//			paramList.add("%"+typename+"%");
//		}
//
//		buf.append(" order by t.typeid,t.typename");
		
//		return sysTypeDao.findByPage(page, buf.toString(), paramList.toArray());
	}

	/**
	 * 根据typeid 获取单个SysType
	 */
	public SysType getTypeById(Serializable typeid) {
		return sysTypeDao.findById(typeid);
	}
}
