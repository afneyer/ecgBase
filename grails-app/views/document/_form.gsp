<%@ page import="ecgBase.Document" %>



<div class="fieldcontain ${hasErrors(bean: documentInstance, field: 'filename', 'error')} required">
	<label for="fileName">
		<g:message code="document.filename.label" default="Filename" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="fileName" required="" value="${documentInstance?.fileName}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: documentInstance, field: 'fullPath', 'error')} required">
	<label for="fullPath">
		<g:message code="document.fullPath.label" default="Full Path" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="fullPath" required="" value="${documentInstance?.fullPath}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: documentInstance, field: 'uploadDate', 'error')} required">
	<label for="uploadDate">
		<g:message code="document.uploadDate.label" default="Upload Date" />
		<span class="required-indicator">*</span>
	</label>
	<g:datePicker name="uploadDate" precision="day"  value="${documentInstance?.uploadDate}"  />

</div>

