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
   <c:if test="${response != null}">
     <c:choose>
       <c:when test="${response.error == false}">
          <div id="depts" class="depts">
	    <h2> List of departments </h2>
	    <c:forEach var="dept" items="${response.entries}">
	      <div id="dept" class="dept">
	        <a href="departments/<c:out value="${dept.name}" />"><c:out value="${dept.name}" /></a>
	      </div>
	    </c:forEach>
	  </div>
       </c:when>
       <c:otherwise>
         <div id="depts-err" class="depts-err">
	   <h3> The error code: <c:out value="${response.httpErrorCode}" default="" /> </h3>
	   <h3> The error Description: <c:out value="${response.httpErrorDescription}" default="" /></h3>
	   <p> Click <a href="departments">here</a> try again </p>
	 </div> 
       </c:otherwise>
     </c:choose>
   </c:if>
</div>
</body>
</html>