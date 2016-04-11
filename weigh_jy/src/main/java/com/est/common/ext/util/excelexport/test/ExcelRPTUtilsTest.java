package com.est.common.ext.util.excelexport.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.est.common.ext.util.excelexport.ExcelRPTUtils;

public class ExcelRPTUtilsTest {
	public static void main(String[] args) {

		try {

			/** 来煤月报表开始* */

			List<Object[]> datas1 = new ArrayList<Object[]>();
			// 月报数据格式
			Object[] dataItems = new Object[21];

			dataItems[0] = "山西大同兴泰煤矿";
			dataItems[1] = "无烟煤";
			dataItems[2] = "火车";
			dataItems[3] = "合同采购";
			dataItems[4] = "1000";
			dataItems[5] = "10";
			dataItems[6] = "20000";
			dataItems[7] = "15000";
			dataItems[8] = "1000";
			dataItems[9] = "700";
			dataItems[10] = "100";
			dataItems[11] = "70";
			dataItems[12] = "10";
			dataItems[13] = "7";
			dataItems[14] = "5";
			dataItems[15] = "2";
			dataItems[16] = "100";
			dataItems[17] = "8000";
			dataItems[18] = "5000";
			dataItems[19] = "1000";
			dataItems[20] = "900";

			datas1.add(dataItems);

			ExcelRPTUtils tpl1 = new ExcelRPTUtils("D:\\tpl\\JGYB_tpl.xls",
					"d:\\tpl\\JGYB-入厂煤价格月报.xls");
			tpl1.putDataByLst(datas1, 5);

			tpl1.flushData();

			/** 来煤月报表结束* */

			/** 来煤日报表开始* */
			ExcelRPTUtils tpl2 = new ExcelRPTUtils("D:\\tpl\\RLRB_tpl.xls",
					"d:\\tpl\\RLRB-入厂煤价格日报.xls");

			Object[] data = new Object[6];

			data[0] = "1";
			data[1] = "2";
			data[2] = "3";
			data[3] = "4";
			data[4] = "5";
			data[5] = "6";

			tpl2.setRowData(4, data);// 填充某行数据

			data = new Object[6];

			data[0] = "11";
			data[1] = "12";
			data[2] = "13";
			data[3] = "14";
			data[4] = "15";
			data[5] = "16";

			List<Object[]> dailyData = new ArrayList<Object[]>();
			dailyData.add(data);

			tpl2.putDataByLst(dailyData, 8);

			/* 读取报表返回给客户 */
			FileInputStream fis = tpl2.flushData();
			FileOutputStream fos = new FileOutputStream("d:\\tpl\\测试数据.xls");

			byte[] bytes = new byte[1024];

			while (fis.read(bytes) != -1) {
				fos.write(bytes);
			}
			fis.close();
			fos.close();
			/*
			 * action 中调用方法 String fileName =java.net.URLEncoder.encode("文件名称","UTF-8");
			 * response.setHeader("Content-Type", "application/force-download");
			 * response.setHeader("Connection","close");
			 * response.setHeader("Content-Type","application/octet-stream");
			 * response.setHeader("Content-Disposition", "attachment;filename="+fileName);
			 * 
			 * ServletOutputStream out = response.getOutputStream();
			 * 
			 * byte[] buffer = new byte[1024]; 
			 * int length = 0; 
			 * while((length = fis.read(buffer)) > 0){ 
			 * 			out.write(buffer,0,length); 
			 * }
			 * 
			 * fis.close();
			 */

			/* 获取报表文件返回给客户 */

			/** 来煤日报表结束* */
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

}
