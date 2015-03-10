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
	    <table>
	    <tr><td>Email: </td><td><input type="text" name="email" value="${user.email}" <c:if test="${type == 'Persona'}">readonly</c:if>/></td></tr>
	    <tr><td>Full name: </td><td><input type="text" name="names" value="${user.names}" /></td></tr>
	    <tr><td colspan="2"><input type="checkbox" name="receiveDailyDigest" id="receiveDailyDigest" style="margin-top: 0px;" checked="checked" /><label for="receiveDailyDigest" style="display: inline; margin-left:3px; margin-bottom: 0px; font-size: 13px;">Receive daily digest?</label></td></tr>
	    <c:if test="${type == 'Persona'}">
	        <tr><td colspan="2"><input type="checkbox" name="loginAutomatically" id="loginAutomatically" style="margin-top: 0px;"/><label for="loginAutomatically" style="display: inline; margin-left:3px; margin-bottom: 0px; font-size: 13px;">Login automatically if authenticated in Persona?</label></td></tr>
	    </c:if>
	    </table>
	    <input type="submit" value="Sign up" class="btn" style="margin-top: 4px;"/>
	    <input type="hidden" name="type" value="${type}" />
	</form>
</jsp:body>
</t:template>