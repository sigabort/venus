<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
</head>
<body>
<h2>Hello Contacts!</h2>
<div id="contact-data">
 <c:forEach var="contact" items="${users}">
  <span>Hello <a href="/venus/data/${contact.lastName}">${contact.firstName} ${contact.lastName}</a></span>
 </c:forEach>
</div>

<p>Save a contact, save the world:</p> 
<form action="/venus" method="post">
 First Name: <input type="text" name="firstName" /><br>
 Last Name: <input type="text" name="lastName" /><br>
 <input type="submit" value="submit"/>
</form>
</body>
</html>
