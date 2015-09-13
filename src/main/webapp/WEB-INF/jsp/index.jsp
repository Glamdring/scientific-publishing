<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="includes.jsp"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<c:set var="currentPage" value="home" scope="request" />
<t:template>

	<jsp:attribute name="header"></jsp:attribute>

	<jsp:body>
	
	<c:forEach items="${publications}" var="entry">
         <h2>${entry.key}</h2>
         <ul>
         <c:forEach items="${entry.value}" var="publication">
          <li>
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
								href="${root}/publication?uri=${publication.uri}"
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
				<li>
	          </c:forEach>
	          </ul>
	      </c:forEach>
       <!-- content #end -->
</jsp:body>
</t:template>