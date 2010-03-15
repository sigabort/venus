<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
  <head>
    <title>${pageTitle}</title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta name="language" content="en" />
    <meta name="description" content="Venus Web App" />
    <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.7.0/build/reset-fonts-grids/reset-fonts-grids.css"/>
    <link rel="stylesheet" type="text/css" href="${rootContextPath}/css/venus.css"/>
  </head>

  <body class="main-body" id="main-body">
    <#include "header-nav.ftl">
    <div id="contents" class="contents">
      <div id="navigation" class="navigation">
	<ul class="nav-list">
 	  <li class="home" id="home"><a href="${contextPath}/home">Home</a></li>
 	  <li class="departments" id="departments"><a href="${contextPath}/departments">Departments</a></li>
 	  <li class="programs" id="programs"><a href="${contextPath}/programs">Programs</a></li>
 	  <li class="users" id="users"><a href="${contextPath}/users">Users</a></li>
	</ul>
      </div> <!-- navigation -->
