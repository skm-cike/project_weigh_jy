package com.est.common.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;


public interface IBaseDao<E> {
	
	/**
	 *@desc clear hibernate Session <please use this method carefully>
	 *@date Nov 10, 2009
	 *@author jingpj
	 */
	public void flushSession();
	
	/**
	 * save or update an object
	 */

	
	public void save(Object instance);
	public void update(Object instance);
	
	public void save(Object instance,int type);
	/**
	 * save or update objects which in parameter list
	 */
	public void saveAll(List list);
	/**
	 * clear session object
	 * save or update an object
	 */
	public void saveAndClear(E instance);
	/**
	 * delete an object
	 */
	public void del(Object instance);
	/**
	 * delete objects which in parameter list
	 */
	public void delAll(List list);
	
	/**
	 * find an object by primarykey id
	 */
	public E findById(Serializable pk) ;
	
	
	public Object findById(Class clazz , Serializable pk);
	
	/**
	 * find objects by Example
	 */
	public List findByExample(E entityClass) ;
	/**
	 * create a hql query 
	 */
	public Query createQuery(String hql, Object... params) ;
	/**
	 * remove object by primarykey id
	 */
	public void delById(Serializable id) ;
	
	public void delById(Class clazz,Serializable id);
	/**
	 * get all objects of a class
	 */
	public List<E> findAll() ;
	
	public List findAll(Class clazz);

	/**
	 * page query with hql 
	 */
	public Result findByPage(Page page,String hql, Object... params) ;
	
	/**
	 * query by hql 
	 */
	public List findByHql(String hql,Object... params);
	
	/**
	 * query unique object by hql
	 */
	public Object findUniqueByHql(String hql, Object... params);
	
	public E findUniqueBy(String propertyName,Object value) ;
	
	public <F> F findUniqueBy(Class<F> entityClass, String propertyName,Object value) ;
	
	public void updateByHql(String hql,Object... params) throws Exception ;
	
	/**
	 * query with sql statement
	 * @param sql
	 * @param page
	 * @param entities
	 * @return
	 */
	public List sqlQuery(String sql,Object... params);
	
	/**
	 * query with sql statement and Entities binding
	 * @param sql
	 * @param page
	 * @param entities
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List sqlQuery(String sql,HashMap entities,HashMap joins,Object... params);
	/**
	 * page query with sql statement
	 * @param sql
	 * @param page
	 * @param entities
	 * @return
	 */
	public Result sqlQueryByPage(String sql,Page page,Object... params);
	
	/**
	 * page query with sql statement and entieies binding
	 * @param sql
	 * @param page
	 * @param entities
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Result sqlQueryByPage(String sql,Page page,HashMap entities,HashMap joins,Object... params);
	

	
	
	/**
	 * get id name of a class from classMetadata of Hibernate mappings 
	 * @param clazz
	 * @return
	 */
	public String getIdName(Class<?> clazz);
	
	
	
	/**
	 *@desc  get hibernateTemplate
	 *@return HibernateTemplate
	 */
	public HibernateTemplate getTemplate();
	
	
	/**
	 *@desc get field's memo of a  mapping property
	 *@date Nov 29, 2009
	 *@author jingpj
	 *@param clazz
	 *@param propertyname
	 *@return
	 */
	public String getFieldMemo(Class clazz,String propertyname);
	
	/**
	 *@desc get Session Object
	 *@return Session
	 */
	public Session getSessionObj();
	
	
}
