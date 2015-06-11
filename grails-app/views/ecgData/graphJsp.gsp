<%@ page import="ecgBase.EcgData" %>
<!DOCTYPE html>
<html>
	<head>
		<script type="text/javascript" src="https://www.google.com/jsapi"></script>
<%--		<asset:javascript src="drawGraph.js"/>--%>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'ecgData.label', default: 'EkgData')}" />
		<title><g:message code="default.edit.label" args="[entityName]" /></title>
	</head>

	<body>
		${graphData}
		<g:javascript>
		    google.load('visualization', '1', {packages: ['corechart', 'line']});
			
			var data3 = new Array( '${graphData}' );
			alert("type="+typeof data3+"\n"+"value="+data3);
			
			var data2 = '${graphData}';
			alert("type="+typeof data2+"\n"+"value="+data2);
			
            var data1 = [
					        [0, 0],   [1, 10],  [2, 23],  [3, 17],  [4, 18],  [5, 9],
					        [6, 11],  [7, 27],  [8, 33],  [9, 40],  [10, 32], [11, 35] 
					    ];
            alert("type="+typeof data1+"\n"+"value="+data1);
			google.setOnLoadCallback( function() { drawGraph( data1 ); } );		
			function drawGraph( graphData1 ) {
			
			// google.setOnLoadCallback(drawGraph);
			// function drawGraph() {

			      var data = new google.visualization.DataTable();
			      data.addColumn('number', 'X');
			      data.addColumn('number', 'Cats');
 
			      var graphData = 
					      [
					        [0, 0],   [1, 10],  [2, 23],  [3, 17],  [4, 18],  [5, 9],
					        [6, 11],  [7, 27],  [8, 33],  [9, 40],  [10, 32], [11, 35],
					        [12, 30], [13, 40], [14, 42], [15, 47], [16, 44], [17, 48],
					        [18, 52], [19, 54], [20, 42], [21, 55], [22, 56], [23, 57],
					        [24, 60], [25, 50], [26, 52], [27, 51], [28, 49], [29, 53],
					        [30, 55], [31, 60], [32, 61], [33, 59], [34, 62], [35, 65],
					        [36, 62], [37, 58], [38, 55], [39, 61], [40, 64], [41, 65],
					        [42, 63], [43, 66], [44, 67], [45, 69], [46, 69], [47, 70],
					        [48, 72], [49, 68], [50, 66], [51, 65], [52, 67], [53, 70],
					        [54, 71], [55, 72], [56, 73], [57, 75], [58, 70], [59, 68],
					        [60, 64], [61, 60], [62, 65], [63, 67], [64, 68], [65, 69],
					        [66, 70], [67, 72], [68, 75], [69, 80]
					      ];
			      alert("here is graph data\n"+graphData);
			      alert("here is passed data\n"+graphData1);
			      data.addRows(graphData1);

			      var options = {
			        hAxis: {
			          title: 'Time'
			        },
			        vAxis: {
			          title: 'Popularity'
			        }
			      };

			      var chart = new google.visualization.LineChart(document.getElementById('chart_div'));

			      chart.draw(data, options);
			}
		</g:javascript>
		
		<div id="chart_div" ></div>

	</body>
</html>