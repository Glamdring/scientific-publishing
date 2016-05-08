<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="includes.jsp"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<c:set var="currentPage" value="home" scope="request" />
<t:template>

	<jsp:attribute name="header">
        <script type="text/javascript" src="${staticRoot}/js/jQuery.highligther.js"></script>
        <script type="text/javascript" src="${staticRoot}/js/pdfobject.min.js"></script>
        <script type="text/javascript">
           $(document).ready(function() {
	           $("#clarity, #novelty, #methods, #quality, #significance, #dataAnalysis").slider({
	        	   max: 5,
	        	   change: function(event, ui) {
	        		   $(event.target).parent().find(".val").text($(this).slider("value"));   
	        	   }
	           });
	           
	           <c:if test="${publication.currentRevision.contentSource == 'EXTERNAL'}">
	              <c:if test="${publication.currentRevision.contentLink.contains('pdf')}">
                     PDFObject.embed("${publication.currentRevision.contentLink}", "#pdfPreview");
                  </c:if>
	           </c:if>
           });
           
           function postPreliminaryReview(acceptable) {
        	   $.post("${root}/peerReview/submitPreliminary", {acceptable: acceptable, publicationUri: '${publication.uri}'}, function() {
        		   alert("Your submission is successful");
        	   });
           }
        </script>
    </jsp:attribute>
    
	<jsp:body>
	
    <article class="rel anonymous" itemtype="http://schema.org/Question" itemscope="">
	   <section class="publication">
			<header>
		        <span class="branches">
                         <c:forEach items="${publication.branches}" var="branch">
                             <a class="light-gray hover-blue" href="TODO">${branch.name}</a>
                         </c:forEach>
                      </span>
				<time datetime="2015-06-27T04:59:20.982Z" content="2015-06-27T04:59:20.982Z"
						itemprop="datePublished" class="">3 hrs</time></span>
				<h3>
					<a rel="canonical"
						href="TODO"
						class="link-hover tooltip-hover" itemprop="name">${publication.currentRevision.title}</a>
				</h3>
			</header>
			<div>
				<div>
					<p class="expandable" itemprop="text">
					   ${publication.currentRevision.publicationAbstract} 
					</p>
					<aside class="meta-action dib">
						<div class="tooltip-hover dib rel">
							<span class="appreciate-button rel icon-bell"> <a href="">${publication.reviews} </a> Reviews </span>
							<p>
                                 <span class="user">
                                      Published by:
                                      <c:forEach items="${publication.authors}" var="author"> 
                                         <strong><a href="TODO" title="TODO" rel="author">${author.firstName} ${author.lastName}</a></strong>
                                      </c:forEach>
                                      <c:forEach items="${publication.nonRegisteredAuthors}" var="author"> 
                                         <strong><a href="TODO" title="TODO" rel="author">${author}</a></strong>
                                      </c:forEach>
                                 </span> 
                                 <span class="views"><b>1111 </b> views </span> 
                             </p>
						</div>
					</aside>
				</div>

                <!-- Limit the text to a fixed-height portion -->				
				<c:if test="${publication.currentRevision.contentSource == 'EXTERNAL'}">
				    <div id="linkHolder">
				        <!-- TODO icon -->
				        <a href="${publication.currentRevision.contentLink}" target="_blank" rel="noopener noreferer" class="link-hover">Read publication</a>
				    </div>
				    <div id="pdfPreview"></div>
				    <c:if test="!${publication.currentRevision.contentLink.contains('pdf')}">
				        <iframe src="${publication.currentRevision.contentLink}"></iframe>
				    </c:if>
				</c:if>
			</div>
		  </section>
	  </article>
		 
	  <c:if test="${userLoggedIn}">
	      <c:if test="${ownPreliminaryReview == null}">
			  <section>
	              <h3>Quick assessment</h3>
	              Does this publication meet the basic criteria for scientific work?
	              <input type="hidden" name="publicationUri" id="${param.uri}" />
	              <input type="button" value="Yes" onclick="postPreliminaryReview(true);"/>
	              <input type="button" value="No" onclick="postPreliminaryReview(false)"/>
	          </section>
          </c:if>
		  
		  <section>
            <form>
               <!-- TODO load peer review from ${ownPeerReview} -->
	            <h3>Write a peer review</h3>
	
	            <div>
		            <label id="clarityLabel" class="review-sliderLabel">Clarity of background and rationale</label>
		             <span class="val">0</span>
    		             <div id="clarity" class="review-slider"></div>
	            </div>
	              
	            <div>
		            <label id="significanceLabel" class="review-sliderLabel">Significance to the field</label>
		            <span class="val">0</span>
		            <div id="significance" class="review-slider"></div>
	            </div>
	            
	            <div>
		            <label id="methodsLabel" class="review-sliderLabel">Study design and methods</label>
		            <span class="val">0</span>
		            <div id="methods" class="review-slider"></div>
	            </div>
	             
	            <div>
		            <label id="noveltyLabel" class="review-sliderLabel">Novelty of conclusions</label>
		            <span class="val">0</span>
		            <div id="novelty" class="review-slider"></div>
	            </div>
	            
	            <div>  
		            <label id="qualityLabel" class="review-sliderLabel">Quality of presentation</label>
		            <span class="val">0</span>
		            <div id="quality" class="review-slider"></div>
	            </div>
	              
	            <div>
		            <label id="dataAnalysisLabel" class="review-sliderLabel">Quality of data analysis</label>
		            <span class="val">0</span>
		            <div id="dataAnalysis" class="review-slider"></div>
	            </div>
	              
	            <div class="form-group" id="reviewContent">
	                <c:set var="editorSuffix" value="1" />
	                <c:set var="includeResources" value="true" />
	                <label>Review content</label>
	                <%@include file="editor.jsp"%>
	                <input type="hidden" name="content" id="content" />
	            </div>
	              
	            <input type="checkbox" id="conflictOfInterestsDeclaration"><label for="conflictOfInterestsDeclaration">I declare that I am not in a conflict of interests (e.g. reviewing a friend's paper)</label>
	            
                <input type="submit" value="Submit peer review" onclick="$('content').val($('#wmd-input1').val());"/>
             </form>
		  </section>
		  - invite reviewers
	  </c:if>
	  
	  <c:if test="${!userLoggedIn}">
	      <a href="${root}/signin">Want to leave a review? Sign up</a>
	  </c:if>

       <!-- content #end -->
</jsp:body>
</t:template>