package com.est.common.resulttype;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.dispatcher.StrutsResultSupport;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;

public class FileResultType extends StrutsResultSupport {

	protected final Log log = LogFactory.getLog(FileResultType.class);

	private static final long serialVersionUID = -2659052470244868842L;

	private Object content;
	
	private String filename;
	
	private String filetype;
	


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


	@Override
	protected void doExecute(String finalLocation, ActionInvocation invocation)
			throws Exception {
		filename =  (String) invocation.getStack().findValue(
				conditionalParse("filename", invocation));
		
		content = (Object) invocation.getStack().findValue(
				conditionalParse("content", invocation));
		
		filetype = (String) invocation.getStack().findValue(
				conditionalParse("filetype", invocation));
		
		ActionContext context = invocation.getInvocationContext();
		HttpServletResponse res = (HttpServletResponse) context
				.get(HTTP_RESPONSE);
		
		//res.addHeader("Content-Type", "application/octet-stream;"+getContentType(filetype)+";charset='UTF-8'");
		
		res.setHeader("Content-Type", "application/octet-stream");
		res.addHeader("Content-Type",getContentType(filetype)+";charset='UTF-8'");
		res.addHeader("Content-Disposition", "attachment;filename="+ filename);
		res.setCharacterEncoding("UTF-8");
		
			
			if(content instanceof String) {
				PrintWriter writer = res.getWriter();
				try {
					
					writer.write((String)content);
					writer.flush();
					writer.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					if(writer!=null) {
						writer.close();
					}
				}
			} else {
				
				InputStream is = null;
				OutputStream os = res.getOutputStream();
				try{
					if(content instanceof File) {
						File f = (File) content;
						is = new FileInputStream(f);
					} else {
						is = (InputStream) content;
					}
					
					byte[] outBuf = new byte[1024];
					int len = 0;
					while ((len = is.read(outBuf))!=-1){
						os.write(outBuf, 0, len);
					}
					os.flush();
					os.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					if(is != null) {
						is.close();
					} 
					if(os != null) {
						os.close();
					}
				}
			}
		
		//OutputStream out = res.getOutputStream();
//		try {
//			writer.write(content);
//		} finally {
//			writer.flush();
//			writer.close();
//		}
	}
	
	public String getContentType(String type) {
		if("xls".equals(type)) {
			return "application/vnd.ms-excel";
		}
		return "";
	}

}
