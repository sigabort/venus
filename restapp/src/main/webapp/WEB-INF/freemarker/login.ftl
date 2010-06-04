<#include "includes/header.ftl">

<div id="container" class="container">
  <div  id="nav-bar" class="nav-bar">
    <ul class="navbar">
      <li><a href="index">Home</a>
      <li><a href="departments">Departments</a>
      <li><a href="programs">Programs</a>
      <li><a href="users">Staff</a>
    </ul>
  </div>
  
  <div id="login-panel" class="login-panel">
    <form id="login-form" class="login-form">
      Username: <input type="text" name="login-username" id="login-username" class="login-username"></input>
      Password: <input type="text" name="login-password" id="login-password" class="login-password"></input>
      <input type="submit" name="login-submit" id="login-submit" class="login-submit"></input>
    </form>
  </div>
  
</div>


<#include "includes/footer.ftl">
