<%@tag description="Main template" pageEncoding="UTF-8"%>
<%@attribute name="header" fragment="true" %>

<%@ include file="../jsp/includes.jsp" %>
<c:set value="${pageContext.request.contextPath}/static" var="staticRoot" scope="request" />
<c:set value="${pageContext.request.contextPath}" var="root" scope="request" />

<c:if test="${root == '//'}">
    <c:set value="" var="root" scope="request" />
    <c:set value="/static" var="staticRoot" scope="request" />
</c:if>

<c:set var="userLoggedIn" value="${userContext.user != null}" scope="request" />

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<link rel="shortcut icon" href="${staticRoot}/img/favicon.png">

<title>Scienation - truly open science</title>

<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap-theme.min.css">

<link rel="stylesheet" type="text/css" href="${staticRoot}/css/style.css" media="screen" />
<link rel="stylesheet" type="text/css" href="${staticRoot}/css/normalize.css" media="screen" />

<link rel="alternate" type="application/rss+xml" title="RSS 2.0" href="TODO" />

<!--[if lt IE 7]>
<script type="text/javascript" src="${staticRoot}/js/pngfix.js"></script>
<link type="text/css" href="${staticRoot}/css/basic_ie.css" rel="stylesheet" media="screen" />

<script src="${staticRoot}/js/jquery.helper.js" type="text/javascript"></script>

<![endif]-->

<link href="${staticRoot}/css/superfish.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="${staticRoot}/css/print.css" media="print" />

<link type="text/css" href="${staticRoot}/css/dropdownmenu.css" rel="stylesheet" media="screen" />

<script type="text/javascript" src="https://code.jquery.com/jquery-1.11.2.min.js"></script>
<script type="text/javascript" src="http://code.jquery.com/ui/1.10.1/jquery-ui.js"></script>
<script src="http://code.jquery.com/jquery-migrate-1.2.1.js"></script>

<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.1/themes/base/jquery-ui.css">

<link rel="canonical" href="TODO" />

<script type="text/javascript" src="https://login.persona.org/include.js"></script>

<script type="text/javascript" src="//cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"></script>
<script type="text/x-mathjax-config">
  MathJax.Hub.Config({tex2jax: {inlineMath: [['$','$'], ['\\(','\\)']]}});
</script>

<script type="text/javascript">
    var loggedInUser = '${userContext.user != null ? userContext.user.email : null}';
    var userRequestedAuthentication = false;
    $(document).ready(function() {
        navigator.id.watch({
            loggedInUser : loggedInUser,
            onlogin : function(assertion) {
                $.ajax({
                    type : 'POST',
                    url : '${root}/persona/auth',
                    data : {assertion : assertion, userRequestedAuthentication : userRequestedAuthentication},
                    success : function(data) {
                        if (data != '') {
                            window.location.href = '${root}' + data;
                        }
                    },
                    error : function(xhr, status, err) {
                        alert("Authentication failure: " + err);
                    }
                });
            },
            onlogout : function() {
                //window.location.href = "${root}/logout";
            }
        });
    });
</script>

<c:if test="${!userLoggedIn}">
 <script type="text/javascript">
     $(document).ready(function() {
         var signinLink = $("#personaSignin");
         signinLink.click(function() {
             userRequestedAuthentication = true;
             navigator.id.request({siteName: 'Scienation'});
         });
     });
 </script>
</c:if>

<jsp:invoke fragment="header"/>

</head>

<body>
    <div>
        <div id="top_strip">
            <div id="top_strip_inner">
                <div class="widget">
                    <div>
                        <ul class="menu">
                            <li class="menu-item current-menu-item"><a href="TODO">Home</a></li>
                            <li class="menu-item"><a href="${root}/about">About</a></li>
                        </ul>
                    </div>
                </div>
                <p>
                    Welcome
                    <a href="${root}/signin">Sign in</a>
                     <c:if test="${userLoggedIn}">
                        <a href="${root}/logout">Logout</a>
                     </c:if>
                </p>
            </div>
        </div>
        <!-- top strip #end -->


        <div id="header">
            <div id="header_inner">
                <div class="logo">
                    <a href="TODO"><img src="" alt="Scienation logo" /></a>
                </div>

                <div class="header_right">
                    <!-- What here? The Menu maybe?TODO -->
                </div>
            </div>
        </div>
        <!-- header end -->
        
        <div id="nav">
            <div id="nav_inner">
                <div class="widget">
                    <div>
                        <ul class="menu">
                            <li class="menu-item<c:if test="${currentPage == 'home'}"> current-menu-item</c:if>"><a
                                href="${root}/">Home</a></li>
                                
                            <li class="menu-item<c:if test="${currentPage == 'branches'}"> current-menu-item</c:if>"><a
                                href="${root}/branches">Branches</a></li>
                                
                            <li class="menu-item<c:if test="${currentPage == 'submit'}"> current-menu-item</c:if>"><a
                                href="${root}/publication/new">Submit a publication</a></li>
                                
                            <li class="menu-item"><a
                                href="">Stuff</a>
                                <ul class="sub-menu">
                                    <li class="menu-item"><a href="">Stuff</a></li>
                                    <li class="menu-item"><a href="">Other Stuff</a></li>
                                </ul>
                            </li>
                        </ul>
                    </div>
                </div>
                
                <div class="search">
                    <form method="get" id="searchform" action="${root}/search">
                        <input type="text" value="Find a Publication" name="s" id="s" class="textfield"
                            onfocus="if (this.value == 'Find a Publication') {this.value = '';}"
                            onblur="if (this.value == '') {this.value = 'Find a Publication';}" /> <input type="image"
                            class="search_button"
                            src="${staticRoot}/img/search.png"
                            alt="Search" />
                    </form>
                </div>

            </div>
        </div>
        <!-- nav #end -->

        <div id="wrapper" class="container-fluid">
        
        <c:if test="${!empty param.message}">
            <div style="color: green; text-align: center; font-size: 15pt;">${param.message}</div>
        </c:if>

<jsp:doBody />


        </div>
        <!-- wrapper #end -->

        <div id="footer">
            <div id="footer_inner">
                <div class="fleft">
                    <p>&copy; 2015 Scienation. TODO creative commons</p>
                </div>

            </div>
        </div>
        <!--footer end-->
    </div>
    <!-- TODO google analytics (conditionally) -->
</body>
</html>
   