<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
  <head>
    <title>${pageTitle}</title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta name="language" content="en" />
    <meta name="description" content="Venus Web App" />
    <link rel="stylesheet" type="text/css" href="${rootContextPath}/css/venus.css"/>
  </head>

  <body class="main-body" id="main-body">
    <#include "../header-nav.ftl">
    <div id="contents" class="contents">
      <div id="navigation" class="navigation">
	<ul class="nav-list">
 	  <li class="home" id="home"><a href="${contextPath}/home">Home</a></li>
 	  <li class="create-dept" id="create-dept"><a href="${contextPath}/admin/createDepartment">Create Department</a></li>
 	  <li class="create-program" id="create-program"><a href="${contextPath}/admin/createProgram">Create Program</a></li>
 	  <li class="create-user" id="create-user"><a href="${contextPath}/admin/createUser">Create User</a></li>
	</ul>
      </div> <!-- navigation -->
