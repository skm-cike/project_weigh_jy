package com.est.common.fusioncharts;


import java.util.HashMap;

@SuppressWarnings("serial")
public class FusionChartCfg{
	
	
	private String DateFormat;
	
	private HashMap<String,String> graphProperties = new HashMap<String,String>();
	/**  可设置总体属性列表
	 *   bgColor;     		//背景颜色
	 *	 caption;			//标题
	 *	 subCaption;  		//子标题
	 *	 xAxisName;   		//X轴名称
	 *	 yAxisName;			//Y轴名称
	 *	 yAxisMinValue; 	//Y轴最小值
	 *	 yAxisMaxValue; 	//y轴最大值
	 *	 numdivlines;		//分隔线数量
	 * ===================================
	 *	 shownames			//显示名称
	 *   showValues			//显示值
	 *   showLimits			//显示最大值
	 *   rotateNames		//翻转名称
	 *   animation			//动画效果
	 *   showColumnShadow	//列阴影
	 *   showShadow			//显示阴影
	 *   
	 *   ==================================font
	 *   baseFontSize		//字体大小
	 *   baseFont			//字体类型
	 *   baseFontColor		//字体颜色
	 *   outCnvBaseFont		//画布外字体
	 *   outCnvBaseFontSze	//画布外字体大小
	 *   outCnvBaseFontColor//画布外字体颜色
	 *   ==================================line
	 *   bgSWF			 	//背景图片
	 *   labelstep			//名称显示间隔
	 */ 
	
	
	public FusionChartCfg(){
		this.setShowValues("0");
		this.setNumdivlines("4");
		this.setBaseFont("宋体");
		this.setBaseFontSize("12");
		this.setDateFormat("yyyy-MM-dd HH:mm");
		this.setLabelstep("1");
	}



	
	public String getDateFormat() {
		return DateFormat;
	}

	public void setDateFormat(String dateFormat) {
		DateFormat = dateFormat;
	}

	public String getBgColor() {
		return (String)this.graphProperties.get("bgColor");
	}
	public void setBgColor(String bgColor) {
		this.graphProperties.put("bgColor", bgColor);
	}
	public String getCaption() {
		return (String)this.graphProperties.get("caption");
	}
	public void setCaption(String caption) {
		this.graphProperties.put("caption", caption);
	}
	public String getSubCaption() {
		return (String)this.graphProperties.get("subCaption");
	}
	public void setSubCaption(String subCaption) {
		this.graphProperties.put("subCaption", subCaption);
	}
	public String getXAxisName() {
		return (String)this.graphProperties.get("xAxisName");
	}
	public void setXAxisName(String xAxisName) {
		this.graphProperties.put("xAxisName", xAxisName);
	}
	public String getYAxisName() {
		return (String)this.graphProperties.get("yAxisName");
	}
	public void setYAxisName(String yAxisName) {
		this.graphProperties.put("yAxisName", yAxisName);
	}
	public String getYAxisMinValue() {
		return (String)this.graphProperties.get("yAxisMinValue");
	}
	public void setYAxisMinValue(String yAxisMinValue) {
		this.graphProperties.put("yAxisMinValue", yAxisMinValue);
	}
	public String getYAxisMaxValue() {
		return (String)this.graphProperties.get("yAxisMaxValue");
	}
	public void setYAxisMaxValue(String yAxisMaxValue) {
		this.graphProperties.put("yAxisMaxValue", yAxisMaxValue);
	}
	public String getNumdivlines() {
		return (String)this.graphProperties.get("numdivlines");
	}
	public void setNumdivlines(String numdivlines) {
		this.graphProperties.put("numdivlines", numdivlines);
	}
	public String getShownames() {
		return (String)this.graphProperties.get("shownames");
	}
	public void setShownames(String shownames) {
		this.graphProperties.put("shownames", shownames);
	}
	
	public String getShowValues() {
		return (String)this.graphProperties.get("showValues");
	}
	public void setShowValues(String showValues) {
		this.graphProperties.put("showValues", showValues);
	}
	public String getShowLimits() {
		return (String)this.graphProperties.get("showLimits");
	}
	public void setShowLimits(String showLimits) {
		this.graphProperties.put("showLimits", showLimits);
	}
	public String getRotateNames() {
		return (String)this.graphProperties.get("rotateNames");
	}
	public void setRotateNames(String rotateNames) {
		this.graphProperties.put("rotateNames", rotateNames);
	}
	public String getAnimation() {
		return (String)this.graphProperties.get("animation");
	}
	public void setAnimation(String animation) {
		this.graphProperties.put("animation", animation);
	}
	public String getShowColumnShadow() {
		return (String)this.graphProperties.get("showColumnShadow");
	}
	public void setShowColumnShadow(String showColumnShadow) {
		this.graphProperties.put("showColumnShadow", showColumnShadow);
	}
	public String getBaseFont() {
		return (String)this.graphProperties.get("baseFont");
	}
	public void setBaseFont(String baseFont) {
		this.graphProperties.put("baseFont", baseFont);
	}
	public String getBaseFontSize() {
		return (String)this.graphProperties.get("baseFontSize");
	}
	public void setBaseFontSize(String baseFontSize) {
		this.graphProperties.put("baseFontSize", baseFontSize);
	}
	public String getBaseFontColor() {
		return (String)this.graphProperties.get("baseFontColor");
	}
	public void setBaseFontColor(String baseFontColor) {
		this.graphProperties.put("baseFontColor", baseFontColor);
	}

	public String getOutCnvBaseFont() {
		return (String)this.graphProperties.get("outCnvBaseFont");
	}
	public void setOutCnvBaseFont(String outCnvBaseFont) {
		this.graphProperties.put("outCnvBaseFont", outCnvBaseFont);
	}
	public String getOutCnvBaseSze() {
		return (String)this.graphProperties.get("outCnvBaseSze");
	}
	public void setOutCnvBaseSze(String outCnvBaseSze) {
		this.graphProperties.put("outCnvBaseSze", outCnvBaseSze);
	}
	public String getOutCnvBaseColor() {
		return (String)this.graphProperties.get("outCnvBaseColor");
	}
	public void setOutCnvBaseColor(String outCnvBaseColor) {
		this.graphProperties.put("outCnvBaseColor", outCnvBaseColor);
	}	
	
	public String getBgSWF() {
		return (String)this.graphProperties.get("bgSWF");
	}
	public void setBgSWF(String bgSWF) {
		this.graphProperties.put("bgSWF", bgSWF);
	}	

	public String getLabelstep() {
		return (String)this.graphProperties.get("labelstep");
	}
	public void setLabelstep(String labelstep) {
		this.graphProperties.put("labelstep", labelstep);
	}	
	
	
	public HashMap<String, String> getGraphProperties() {
		return graphProperties;
	}
	
}
