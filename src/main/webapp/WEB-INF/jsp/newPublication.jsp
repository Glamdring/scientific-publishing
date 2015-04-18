<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="includes.jsp"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<c:set var="currentPage" value="home" scope="request" />
<t:template>

    <jsp:attribute name="header"></jsp:attribute>

    <jsp:body>
        <form role="form" style="width: 400px;">
            <div class="form-group">
                <label for="title">Title</label>
                <input type="text" name="title" id="title" class="form-control" />
            </div>
        
            <div class="form-group">
		        <c:set var="editorSuffix" value="1" />
		        <c:set var="includeResources" value="true" />
		        <label>Abstract</label>
		        <%@include file="editor.jsp" %>
	        </div>
	        
	        <div class="form-group">
		        <c:set var="editorSuffix" value="2" />
		        <c:set var="includeResources" value="false" />
		        <label>Content</label>
		        <%@include file="editor.jsp" %>
	        </div>
	        
	        <div class="form-group">
	            <label>Classification</label>
		        <jsp:include page="branches.jsp" />
		        <script type="text/javascript">
		        $(document).ready(function() {
			        $("input[name='scienceBranch']").change(function(){
			        	var input = $(this);
			            if (input.prop("checked")) {
			                $("#primaryBranch").append($("<option></option>")
			                		.attr("value", input.val())
			                        .text(input.parent().children("label").text()));
			            } else {
			            	$("#primaryBranch").children("option[value='" + input.val() + "']").remove();
			            }
			        });
		        });
		        </script>
	        </div>
	       
            <div class="checkbox"> 
	           <input type="checkbox" id="pushToArxiv" /><label for="pushToArxiv">Push to arxiv</label>
	        </div>
	
            <div class="form-group">
                <label for="language">Language</label>
		        <select name="language" id="language" class="form-control">
		            <c:forEach items="${languages}" var="lang">
		                <option value="${lang.code}">${lang.name}</option>
		            </c:forEach>
		        </select> 
	        </div>
	        
	        <div class="form-group">
	           <label for="primaryBranch">Primary branch</label>
	           <select id="primaryBranch" name="primaryBranch" class="form-control"></select>
            </div>
	                
	        - registered editors
	        - non registered editors (send invites)
	        - follow-up-to (internal|link|doi)
	        - link to content
	        - upload file
	        
	        - invite reviewers
        </form>
    </jsp:body>
</t:template>