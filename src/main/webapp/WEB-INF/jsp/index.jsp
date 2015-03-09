<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="includes.jsp" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<c:set var="currentPage" value="home" scope="request" />
<t:template>

<jsp:attribute name="header">
</jsp:attribute>

<jsp:body>
        <div class="intro">
            <h2>Welcome to Scienation</h2>
            <p class="lead">This is a dummy homepage</p>
        </div>
</jsp:body>
</t:template>