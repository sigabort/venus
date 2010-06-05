<#assign pageTitle="Login Page"/>
<#include "includes/header.ftl">

  <#-- Login panel containing the form for login -->
  <div id="login-panel" class="login-panel">
    <form id="login-form" class="login-form" action="j_spring_security_check" method="post">
      Username: <input type="text" name="j_username" id="login-username" class="login-username"></input>
      Password: <input type="password" name="j_password" id="login-password" class="login-password"></input>
      <input type="submit" name="login-submit" id="login-submit" class="login-submit"></input>
    </form>
  </div> <#-- End of login panel -->
  
<#include "includes/footer.ftl">
