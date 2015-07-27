<html>
	<head>
		<meta name="layout" content="main">
		<title>Upload New ECG-File</title>
	</head>
	<body>
		<div class="nav" role="navigation">
			<ul><li><g:link class="index" action="index">Back to ECG-File List</g:link></li></ul>
		</div>
		<div class="content scaffold-create" role="main">
			<h1>Upload New ECG-File</h1>
			<g:if test="${flash.message}"><div class="message" role="status">${flash.message}</div></g:if>
			<g:uploadForm action="upload">
				<fieldset class="form">
                    <input type="file" name="file" />
				</fieldset>
				<fieldset class="buttons">
					<g:submitButton name="upload" class="save" value="Upload" />
				</fieldset>
			</g:uploadForm>
		</div>
	</body>
</html>

<%--<!DOCTYPE html>--%>
<%--<html>--%>
<%--	<head>--%>
<%--		<meta name="layout" content="main">--%>
<%--		<g:set var="entityName" value="${message(code: 'ecgData.label', default: 'EkgData')}" />--%>
<%--		<title><g:message code="default.create.label" args="[entityName]" /></title>--%>
<%--	</head>--%>
<%--	<body>--%>
<%--		<a href="#create-ecgData" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>--%>
<%--		<div class="nav" role="navigation">--%>
<%--			<ul>--%>
<%--				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>--%>
<%--				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>--%>
<%--			</ul>--%>
<%--		</div>--%>
<%--		<div id="create-ecgData" class="content scaffold-create" role="main">--%>
<%--			<h1><g:message code="default.create.label" args="[entityName]" /></h1>--%>
<%--			<g:if test="${flash.message}">--%>
<%--			<div class="message" role="status">${flash.message}</div>--%>
<%--			</g:if>--%>
<%--			<g:hasErrors bean="${ecgDataInstance}">--%>
<%--			<ul class="errors" role="alert">--%>
<%--				<g:eachError bean="${ecgDataInstance}" var="error">--%>
<%--				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>--%>
<%--				</g:eachError>--%>
<%--			</ul>--%>
<%--			</g:hasErrors>--%>
<%--			<g:form url="[resource:ecgDataInstance, action:'save']" >--%>
<%--				<fieldset class="form">--%>
<%--					<g:render template="form"/>--%>
<%--				</fieldset>--%>
<%--				<fieldset class="buttons">--%>
<%--					<g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />--%>
<%--				</fieldset>--%>
<%--			</g:form>--%>
<%--		</div>--%>
<%--	</body>--%>
<%--</html>--%>
