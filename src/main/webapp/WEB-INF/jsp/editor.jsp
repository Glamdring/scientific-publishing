<%@page contentType="text/html" pageEncoding="UTF-8"%>

<script src="//cdnjs.cloudflare.com/ajax/libs/pagedown/1.0/Markdown.Converter.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/pagedown/1.0/Markdown.Editor.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/pagedown/1.0/Markdown.Sanitizer.js"></script>

<link rel="stylesheet" href="${staticRoot}/css/editor.css" />

<div class="wmd-panel">
	<div id="wmd-button-bar"></div>
	<textarea class="wmd-input" id="wmd-input"></textarea>
</div>
<div id="wmd-preview" class="wmd-panel wmd-preview"></div>

<br />

<script>
    var converter = new Markdown.getSanitizingConverter();
    var editor = new Markdown.Editor(converter);
    editor.run();
</script>
