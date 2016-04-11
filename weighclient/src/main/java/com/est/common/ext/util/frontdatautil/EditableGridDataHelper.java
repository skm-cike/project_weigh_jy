package com.est.common.ext.util.frontdatautil;

import java.util.ArrayList;
import java.util.Iterator;

import com.est.common.ext.util.classutil.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

/**
 * 
 * @corporation Enstrong S&T
 * @author jingpj
 * @date Jun 19, 2009
 * @path com.est.common.ext.util.frontdatautil
 * @name com.est.common.ext.util.frontdatautil.EditableGridDataHelper
 * @description 可编辑grid前台返回数据转换为Bean帮助类
 */
public class EditableGridDataHelper {
	
	/**
	 * List of objects need save or update
	 */
	private ArrayList<Object> saveObjects;
	/**
	 * List of objects need delete
	 */
	private ArrayList<Object> delObjects;
	
	public EditableGridDataHelper(){
		saveObjects = new ArrayList<Object>();
		delObjects = new ArrayList<Object>();
	}
	
	public ArrayList<Object> getSaveObjects() {
		return saveObjects;
	}


	public void setSaveObjects(ArrayList<Object> saveObjects) {
		this.saveObjects = saveObjects;
	}


	public ArrayList<Object> getDelObjects() {
		return delObjects;
	}


	public void setDelObjects(ArrayList<Object> delObjects) {
		this.delObjects = delObjects;
	}


	@SuppressWarnings("unchecked")
	public static EditableGridDataHelper data2bean(String dataStr , Class<?> clazz){
		EditableGridDataHelper gridData = new EditableGridDataHelper(); 
		JSONArray jsonArr = JSONArray.fromObject(dataStr);
		
		DateMorpher dateMorpher = new DateMorpher(new String[]{"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH","yyyy-MM-dd HH:mm","yyyy-MM-dd"});
		dateMorpher.setUseDefault(true);
		JSONUtils.getMorpherRegistry().registerMorpher(dateMorpher);
		
		Iterator<JSONObject> iterator = jsonArr.iterator();
		while(iterator.hasNext()){
			JSONObject jsonObj = iterator.next();
			
			String modifystatus = jsonObj.getString("modifystatus");
			if("m".equals(modifystatus)||"a".equals(modifystatus)) {
				//添加和修改
				gridData.saveObjects.add(JSONObject.toBean(jsonObj, clazz));
			} else if ("d".equals(modifystatus)) {
				//删除
				gridData.delObjects.add(JSONObject.toBean(jsonObj, clazz));
			}
		}
		
		return gridData;
	}

}
