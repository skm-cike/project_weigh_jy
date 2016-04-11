<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>

<html>
	<HEAD>
		<TITLE></TITLE>	
		<style type="text/css">
		body {
			font-family: 宋体;
			font-size: 9px;
		}
		</style>
		
	</HEAD>
	
	<BODY bgcolor="#6666cc" fullscreen=yes>
		<div align='right' style='margin-right:10px;'>
			
				
			<% if(!((String)request.getAttribute("content")).contains("category")) { //单指标%>
				<input type='button' class='button' value='线形图' onClick="javaScript:updateChart('<%=request.getContextPath()%>/FusionCharts/Line.swf');" name='btnLine' />		
				<input id="init" type='button' class='button' value='柱状图' onClick="javaScript:updateChart('<%=request.getContextPath()%>/FusionCharts/Column3D.swf');" name='btnColumn' />		
				<input type='button' class='button' value='饼状图' onClick="javaScript:updateChart('<%=request.getContextPath()%>/FusionCharts/Pie2D.swf');" name='btnPie' />		
			<%} else { //多指标%>
				<input type='button' class='button' value='曲线图' onClick="javaScript:updateChart('<%=request.getContextPath()%>/FusionCharts/MSLine.swf');" name='msLine' />		
				<input id="init" type='button' class='button' value='柱状图' onClick="javaScript:updateChart('<%=request.getContextPath()%>/FusionCharts/MSColumn3D.swf');" name='msColumn' />		
				<input type='button' class='button' value='面积图' onClick="javaScript:updateChart('<%=request.getContextPath()%>/FusionCharts/MSArea.swf');" name='msBar' />	
			<%} %>
			
			<% java.util.Random rand = new java.util.Random();
			   String id = "id"+rand.nextLong();
			%>
	    </div>
		<CENTER>
			<h2></h2> <!-- 图表标题 -->
			<div id="<%=id %>">
			</div>
			<BR />
	
	    </CENTER>
	</BODY>

	<SCRIPT LANGUAGE="JavaScript">
		var chart1 = null;  //图形对象
		//We store the XML data as a string
		var content = "<%=request.getAttribute("content")%>"; 
		/*
		var screenheight = document.body.offsetHeight || document.body.scrollHeight;
		var screenwidth = document.body.offsetWidth || document.body.scrollWidth;
		var offsetheight = <%=request.getAttribute("offsetheight") %> ;
		var offsetwidth = <%=request.getAttribute("offsetwidth") %> ;
		
		var chartheight = <%=request.getAttribute("chartheight") %>;
		var chartwidth = <%=request.getAttribute("chartwidth") %>;
		
		if(offsetheight == null) {
			offsetheight = 140;
		}
		if(offsetwidth == null) {
			offsetwidth = 30;
		}
		if(chartheight == null){
			chartheight = screenheight - offsetheight;
		}
		if(chartwidth == null) {
			chartwidth = screenwidth - offsetwidth;
		}
		*/
		
		chartwidth = 1100;
		chartheight = 480;
		
		/*
		 * updateChart method is called, when user clicks the button
		 * Here, we change the chart from Column to line
		 */
		 updateChart =function(chartSWF){
			//Create another instance of the chart.
			chart1 = null;//释放资源
			
			
			chart1 = new FusionCharts(chartSWF, "<%=id %>", ""+chartwidth, ""+chartheight, "0", "0");		   			
			chart1.setDataXML(content);
			chart1.render("<%=id %>");
			charttype = chartSWF;
		}
		
		if(charttype == ''){
			<% if(!((String)request.getAttribute("content")).contains("category")) { //多指标对比%>
				charttype = '<%=request.getContextPath()%>/FusionCharts/Line.swf';
			<%} else { //单一指标%>
				charttype = '<%=request.getContextPath()%>/FusionCharts/MSLine.swf';
			<%} %>
		}
			
			
		//打开页面显示默认图形
		updateChart(charttype);
	</script>
</html>
