<%@tag description="Main template" pageEncoding="UTF-8"%>
<%@attribute name="header" fragment="true"%>

<%@ include file="../jsp/includes.jsp"%>
<c:set value="${pageContext.request.contextPath}/static" var="staticRoot" scope="request" />
<c:set value="${pageContext.request.contextPath}" var="root" scope="request" />

<c:if test="${root == '//'}">
  <c:set value="" var="root" scope="request" />
  <c:set value="/static" var="staticRoot" scope="request" />
</c:if>

<c:set var="userLoggedIn" value="${userContext.user != null}" scope="request" />

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta name="keywords" content="">
<meta name="abstract" content="">
<meta name="description" content="">
<meta name="author" content="">
<link rel="shortcut icon" href="${staticRoot}/img/favicon.png">
<!-- link rel="apple-touch-icon-precomposed" href="60.png" type="image/png"/>
        <link rel="apple-touch-icon-precomposed" sizes="72x72" href="72.png" type="image/png"/>
        <link rel="apple-touch-icon-precomposed" sizes="120x120" href="120.png" type="image/png"/>
        <link rel="apple-touch-icon-precomposed" sizes="152x152" href="152.png" type="image/png"/-->

<title>Scienation - truly open science</title>


<link href="http://fonts.googleapis.com/css?family=Open+Sans:400,300,700|Merriweather:400,400italic,700italic"
  rel="stylesheet" type="text/css">
<link rel="stylesheet" href="${staticRoot}/css/bootstrap.min.css">
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="${staticRoot}/css/style.css">

<link rel="alternate" type="application/rss+xml" title="RSS 2.0" href="TODO" />

<!--[if lt IE 9]>
            <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
            <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
        <![endif]-->
<!--[if lt IE 7]>
			<script type="text/javascript" src="${staticRoot}/js/pngfix.js"></script>
			<script src="${staticRoot}/js/jquery.helper.js" type="text/javascript"></script>
	   <![endif]-->

<script type="text/javascript" src="https://code.jquery.com/jquery-1.11.2.min.js"></script>
<script type="text/javascript" src="http://code.jquery.com/ui/1.10.2/jquery-ui.js"></script>
<script src="http://code.jquery.com/jquery-migrate-1.2.1.js"></script>

<script type="text/javascript" src="${staticRoot}/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${staticRoot}/js/placeholders.min.js"></script>


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
					data : {
						assertion : assertion,
						userRequestedAuthentication : userRequestedAuthentication
					},
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
					navigator.id.request({siteName : 'Scienation'});
				});
			});
		</script>
</c:if>

<jsp:invoke fragment="header" />
</head>

<body id="home">
  <div id="wrap">
    <div id="main-nav" class="">
      <div class="container">
        <div class="nav-header">
          <a class="nav-brand" href="${root}/"><i class="icon-lime"></i>Scienation</a> 
          <a class="menu-link nav-icon"href="#"><i class="icon-menu2"></i>
          </a> Welcome <a href="${root}/profile">${userContext.user.firstName}</a>
          
          <c:if test="${!userLoggedIn}">
            <!--  TODO modal? data-toggle="modal" data-target="#loginModal" -->
            <a class="btn btn-blog outline-white pull-right" href="${root}/signin">Signin</a>
          </c:if>
          <c:if test="${userLoggedIn}">
            <a class="btn btn-blog outline-white pull-right" href="${root}/logout">Logout</a>
          </c:if>
        </div>
      </div>
    </div>

    <section id="header-section" class="light-typo">
      <div id="cover-image" class="image-bg"></div>
      <div class="container menu-content">
        <div class="middle-text">
          
          <a class="btn <c:if test="${currentPage == 'home'}"> active</c:if>" href="${root}/">Home</a>
          <a class="btn <c:if test="${currentPage == 'branches'}"> active</c:if>"
              href="${root}/branches">Branches</a>
          <a class="btn <c:if test="${currentPage == 'submit'}"> active</c:if>"
              href="${root}/publication/new">Submit a publication</a>
          <a class="btn " href="/?tab=popular">Popular</a>
        </div>
      </div>
    </section>

    <div class="container content">
      <div class="row">
        <c:if test="${!empty param.message}">
          <div style="color: green; text-align: center; font-size: 15pt;">${param.message}</div>
        </c:if>

        <!-- col-md-10 col-md-offset-1 -->
        <div class="col-md-10">

          <jsp:doBody />

          <div class="paging clearfix">
          </div>

        </div>
        <div class="col-md-2">
            test
        </div>
      </div>
      <!-- end row -->
    </div>
    <footer>
      <div class="copyright">
        <div class="container">
          <p class="pull-left">
            copy; 2015 Scienation. TODO creative commons
          </p>
          <ul class="social-links pull-right">
            <li><a href="#link"><i class="icon-twitter"></i></a></li>
            <li><a href="#link"><i class="icon-facebook"></i></a></li>
            <li><a href="#link"><i class="icon-googleplus"></i></a></li>
          </ul>
        </div>
      </div>
    </footer>
  </div>
</body>
</html>


<!-- TODO google analytics (conditionally) -->
