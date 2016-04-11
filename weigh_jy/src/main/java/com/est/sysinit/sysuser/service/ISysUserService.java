package com.est.sysinit.sysuser.service;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.sysinit.sysuser.vo.SysUser;

/**
 * 
 * @corporation Enstrong S&T
 * @author jingpj
 * @date May 5, 2009
 * @path com.est.sysinit.sysuser.service
 * @name com.est.sysinit.sysuser.service.ISysUserService
 * @description 系统用户 service 接口
 */
public interface ISysUserService {

	/**
	 * 
	 * @description 分页查询用户
	 * @date May 5, 2009
	 * @author jingpj
	 * @param page
	 *            分页对象
	 * @param condition
	 *            查询条件
	 * @return
	 */
	public Result<SysUser> getUserList(Page page, SearchCondition condition);

	/**
	 * 
	 * @desc 根据条件查询所有用户
	 * @date 2011-3-31
	 * @author hebo
	 * @param condition
	 * @return
	 */
	public List<SysUser> getUserList(SearchCondition condition);

	/**
	 * 
	 * @desc
	 * @date Nov 20, 2012
	 * @author heb
	 * @param sync
	 *            同步标示(1表示需要同步,0表示不需要同步)
	 * @param username
	 *            用户姓名
	 * @param login
	 *            登录用户名
	 * @param pwd
	 *            登录密码
	 * @return
	 * @throws Exception
	 */
	public void savSyncUser(String sync, String username, String login, String pwd) throws Exception;

	/**
	 * 
	 * @description 删除用户
	 * @date May 5, 2009
	 * @author jingpj
	 * @param userId
	 *            用户id
	 */
	public void delUser(Serializable userId);

	/**
	 * 
	 * @description 通过id得到用户
	 * @date May 5, 2009
	 * @author jingpj 4:08:24 PM
	 * @param userId
	 *            用户id
	 * @return
	 */
	public SysUser getUserById(Serializable userId);

	/**
	 * 
	 * @description 保存用户
	 * @date May 5, 2009
	 * @author jingpj 4:08:48 PM
	 * @param sysUser
	 *            被保存用户
	 */
	public void savUser(SysUser sysUser);

	/**
	 * 
	 * @desc 保存指纹用户特征
	 * @date Nov 25, 2012
	 * @author heb
	 * @param sysUser
	 * @return
	 */
	public SysUser savUserFp(SysUser sysUser) throws Exception;

	/**
	 * 
	 * @desc 删除指纹用户特征
	 * @date Nov 25, 2012
	 * @author heb
	 * @param sysUser
	 * @return
	 */
	public SysUser delUserFp(SysUser sysUser) throws Exception;

	/**
	 * 
	 * @desc 更改用户密码
	 * @date Sep 22, 2009
	 * @author hebo
	 * @param sysUser
	 * @throws Exception
	 */
	public void savModifiedUserPwd(SysUser sysUser) throws Exception;

	/**
	 * 
	 * @description 验证用户
	 * @date May 5, 2009
	 * @author hebo 9:48 AM
	 * @param condition
	 *            用户验证条件
	 */
	public SysUser vefUser(SearchCondition condition);

	/**
	 * 
	 * @desc 指纹验证
	 * @date Nov 25, 2012
	 * @author heb
	 * @param username
	 *            用户姓名
	 * @param fp
	 *            指纹特征码
	 * @return
	 */
	public SysUser verfyUserFp(String username, String fp) throws Exception;

	/**
	 * 
	 * @description 用户名登录名检测
	 * @date May 5, 2009
	 * @author hebo 9:48 AM
	 * @param condition
	 *            查询条件
	 * @throws UnsupportedEncodingException
	 * 
	 */
	public SysUser chkUser(SearchCondition condition);

	public String getChartXml();

	public SysUser getUserByLogin(String login);

	/**
	 * 查询该userid用户是否具备该power权限
	 * 
	 * @param userid
	 * @return
	 */
	public boolean getUserRole(String userid, String power);

}
