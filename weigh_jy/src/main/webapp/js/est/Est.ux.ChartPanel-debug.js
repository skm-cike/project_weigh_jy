Ext.namespace('Est.ux');
/**
 * Est.ux.ChartPanel Extension Class xtype is estpanel
 * 
 * @author jingpj
 * @version 1.0
 * 
 * @class Est.ux.SearchPanel
 * @extends Ext.Panel
 * @constructor
 * @param {Object}
 *            cfg Configuration options
 * 
 * @cfg {Array} formitems
 * 
 */
Est.ux.ChartPanel = function(cfg) {
	
	var self = this;
	
	this.autoScroll = true;
	this.hidden = true;
	this._preInit(cfg);
	this.id = cfg.id;
	
	this.chartDivId = 'chartdiv'+cfg.id;
	
	this.chartWidth = '100%';
	
	this.chartHeight = '100%';
	
	this.dataXml = "";
	
	this.chartUrl = "";
	
	this.showtoolbar = cfg.showtoolbar?cfg.showtoolbar:false;  //但指标
	this.showtoolbarMS = cfg.showtoolbarMS?cfg.showtoolbarMS:false; // 多指标  @author suf
	if(this.showtoolbar){
	   this.defaultType = cfg.defaultType?cfg.defaultType:'Line';
	}else if(this.showtoolbar){
	   this.defaultType = cfg.defaultType?cfg.defaultType:'MSLine';
		
	}
	this.chart = null;
	
	this.chartType = this.typeMapping[this.defaultType];
	
	//单指标 
	if(this.showtoolbar){
		this.tbar = [
			{text:'曲线图',handler:function(){self.chartType = self.typeMapping['Line'];self.changeshowChart(self.chartType)}},
			{text:'柱状图',handler:function(){self.chartType = self.typeMapping['Column3D'];self.changeshowChart(self.chartType)}},
			{text:'饼图',handler:function(){self.chartType = self.typeMapping['Pie2D'];self.changeshowChart(self.chartType)}}
		];
	}
	//多指标  suf
	else if(this.showtoolbarMS){   
		this.tbar = [
			{text:'曲线图',handler:function(){self.chartType = self.typeMapping['MSLine'];self.changeshowChart(self.chartType)}},
			{text:'柱状图',handler:function(){self.chartType = self.typeMapping['MSColumn3D'];self.changeshowChart(self.chartType)}},
			{text:'面积图',handler:function(){self.chartType = self.typeMapping['MSArea'];self.changeshowChart(self.chartType)}}
		];
	}
	
	cfg.html = '<div id="chartdiv'+cfg.id+'" style="width:100%;height:100%;background-color" align="center"></div>';
	
	Est.ux.Panel.superclass.constructor.call(this, cfg);
	
	this.loadData();
	
};

Ext.extend(Est.ux.ChartPanel, Ext.Panel, {
	_preInit : function(cfg) {
		
	},
	
	typeMapping : {
		'Line': contextPath+'/FusionCharts/Line.swf',
		'Column3D': contextPath+'/FusionCharts/Column3D.swf',
		'Pie2D': contextPath+'/FusionCharts/Pie2D.swf',
		
		'MSLine': contextPath+'/FusionCharts/MSLine.swf',
		'MSColumn3D': contextPath+'/FusionCharts/MSColumn3D.swf',
		'MSArea': contextPath+'/FusionCharts/MSArea.swf'
	},
	
	
	
	/** 载入数据 */
	loadData: function(params){
			var self = this;
			self.on({'activate':function(){
				var tmpData ;
				//alert(this.chartUrl);
				Ext.Ajax.request({
					url:this.chartUrl,
					params:params,
					success: function(response){
						tmpData = response.responseText;
						self.dataXml = tmpData;
						self.changeChartType(self.chartType);
					},
					failure: function(){
						alert('failure');
					}
				});
				
			},scope:self});
			
	},
	
	/** @author suf  查询时载入数据 */
	searchData: function(params){
			var self = this;
			var tmpData ;
                Ext.Ajax.request({
					url:this.chartUrl,
					params:params,
					success: function(response){
						tmpData = response.responseText;
						self.dataXml = tmpData;
						self.changeChartType(self.chartType);
					},
					failure: function(){
						alert('failure');
					}
				});				
				
				
			
	},
	
	/** 切换图表类型*/
	changeChartType : function(chartType){
		this.chart = null;
		this.chart = new FusionCharts(chartType, this.chartDivId, ""+this.chartWidth, ""+this.chartHeight, "0", "0");
		//this.chart = new FusionCharts("/tpm/FusionCharts/Pie2D.swf", this.chartDivId, 500, 600, "0", "0");
		this.chart.setDataXML(this.dataXml);
		this.chart.render(this.chartDivId);
		this.chartType = chartType;
	},
	/** @author  suf */
	changeshowChart : function(chartType,params) {
		this.loadData(params);
		this.changeChartType(this.chartType);
	},	
	showChart : function(chartType,params) {
		this.chartType  = this.typeMapping[chartType];
		this.loadData(params);
		this.changeChartType(this.chartType);
	},
    /** @author  suf */
	searchshowChart : function(chartType,params) {
		this.chartType  = this.typeMapping[chartType];
		this.searchData(params);
		//this.changeChartType(this.chartType);//1
	}
	
});

Ext.reg('estchartpanel', Est.ux.ChartPanel);
