package com.est.common.fusioncharts;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeMap;

import com.est.common.ext.util.classutil.DateUtil;
import com.est.common.ext.util.classutil.StringUtil;

/**
 * @corporation Enstrong S&T
 * @author jingpj
 * @date Nov 30, 2008
 * @path com.enstrong.mis.common.web.fusioncharts
 * @description 根据FusionChartContainer中内容，拼装xml，
 * 				所需格式配置用FusionChartCfg对象传递
 */
@SuppressWarnings("unchecked")
public class FusionCharts {
	public static String[] colors = {"6092B3","E24E24","A24EE2","8AAF52","EDCC49"};;
	private String dateFormat ;
	
	public String parse(FusionChartContainer container){
		return this.parse(container, new FusionChartCfg());
	}
	
	/**
	 *@description 解析方法
	 *@date Nov 30, 2008
	 *@author jingpj
	 *@param container
	 *@param cfg
	 *@return
	 */
	@SuppressWarnings("unchecked")
	public String parse(FusionChartContainer container,FusionChartCfg cfg){
		this.dateFormat = cfg.getDateFormat();
		double max = Double.MIN_VALUE;
		double min = Double.MAX_VALUE;
		
		StringBuffer bufferhead = new StringBuffer(1000);
		StringBuffer bufferbody = new StringBuffer(1000);
		
		
		if(container.size()==1) {
			//单一指标
			
			//指标遍历器
			Iterator seriesKeyIterator = container.keySet().iterator();
			while(seriesKeyIterator.hasNext()){
				//得到指标对象
				FusionChartSeries series = container.get(seriesKeyIterator.next());
				//得到指标数据treemap
				TreeMap datamap = series.getSeriesContent();
				
				//遍历treemap，拼装xml语句
				Iterator mapIterator = datamap.keySet().iterator();  //key遍历器
				int i=0;
				
				while(mapIterator.hasNext()){
					Object category = mapIterator.next();
					
					
					Double value = object2Double( datamap.get(category));
					max = max<value?value:max;
					min = min>value?value:min;
					bufferbody.append("<set label='"+this.getCategoryString(category)+"' name='"+this.getCategoryString(category)+"'  value='"+ value +"'  color='"+colors[i%5]+"' ");
					if(i%2 == 0) {
						bufferbody.append(" isSliced='1'");
					}
					bufferbody.append("/>");
					i++;
				}
			}
		} else {
			//多指标
			
			//拼装分类/时间<categores>标签 开始
			bufferbody.append("<categories>");
			//指标遍历器
			Iterator seriesKeyIterator = container.keySet().iterator();
			//分类/时间treemap,按时间先后排序
			TreeMap categoriesMap = new TreeMap(new Comparator(){
				public int compare(Object o1, Object o2) {
					if(o1 instanceof Date) {
						Date d1 = (Date)o1;
						Date d2 = (Date)o2;
						return d1.compareTo(d2);
					} else {
						String s1 = (String)o1;
						String s2 = (String)o2;
						return s1.compareTo(s2);
					}
				}
			});
			while(seriesKeyIterator.hasNext()){
				//得到指标对象
				FusionChartSeries series = container.get(seriesKeyIterator.next());
				
				//得到指标数据treemap
				TreeMap datamap = series.getSeriesContent();
				
				//遍历treemap
				Iterator it = datamap.keySet().iterator();
				while(it.hasNext()){
					//添加分类/时间到分类/时间map
					categoriesMap.put(it.next(), "");
				}
			}
			
			//遍历分类/时间map
			Iterator categoriesIterator = categoriesMap.keySet().iterator();
			while(categoriesIterator.hasNext()){
				bufferbody.append("<category label='"+this.getCategoryString(categoriesIterator.next())+"'/>");;
			}
			bufferbody.append("</categories>");
			
			//拼装分类/时间<categores>标签 结束
			
			//拼装数据集<dataset>标签 开始
			//重置指标遍历器
			seriesKeyIterator = container.keySet().iterator();
			int colorIndex = 0;
			while(seriesKeyIterator.hasNext()){
				//得到指标对象
				FusionChartSeries series = container.get(seriesKeyIterator.next());
				
				//得到指标数据treemap
				TreeMap datamap = series.getSeriesContent();
				bufferbody.append("<dataset  seriesname='"+series.getSeriesName()+"'  color='"+colors[colorIndex]+"'>");
				
				//遍历categoriesMap -遍历该集合可以保证不会因各指标查询结果长度不同，造成错位，同时保证按时间先后顺序
				categoriesIterator = categoriesMap.keySet().iterator();
				double value;
				while(categoriesIterator.hasNext()){
					Object nextkey = categoriesIterator.next();
					
					//Date datekey = (Date) categoriesIterator.next();
					if(datamap.containsKey(nextkey)){
						
						value = object2Double(datamap.get(nextkey));

						max = max<value?value:max;
						min = min>value?value:min;
						bufferbody.append("<set alpha='80' value='"+value+"' dashed='1'  link='javascript:alert(1);'/>");
					} else {
						min = 0d;
						bufferbody.append("<set alpha='80' value='0'/>");
					}
				}
				bufferbody.append("</dataset>");
				colorIndex++;
			}
			
			//拼装数据集<dataset>标签 结束

		}
		
		bufferhead.append("<graph showShadow='1' divLineColor='aaaaaa' divLineIsDashed='1'  imageSave='1' adjustDiv='1' bgAlpha='20'  numDivLines='3' staggerLines='3' decimals='2' divLineColor='777777'   yAxisValueDecimals='2'  canvasBgAlpha='80' labeldisplay='WRAP' slantLabels='1' staggerLines='2' anchorAlpha='0'");
	
		//设置FusionChartCfg中设置的属性
		HashMap<String,String> properties = cfg.getGraphProperties();
		int numdivlines = StringUtil.parseInt(properties.get("numdivlines"));
		
		
		if(max != min){
		
			double height = (max-min)*0.1;
			max += height;
			if(min >= 0 && min - height < 0 ){
				min = 0;
			} else {
				min -= height;
			}
			
			max += 4-((int)max - (int)min)%4;
			//yaxisminvalue,yaxismaxvalue只能为整形
			bufferhead.append(" yaxisminvalue='" +new Double(min).intValue()+ "'");
			bufferhead.append(" yaxismaxvalue='" +(new Double(max).intValue()+1)+ "'");
		}
		
		
		Iterator<String> iterator = properties.keySet().iterator();
		while(iterator.hasNext()){
			String propname = iterator.next();
			bufferhead.append(propname+"='");
			bufferhead.append(properties.get(propname)+"' ");
		}
		bufferhead.append(">");
		bufferbody.append("</graph>");
		return bufferhead.toString() + bufferbody.toString();
	}
	
	public String getCategoryString(Object d){
		if(d instanceof Date){
			return DateUtil.format((Date)d, this.dateFormat);
		} else {
			return d.toString();
		}
	}
	
	/**
	 * 
	 *@description 将object对象转换成double
	 *@date Dec 7, 2008
	 *@author jingpj
	 *@param o
	 *@return
	 */
	public Double object2Double(Object o){
		if(o == null){
			return 0d;
		} else if(o instanceof Double) {
			return (Double)o;
		} else if(o instanceof Integer) {
			return new Double((Integer)o);
		} else if(o instanceof BigDecimal){
			return ((BigDecimal)o).doubleValue();
		} else if(o instanceof Float){
			return new Double((Float)o);
		} else if(o instanceof Long){
			return new Double((Long)o);
		} else if(o instanceof Boolean){
			return (Boolean)o?1d:0d;
		} else if(o instanceof Byte){
			return new Double((Byte)o);
		} else {
			return 0d;
		}
		
	}
	
	
	
	
	
	public String test(){
		FusionChartContainer container = new FusionChartContainer();
		
		Random rand = new Random();
		int SERIES_MAX = 1;  
		int DATE_MAX = 20;
		
		Date[] dates = new Date[DATE_MAX];
		for(int i=0;i<DATE_MAX;i++) {
			dates[i] = new Date(5000000-i*500000);
		}
		
		for(int i=0;i<SERIES_MAX;i++) {
			
			TreeMap map = new TreeMap();
			for(int j=0;j<DATE_MAX;j++){
				
				map.put(dates[j], new BigDecimal(rand.nextDouble()*1000+500));
			}
			
			
			container.setSeriesContent("指标"+i, map);
		}
		
		return parse(container);
	}
	
}