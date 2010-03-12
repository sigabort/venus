<div id="header-nav">
  <ul id="header-nav-list">
    <@security.authorize ifAnyGranted="ROLE_ADMIN"><li><a href="/venus/admin">Admin</a></li></@security.authorize>
    <@security.authorize ifAnyGranted="ROLE_USER"><li><a href="/venus/j_spring_security_logout">Logout</a></li></@security.authorize>
    <@security.authorize ifNotGranted="ROLE_USER,ROLE_ADMIN"><li><a href="/venus/login">Login</a></li></@security.authorize>
  </ul>
</div>