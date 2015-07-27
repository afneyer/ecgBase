<%@ page import="ecgBase.EcgDataFile"%>
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <asset:javascript src="drawGraph.js"/>
    <asset:javascript src="printDiv.js"/>
     <meta name="layout" content="main">
     <g:set var="entityName"
	     value="${message(code: 'ecgData.label', default: 'EkgData1')}" />
      <title><g:message code="default.edit.label" args="[entityName]" /></title>
      <link rel="stylesheet" href="${resource(dir: 'css', file: 'ecgApp.css')}" type="text/css">
</head>

<body>

    <div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
				<li><input type="button" value="Print" onclick="javascript:printDiv('chart_div')" /></li>
			</ul>
    </div>

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