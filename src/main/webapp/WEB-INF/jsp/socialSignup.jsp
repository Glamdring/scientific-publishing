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
	    <input type="text" name="email" value="${user.email}" placeholder="Email" <c:if test="${type == 'Persona'}">readonly</c:if>/>
	    <input type="text" name="firstName" placeholder="First name" value="${user.firstName}" />
	    <input type="text" name="middleName" placeholder="Middle name" value="${user.middleName}" />
	    <input type="text" name="lastName" placeholder="Last name" value="${user.lastName}" />
	    <input type="text" name="degree" placeholder="degree" value="${user.degree}" />

        <!-- TODO input branch -->	    
	    <c:if test="${type == 'Persona'}">
	        <input type="checkbox" name="loginAutomatically" id="loginAutomatically" style="margin-top: 0px;"/><label for="loginAutomatically" style="display: inline; margin-left:3px; margin-bottom: 0px; font-size: 13px;">Login automatically if authenticated in Persona?</label>
	    </c:if>
	    <input type="submit" value="Sign up" class="btn" style="margin-top: 4px;"/>
	    <input type="hidden" name="type" value="${type}" />
	</form>
</jsp:body>
</t:template>