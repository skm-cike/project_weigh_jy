package com.est.common.ext.util.excelimport;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.est.common.ext.util.classutil.ClassUtils;

/**
 *@desc EXCEL文件导入
 *@author jingpj
 *@date Dec 7, 2009
 *@path com.est.common.ext.util.excelimport.ExcelImport
 *@corporation Enstrong S&T
 */
public class ExcelImport<T> {
	
	/** 保存对象的class */
	private Class clazz;
	
	/** 需导入的字段 */
	private String[] fieldnames;
	
	public ExcelImport(){
		clazz =  ClassUtils.getSuperClassGenricType(getClass());
	}
	
	
	public List<T> getExcelData(File file) throws Exception{
		
		List<T> result = new ArrayList<T>();
		
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));
		
		HSSFSheet sheet = workbook.getSheetAt(0);
		
		ArrayList<ExcelImportFieldProp> propList = new ArrayList<ExcelImportFieldProp>(); 
		
		
		for(int i=0; i< fieldnames.length; i++ ) {
			
			String fieldname = fieldnames[i];
			
			Field field = clazz.getField(fieldname);
			
			Class fieldtype = field.getType();
			
			Method setmethod = clazz.getMethod(ClassUtils.getSetMethodName(fieldname), fieldtype);
			
			
			ExcelImportFieldProp fieldprop = new ExcelImportFieldProp();
			
			fieldprop.setFieldType(fieldtype);
			
			fieldprop.setSetMethod(setmethod);
			
			propList.add(fieldprop);
		}
		
		
		for(int intRow=1; intRow <= sheet.getLastRowNum(); intRow ++) {
			
			HSSFRow row = sheet.getRow(intRow);		// 获取行
			
			if(row != null) {
				
				T obj = (T) clazz.newInstance();
				
				//读取excel中的数据
				for(int colindex=0; colindex < propList.size(); colindex++ ) {
					ExcelImportFieldProp fieldprop = propList.get(colindex);
					
					HSSFCell cell = row.getCell((short)colindex);
					
					int celltype = this.getCellType(fieldprop.getFieldType());
					
					cell.setCellType(celltype);
					
					Object cellStringValue = this.getCellValue(cell, colindex,fieldprop.getFieldType());
					
					fieldprop.getSetMethod().invoke(obj, cellStringValue);
					
					
					
				}
				
				result.add(obj);
				
				
			}
		}
		
		
		
		return result;
	}
	
	
	
	private String readExcelCellValue(HSSFCell cell,int intCol)
	{
		String cellValue = "";
		switch (cell.getCellType())
		{
			case HSSFCell.CELL_TYPE_NUMERIC: 
			{
				// 判断当前的cell是否为Date
               if (HSSFDateUtil.isCellDateFormatted(cell))
               {
                  // 如果是Date类型则，取得该Cell的Date值
                  Date date = cell.getDateCellValue();
                  // 把Date转换成本地格式的字符串
                  cellValue = cell.getDateCellValue().toLocaleString();
               }       
            		// 如果是纯数字
               else		
               {
            	   if(intCol == 0)
            	   {
	               		Integer num = new Integer((int) cell.getNumericCellValue());
	                 	cellValue = String.valueOf(num);
            	   }
            	   else
            	   {
            		   // 取得当前Cell的数值
                       Double num = cell.getNumericCellValue();
                       cellValue = String.valueOf(num);
            	   }                 
               }
               break;
			}
            // 如果当前Cell的Type为String
            case HSSFCell.CELL_TYPE_STRING:
            {
               // 取得当前的Cell字符串
            	//cell.getRichStringCellValue();
            	cellValue = cell.getStringCellValue().trim();
               break;
            }
            default: 	//默认的Cell值
            	cellValue = "";         
		}
		return cellValue;
	}
	
	/**
	 *@desc 设置cell的类型
	 *@date Dec 7, 2009
	 *@author jingpj
	 *@param proptype
	 */
	private int getCellType(Class proptype) {
		
		if(Boolean.class.equals(proptype)){
			return HSSFCell.CELL_TYPE_BOOLEAN;
		} else if (Double.class.equals(proptype) || Float.class.equals(proptype) || Integer.class.equals(proptype) || Long.class.equals(proptype)) {
			return HSSFCell.CELL_TYPE_NUMERIC;
		} else {
			return HSSFCell.CELL_TYPE_STRING;
		}
	}
	
	private Object getCellValue(HSSFCell cell,int colindex,Class proptype) {
		
		Object cellValue = null;
		
		switch (cell.getCellType()) {
			
			case HSSFCell.CELL_TYPE_NUMERIC: {// 判断当前的cell是否为Date
			
				if (Date.class.equals(proptype)) {
					if (HSSFDateUtil.isCellDateFormatted(cell)){

		            	  // 如果是Date类型则，取得该Cell的Date值
		                  Date date = cell.getDateCellValue();
		                  
		                  // 把Date转换成本地格式的字符串
		                  cellValue = cell.getDateCellValue();
		                  
		                  return cellValue;
		                  
		            } else {
		            	return null;
		            }
					
				}
				
				// 取得当前Cell的数值
				cellValue = cell.getNumericCellValue();
				
				if (Double.class.equals(proptype)  ) {
					return cellValue;
				}
				
				if(Float.class.equals(proptype)){
					return ((Double)cellValue).floatValue();
				}
				
				if ( Integer.class.equals(proptype)) {
					return ((Double)cellValue).intValue();
				}
				
				if(Long.class.equals(proptype)) {
					return ((Double)cellValue).longValue();
				}
				
               break;
			}
           
            case HSSFCell.CELL_TYPE_STRING: {// 如果当前Cell的Type为String

            	// 取得当前的Cell字符串
            	//cell.getRichStringCellValue();
            	
            	cellValue = cell.getStringCellValue().trim();
               
            	break;
            }
            default: 	//默认的Cell值
            	cellValue = null;         
		}
		return cellValue;
		
	}
	

	
}


class ExcelImportFieldProp {
	
	/** 属性的类型*/
	private Class fieldType;
	
	/** 属性的set方法*/
	private Method setMethod;
	
	public Class getFieldType() {
		return fieldType;
	}
	public void setFieldType(Class fieldType) {
		this.fieldType = fieldType;
	}
	public Method getSetMethod() {
		return setMethod;
	}
	public void setSetMethod(Method setMethod) {
		this.setMethod = setMethod;
	}
	
	
	
}
