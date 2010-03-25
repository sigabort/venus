<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
	<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
	<title>Create Account</title>
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
		Create User
	</h1>
	<div class="span-12 last">	
		<form:form modelAttribute="userRequest" method="post">
		  	<fieldset>		
				<legend>Account Fields</legend>
				<p>
					<form:label	for="username" path="username" cssErrorClass="error">Username</form:label><br/>
					<form:input path="username" /> <form:errors path="username" />			
				</p>
				<p>
					<form:label	for="userId" path="userId" cssErrorClass="error">UserId</form:label><br/>
					<form:input path="userId" /> <form:errors path="userId" />			
				</p>
				<p>
					<form:label	for="firstName" path="firstName" cssErrorClass="error">FirstName</form:label><br/>
					<form:input path="firstName" /> <form:errors path="firstName" />			
				</p>
				<p>
					<form:label	for="lastName" path="lastName" cssErrorClass="error">LastName</form:label><br/>
					<form:input path="lastName" /> <form:errors path="lastName" />			
				</p>
				<p>
					<form:label	for="email" path="email" cssErrorClass="error">Email</form:label><br/>
					<form:input path="email" /> <form:errors path="email" />			
				</p>
				<p>
					<form:label	for="joinDate" path="joinDate" cssErrorClass="error">JoinDate</form:label><br/>
					<form:input path="joinDate" /> <form:errors path="joinDate" />			
				</p>
					<form:label	for="birthDate" path="birthDate" cssErrorClass="error">BirthDate</form:label><br/>
					<form:input path="birthDate" /> <form:errors path="birthDate" />			
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

