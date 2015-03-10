<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="includes.jsp" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<c:set var="currentPage" value="signup" scope="request" />
<t:template>

<jsp:attribute name="header">
</jsp:attribute>

<jsp:body>
	<c:if test="${!userLoggedIn}">
	    <a href="javascript:void(0);" id="personaSignin"><img src="${staticRoot}/img/persona-connect.png" /></a>
	    <a href="${root}/signin/twitter?home=true"><img src="${staticRoot}/img/twitter-connect.png" /></a>
	    <a href="${root}/signin/facebook?home=true&scope=email"><img src="${staticRoot}/img/facebook-connect.png" /></a>
	</c:if>
</jsp:body>
</t:template>