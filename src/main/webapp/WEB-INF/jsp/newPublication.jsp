<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="includes.jsp"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<c:set var="currentPage" value="home" scope="request" />
<t:template>

    <jsp:attribute name="header"></jsp:attribute>

    <jsp:body>
        <jsp:include page="editor.jsp" />
        <jsp:include page="branches.jsp" />
    </jsp:body>
</t:template>