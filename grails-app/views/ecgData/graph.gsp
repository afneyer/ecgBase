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

		<gvisualization:lineCoreChart elementId="piechart" title="Ecg Graphs" width="${800}" height="${300}"
			columns="${ecgColumns}" data="${graphData}" />
		<div id="piechart"></div>

	</body>
</html>