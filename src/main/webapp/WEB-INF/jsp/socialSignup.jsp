<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="includes.jsp" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<c:set var="currentPage" value="home" scope="request" />
<t:template>

<jsp:attribute name="header">
</jsp:attribute>

<jsp:body>
	Please complete your registration:<br />
	<form action="<c:url value="/social/completeRegistration" />" method="POST">
      <div class="row">
        <label for="email">Email</label>
	    <input type="text" name="email" value="${user.email}" placeholder="Email" <c:if test="${type == 'Persona'}">readonly</c:if> class="form-control input-lg"/>
      
        <div class="col-md-5" style="padding-left: 0px;">
          <label for="firstName">First name</label>
  	      <input type="text" name="firstName" placeholder="First name" value="${user.firstName}" class="form-control input-lg" />
        </div>
        
        <div class="col-md-5" style="padding-left: 0px;">
          <label for="lastName">Last name</label>
  	      <input type="text" name="lastName" placeholder="Last name" value="${user.lastName}" class="form-control input-lg" />
        </div>
        
        <div class="col-md-2" style="padding-left: 0px;">
          <label for="degree">Degree</label>
  	      <input type="text" name="degree" placeholder="Degree" value="${user.degree}" class="form-control input-lg" />
        </div>

        <label for="organization">Organization</label>
        <input type="text" name="degree" placeholder="Degree" value="${user.degree}" class="form-control input-lg" />
        
        <!-- TODO input branch -->	    
	    <c:if test="${type == 'Persona'}">
	        <input type="checkbox" name="loginAutomatically" id="loginAutomatically" style="margin-top: 0px;"/><label for="loginAutomatically" style="display: inline; margin-left:3px; margin-bottom: 0px; font-size: 13px;">Login automatically if authenticated in Persona?</label>
	    </c:if>
      
        <label for="orcid">ORCID</label>
        <input type="text" name="orcid" placeholder="ORCID" class="form-control input-lg" />
        <a style="float: left;" href="http://orcid.org/" target="_blank">Don't have ORCID? You can get one</a>
        
	    <input type="submit" value="Sign up" class="btn btn-block" style="margin-top: 4px;" />
	    <input type="hidden" name="type" value="${type}" />
      </div>
	</form>
</jsp:body>
</t:template>