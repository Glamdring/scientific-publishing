<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="includes.jsp"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<c:set var="currentPage" value="home" scope="request" />
<t:template>

	<jsp:attribute name="header"></jsp:attribute>

	<jsp:body>
       <div id="content" class="col-md-9">
           <ul id="tab">
               <li class="page_item current_page_item shadow-left shadow-right shadow-top"><a href="${root}/recent">Recent</a></li>
               <li class="page_item"><a href="${root}/popular">Popular</a></li>
           </ul>
           <div class="content_wrapper">
                  <div class="content_over_shadow"></div>
                  <div class="content_body">
                  <c:forEach items="${publications}" var="entry">
                    <h2>${entry.key}</h2>
                    <c:forEach items="${entry.value}" var="publication">
		                  <div class="publication_list_entry">
		                      <span class="reviews_total"> <a href="">${publication.reviews} </a> Reviews </span>
		
		                      <h3>
		                          <a href="TODO">${publication.currentRevision.title}</a>
		                      </h3>
		                      <p class="abstract">
		                           ${publication.currentRevision.publicationAbstract}
		                      </p>
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
		                          
		                          <span class="branches">
		                              <c:forEach items="${publication.branches}" var="branch">
		                                  <a href="TODO">${branch.name}</a>
	                                  </c:forEach>
		                          </span>
		                      </p>
		                  </div></c:forEach>
	                  <!-- publication #end -->
	                  </c:forEach>
	                  </div>
                      <div class="pagination">
	                      <div class='Navi'>
	                          <strong class='on'>1</strong> 
	                          <a href="TODO">2</a>
	                          <a href="TODO"><strong>»</strong></a>
	                      </div>
	                  </div>
                  </div>
           </div>
       </div>
       <!-- content #end -->

       <div id="sidebar" class="sidebar_spacer col-md-3">

           <a href="${root}/new" class="submitpublication_button">Submit a publication</a>

           <div class="widget login_widget">
               <h3>Login</h3>
               <!-- class="btn_input_highlight" TODO !--> 
           </div>

           <div class="widget popular">
               <h3 class="hl">
                   <span>Stuff</span>
               </h3>
               <ul>
                   <li>Stuff...</li>
               </ul>
           </div>
    </div>
    <!-- sidebar #end -->
</jsp:body>
</t:template>