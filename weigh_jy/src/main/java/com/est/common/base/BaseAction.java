package com.est.common.base;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ParameterAware;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.util.ServletContextAware;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.est.common.ext.util.Page;
import com.est.common.ext.util.SearchCondition;
import com.est.common.ext.util.classutil.StringUtil;
import com.est.common.ext.util.excelexport.ExcelExportData;
import com.est.common.ext.util.jsonlibhelper.propertyfilter.JSONPropertyFilter;
import com.est.common.ext.util.jsonlibhelper.propertyfilter.JSONPropertyFilterFactory;
import com.est.sysinit.sysmodule.service.ISysModuleService;
import com.est.sysinit.sysuser.vo.SysUser;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.interceptor.ScopedModelDriven;

@SuppressWarnings("unchecked")
public abstract class BaseAction extends ActionSupport implements ModelDriven,
		ServletRequestAware, ServletResponseAware, ServletContextAware,ParameterAware {

	private static final long serialVersionUID = 6279754118729869294L;

	protected final Log log = LogFactory.getLog(getClass());

	protected final String TPL = "tpl"; 
	protected final String JSP = "jsp";
	protected final String HTML = "html";
	protected final String INPUT = "input";
	protected final String STR = "str";
	protected final String INDEX = "index";
	protected final String JSON = "json";
	protected final String ERROR = "error";
	protected final String FILE = "file";
	protected final String CHART = "chart";

	protected HttpServletRequest req;
	protected HttpServletResponse res;
	protected ServletContext context;
	
	protected JSONPropertyFilter jsonPropertyFilter;

	private String target;
	private String str; // for toSTR method
	
	private String jsonType;
	private Object jsonValue;
	
	private String prefix;
	private String suffix;
	
	private Object content;
	private String filename;
	private String filetype;
	
	protected SearchCondition params;
	

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getStr() {
		return str;
	}
	
	public String getJsonType() {
		return jsonType;
	}

	public Object getJsonValue() {
		return jsonValue;
	}
	
	
//	public void setStr(String str) {
//		this.str = str;
//	}

	public String getPrefix() {
		return prefix;
	}

	public String getSuffix() {
		return suffix;
	}
	

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFiletype() {
		return filetype;
	}

	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}

	/** ******** begin forward ********* */
	protected String toTPL(String _target) {
		setTarget(_target);
		return TPL;
	}

	protected String toJSP(String _target) {
		try{
			ISysModuleService sysModuleService  = (ISysModuleService) getBean("sysModuleService");
			Long moduleId = StringUtil.parseLong(req.getParameter("_moduleID"));
			String moduleFullPath = sysModuleService.getModuleFullPathById(moduleId);
			if(moduleFullPath == null) {
				moduleFullPath = "";
			}
			req.setAttribute("moduleFullPath", moduleFullPath);
			String rwFlag = req.getParameter("rw");
			req.setAttribute("isSaveValid", "none");
			req.setAttribute("isDelValid", "none");
			req.setAttribute("_moduleId", moduleId);
				
			if(rwFlag!=null) {
				if(rwFlag.contains("M")){
					req.setAttribute("isSaveValid", "");
				}
				if(rwFlag.contains("D")){
					req.setAttribute("isDelValid", "");
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		setTarget(_target);
		return JSP;
	}

	protected String toHTML(String _target) {
		setTarget(_target);
		return HTML;
	}

	protected String toSTR(String str) {
		this.str = str;
		return STR;
	}
	
	protected String toInfo(String info){
		this.str = "{success:true,message:'"+info+"';";
		return STR;
	}

	protected String toINDEX() {
		return INDEX;
	}

	public String toINPUT() {
		// ActionContext ctx = ActionContext.getContext();
		// HttpServletRequest req = (HttpServletRequest) ctx
		// .get(ServletActionContext.HTTP_REQUEST);
		// setTarget(req.getHeader("referer"));
		// logger.info("path========>"+req.getContextPath());
		setTarget("init/Index/index");
		return INPUT;
	}
	
	public String toJSON(String jsonStr) {
		this.jsonType = "jsonStr";
		this.jsonValue=jsonStr;
		return JSON;
	}
	public String toJSON(Object jsonObj) {
		this.jsonType = "jsonObj";
		this.jsonValue=jsonObj;
		return JSON;
	}
	public String toJSON(List jsonArr) {
		this.jsonType = "jsonArr";
		this.jsonValue=jsonArr;
		return JSON;
	}

	
	public String toJSON(String jsonStr,String prefix,String suffix) {
		this.jsonType = "jsonStr";
		this.jsonValue=jsonStr;
		this.prefix = prefix;
		this.suffix = suffix;
		return JSON;
	}
	public String toJSON(Object jsonObj,String prefix,String suffix) {
		this.jsonType = "jsonObj";
		this.jsonValue=jsonObj;
		this.prefix = prefix;
		this.suffix = suffix;
		return JSON;
	}
	public String toJSON(List jsonArr,String prefix,String suffix) {
		this.jsonType = "jsonArr";
		this.jsonValue=jsonArr;
		this.prefix = prefix;
		this.suffix = suffix;
		return JSON;
	}
	
	
	
	
	
	public String toFILE(String filename,Object content,String filetype) {
		this.filename=filename;
		this.content = content;
		this.filetype = filetype;
		return FILE;
	}
	
	
	
	public String toERROR(String str) {
		this.str = "{success:false,info:'"+str+"'}";
		return STR;
	}
	
	public String toCHART(String content) {
		req.setAttribute("content", content);
		return CHART;
	}
	
	/** ******** end forward ********* */

	public abstract Object getModel();

	// If no such method
	public String doDefault() {
		List<String> errors = new ArrayList<String>();
		errors.add(getText("err.unauthorized_access"));
		this.setActionErrors(errors);
		return toINPUT();
	}

	public void setServletRequest(HttpServletRequest req) {
		this.req = req;
	}

	public void setServletResponse(HttpServletResponse res) {
		this.res = res;
	}
	
	public void setServletContext(ServletContext context) {
		this.context = context;
	}
	
	public Object getBean(String name) {
		ApplicationContext ctx = WebApplicationContextUtils
				.getRequiredWebApplicationContext(context);
		return ctx.getBean(name);
	}
	
	
	/**
	 * 
	 *@description 通过request中的参数，构造分页对象
	 *@date May 27, 2009
	 *@author jingpj
	 *11:45:25 AM
	 *@param req
	 *@return 
	 */
	public Page getPage(){
		String start = req.getParameter("start");
		String limit = req.getParameter("limit");
		return new Page(start,limit);
	}
	
	/**
	 *@desc 得到当前登录用户
	 *@date Oct 16, 2009
	 *@author jingpj
	 *@return
	 */
	public SysUser getCurrentUser(){
		return (SysUser) req.getSession().getAttribute("loginUser");
	}
	
	/**
	 *@desc 导出到excel 
	 *@date Jan 22, 2010
	 *@author jingpj
	 *@return
	 */
	public String exportToExcel() {
		ExcelExportData excelData = queryExcelData();
		
		
		if (!"".equals(excelData)) {// 如果数据不为空，则导出Excel
			// xls为检查页面返回为success的时候，对应回调函数的参数，目的为导出Excel(如果此参数不为null)
			String xls = req.getParameter("xls");
			if (xls != null) {
				String fileName = "";
				try {
					fileName = java.net.URLEncoder.encode(excelData.getFileName()+".xls","UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return toFILE(fileName, excelData.getResult(), "xls");
			}
			return NONE;
		} else {
			return toSTR("{success:false}");
		}
	}
	
	public ExcelExportData queryExcelData(){
		return null;
	}

	public JSONPropertyFilter getJsonPropertyFilter() {
		return jsonPropertyFilter;
	}
	
	public void setJsonPropertyFilter(String filtertype,String[] fields) {
		try{
			this.jsonPropertyFilter = JSONPropertyFilterFactory.getFilter(filtertype,fields);
		} catch (Exception ex) {
			log.debug("设置json属性过滤器失败！");
			ex.printStackTrace();
		}
	}


	public void setJsonPropertyFilter(JSONPropertyFilter jsonPropertyFilter) {
		this.jsonPropertyFilter = jsonPropertyFilter;
	}

	
	public void setParameters(Map<String, String[]> parameters) {
		if(this.params==null) {
			this.params = new SearchCondition();
			this.params.set("page", getPage());
		}
		
		Iterator<String> it = parameters.keySet().iterator();
		
		while (it.hasNext()){
			String key = it.next();
			String[] values = parameters.get(key);
			
			if(values.length>1){
				this.params.set(key, values);
			} else {
				this.params.set(key, values[0]);
			}
		}
	}
}
