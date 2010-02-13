<#assign pageTitle="Login">
<#assign login=true>
<#include "header.ftl">

<div>
  <div>
    <form action="/venus/j_spring_security_check" method="POST" name="login_form">
      <h3>Log in:</h3>
      <div class='row'>
        <label for="j_username">Username:</label>
        <input type="text" name="j_username" id="j_username"/>
      </div>
      <div class='row'>
        <label for="j_password">Password:</label>
	<input type="password" name="j_password" id="j_password"/>
      </div>
      <button type='submit'>Go</button>
    </form>
  </div>
</div>

<#include "footer.ftl">