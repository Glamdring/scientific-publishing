<%@ include file="../includes.jsp" %>
<%@ page pageEncoding="UTF-8" %>
<% out.clear(); %>
<c:set var="size" value="36" />
<c:set var="pictureType" value="small" />

<c:forEach items="${users}" var="user">
   <div class="userListItem unselected" onclick="window.location.href='${root}${user.username}';">
        <%@ include file="../includes/profilePhotoNoLink.jsp" %>
        <img src="${src}" class="profilePictureSmall" />
        @${user.username} - ${user.names}
        <input class="hdnUrl" type="hidden" value="${root}${user.username}" />
    </div>
</c:forEach>

