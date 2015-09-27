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
<html xmlns="http://www.w3.org/1999/xhtml" class="logged-in subsection-wall section-frontpage p-frontpage">
<head>

<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="keywords" content="">
<meta name="abstract" content="">
<meta name="description" content="">
<meta name="author" content="">
<link rel="shortcut icon" href="${staticRoot}/img/favicon.png">

<title>Scienation - truly open science</title>

<!-- 
<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css">
<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap-theme.min.css">
<link rel="stylesheet" type="text/css" href="${staticRoot}/css/normalize.css" media="screen" />
<link href="${staticRoot}/css/superfish.css" rel="stylesheet" type="text/css" />
 -->
<link rel="stylesheet" type="text/css" href="${staticRoot}/css/app.css" media="screen" />


<link rel="alternate" type="application/rss+xml" title="RSS 2.0" href="TODO" />

<!--[if lt IE 7]>
<script type="text/javascript" src="${staticRoot}/js/pngfix.js"></script>
<link type="text/css" href="${staticRoot}/css/basic_ie.css" rel="stylesheet" media="screen" />

<script src="${staticRoot}/js/jquery.helper.js" type="text/javascript"></script>

<![endif]-->


<link rel="stylesheet" type="text/css" href="${staticRoot}/css/print.css" media="print" />

<script type="text/javascript" src="https://code.jquery.com/jquery-1.11.2.min.js"></script>
<script type="text/javascript" src="http://code.jquery.com/ui/1.10.2/jquery-ui.js"></script>
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
	<header class="small" id="header" style="background-color: transparent;">
		<div class="page-width clearfix rel">
			<div class="column fl clearfix">
				<div class="fl" id="logo-wrapper">
					<a rel="index" id="logo"></a>
				</div>
				<div class="search-input dib fl">
					<autocomplete on-blur="removeActive" on-enter="onSubmit"
						on-focus="makeActive" on-submit="onSubmit" on-type="onType" data="autocompleteSuggestions">
					<div id="" class="rel">
    					<form method="get" id="searchform" action="${root}/search">
							<input style="background-position: top right" id="" class="magglass box"
								placeholder="Find a Publication"> <span
								class="fade-out-left no-touch"></span>
							<button type="button" class="search-icon"></button>
						</form>
						<ul class="autocomplete"></ul>
					</div>
					</autocomplete>
				</div>
			</div>
			<aside class="fr ta-right">
				 <p>
                    Welcome
                    <a href="${root}/signin">Sign in</a>
                     <c:if test="${userLoggedIn}">
                        <a href="${root}/logout">Logout</a>
                     </c:if>
                </p>
			</aside>
		</div>
	</header>


	<section role="main" id="main" ui-view="">
		<div id="bg-cover" style="transform: translate(0px, 0px);"></div>
		<div class="bg-cover not-topic hero-short">
			<div class="header-padding page-width height-full rel border-box" header-fadeout="">
				<header class="content-header rel ml clearfix">
					<div class="headline animate">
						<div style="opacity: 1;">
							<h1 class="anonymous-headline">
								Ask Questions &amp;&nbsp;Get&nbsp;Answers<br>From Other Software&nbsp;Engineers
							</h1>
							<p class="larger" >Learn the life of a developer,
								not&nbsp;the&nbsp;bug&nbsp;fixes.&nbsp;It's&nbsp;free.</p>
						</div>
					</div>
				</header>

				<div class="wall-tabs abs animate ml">
					<div class="inner clearfix" style="">
						<div class="fl">
							<nav role="navigation">
							  <div id="feed-tabs" class="inner">
								<ul class="clearfix">
									<li><a class="tab heavy<c:if test="${currentPage == 'home'}"> active</c:if>" href="${root}/">Home</a></li>
		                            <li><a class="tab heavy<c:if test="${currentPage == 'branches'}"> active</c:if>" href="${root}/branches">Branches</a></li>		                                
		                            <li><a class="tab heavy<c:if test="${currentPage == 'submit'}"> active</c:if>" href="${root}/publication/new">Submit a publication</a></li>
									<li><a class="tab heavy" href="/?tab=popular">Popular</a></li>
								</ul>
						      </div>
							</nav>
						</div>
						<div class="fr header-nav-toggle">
							<a href="/about"><span>About</span><span class="not-small">&nbsp;Sceination</span>
								<span class="arrow-right-header"></span></a>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="content-cover">
			<div class="page-width clearfix" id="content">
				<aside class="column left">
				    <nav>
						<div class="nav main">
							<a href="/" class="all nav-icon active"><span class="title">AllPosts</span></a>
						</div>
						<div class="nav topics rel">
							<h6 class="block-header dib">Topics</h6>
							<a class="blue-nav small semi-bold"
								href="/topics?tab=discover">View All</a>
						</div>
					</nav>
				</aside>
				<main class="main column center">
				<section itemtype="http://schema.org/WebPageElement" itemscope="" itemprop="mainContentOfPage"
					class="block box first mb">
                    <div class="wall">
                        <!-- -Content goes here -->					
				
        <c:if test="${!empty param.message}">
            <div style="color: green; text-align: center; font-size: 15pt;">${param.message}</div>
        </c:if>

<jsp:doBody />

                    </div>
                </section>
                </main>
                <aside class="column right">
                    <div class="first block">
                        <section data-reactid=".4" class="leaderboard">
                            <header class="rel clearfix">
                                <h6 class="dib block-header">
                                    <span >Leaderboard</span>
                                </h6>
                                <nav class="fr">
                                    
                                </nav>
                            </header>
                            <div class="container rel">
                                
                            </div>
                        </section>
                        </react>
                    </div>
                    <div offset="58" sticky="" class="block activity-block" style="top: 58px;">
                        <div>
                            <h6 class="block-header">
                                <span >Activity</span>
                            </h6>
                            <div>
                             
                            </div>
                        </div>
                    </div>
                </aside>
            </div>
        </div>
    </section>
 </body>
</html>

<!-- &copy; 2015 Scienation. TODO creative commons</p> -->
<!-- TODO google analytics (conditionally) -->
   