package com.est.common.base;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.persister.entity.SingleTableEntityPersister;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.classutil.ClassUtils;

@SuppressWarnings("unchecked")
public class BaseDaoImp<E> extends BaseDaoSupport implements IBaseDao<E> {
	private final Log log = LogFactory.getLog(getClass());
	
	public final static int SAVE = 1;
	public final static int UPDATE = 2;
	
	protected Class<E> clazz;

	
	//protected abstract Class getClazz();
	public BaseDaoImp(){
		clazz =  ClassUtils.getSuperClassGenricType(getClass());
	}
	
	/**
	 *@desc clear hibernate Session <please use this method carefully>
	 *@date Nov 10, 2009
	 *@author jingpj
	 */
	public void flushSession(){
		getHibernateTemplate().getSessionFactory().getCurrentSession().flush();
	}
	/**
	 * save or update an object
	 */
	public void save(Object instance){
		log.debug("saving instance");
		try{
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save error",re);
			throw re;
		}
	}
	
	public void update(Object instance){
		log.debug("saving instance");
		try{
			getHibernateTemplate().setFlushMode(getHibernateTemplate().FLUSH_AUTO); 
			getHibernateTemplate().getSessionFactory().getCurrentSession().clear();
			getHibernateTemplate().update(instance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save error",re);
			throw re;
		}
	}

	public void save(Object instance,int type){
		log.debug("saving instance");
		try{
			if(type == SAVE) {
				getHibernateTemplate().save(instance);
			} else {
				getHibernateTemplate().update(instance);
			}
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save error",re);
			throw re;
		}
	}
	/**
	 * save or update objects which in parameter list
	 */
	public void saveAll(List list){
		log.debug("saving list");
		try{
			getHibernateTemplate().saveOrUpdateAll(list);
			log.debug("save list successful");
		} catch (RuntimeException re) {
			log.error("save list error",re);
			throw re;
		}
	}
	
	/**
	 * clear session object
	 * save or update an object
	 */
	public void saveAndClear(E instance){
		log.debug("saving instance");
		try{
			getHibernateTemplate().getSessionFactory().getCurrentSession().clear();
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("saveAndClear successful");
		} catch (RuntimeException re) {
			log.error("saveAndClear error",re);
			throw re;
		}
	}
	
	/**
	 * delete an object
	 */
	public void del(Object instance){
		log.debug("deleting instance");
		try{
			getHibernateTemplate().delete(instance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete error",re);
			throw re;
		}
	}
	
	/**
	 * delete objects which in parameter list
	 */
	public void delAll(List list){
		log.debug("deleting instance list");
		try{
			getHibernateTemplate().deleteAll(list);
			log.debug("delete list successful");
		} catch (RuntimeException re) {
			log.error("delete list error",re);
			throw re;
		}
	}
	
	/**
	 * find an object by primarykey id
	 */
	public E findById(Serializable pk) {
		log.debug("getting instance with id: " + pk);
		try{
			E instance = (E) getHibernateTemplate().get(clazz, pk, null);
			return instance;
		} catch(RuntimeException re){
			log.error("get instance error",re);
			throw re;
		}
	}

	public Object findById(Class clazz, Serializable pk) {
		log.debug("getting instance with id: " + pk);
		try{
			E instance = (E) getHibernateTemplate().get(clazz, pk, null);
			return instance;
		} catch(RuntimeException re){
			log.error("get instance error",re);
			throw re;
		}
	}
	/**
	 * find objects by Example
	 */
	public List<E> findByExample(E entityClass) {
		log.debug("finding instance by example");
		try {
			List<E> results = getHibernateTemplate().findByExample(entityClass);
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	/**
	 * create a hql query 
	 */
	public Query createQuery(String hql, Object... params) {
		
		Query query = getSession().createQuery(hql);
		for(int i=0; i<params.length; i++ ){
			query.setParameter(i, params[i]);
		}
		return query;
	}

	/**
	 * remove object by primarykey id
	 */
	public void delById(Serializable id) {
		log.debug("delete instance by id");
		try {
			getHibernateTemplate().delete(this.findById(id));
		} catch (RuntimeException ex){
			log.error("delete instance by id error");
			throw ex;
		}
	}
	
	/**
	 * remove object by primarykey id
	 */
	public void delById(Class clazz,Serializable id) {
		log.debug("delete instance by id");
		try {
			getHibernateTemplate().delete(this.findById(clazz,id));
		} catch (RuntimeException ex){
			log.error("delete instance by id error");
			throw ex;
		}
	}

	/**
	 * get all objects of a class
	 */
	public List<E> findAll() {
		log.debug("find all instance of " + clazz.getName());
		try {
			return getHibernateTemplate().loadAll(clazz);
		} catch (RuntimeException ex) {
			log.error("error : find all instance of " + clazz.getName());
			throw ex;
		}
	}
	
	public List findAll(Class clazz) {
		log.debug("find all instance of " + clazz.getName());
		try {
			return getHibernateTemplate().loadAll(clazz);
		} catch (RuntimeException ex) {
			log.error("error : find all instance of " + clazz.getName());
			throw ex;
		}
	}

	/**
	 * page query with hql 
	 */
	public Result findByPage(Page page,String hql, Object... params) {
		String hqlcnt = BaseDaoImp.removeSelect(hql);
		hqlcnt = BaseDaoImp.removeOrder(hqlcnt);
		hqlcnt = "select count(*) " + hqlcnt;
		Query query = this.getSession().createQuery(hqlcnt);
		for(int i=0; i<params.length; i++ ){
			
			query.setParameter(i, params[i]);
		}
		List lst = query.list();
		int cnt = ((Long)lst.get(0)).intValue();
		page.pagging(cnt);
		if(cnt == 0) {
			return new Result(page,new ArrayList());
		} 
		query = this.getSession().createQuery(hql);
		for(int i=0; i<params.length; i++ ){
			query.setParameter(i, params[i]);
		}
		query.setMaxResults(page.getRowPerPage());
		query.setFirstResult(page.getStartRowIndex());
		lst = query.list();
		return new Result(page,lst);
	}
	
	
	
	/**
	 * query by hql 
	 */
	public List findByHql(String hql, Object... params) {
		try{
			Query query = this.getSession().createQuery(hql);
			for(int i=0; i<params.length; i++ ){
				query.setParameter(i, params[i]);
			}
			return query.list();
		}catch(Exception ex) {
			ex.printStackTrace();
			return new ArrayList();
		}
		
	}
	
	public <F> F findUniqueBy(Class<F> entityClass, String propertyName, Object value) {
		return (F) createCriteria(entityClass,Restrictions.eq(propertyName, value)).uniqueResult();
	}
	
	public  E findUniqueBy(String propertyName, Object value) {
		return findUniqueBy(clazz,propertyName, value);
	}
	
	public <F> Criteria createCriteria(Class<F> entityClass,Criterion... criterions) {
		Criteria criteria = getSession().createCriteria(entityClass);
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}
	
	/**
	 * query unique object by hql
	 */
	public Object findUniqueByHql(String hql, Object... params){
		try{
			Query query = this.getSession().createQuery(hql);
			for(int i=0; i<params.length; i++ ){
				query.setParameter(i, params[i]);
			}
			return query.uniqueResult();
		}catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
		
	}

	/**
	 * remove select part form hql
	 * @param hql
	 * @return
	 */
	private static String removeSelect(String hql){
		int fromPosition = hql.toLowerCase().indexOf("from");
		if(fromPosition != -1){
			String tmp = hql.substring(fromPosition);
			return tmp;
		} else {
			return hql;
		}
	}
	
	/**
	 * remove order part from hql
	 * @param hql
	 * @return
	 */
	private static String removeOrder(String hql){
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(hql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}
	
	
	
	/**
	 * query with sql statement
	 * @param sql
	 * @param page
	 * @param entities
	 * @return
	 */
	public List sqlQuery(String sql,Object... params){
		return sqlQuery( sql, new HashMap(),new HashMap(),params);
	}
	
	

	/**
	 * query with sql statement and Entities binding
	 * @param sql
	 * @param page
	 * @param entities
	 * @return
	 */
	public List sqlQuery(String sql,HashMap entities,HashMap joins,Object... params){
		SQLQuery sqlquery = this.getSession().createSQLQuery(sql);
		for(int i=0; i<params.length; i++ ) {
			sqlquery.setParameter(i, params[i]);
		}
		
		java.util.Iterator<String> it = null;
		if(entities != null) {
			it = entities.keySet().iterator();
			while(it.hasNext()){
				String entityname = it.next();
				sqlquery.addEntity(entityname,(Class)entities.get(entityname));
			}
		}
		if(joins != null){
			it = joins.keySet().iterator();
			while(it.hasNext()){
				String joinname = it.next();
				sqlquery.addJoin(joinname,(String)entities.get(joinname));
			}
		}
		
		return sqlquery.list();
	}
	
	
	/**
	 * page query with sql statement
	 * @param sql
	 * @param page
	 * @param entities
	 * @return
	 */
	public Result sqlQueryByPage(String sql,Page page,Object... params){
		return sqlQueryByPage( sql, page, new HashMap(),new HashMap(),params);
	}
	
	/**
	 * page query with sql statement and entieies binding
	 * @param sql
	 * @param page
	 * @param entities
	 * @return
	 */
	public Result sqlQueryByPage(String sql,Page page,HashMap entities,HashMap joins,Object... params){
		String cntString = "select count(1) from (" + sql +")";
		SQLQuery sqlquery = this.getSession().createSQLQuery(cntString);
		for(int i=0; i<params.length; i++ ) {
			sqlquery.setParameter(i, params[i]);
		}
		int cnt = ((BigDecimal)sqlquery.uniqueResult()).intValue();
		page = page.pagging(cnt);
		
		sqlquery = this.getSession().createSQLQuery(sql);
		for(int i=0; i<params.length; i++ ) {
			sqlquery.setParameter(i, params[i]);
		}
		sqlquery.setFirstResult(page.getStartRowIndex());
		sqlquery.setMaxResults(page.getRowPerPage());
		
		java.util.Iterator<String> it = null;
		if(entities != null) {
			it = entities.keySet().iterator();
			while(it.hasNext()){
				String entityname = it.next();
				sqlquery.addEntity(entityname,(Class)entities.get(entityname));
			}
		}
		if(joins != null){
			it = joins.keySet().iterator();
			while(it.hasNext()){
				String joinname = it.next();
				sqlquery.addJoin(joinname,(String)joins.get(joinname));
			}
		}
		List lst =  sqlquery.list();
		return new Result(page, lst);
	}
	
	
	
	
	/**
	 * get id name of a class from classMetadata of Hibernate mappings 
	 * @param clazz
	 * @return
	 */
	public String getIdName(Class clazz) {
		ClassMetadata cm = getSessionFactory().getClassMetadata(clazz);
		String idName = cm.getIdentifierPropertyName();
		return idName;
	}
	
	
	public void updateByHql(String hql, Object... params) throws Exception {
		try { 
			Query query = this.getSession().createQuery(hql);
			for(int i=0; i<params.length; i++ ){
				query.setParameter(i, params[i]);
			}
			query.executeUpdate();
		} catch(Exception ex) {
			log.info("update failed！");
			throw ex;
		}
	}
	
	/**
	 *@desc  get hibernateTemplate
	 *@return HibernateTemplate
	 */
	public HibernateTemplate getTemplate(){
		this.getSession();
		return getHibernateTemplate();
	}
	
	
	/**
	 *@desc get field's memo of a  mapping property
	 *@date Nov 29, 2009
	 *@author jingpj
	 *@param clazz
	 *@param propertyname
	 *@return
	 */
	public String getFieldMemo(Class clazz,String propertyname) {
		
		AbstractEntityPersister classMetadata = (SingleTableEntityPersister)getSession().getSessionFactory().getClassMetadata(clazz); 
		
		String tablename = classMetadata.getTableName();
		
		String fieldname = classMetadata.getPropertyColumnNames(propertyname)[0];
		
		//String fieldname = HibernateConfigurationHelper.getFieldName(clazz, propertyname);
		
		String hql = "select a.comments from  user_col_comments a where a.COLUMN_NAME=? and a.Table_Name = ?";
		
		List lst = this.sqlQuery(hql,fieldname,tablename);
		
		if(lst.size() > 0) {
			return (String)(lst.get(0));
		} else {
			return "";
		}
		
	}
	
	/**
	 *@desc get Session Object
	 *@return Session
	 */
	public Session getSessionObj(){
		return super.getSession();
	}
	
	
	
	
	
	
}
