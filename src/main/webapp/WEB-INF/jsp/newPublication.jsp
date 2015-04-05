<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="includes.jsp"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<c:set var="currentPage" value="home" scope="request" />
<t:template>

    <jsp:attribute name="header"></jsp:attribute>

    <jsp:body>
        <h3>Title</h3>
        <input type="text" name="title" />
        
        <c:set var="editorSuffix" value="1" />
        <c:set var="includeResources" value="true" />
        <h3>Abstract</h3>
        <%@include file="editor.jsp" %>
        
        <c:set var="editorSuffix" value="2" />
        <c:set var="includeResources" value="false" />
        <h3>Content</h3>
        <%@include file="editor.jsp" %>
        
        <jsp:include page="branches.jsp" />
        <input type="checkbox" id="pushToArxiv"/><label for="publishToArxiv">Push to arxiv</label>

        <select name="language">
            <c:forEach items="${languages}" var="lang">
                <option value="${lang.code}">${lang.name}</option>
            </c:forEach>
        </select> 
        
        - primary branch (of the selected)        
        - registered editors
        - non registered editors (send invites)
        - follow-up-to (internal|link|doi)
        - link to content
        - upload file
        
        - invite reviewers
    </jsp:body>
</t:template>