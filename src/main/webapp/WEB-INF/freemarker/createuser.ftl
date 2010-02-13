<#assign pageTitle="Create a User">
<#include "header.ftl">

  <h3> Create a User!</h3>

  <div id="create-user"> 
    <form action="users" method="POST" name="create_user_form">
      <div class='row'>
        <label for="username">Username:</label><input type="text" name="username" id="username"/>
      </div>
      <div class='row'>
        <label for="password">Password:</label><input type="password" name="password" id="password"/>
      </div>
      <div class='row'>
        <label for="firstname">First Name:</label><input type="text" name="firstname" id="firstname"/>
      </div>
      <div class='row'>
        <label for="lastname">Last Name:</label><input type="text" name="lastname" id="lastname"/>
      </div>
      <button type='submit'>Create</button>
    </form>
  </div>

<#include "footer.ftl">