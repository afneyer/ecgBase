<%@ page import="myfirstapp01.EkgData" %>



<div class="fieldcontain ${hasErrors(bean: ekgDataInstance, field: 'identifier', 'error')} required">
	<label for="identifier">
		<g:message code="ekgData.identifier.label" default="Identifier" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="identifier" required="" value="${ekgDataInstance?.identifier}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: ekgDataInstance, field: 'name', 'error')} required">
	<label for="name">
		<g:message code="ekgData.name.label" default="Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="name" required="" value="${ekgDataInstance?.name}"/>

</div>

