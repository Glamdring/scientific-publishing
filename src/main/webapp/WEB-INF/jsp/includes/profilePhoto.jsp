<%@ page pageEncoding="UTF-8" %>

<%@ include file="profilePhotoNoLink.jsp" %>

<a href="${root}/${user.id}/${user.firstName}-${user.lastName}">
    <img src="${src}" class="linkedImage ${cssClass}" alt="${user.names}" title="${user.names}" />
</a>