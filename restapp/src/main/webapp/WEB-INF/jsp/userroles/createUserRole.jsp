<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
  <head>
    <META http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <title>Create User Role</title>
    <link rel="stylesheet" href="<c:url value="/resources/styles/blueprint/screen.css" />" type="text/css" media="screen, projection">
    <link rel="stylesheet" href="<c:url value="/resources/styles/blueprint/print.css" />" type="text/css" media="print">
    <!--[if lt IE 8]>
	<link rel="stylesheet" href="<c:url value="/resources/styles/blueprint/ie.css" />" type="text/css" media="screen, projection">
	<![endif]-->
    <link rel="stylesheet" href="<c:url value="/resources/styles/blueprint/print.css" />" type="text/css" media="print">	
  </head>	
  <body>
    <div class="container">
      <h1>
	Create User Role
      </h1>
      <div class="span-12 last">	
	<form:form modelAttribute="userRoleRequest" method="post">
	  <fieldset>		
	    <legend>User Role Fields</legend>
	    <p>
	      <form:label	for="username" path="username" cssErrorClass="error">Username</form:label><br/>
	      <form:input path="username" /> <form:errors path="username" />			
	    </p>
	    <p>
	      <form:label	for="departmentName" path="departmentName" cssErrorClass="error">Department Name</form:label><br/>
	      <form:input path="departmentName" /> <form:errors path="departmentName" />
	    </p>
	    <p>
	      <form:label	for="role" path="role" cssErrorClass="error">Role</form:label><br/>
	      <form:select multiple="multiple" size="3" path="role">
		<form:option value="PRINCIPAL">PRINCIPAL</form:option>
		<form:option value="ADMIN">ADMIN</form:option>
		<form:option value="HEADOFDEPARTMENT">HEAD OF DEPARTMENT</form:option>
		<form:option value="INSTRUCTOR">INSTRUCTOR</form:option>
		<form:option value="STUDENT">STUDENT</form:option>
		<form:option value="STAFF">STAFF</form:option>
		<form:option value="  ">  </form:option>
	      </form:select>
	      <form:errors path="role" />
	    </p>
	    <p>	
	      <input type="submit" />
	    </p>
	  </fieldset>
	</form:form>
      </div>
      <hr>	
    </div>
  </body>
</html>
