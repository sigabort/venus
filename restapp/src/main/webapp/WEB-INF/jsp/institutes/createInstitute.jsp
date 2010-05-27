<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
	<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
	<title>Create Institute</title>
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
		Create Institute
	</h1>
	<div class="span-12 last">	
		<form:form modelAttribute="instituteRequest" method="post">
		  	<fieldset>		
				<legend>Institute Fields</legend>
				<p>
					<form:label	for="name" path="name" cssErrorClass="error">Name</form:label><br/>
					<form:input path="name" /> <form:errors path="name" />			
				</p>
        <p>
          <form:label for="code" path="code" cssErrorClass="error">Code</form:label><br/>
          <form:input path="code" /> <form:errors path="code" />      
        </p>
        <p>
          <form:label for="displayName" path="displayName" cssErrorClass="error">Display Name</form:label><br/>
          <form:input path="displayName" /> <form:errors path="displayName" />      
        </p>
        <p>
          <form:label for="parent" path="parent" cssErrorClass="error">Parent Institute</form:label><br/>
          <form:input path="parent" /> <form:errors path="parent" />      
        </p>
				<p>
					<form:label	for="description" path="description" cssErrorClass="error">Description</form:label><br/>
					<form:input path="description" /> <form:errors path="description" />			
				</p>
				<p>
					<form:label	for="photoUrl" path="photoUrl" cssErrorClass="error">PhotoUrl</form:label><br/>
					<form:input path="photoUrl" /> <form:errors path="photoUrl" />			
				</p>
				<p>
					<form:label	for="email" path="email" cssErrorClass="error">Email</form:label><br/>
					<form:input path="email" /> <form:errors path="email" />			
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

