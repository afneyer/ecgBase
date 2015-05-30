<%@ page import="ecgBase.EcgData"%>



<div
	class="fieldcontain ${hasErrors(bean: ecgDataInstance, field: 'identifier', 'error')} required">
	<label for="identifier"> <g:message
			code="ecgData.identifier.label" default="Identifier" /> <span
		class="required-indicator">*</span>
	</label>
	<g:textField name="identifier" required=""
		value="${ecgDataInstance?.identifier}" />

</div>

<div
	class="fieldcontain ${hasErrors(bean: ecgDataInstance, field: 'fileName', 'error')} required">
	<label for="fileName"> <g:message code="ecgData.fileName.label"
			default="File Name" /> <span class="required-indicator">*</span>
	</label>
	<g:textField name="fileName" required=""
		value="${ecgDataInstance?.fileName}" />

</div>

<div
	class="fieldcontain ${hasErrors(bean: ecgDataInstance, field: 'fileData', 'error')} required">
	<label for="fileData"> <g:message code="ecgData.fileData.label"
			default="File Raw Data" /> <span class="required-indicator">*</span>
	</label> <input type="file" id="fileData" name="fileData" />

</div>

<div
	class="fieldcontain ${hasErrors(bean: ecgDataInstance, field: 'uploadDate', 'error')} required">
	<label for="uploadDate"> <g:message
			code="ecgData.uploadDate.label" default="Upload Date" /> <span
		class="required-indicator">*</span>
	</label>
	<g:datePicker name="uploadDate" precision="day"
		value="${ecgDataInstance?.uploadDate}" />

</div>

