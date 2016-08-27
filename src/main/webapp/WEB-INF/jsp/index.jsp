<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="includes.jsp"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<c:set var="currentPage" value="home" scope="request" />
<t:template>

	<jsp:attribute name="header"></jsp:attribute>

	<jsp:body>
	
	<c:forEach items="${publications}" var="entry">
         <h2>${entry.key}</h2>
         <section>
         <c:forEach items="${entry.value}" var="publication">
              <article class="clearfix">
                  <h2><a href="${root}/publication?uri=${publication.uri}">${publication.currentRevision.title}</a></h2>
                  <div class="post-date">
                      <time datetime="2015-06-27T04:59:20.982Z" content="2015-06-27T04:59:20.982Z"
                                itemprop="datePublished" class="">3 hrs</time>
                       | Published by:
                       <c:forEach items="${publication.authors}" var="author"> 
                          <strong><a href="TODO" title="TODO" rel="author">${author.firstName} ${author.lastName}</a></strong>
                       </c:forEach>
                       <c:forEach items="${publication.nonRegisteredAuthors}" var="author"> 
                          <strong><a href="TODO" title="TODO" rel="author">${author}</a></strong>
                       </c:forEach>
                       
                       <a href="">${publication.reviews} reviews</a></span>
                  </div>
                  <span class="branches">
                     <c:forEach items="${publication.branches}" var="branch">
                         <a class="light-gray hover-blue" href="TODO">${branch.name}</a>
                     </c:forEach>
                  </span>
                        
                  <p>${publication.currentRevision.publicationAbstract} <a class="" href="${root}/publication?uri=${publication.uri}">Read more</a>
                  </p>
                  
              </article>
	      </c:forEach>
        </section>
	 </c:forEach>
     <!-- content #end -->
</jsp:body>
</t:template>