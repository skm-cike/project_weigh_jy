package com.est.sysinit.sysdept.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.est.common.ext.util.businessutil.SerialNumGeneratorUtil;
import com.est.sysinit.sysdept.dao.ISysDeptDao;
import com.est.sysinit.sysdept.vo.SysDept;
import com.est.sysinit.sysuser.dao.ISysUserDao;

/**
 * 
 * @corporation Enstrong S&T
 * @author jingpj
 * @date May 4, 2009
 * @path com.est.sysinit.sysuser.service
 * @name com.est.sysinit.sysuser.service.SysUserServiceImp
 * @description
 */
public class SysDeptServiceImp implements ISysDeptService {
	private final Log log = LogFactory.getLog(SysDeptServiceImp.class);
	
	private ISysDeptDao sysDeptDao;
	private ISysUserDao	sysUserDao;
 	private static final int SERIAL_LEN = 3;
	
	public void setSysDeptDao(ISysDeptDao sysDeptDao) {
		this.sysDeptDao = sysDeptDao;
	}

	public void setSysUserDao(ISysUserDao sysUserDao) {
		this.sysUserDao = sysUserDao;
	}

	public void delDept(Serializable deptId) {
		this.isExistsContents((Long)deptId);
		sysDeptDao.delById(deptId);
	}

	@SuppressWarnings("unchecked")
	public List<SysDept> getDepartTree(Serializable deptId) {
		Long id = (Long)deptId;
		StringBuffer buf = new StringBuffer(200);
		List<Object> paramList = new ArrayList<Object>();
		buf.append("from SysDept t where ");
		if(id!=null && id!=0L) {
			buf.append(" t.sysDept.deptid = ?");
			paramList.add(id);
		} else {
			buf.append(" t.sysDept is null");
		}
		buf.append(" order by t.deptorder asc");
		return sysDeptDao.findByHql(buf.toString(), paramList.toArray());
	}
	
	
	public List<SysDept> getAllDepart() {
		
		
		return sysDeptDao.findAll();
	}


	public void savDept(SysDept sysDept) throws Exception {
		
		boolean isNew = false;
		if(sysDept == null ){
			return ;
		} else if(sysDept.getDeptid()==null || sysDept.getDeptid()==0){
			isNew = true;
		}
		//修改父部门为空时，拦截器将新建一个内容全部为null的对象，导致保存失败，需要手动将父部门设置成null
		if(sysDept.getSysDept() != null && sysDept.getSysDept().getDeptid() == null) {
			sysDept.setSysDept(null);
		}
		//保存前的部门
		SysDept oldDept = null; 
		if(sysDept.getDeptid()!=null && sysDept.getDeptid()!= 0) {
			oldDept = sysDeptDao.findById(sysDept.getDeptid());
		}		
		//新的父模块
		SysDept parentDept = sysDept.getSysDept(); 
		if(parentDept!=null) {
			parentDept = sysDeptDao.findById(parentDept.getDeptid());
		}
		
		
		//修改后，如果父id等于自身id，不进行保存
		if(sysDept.getDeptid()!=null && sysDept.getSysDept()!=null) {
			if(sysDept.getDeptid().equals(sysDept.getSysDept().getDeptid())){
				throw new RuntimeException("父级id等于自身id");
			}
		}
		
		//修改后，如果将其子设置为父，不进行保存
		if(oldDept!=null && parentDept!=null && parentDept.getDeptcode().startsWith(oldDept.getDeptcode())){
			throw new RuntimeException("不能将其子级设置为父级");
			
		}
		
		
		//修改后，为其添加编号（父模块编号＋流水号）
		String parentCode = null;	//父模块编号
		if(parentDept != null ) {
			parentDept = sysDeptDao.findById(parentDept.getDeptid());
			parentCode = parentDept.getDeptcode();
		} 
		//需要修改编码情况：
		if(	
				// 1 ：新增
				isNew   
				 	//2：第一级，但编码长度不为SERIAL_LEN
				|| (parentDept == null && oldDept.getDeptcode().length() != SERIAL_LEN )
					//3：非一级,但编码长度不等于该级长度，或者编码不是以父级编码开始
				|| (parentDept != null &&  (oldDept.getDeptcode().length() != SERIAL_LEN + parentCode.length() || !oldDept.getDeptcode().startsWith(parentCode)))) 
		{
			try{	
				String newCode = new SerialNumGeneratorUtil().getSerialNum(sysDeptDao, "SysDept", "deptcode", parentCode, SERIAL_LEN);
				sysDept.setDeptcode(newCode);
				//在不是新增时，修改所有子级编码
				if(!isNew){
					setAllSubCode(oldDept.getDeptcode(),newCode);
				}
				
			} catch (Exception ex) {
				log.info("修改所有子级编码错误！");
				throw ex;
			}
		}
		
		if(oldDept!=null) {
			//因为已经从数据库中读取了修改前的模块，为避免hibernate验证错误，这里保存读取的对象
			BeanUtils.copyProperties(oldDept, sysDept);
		} else {
			oldDept = sysDept;
		}
		
		sysDeptDao.save(oldDept);
	}


	public SysDept getDeptById(Serializable deptId) {
		return sysDeptDao.findById(deptId);
	}
	
	/**
	 * 
	 *@description 在父模块编码改变的时候，修改所有子模块的编码、层数
	 *@date May 29, 2009
	 *@author jingpj
	 *8:18:04 PM
	 *@param oldCode 原编码
	 *@param newCode 新编码
	 * @throws Exception 
	 */
	private void setAllSubCode(String oldCode,String newCode) throws Exception{
		StringBuffer buf = new StringBuffer();
		int oldCodeLen = oldCode.length();
		buf.append("update SysDept set deptcode = '"+newCode+"'||substr(deptcode,"+(oldCodeLen+1)+") ");
		buf.append(" where deptcode!='"+oldCode+"' and deptcode like '"+oldCode+"%'");
		sysDeptDao.updateByHql(buf.toString());
	}


	/**
	 * 
	 *@description 找到某部门的一级（顶极）部门
	 *@date Jun 5, 2009
	 *@author jingpj
	 *9:44:57 AM
	 *@param deptId
	 *@return
	 */
	public SysDept findFirstLevelDept(Long deptId) {
		if(deptId == null || deptId == 0 ) {
			return null ;
		}
		StringBuilder buf = new StringBuilder();
		buf.append("from SysDept t where t.deptcode = ");
		buf.append(" (select substr(a.deptcode,0,"+SERIAL_LEN+") from SysDept a where a.deptid = ?)");
		buf.append(" and t.sysDept is null");
		List<SysDept> deptList = sysDeptDao.findByHql(buf.toString(), deptId);
		SysDept dept = null;
		if(deptList!=null && deptList.size()>0) {
			dept = deptList.get(0);
		}
		return dept;
	}
   /**
    * 
    *@desc 检验部门下是否存在子内容
    *@date Oct 19, 2009
    *@author hebo
    *@param deptId
    *@return
 * @throws Exception 
    *@see com.est.sysinit.sysdept.service.ISysDeptService#isExistsContents(java.lang.Long)
    */

	public void isExistsContents(Long deptId) {
			//查询部门下是否存在用户
			String hql="select count(*) from SysUser t where t.sysDept.deptid=?";
			Long count=(Long)sysUserDao.findUniqueByHql(hql, deptId);
			
			if(count>0){
				throw new RuntimeException("此部门下存在用户，请先删除此部门下的用户，再执行删除操作!");
			}
			
			//查询此部门下是否存在部门
			hql="select count(*) from SysDept t where t.sysDept.deptid=?";
			count=(Long)sysDeptDao.findUniqueByHql(hql, deptId);
			if(count>0){
					throw new RuntimeException("此部门下存在部门，请先删除此部门下的子部门，再执行删除操作!");
			}
	}
}
