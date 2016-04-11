package com.est.common.ext.util;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import com.est.common.ext.util.frontdatautil.IExtJsonConverter;


/**
 * 查询结果封装
 * @author jingpj
 *
 * @param <T>
 */
public class Result<T> implements IExtJsonConverter{
	
	private Page page;
	private List<T> content;
	
	public Result(){
		
	}
	
	public Result(Page page,List<T> content){
		this.page = page;
		this.content = content;
	}
	
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public List<T> getContent() {
		return content;
	}
	public void setContent(List<T> content) {
		this.content = content;
	}
	
	public String getExtJson(JsonConfig jc){
//		jc.setCycleDetectionStrategy(CycleDetectionStrategy.NOPROP);
//		jc.setJsonPropertyFilter(new PropertyFilter(){
//			public boolean apply(Object source, String name, Object value) {
////				if(value!=null){
////					System.out.print("===>name:"+name);
////					System.out.print("===>sourceClassName:"+source.getClass().getName());
////					//System.out.print("===>valueClassName:"+value.getClass().getName());
////					
////					//System.out.print("===>value:"+value);
////				}
//				if( value instanceof Set || value instanceof List){
//					return true;
//				}
//				
//				if("hibernateLazyInitializer".equals(name)){
//					return true;
//				}
//				if(value != null && ((source+"").contains("cglib") ||(source+"").contains("Session") ) ){
//					return true;
//				}
//				
//				return false;
//			}
//			
//		});
		StringBuffer buf = new StringBuffer();
		buf.append("{total:" + page.getTotalRows( )+ ",");
		buf.append("rows:");
		try{
			
			buf.append(JSONArray.fromObject(content,jc).toString().replaceAll(":null", ":''"));
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		buf.append("}");
		return buf.toString();
	}
	
	
}
