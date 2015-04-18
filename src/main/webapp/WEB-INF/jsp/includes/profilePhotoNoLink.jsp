<%@ page pageEncoding="UTF-8" %>
<%@ include file="../includes.jsp" %>

<c:choose>
    <c:when test="${pictureType == 'small' and !empty user.smallPhotoUri}">
        <c:set var="src" value="${user.smallPhotoUri}" />
    </c:when>
    <c:when test="${pictureType == 'large' and !empty user.largePhotoUri}">
        <c:set var="src" value="${user.smallPhotoUri}" />
    </c:when>
    <c:otherwise>
        <c:set var="src" value="http://www.gravatar.com/avatar/${user.gravatarHash}?s=${size}&d=monsterid&r=PG" />
    </c:otherwise>
</c:choose>