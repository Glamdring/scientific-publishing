<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="includes.jsp"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<c:set var="currentPage" value="home" scope="request" />
<t:template>
    <jsp:attribute name="header">
        <script type="text/javascript" src="${staticRoot}/js/jquery.fcbkcomplete.min.js"></script>
        <link href="${staticRoot}/css/autocomplete.css" rel="stylesheet" />
        
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
                    
                    $("#authors").fcbkcomplete({
                        json_url: "${root}/users/autocompleteList",
                        cache: false,
                        height: 10,
                        filter_case: false,
                        filter_hide: true,
                        newel: true,
                        addontab: true,
                        addoncomma: true,
                        addonblur: true,
                        maxitems: 5,
                        bricket: false,
                        width: "auto"
                    });
                    $("#authors").trigger("addItem", {title:'${userContext.user.displayName}, ${userContext.user.degree}', value: 'id:${userContext.user.id}'});
                    
                    //$("#followUpToInternal").autocomplete();
                    $("input[name='followUpType']").change(function() {
                        var selected = $('input[name=followUpType]:checked').val();
                        if (selected == "internal") {
                            $("#followUpToInternal").show();
                            $("#followUpToLink,#followUpToDoi").hide();
                        } else if (selected == "link") {
                            $("#followUpToLink").show();
                            $("#followUpToInternal,#followUpToDoi").hide();
                        } else if (selected == "doi") {
                            $("#followUpToDoi").show();
                            $("#followUpToLink,#followUpToInternal").hide();
                        }
                        
                    });
                });
                
                function fillAuthors() {
                	var authors = $("#authors :selected");
                    var idDelim = "";
                    var nameDelim = "";
                    var authorIds = "";
                    var nonRegisteredAuthors = "";
                    authors.each(function(idx, elem) {
                    	var value = $(elem).val();
                    	if (value.indexOf("id:") === 0) {
                    		authorIds += idDelim + value.replace("id:", "");
                            idDelim = ",";
                    	} else {
                    		nonRegisteredUsers += nameDelim + value;
                            nameDelim = ",";
                    	}
                    });
                    $("#authorIds").val(authorIds);
                    $("#nonRegisteredAuthors").val(nonRegisteredAuthors);
                }
          </script>
    </jsp:attribute>

    <jsp:body>
        <form role="form" style="width: 400px;" onsubmit="fillAuthors();">
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
	        </div>
	       
	        <div class="form-group">
               <label for="primaryBranch">Primary branch</label>
               <select id="primaryBranch" name="primaryBranch" class="form-control"></select>
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
				<label for="authors">Authors</label>
				<input type="text" name="authors" id="authors" class="form-control" />
				<input type="hidden" name="authorIds" id="authorIds" value="${userContext.user.id}" />
                <input type="hidden" name="nonRegisteredAuthors" id="nonRegisteredAuthors" />
	        </div>
	        
	        <div class="form-group">
                <label>This publication is a follow-up to:</label>
                <br />
                <input type="radio" id="followUpTypeInternal" name="followUpType" value="internal" checked>
                <label for="followUpTypeInternal">Scienation publication</label><br />
                
                <input type="radio" id="followUpTypeLink" name="followUpType" value="link">
                <label for="followUpTypeLink">External publication (link)</label><br />
                
                <input type="radio" id="followUpTypeDoi" name="followUpType" value="doi">
                <label for="followUpTypeDoi">External publication (DOI)</label><br />
                
                <input type="text" id="followUpToInternal" name="followUpToInternal" class="form-control" />
                <input type="hidden" id="followUpTo" name="followUpTo" />
                <input type="text" id="followUpToLink" name="followUpToLink" style="display: none;" class="form-control" />
                <input type="text" id="followUpToDoi" name="followUpToDoi" style="display: none;" class="form-control" />
            </div>
            
	        <input type="text" />
	        
	        - non registered editors (send invites)
	        - follow-up-to (internal|link|doi)
	        - link to content
	        - upload file
	        
	        - invite reviewers
        </form>
    </jsp:body>
</t:template>