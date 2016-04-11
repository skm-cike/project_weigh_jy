package com.est.common.fusioncharts;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@SuppressWarnings("unchecked")
public class FusionChartSeries {
	

	private String seriesName;
	
	private String seriesSql;
	
	private String seriesHql;
	
	private TreeMap seriesContent;
	
	private List scales;
	
	public FusionChartSeries(){
		scales = new ArrayList();
	}
    
	public String getSeriesName() {
		return seriesName;
	}

	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}

	public String getSeriesSql() {
		return seriesSql;
	}

	public void setSeriesSql(String seriesSql) {
		this.seriesSql = seriesSql;
	}

	
	
	public String getSeriesHql() {
		return seriesHql;
	}

	public void setSeriesHql(String seriesHql) {
		this.seriesHql = seriesHql;
	}

	public TreeMap getSeriesContent() {
		return seriesContent;
	}

	public void setSeriesContent(TreeMap seriesContent) {
		this.seriesContent = seriesContent;
	}

	public List getScales() {
		return scales;
	}

	public void setScales(List scales) {
		this.scales = scales;
	}
	
	
   

}