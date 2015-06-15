<%@ page import="ecgBase.EcgData"%>
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
<%--		<asset:javascript src="drawGraph.js"/>--%>
     <meta name="layout" content="main">
     <g:set var="entityName"
	     value="${message(code: 'ecgData.label', default: 'EkgData1')}" />
      <title><g:message code="default.edit.label" args="[entityName]" /></title>
      <link rel="stylesheet" href="${resource(dir: 'css', file: 'ecgApp.css')}" type="text/css">
</head>

<body>

	<g:javascript>
		    
		    google.load('visualization', '1', {packages: ['corechart', 'line']});
			
			var columns = '${graphColumns}';
			// var columns = ' ';
			
			var data = '${graphData}';
			// alert("type="+typeof data+"\n"+"value="+data);
			
			var options = '${graphOptions}';
			
			
			
			google.setOnLoadCallback( function() { drawGraph( columns, data, options ); } );
			
		    function drawGraph( graphColumns, graphData, graphOptions ) {

				var data = new google.visualization.DataTable();
			
				// alert("here is passed data\n"+graphData);
			    // alert("here are passed graph options\n" + graphOptions );
			    
			    // alert ("before Columns\n" + 'var gColumns = ' + graphColumns );
			    eval('var gColumns = ' + graphColumns);
			    // alert("here is gColumns\n" + gColumns );
				for (var i = 0; i < gColumns.length; i++) {
				 	 // alert(gColumns[i]);
                     data.addColumn( gColumns[i][0], gColumns[i][1] );
                }          
			    
				eval('var gData = ' + graphData);
			    data.addRows(gData);
			
				eval('var options = ' + graphOptions );
	
				var chart = new google.visualization.LineChart(document.getElementById('chart_div'));
			
				chart.draw(data, options);
			}	

		</g:javascript>

	<div id="chart_div"></div>

</body>
</html>