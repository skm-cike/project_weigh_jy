package com.est.common.fusioncharts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import com.est.common.base.IBaseDao;

/**
 *@desc 图表工具类
 *@author jingpj
 *@date Feb 21, 2010
 *@path com.est.common.fusioncharts.FusionChartContainer
 *@corporation Enstrong S&T
 */

@SuppressWarnings({ "unchecked", "serial" })
public class FusionChartContainer extends TreeMap<String,FusionChartSeries> {
	
	
	public FusionChartContainer(){
	}
	
	public void setSeriesSql(String serielname,String sql){
		if(!this.containsKey(serielname)){
			this.put(serielname, new FusionChartSeries());
			this.get(serielname).setSeriesName(serielname);
		}
		this.get(serielname).setSeriesSql(sql);
	}
	public void setSeriesHql(String serielname,String hql){
		if(!this.containsKey(serielname)){
			this.put(serielname, new FusionChartSeries());
			this.get(serielname).setSeriesName(serielname);
		}
		this.get(serielname).setSeriesHql(hql);
	}
	public void setSeriesContent(String serielname,TreeMap seriesContent){
		if(!this.containsKey(serielname)){
			this.put(serielname, new FusionChartSeries());
			this.get(serielname).setSeriesName(serielname);
		}
		this.get(serielname).setSeriesContent(seriesContent);
	}
	
	
	public String generateResult(IBaseDao dao) {
		return this.generateResult(dao,new FusionChartCfg());
	}
	
	
	public String generateResult(IBaseDao dao,FusionChartCfg cfg){
		Iterator iterator = this.keySet().iterator();
		while(iterator.hasNext()){
			FusionChartSeries series = this.get(iterator.next());
			
			List resultList = new ArrayList();
			if(series.getSeriesHql()!=null) {
				String hql = series.getSeriesHql();
				resultList=dao.findByHql(hql);
			} else if(series.getSeriesSql()!=null){
				String sql = series.getSeriesSql();
				resultList=dao.sqlQuery(sql);
			}
			TreeMap content  = new TreeMap();
			for(int i=0;i<resultList.size();i++){
				content.put(((Object[])resultList.get(i))[0],((Object[])resultList.get(i))[1]);
			}
			series.setSeriesContent(content);
		}
		FusionCharts fc = new FusionCharts();
		
		return fc.parse(this,cfg);
	}
	

}
