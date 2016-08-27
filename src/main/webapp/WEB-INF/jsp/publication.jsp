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
        	   var converter = new showdown.Converter();
        	   $(".review-content-output").each(function(idx, elem) {
        		   var html = converter.makeHtml($(elem).html())
        		   $(elem).html(html);
        	   })
        	   
	           $("#clarity, #novelty, #methods, #quality, #significance, #dataAnalysis").slider({
	        	   max: 5,
	        	   change: function(event, ui) {
	        		   var value = $(this).slider("value");
	        		   $(event.target).parent().find(".val").text(value);
	        		   $(event.target).parent().find(".slider-value").val(value);
	        	   }
	           });
	           $("#clarity, #novelty, #methods, #quality, #significance, #dataAnalysis").each(function(idx, elem) {
	        	   var initialValue = $(elem).parent().find(".slider-value").val();
	        	   $(elem).slider("value", initialValue);
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
	   <article class="clearfix">
                  <h2><a href="${root}/publication?uri=${publication.uri}">${publication.currentRevision.title}</a></h2>
                  <div class="post-date">
                      <time datetime="${publication.created}" class="timeago"><javatime:format value="${publication.created}" style="MM" /></time>
                       | Published by:
                       <c:forEach items="${publication.authors}" var="author"> 
                          <strong><a href="TODO" title="TODO" rel="author">${author.firstName} ${author.lastName}</a></strong>
                       </c:forEach>
                       <c:forEach items="${publication.nonRegisteredAuthors}" var="author"> 
                          <strong><a href="TODO" title="TODO" rel="author">${author}</a></strong>
                       </c:forEach>
                       
                       <a href="">${publication.reviews} review(s)</a></span>
                  </div>
                  <span class="branches">
                     <c:forEach items="${publication.branches}" var="branch">
                         [<a href="TODO">${branch.name}</a>]
                     </c:forEach>
                  </span>
                        
                  <p>${publication.currentRevision.publicationAbstract}
                  
                  <!-- Limit the text to a fixed-height portion -->             
                  <c:if test="${publication.currentRevision.contentSource == 'EXTERNAL'}">
                    <div id="linkHolder">
                        <!-- TODO icon -->
                        <a href="${publication.currentRevision.contentLink}" target="_blank" rel="noopener noreferer">Read publication</a>
                    </div>
                    <div id="pdfPreview"></div>
                    <c:if test="!${publication.currentRevision.contentLink.contains('pdf')}">
                        <iframe src="${publication.currentRevision.contentLink}"></iframe>
                    </c:if>
                  </c:if>
                  
                  <c:if test="${publication.currentRevision.contentSource != 'EXTERNAL'}">
                    ${publication.currentRevision.content}
                  </c:if>
                  
              </article>
	  </article>
		 
	  <c:if test="${userLoggedIn}">
	      <c:if test="${ownPreliminaryReview == null}">
			  <section>
	              <h3>Quick assessment</h3>
	              Does this publication meet the basic criteria for scientific work?
	              <input type="hidden" name="publicationUri" id="publicationUri" value="${param.uri}" />
	              <input type="button" class="btn" value="Yes" onclick="postPreliminaryReview(true);"/>
	              <input type="button" class="btn" value="No" onclick="postPreliminaryReview(false)"/>
	          </section>
          </c:if>
      </c:if>
      
      <section>
        <c:if test="${peerReviews.isEmpty()}">
            No peer reviews yet. Be the first one to provide an through assessment of this publication.
        </c:if>
        <c:if test="${!peerReviews.isEmpty()}">
            ${peerReviews.size()} peer reviews
        </c:if>
        <c:forEach items="${peerReviews}" var="peerReview">
          <article class="media">
            <a class="pull-left avatar" href="#">
                <img class="media-object img-circle" width="40" height="40" alt="" src="${peerReview.reviewer.smallPhotoUri}">
            </a>
            <div class="media-body">
              <h4 class="media-heading">
                <a href="">${peerReview.reviewer.displayName}</a>
                <span> ..time | <!-- TODO vote --></span>
              </h4>
              <p class="review-content-output">${peerReview.content}</p>
            </div>
          </article>
        </c:forEach>
      </section>
      
	  <c:if test="${userLoggedIn}">
		  <section>
            <form action="${root}/peerReview/submit" method="POST" id="peerReviewForm">
                <input type="hidden" name="publicationUri" value="${param.uri}" />
	            <h3>${ownPeerReview == null ? 'Write a' : 'Edit your'} peer review</h3>
	
	            <div>
		            <label id="clarityLabel" class="review-sliderLabel">Clarity of background and rationale</label>
		            <span class="val">${ownPeerReview != null ? ownPeerReview.clarityOfBackground : 0}</span>
  		            <div id="clarity" class="review-slider"></div>
                    <input type="hidden" name="clarityOfBackground" class="slider-value" value="${ownPeerReview != null ? ownPeerReview.clarityOfBackground : 0}" />
	            </div>
	              
	            <div>
		            <label id="significanceLabel" class="review-sliderLabel">Significance to the field</label>
		            <span class="val">${ownPeerReview != null ? ownPeerReview.significance : 0}</span>
		            <div id="significance" class="review-slider"></div>
                    <input type="hidden" name="significance" class="slider-value" value="${ownPeerReview != null ? ownPeerReview.significance : 0}" />
	            </div>
	            
	            <div>
		            <label id="methodsLabel" class="review-sliderLabel">Study design and methods</label>
		            <span class="val">${ownPeerReview != null ? ownPeerReview.studyDesignAndMethods : 0}</span>
		            <div id="methods" class="review-slider"></div>
                    <input type="hidden" name="studyDesignAndMethods" class="slider-value" value="${ownPeerReview != null ? ownPeerReview.studyDesignAndMethods : 0}"/>
	            </div>
	             
	            <div>
		            <label id="noveltyLabel" class="review-sliderLabel">Novelty of conclusions</label>
		            <span class="val">${ownPeerReview != null ? ownPeerReview.noveltyOfConclusions : 0}</span>
		            <div id="novelty" class="review-slider"></div>
                    <input type="hidden" name="noveltyOfConclusions" class="slider-value" value="${ownPeerReview != null ? ownPeerReview.noveltyOfConclusions : 0}" />
	            </div>
	            
	            <div>  
		            <label id="qualityLabel" class="review-sliderLabel">Quality of presentation</label>
		            <span class="val">${ownPeerReview != null ? ownPeerReview.qualityOfPresentation : 0}</span>
		            <div id="quality" class="review-slider"></div>
                    <input type="hidden" name="qualityOfPresentation" class="slider-value" value="${ownPeerReview != null ? ownPeerReview.qualityOfPresentation : 0}" />
	            </div>
	              
	            <div>
		            <label id="dataAnalysisLabel" class="review-sliderLabel">Quality of data analysis</label>
		            <span class="val">${ownPeerReview != null ? ownPeerReview.dataAnalysis : 0}</span>
		            <div id="dataAnalysis" class="review-slider"></div>
                    <input type="hidden" name="dataAnalysis" class="slider-value" value="${ownPeerReview != null ? ownPeerReview.dataAnalysis : 0}" />
	            </div>
	              
	            <div class="form-group" id="reviewContentPanel">
	                <c:set var="editorSuffix" value="1" />
	                <c:set var="includeResources" value="true" />
                    <c:if test="${ownPeerReview != null}">
                        <c:set var="editorInitialContent" value="${ownPeerReview.content}" />
                    </c:if>
	                <label>Review content</label>
	                <%@include file="editor.jsp"%>
	                <input type="hidden" name="content" id="reviewContent" />
	            </div>
	              
                 <c:if test="${ownPeerReview == null}">
	               <input type="checkbox" id="conflictOfInterestsDeclaration"><label for="conflictOfInterestsDeclaration">I declare that I am not in a conflict of interests (e.g. reviewing a friend's paper)</label>
                 </c:if>
	            
                <input type="submit" value="Submit peer review" class="btn btn-block" onclick="$('#reviewContent').val($('#wmd-input1').val());"/>
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