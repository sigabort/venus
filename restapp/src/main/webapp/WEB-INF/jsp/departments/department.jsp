<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
	<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
	<title>@Controller Example</title>
	<link rel="stylesheet" href="<c:url value="/resources/styles/blueprint/screen.css" />" type="text/css" media="screen, projection">
	<link rel="stylesheet" href="<c:url value="/resources/styles/blueprint/print.css" />" type="text/css" media="print">
	<!--[if lt IE 8]>
		<link rel="stylesheet" href="<c:url value="/resources/styles/blueprint/ie.css" />" type="text/css" media="screen, projection">
	<![endif]-->
	<link rel="stylesheet" href="<c:url value="/resources/styles/blueprint/print.css" />" type="text/css" media="print">	
</head>	
<body>
<div class="container">
	<div id="dept-container" class="dept-container">
	  <c:if test="${response != null}">
	    <c:choose>
              <c:when test="${response.error == false}">
	        <c:if test="${response.entry != null}">
	          <c:set var="department" value="${response.entry}"/>
	          <h1>Department Details:	</h1>
	          <div id="dept" class="dept">
		    <h2> Name: <c:out value="${department.name}"/> </h2>
	            <h2> Code: <c:out value="${department.code}"/> </h2>
	            <h2> Description: <c:out value="${department.description}" default="" /> </h2>
	            <h2> PhotoUrl: <c:out value="${department.photoUrl}" default="" /> </h2>
	            <h2> Email: <c:out value="${department.email}" /> </h2>
	     	  </div>
	      	</c:if>
              </c:when>
              <c:otherwise>
                <h3> Department not found </h3>
		<h3> Error Code: <c:out value="${response.httpErrorCode}" />
		<h3> Error Description: <c:out value="${response.errorDescription}" />
	      </c:otherwise>
	    </c:choose>
          </c:if>
	</div>
</div>
</body>
</html>