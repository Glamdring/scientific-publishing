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
	                  <div class="publication_list_entry">
	                      <span class="reviews_total"> <a href="">3 </a> Reviews </span>
	
	                      <h3>
	                          <a href="TODO">A paper title</a>
	                      </h3>
	                      <p class="abstract">
	                           Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum
	                      </p>
	                      <p>
	                          <span class="user">
	                               Published by: <strong><a href="TODO" title="TODO" rel="author">todo</a></strong>
	                          </span> 
	                          <span class="views"><b>1111 </b> views </span> 
	                          
	                          <span class="branches">Biology</a>
	                          </span>
	                      </p>
	                  </div>
	                  <!-- publication #end -->
	                   
	                  <div class="publication_list_entry">
	                      <span class="reviews_total"> <a href="">3 </a> Reviews </span>
	
	                      <h3>
	                          <a href="TODO">A longer paper title</a>
	                      </h3>
	                      <p clas="abstract">
	                           Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum
	                      </p>
	                      <p>
	                          <span class="user">
	                               Published by: <strong><a href="TODO" title="TODO" rel="author">todo</a></strong>
	                          </span> 
	                          <span class="views"><b>1111 </b> views </span> 
	                          
	                          <span class="branches">Biology</a>
	                          </span>
	                      </p>
	                  </div>
	                  <!-- publication #end -->
	                  
	                  <div class="publication_list_entry">
	                      <span class="reviews_total"> <a href="">3 </a> Reviews </span>
	
	                      <h3>
	                          <a href="TODO">A paper title</a>
	                      </h3>
	                      <p clas="abstract">
	                           Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum
	                      </p>
	                      <p>
	                          <span class="user">
	                               Published by: <strong><a href="TODO" title="TODO" rel="author">todo</a></strong>
	                          </span> 
	                          <span class="views"><b>1111 </b> views </span> 
	                          
	                          <span class="branches">Biology</a>
	                          </span>
	                      </p>
	                  </div>
	                  <!-- publication #end -->
	                   
	                  <div class="publication_list_entry">
	                      <span class="reviews_total"> <a href="">3 </a> Reviews </span>
	
	                      <h3>
	                          <a href="TODO">A longer paper title</a>
	                      </h3>
	                      <p clas="abstract">
	                           Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum
	                      </p>
	                      <p>
	                          <span class="user">
	                               Published by: <strong><a href="TODO" title="TODO" rel="author">todo</a></strong>
	                          </span> 
	                          <span class="views"><b>1111 </b> views </span> 
	                          
	                          <span class="branches">Biology</a>
	                          </span>
	                      </p>
	                  </div>
	                  <!-- publication #end -->
	                  
	                  <div class="publication_list_entry">
	                      <span class="reviews_total"> <a href="">3 </a> Reviews </span>
	
	                      <h3>
	                          <a href="TODO">A paper title</a>
	                      </h3>
	                      <p clas="abstract">
	                           Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum
	                      </p>
	                      <p>
	                          <span class="user">
	                               Published by: <strong><a href="TODO" title="TODO" rel="author">todo</a></strong>
	                          </span> 
	                          <span class="views"><b>1111 </b> views </span> 
	                          
	                          <span class="branches">Biology</a>
	                          </span>
	                      </p>
	                  </div>
	                  <!-- publication #end -->
	                   
	                  <div class="publication_list_entry">
	                      <span class="reviews_total"> <a href="">3 </a> Reviews </span>
	
	                      <h3>
	                          <a href="TODO">A longer paper title</a>
	                      </h3>
	                      <p clas="abstract">
	                           Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum
	                      </p>
	                      <p>
	                          <span class="user">
	                               Published by: <strong><a href="TODO" title="TODO" rel="author">todo</a></strong>
	                          </span> 
	                          <span class="views"><b>1111 </b> views </span> 
	                          
	                          <span class="branches">Biology</a>
	                          </span>
	                      </p>
	                  </div>
	                  <!-- publication #end -->
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