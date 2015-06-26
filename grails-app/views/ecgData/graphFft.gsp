<%@ page import="ecgBase.EcgData"%>
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <asset:javascript src="drawGraph.js"/>
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
			
			var data = '${graphData}';
			// alert("type="+typeof data+"\n"+"value="+data);
			
			var options = '${graphOptions}';
			
			google.setOnLoadCallback( function() { drawGraph( columns, data, options ); } );

		</g:javascript>

	<div id="chart_div"></div>


</body>
</html>