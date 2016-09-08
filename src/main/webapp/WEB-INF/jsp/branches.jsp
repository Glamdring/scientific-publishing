<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="includes.jsp"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<c:set var="currentPage" value="home" scope="request" />
<t:template>
    <jsp:body>
        <h1 class="page_head">Science branches</h1>
            <div class="form-group">
                <jsp:include page="includes/branches.jsp" />
            </div>
    </jsp:body>
</t:template>