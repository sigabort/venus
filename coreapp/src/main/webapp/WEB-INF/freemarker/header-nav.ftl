<div id="header-nav">
  <ul id="header-nav-list">
    <@security.authorize ifAnyGranted="ROLE_ADMIN"><li><a href="${contextPath}/admin">Admin</a></li></@security.authorize>
    <@security.authorize ifAnyGranted="ROLE_USER"><li><a href="${rootContextPath}/j_spring_security_logout">Logout</a></li></@security.authorize>
    <@security.authorize ifNotGranted="ROLE_USER,ROLE_ADMIN"><li><a href="${contextPath}/login">Login</a></li></@security.authorize>
  </ul>
</div>