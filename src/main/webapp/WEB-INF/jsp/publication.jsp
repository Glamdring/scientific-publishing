<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="includes.jsp"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<c:set var="currentPage" value="home" scope="request" />
<t:template>

	<jsp:attribute name="header">
        <script type="text/javascript">
           $(document).ready(function() {
	           $("#clarity, #novelty, #methods, #quality, #importance, #dataAnalysis").slider({
	        	   max: 5
	           });
           });
           
           function postPreliminaryReview(acceptable) {
        	   $.post("${root}/peerReview/submitPreliminary", {acceptable: acceptable}, function() {
        		   alert("Your submission is successful");
        	   });
           }
        </script>
    </jsp:attribute>
    
	<jsp:body>
	
    <article class="rel anonymous" itemtype="http://schema.org/Question" itemscope="">
	   <section class="question">
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
					<p class="expandable" itemprop="text">${publication.currentRevision.publicationAbstract} 
					     <span class="read-more light-gray">Read more</span>
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
			</div>
		  </section>
		  </article>
		 
		 <!-- Limit the text to a fixed-height portion -->
		 
		  <section>
              <h3>Quick assessment</h3>
              Does this publication meet the basic criteria for scientific work?
              <input type="hidden" name="publicationUri" id="${param.uri}" />
              <input type="button" value="Yes" onclick="postPreliminaryReview(true);"/>
              <input type="button" value="No" onclick="postPreliminaryReview(false)"/>
          </section>
		  
		  <section>
		      <h3>Write a peer review</h3>

              <label id="clarityLabel">Clarity of background and rationale</label>
              <div id="clarity"></div>
              
              <label id="importanceLabel">Field importance</label>
              <div id="importance"></div>
              
              <label id="methodsLabel">Study design and methods</label>
              <div id="methods"></div>
              
              <label id="noveltyLabel">Novelty of conclusions</label>
              <div id="novelty"></div>
              
              <label id="qualityLabel">Quality of presentation</label>
              <div id="quality"></div>
              
              <label id="dataAnalysisLabel">Quality of data analysis</label>
              <div id="dataAnalysis"></div>
              
              <textarea id="reviewContent" name="reviewContent"></textarea>
              
              <input type="checkbox" id="conflictOfInterestDeclaration"><label for="conflictOfInterestDeclaration">I declare that I am not in a conflict of interests (e.g. reviewing a friend's paper)</label>
                    
		  </section>
		  - invite reviewers
		  
       <!-- content #end -->
</jsp:body>
</t:template>