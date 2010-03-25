<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
	<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
	<title>User Page</title>
	<link rel="stylesheet" href="<c:url value="/resources/styles/blueprint/screen.css" />" type="text/css" media="screen, projection">
	<link rel="stylesheet" href="<c:url value="/resources/styles/blueprint/print.css" />" type="text/css" media="print">
	<!--[if lt IE 8]>
		<link rel="stylesheet" href="<c:url value="/resources/styles/blueprint/ie.css" />" type="text/css" media="screen, projection">
	<![endif]-->
	<link rel="stylesheet" href="<c:url value="/resources/styles/blueprint/print.css" />" type="text/css" media="print">	
</head>	
<body>
<div class="container">
	<div id="user-container" class="user-container">
	  <c:if test="${response != null}">
	    <c:choose>
              <c:when test="${response.error == false}">
	        <c:if test="${response.entry != null}">
	          <c:set var="user" value="${response.entry}"/>
	          <h1>User Details:	</h1>
	          <div id="user" class="user">
		    <h2> Username: <c:out value="${user.username}" /> </h2>
		    <h2> First Name: <c:out value="${user.firstName}" default=""/> </h2>
	            <h2> Last Name: <c:out value="${user.lastName}" default=""/> </h2>
	            <h2> Email: <c:out value="${user.email}" default=""/> </h2>
	     	  </div>
	      	</c:if>
              </c:when>
              <c:otherwise>
                <h3> User not found </h3>
		<h3> Error Code: <c:out value="${response.httpErrorCode}" />
		<h3> Error Description: <c:out value="${response.errorDescription}" />
	      </c:otherwise>
	    </c:choose>
          </c:if>
	</div>
</div>
</body>
</html>