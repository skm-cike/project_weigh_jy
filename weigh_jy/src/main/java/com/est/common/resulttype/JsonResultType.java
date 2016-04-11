package com.est.common.resulttype;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.StrutsResultSupport;

import com.est.common.ext.util.frontdatautil.DefaultJsonConfig;
import com.est.common.ext.util.frontdatautil.IExtJsonConverter;
import com.est.common.ext.util.jsonlibhelper.propertyfilter.JSONPropertyFilter;
import com.opensymphony.xwork2.ActionInvocation;
@SuppressWarnings("unchecked")
public class JsonResultType extends StrutsResultSupport {

	private static final long serialVersionUID = 6355603611137025312L;

	protected static final Log log = LogFactory.getLog(JsonResultType.class);

	protected final String CONTENTTYPE = "application/json";
	protected final String CHARSET = "utf-8";
	protected String type = "jsonType";
	protected String value = "jsonValue";

	protected String jsonStr = "";
	protected Object jsonObj = null;
	protected List jsonArr = null;
	
	protected String prefix = "";
	protected String suffix = "";

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	protected void doExecute(String finalLocation, ActionInvocation invocation)
			throws Exception {

		HttpServletResponse response = ServletActionContext.getResponse();

		JSONPropertyFilter jsonPropertyFilter = (JSONPropertyFilter) invocation.getStack().findValue(conditionalParse("jsonPropertyFilter", invocation));
		
		JsonConfig jc = new DefaultJsonConfig();
		if(jsonPropertyFilter!=null) {
			jc.setJsonPropertyFilter(jsonPropertyFilter);
		}
		
		response.setContentType(CONTENTTYPE + "; charset=" + CHARSET);
		response.setHeader("Content-Disposition", "inline");
		PrintWriter writer = response.getWriter();
		type = (String) invocation.getStack().findValue(
				conditionalParse(type, invocation));
		if (type == null) {
			type = "jsonStr";
		}

		if (type.equals("jsonStr")) {
			jsonStr = (String) invocation.getStack().findValue(
					conditionalParse(value, invocation));
		} else if (type.equals("jsonObj")) {
			jsonObj = invocation.getStack().findValue(
					conditionalParse(value, invocation));
			if (jsonObj == null) {
				jsonStr = "";
			} else {
				if(jsonObj instanceof IExtJsonConverter) {
					jsonStr = ((IExtJsonConverter)jsonObj).getExtJson(jc);
				} else {
					JSONObject jo = JSONObject.fromObject(jsonObj,jc);
					jsonStr = jo.toString();
				}
			}
		} else {
			jsonArr = (List) invocation.getStack().findValue(
					conditionalParse(value, invocation));
			
			if (jsonArr.size() > 0) {
				jsonStr = JSONArray.fromObject(jsonArr,jc).toString();
			} else {
				jsonStr = "";
			}
		}
		
		System.out.println(jsonStr);
		
		prefix = (String) invocation.getStack().findValue(
				conditionalParse("prefix", invocation));
		suffix = (String) invocation.getStack().findValue(
				conditionalParse("suffix", invocation));
		
		StringBuffer buf = new StringBuffer();
		if(prefix != null) {
			buf.append(prefix);
		}
		buf.append(jsonStr);
		if(suffix != null){
			buf.append(suffix);
		}
		try {
			writer.write(buf.toString());
		} finally {
			if (writer != null) {
				writer.flush();
				writer.close();
			}
		}

	}

}
