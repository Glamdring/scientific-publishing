<%@page contentType="text/html" pageEncoding="UTF-8"%>

<c:if test="${includeResources}">
	<script src="//cdnjs.cloudflare.com/ajax/libs/pagedown/1.0/Markdown.Converter.js"></script>
	<script src="//cdnjs.cloudflare.com/ajax/libs/pagedown/1.0/Markdown.Editor.js"></script>
	<script src="//cdnjs.cloudflare.com/ajax/libs/pagedown/1.0/Markdown.Sanitizer.js"></script>
	
	<link rel="stylesheet" href="${staticRoot}/css/editor.css" />
</c:if>

<div class="wmd-panel">
	<div id="wmd-button-bar${editorSuffix}"></div>
	<textarea class="form-control wmd-input ${editorClass}" id="wmd-input${editorSuffix}"></textarea>
</div>
<div id="wmd-preview${editorSuffix}" class="wmd-panel wmd-preview"></div>

<br />

<script>
    var converter = new Markdown.getSanitizingConverter();
    var editor = new Markdown.Editor(converter, "${editorSuffix}");
    editor.run();
</script>
