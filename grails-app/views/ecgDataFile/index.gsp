
<%@ page import="ecgBase.EcgDataFile"%>
<!DOCTYPE html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName"
	value="${message(code: 'ecgData.label', default: 'EkgData')}" />
<title><g:message code="default.list.label" args="[entityName]" /></title>
<%--<r:require modules="bootstrap"/>--%>
</head>
<body>
	<a href="#list-ecgData" class="skip" tabindex="-1"><g:message
			code="default.link.skip.label" default="Skip to content&hellip;" /></a>
	<div class="nav" role="navigation">
		<ul>
			<li><a class="home" href="${createLink(uri: '/')}"><g:message
						code="default.home.label" /></a></li>
			<li><g:link class="create" action="create">
					<g:message code="default.new.label" args="[entityName]" />
				</g:link></li>
		</ul>
	</div>
	<div id="list-ecgData" class="content scaffold-list" role="main">
		<h1>
			<g:message code="default.list.label" args="[entityName]" />
		</h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>
		<table>
			<thead>
				<tr>
					<g:sortableColumn property="identifier"
						title="${message(code: 'ecgData.identifier.label', default: 'Identifier')}" />

					<g:sortableColumn property="fileName"
						title="${message(code: 'ecgData.fileName.label', default: 'File Name')}" />
						
					<g:sortableColumn property="fileType"
						title="${message(code: 'ecgData.fileType.label', default: 'File Type')}" />

					<g:sortableColumn property="fileData"
						title="${message(code: 'ecgData.fileData.label', default: 'File Data')}" />


					<g:sortableColumn property="uploadDate"
						title="${message(code: 'ecgData.uploadDate.label', default: 'Upload Date')}" />
					
					<th>Download</th>

				</tr>
			</thead>
			<tbody>
				<g:each in="${ecgDataInstanceList}" status="i" var="ecgDataInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

						<td>
							${fieldValue(bean: ecgDataInstance, field: "identifier")}
						</td>

						<td><g:link action="show" id="${ecgDataInstance.id}">
								${fieldValue(bean: ecgDataInstance, field: "fileName")}
							</g:link></td>

						<td>
							${fieldValue(bean: ecgDataInstance, field: "fileDataStr32")}
						</td>

						<td><g:formatDate date="${ecgDataInstance.uploadDate}" /></td>
						
						<td><g:link action="download" id="${ecgDataInstance.id}">
								download
							</g:link>
						</td>
						

					</tr>
				</g:each>
			</tbody>
		</table>
		<div class="pagination">
			<g:paginate total="${ecgDataInstanceCount ?: 0}" />
		</div>
	</div>
</body>
</html>
