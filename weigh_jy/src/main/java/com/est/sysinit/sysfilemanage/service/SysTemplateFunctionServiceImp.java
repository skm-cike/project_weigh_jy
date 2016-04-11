package com.est.sysinit.sysfilemanage.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.Result;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.common.ext.util.frontdatautil.EditableGridDataHelper;
import com.est.sysinit.sysfilemanage.dao.ISysTemplateFunctionDao;
import com.est.sysinit.sysfilemanage.vo.SysTemplatefunction;
import com.est.sysinit.sysfilemanage.vo.SysTemplateparam;


/**
 *@desc 模板函数管理service
 *@author zhanglk
 *@date Jun 26, 2009
 *@path com.est.sysinit.sysfilemanage.service.SysTemplateFunctionServiceImp
 *@corporation Enstrong S&T 
 */
public class SysTemplateFunctionServiceImp implements ISysTemplateFunctionService {

	private ISysTemplateFunctionDao sysTemplateFunctionDao;
	private ISysTemplateparamService sysTemplateparamService ;
	
	public void setSysTemplateFunctionDao(ISysTemplateFunctionDao sysTemplateFunctionDao){
		this.sysTemplateFunctionDao = sysTemplateFunctionDao;
	}
	
	public void setSysTemplateparamService(
			ISysTemplateparamService sysTemplateparamService) {
		this.sysTemplateparamService = sysTemplateparamService;
	}

	/**
	 * 
	 *@desc 分页查询模板函数 
	 *@date Jun 26, 2009
	 *@author zhanglk
	 *@param page
	 *@param sc
	 *@return
	 */
	public Result<SysTemplatefunction> getTemplateFunctionResult(Page page,SearchCondition sc){
		StringBuffer hql = new StringBuffer(100);
		Long templateId = StringUtil.parseLong((String)sc.get("templateid"));
		List<Object> paramList = new ArrayList<Object>();
		hql.append("from SysTemplatefunction t ");
		
		if(templateId!=null && templateId!=0){
			hql.append(" where t.sysTemplate.templateid=?");
			paramList.add(templateId);
		}
		hql.append(" order by t.executeorder");
		return sysTemplateFunctionDao.findByPage(page, hql.toString(),paramList.toArray());
	}
	/**
	 * 
	 *@desc 获取模板函数list 
	 *@date Jun 26, 2009
	 *@author zhanglk
	 *@return
	 */
	public List<SysTemplatefunction> getAllTemplateFunction(){
		return sysTemplateFunctionDao.findAll();
	}
	/**
	 * 
	 *@desc 保存可编辑grid数据 
	 *@date Jun 26, 2009
	 *@author zhanglk
	 *@param changeData
	 */
	public void savChanges(EditableGridDataHelper changeData){
		List<SysTemplatefunction> saveList = new ArrayList<SysTemplatefunction>();
		List<SysTemplatefunction> delList = new ArrayList<SysTemplatefunction>();
		Iterator<Object> it = changeData.getSaveObjects().iterator();
		while(it.hasNext()) {
			SysTemplatefunction sysTemplatefunction = (SysTemplatefunction)it.next();
			if(sysTemplatefunction.getFunctionid()==0) {
				sysTemplatefunction.setFunctionid(null);
			}
			saveList.add(sysTemplatefunction);
		}
		sysTemplateFunctionDao.saveAll(saveList);
		
		it = changeData.getDelObjects().iterator();
		while(it.hasNext()) {
			delList.add((SysTemplatefunction)it.next());
		}
		sysTemplateFunctionDao.delAll(delList);
	}
	
	
	/**
	 * 
	 *@desc 查询模板函数 
	 *@date Jun 26, 2009
	 *@author zhanglk
	 *@param page
	 *@param sc
	 *@return
	 */
	public String getTemplateFunctionContent(Long templateId, String funName){
		StringBuffer hql = new StringBuffer(100);
		List<Object> paramList = new ArrayList<Object>();
		hql.append("select t.functioncontent from SysTemplatefunction t where ");
		hql.append(" t.sysTemplate.templateid=?");
		paramList.add(templateId);
		hql.append(" and t.functioncode=?");
		paramList.add(funName);
		hql.append(" order by t.executeorder");
		return (String) sysTemplateFunctionDao.findUniqueByHql(hql.toString(), paramList.toArray());
		
	}
	
	/**
	 *@desc 计算从用户模块中加载的自定义函数
	 *@date Jul 17, 2009
	 *@author jingpj
	 *@param templateid
	 *@param funName
	 *@param funArg
	 *@return
	 *@see com.est.sysinit.sysfilemanage.service.ISysTemplateFunctionService#calcFunc(java.lang.Long, java.lang.String, java.lang.String)
	 */
	public String calcFunc(Long templateId, String funName, String funArg,String funContent) {
		
		String functionContent = this.getTemplateFunctionContent(templateId, funName);
		Pattern pattern = Pattern.compile("\\((.*)\\)");
		Matcher matcher = pattern.matcher(funContent);
		String  strParamNames = null; 
		List<SysTemplateparam> paramsList = sysTemplateparamService.getTemplateparamsById(templateId);
		String[] paramNames ;
		String[] paramValues ;
		if(matcher.find()) {
			strParamNames = matcher.group(1).trim();
			paramNames = strParamNames.split(",");
			paramValues = funArg.split(",");
			for(int i=0;i<paramNames.length;i++) {
				
				String paramType = this.getParamType(paramNames[i], paramsList);
				if("double".equalsIgnoreCase(paramType)){
					functionContent = functionContent.replaceAll("#"+paramNames[i]+"#", paramValues[i]);
				} else {
					functionContent = functionContent.replaceAll("#"+paramNames[i]+"#", "'"+paramValues[i]+"'");
				}
				
			}
			System.out.println("=========================functionContent:"+functionContent);
			
			
		}
		//List lst = sysTemplateFunctionDao.sqlQuery("select 'fwwwoorr'||'' as a,'bbb'as b,13.5 from dual");
		StringBuilder result = new StringBuilder();
		//functionContent = "select 'fwwwoorr','bbb',22222222222222222213.5 from dual";
		Connection conn = sysTemplateFunctionDao.getSessionObj().connection();
		try {
			PreparedStatement stmt = conn.prepareStatement(functionContent);
			ResultSet rs = stmt.executeQuery();
			int rownum = 0;
			result.append("[");
			while(rs.next()){
				if(rownum!=0){
					result.append(",");
				}
				result.append("[");
				int columnCnt =  rs.getMetaData().getColumnCount();
				for(int i=1; i<=columnCnt ;i++) {
					if(i!=1){
						result.append(",");
					}
					int type = rs.getMetaData().getColumnType(i);
					if(type == Types.BIGINT  
							|| type == Types.BIT      
							|| type == Types.DECIMAL 
							|| type == Types.DOUBLE  
							|| type == Types.INTEGER 
							|| type == Types.FLOAT   
							|| type == Types.NUMERIC 
							|| type == Types.REAL
							|| type == Types.SMALLINT
							|| type == Types.TINYINT) {
						Object o = rs.getObject(i);
						if(o!=null) {
							result.append(o);
						} else {
							result.append(0);
						}
						
					} else {
						Object o = rs.getObject(i);
						if(o!=null) {
							result.append("'"+o+"'");
						} else {
							result.append("''");
						}
						//result.append("'"+rs.getObject(i)+"'");
					}
						
					System.out.println( rs.getMetaData().getColumnTypeName(i)+":"+rs.getObject(i));
				}
				result.append("]");
				rownum++;
			}
			result.append("]");
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
//		List lst = sysTemplateFunctionDao.sqlQuery(functionContent);
//		
//		result.append("[");
//		for(int i=0;i<lst.size();i++){
//			if(i!=0){
//				result.append(",");
//			}
//			Object[] row = (Object[])lst.get(i);
//			result.append("[");
//			for(int j=0;j<row.length;j++){
//				if(j!=0){
//					result.append(",");
//				}
//				Object col = row[j];
//				
//				if(col instanceof BigDecimal) {
//					result.append(col);
//				}else{
//					result.append("'");
//					result.append(col);
//					result.append("'");
//				}
//				
//			}
//			result.append("]");
//			
//		}
//		result.append("]");
		if(result.toString().equals("")){
			return "[]";
		} else {
			return result.toString();
		}
	}
	
	/**
	 * 
	 *@desc 通过模板id和参数名得到参数类型
	 *@date Jul 20, 2009
	 *@author jingpj
	 *@param paramName
	 *@param paramList
	 *@return
	 */
	private String getParamType(String paramName,List<SysTemplateparam> paramList) {
		for(SysTemplateparam param : paramList) {
			String tmpName = param.getParamname();
			if(tmpName!=null && tmpName.equals(paramName)){
				return param.getParamtype();
			}
		} 
		return null;
		
	}
	
}
