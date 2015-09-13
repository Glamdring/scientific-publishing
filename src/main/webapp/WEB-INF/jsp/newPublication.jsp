<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="includes.jsp"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<c:set var="currentPage" value="home" scope="request" />
<t:template>
    <jsp:attribute name="header">
        <script type="text/javascript" src="${staticRoot}/js/jquery.fcbkcomplete.min.js"></script>
        <link href="${staticRoot}/css/autocomplete.css" rel="stylesheet" />
        
        <script src="${staticRoot}/js/jquery.iframe.transport.js"></script>
        <script src="${staticRoot}/js/jquery.fileupload.js"></script>
        
        <script type="text/javascript">
	        $(function () {
			    $('#fileimport').fileupload({
			        dataType: 'json',
			        done: function (e, data) {
		                $("#wmd-input2").val(data.result.content);
			        }
			    });
			});
			
            $(document).ready(function() {
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
                
                $("input[name='contentInputType']").change(function() {
                    var selected = $('input[name=contentInputType]:checked').val();
                    if (selected == "direct") {
                        $("#contentEditor,#fileImport").show();
                        $("#externalContent,#fileUpload").hide();
                    } else if (selected == "link") {
                        $("#externalContent").show();
                        $("#contentEditor,#fileImport,#fileUpload").hide();
                    } else if (selected == "upload") {
                        $("#fileUpload").show();
                        $("#contentEditor,#fileImport,#externalContent").hide();
                    }
                });
                
                 $(document).ready(function () {
			        // Save draft every 30 seconds
			        window.setInterval(saveDraft, 30000);
		          });
			
			      function saveDraft() {
			         fillAuthors();
			         var params = $("#submissionForm").serialize();
			         $.post("${root}/publication/saveDraft", params)
			      }
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
            
            function handlePrimaryBranchOptions() {
                $("#submissionForm input[name='scienceBranch']").change(function(){
                    var input = $(this);
                    if (input.prop("checked")) {
                        $("#primaryBranch").append($("<option></option>")
                                .attr("value", input.val())
                                .text(input.parent().children("label").text()));
                    } else {
                        $("#primaryBranch").children("option[value='" + input.val() + "']").remove();
                    }
                });
            } 
        </script>
    </jsp:attribute>

    <jsp:body>
        <h1 class="page_head">Publication</h1>
        
        <form role="form" id="submissionForm" class="post-form" 
            style="width: 400px;" onsubmit="fillAuthors();" action="${root}/publication/submit" method="POST">
            <div class="form-group">
                <label for="title">Title</label>
                <input type="text" name="title" id="title" />
            </div>
        
        
            <div class="form-group" id="abstractEditor">
		        <c:set var="editorSuffix" value="1" />
		        <c:set var="includeResources" value="true" />
		        <label>Abstract</label>
		        <%@include file="editor.jsp" %>
	        </div>
	        
	         <div class="form-group">
                <label>Content input type</label>

                <div class="radio">
                    <input type="radio" id="contentInputTypeDirect" name="contentInputType" value="direct" checked>
                    <label for="contentInputTypeDirect">Direct input</label>
                </div>
                
                <div class="radio">
                    <input type="radio" id="contentInputTypeUpload" name="contentInputType" value="upload">
                    <label for="contentInputTypeUpload">Upload</label>
                </div>
                
                <div class="radio">
                    <input type="radio" id="contentInputTypeLink" name="contentInputType" value="link">
                    <label for="contentInputTypeLink">External Link</label>
                </div>
            </div>
            
            <div id="fileImport">
                <label for="fileimport">You can import content from a file:</label>
                <input id="fileimport" type="file" name="file" data-url="${root}/publication/importFile"><br />
            </div>
            
	        <div class="form-group" id="contentEditor">
		        <c:set var="editorSuffix" value="2" />
		        <c:set var="includeResources" value="false" />
		        <label>Content</label>
		        <%@include file="editor.jsp" %>
	        </div>
	        
            <div class="form-group" id="externalContent" style="display: none;">
                <label>Link to external content</label>
                <input type="text" name="contentLink" placeholder="http://" />
            </div>
            
            <div id="fileUpload" style="display: none;">
                <label for="fileupload">Select file to upload</label>
                <input id="fileupload" type="file" name="fileUpload" data-url="${root}/publication/uploadFile"><br />
            </div>
	        
	        <div class="form-group">
	            <label>Classification</label>
		        <jsp:include page="branches.jsp" />
		        <script type="text/javascript">
		          $(document).ready(function() {
		              // this snippet is here, because if it's put in the header, the branches are not yet loaded
		              handlePrimaryBranchOptions();
		          });
		        </script>
	        </div>
	       
	        <div class="form-group">
               <label for="primaryBranch">Primary branch</label>
               <select id="primaryBranch" name="primaryBranch"></select>
            </div>
            
            <div class="checkbox"> 
               <div class="checkbox">
	               <input type="checkbox" id="pushToArxiv" /><label for="pushToArxiv">Push to arxiv</label>
               </div>
	        </div>
	
            <div class="form-group">
                <label for="language">Language</label>
		        <select name="language" id="language">
		            <c:forEach items="${languages}" var="lang">
		                <option value="${lang.code}">${lang.name}</option>
		            </c:forEach>
		        </select> 
	        </div>
	        
	        <div class="form-group">
				<label for="authors">Authors</label>
				<input type="text" name="authors" id="authors" />
				<input type="hidden" name="authorIds" id="authorIds" value="${userContext.user.id}" />
                <input type="hidden" name="nonRegisteredAuthors" id="nonRegisteredAuthors" />
	        </div>
	        
	        <div class="form-group">
                <label>This publication is a follow-up to:</label>

                <div class="radio">
                    <input type="radio" id="followUpTypeNone" name="followUpType" value="">
                    <label for="followUpTypeLink">Not a follow-up</label><br />
                </div>
                                
                <div class="radio">
	                <input type="radio" id="followUpTypeInternal" name="followUpType" value="internal">
	                <label for="followUpTypeInternal">Scienation publication</label>
                </div>
                
                <div class="radio">
	                <input type="radio" id="followUpTypeLink" name="followUpType" value="link">
	                <label for="followUpTypeLink">External publication (link)</label><br />
	            </div>
	            
	            <div class="radio">
	                <input type="radio" id="followUpTypeDoi" name="followUpType" value="doi">
	                <label for="followUpTypeDoi">External publication (DOI)</label><br />
                </div>
                	                
                <input type="text" id="followUpToInternal" name="followUpToInternal" style="display: none;" />
                <input type="hidden" id="followUpTo" name="followUpTo" />
                <input type="text" id="followUpToLink" name="followUpToLink" style="display: none;"  placeholder="http://" />
                <input type="text" id="followUpToDoi" name="followUpToDoi" style="display: none;" />
            </div>
            
            <div class="form-group">
                <input type="submit" value="Submit">
            </div>
            
	        - send invites to non registered editors
        </form>
    </jsp:body>
</t:template>