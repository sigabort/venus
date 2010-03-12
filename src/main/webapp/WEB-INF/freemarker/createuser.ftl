<#assign pageTitle="Create a User">
<#include "header.ftl">

  <h3> Create a User!</h3>

  <div id="create-user"> 
    <form action="/venus/users" method="POST" name="create_user_form">
      <div class='row'>
        <label for="username">Username:</label><input type="text" name="username" id="username"/>
      </div>
      <div class='row'>
        <label for="password">Password:</label><input type="password" name="password" id="password"/>
      </div>
      <div class='row'>
        <label for="userId">UserId:</label><input type="text" name="userId" id="userId"/>
      </div>
      <div class='row'>
        <label for="firstname">First Name:</label><input type="text" name="firstname" id="firstname"/>
      </div>
      <div class='row'>
        <label for="lastname">Last Name:</label><input type="text" name="lastname" id="lastname"/>
      </div>
      <div class='row'>
        <label for="email">Email:</label><input type="text" name="email" id="email"/>
      </div>
      <div class='row'>
        <label for="gender">Gender:</label><input type="text" name="gender" id="gender"/>
      </div>
      <div class='row'>
        <label for="url">Profile URL:</label><input type="text" name="url" id="url"/>
      </div>
      <div class='row'>
        <label for="phone">Phone:</label><input type="text" name="phone" id="phone"/>
      </div>
      <div class='row'>
        <label for="address1">Address1:</label><input type="text" name="address1" id="address1"/>
      </div>
      <div class='row'>
        <label for="address2">Address2:</label><input type="text" name="address2" id="address2"/>
      </div>
      <div class='row'>
        <label for="city">City:</label><input type="text" name="city" id="city"/>
      </div>
      <div class='row'>
        <label for="country">Country:</label><input type="text" name="country" id="country"/>
      </div>
      <div class='row'>
        <label for="postalCode">PostalCode:</label><input type="text" name="postalCode" id="postalCode"/>
      </div>
      <div class='row'>
        <label for="photoUrl">PhotoUrl:</label><input type="text" name="photoUrl" id="photoUrl"/>
      </div>
      <div class='row'>
        <label for="birthDate">BirthDate:</label><input type="text" name="birthDate" id="birthDate"/>
      </div>
      <div class='row'>
        <label for="joinDate">JoinDate:</label><input type="text" name="joinDate" id="joinDate"/>
      </div>
      <button type='submit'>Create</button>
    </form>
  </div>

<#include "footer.ftl">