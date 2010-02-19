<#assign pageTitle="Create a Department">
<#include "header.ftl">

  <h3> Create a Department!</h3>

  <div id="create-user"> 
    <form action="/venus/app/departments" method="POST" name="create_dept_form">
      <div class='row'>
        <label for="name">Name:</label><input type="text" name="name" id="name"/>
      </div>
      <div class='row'>
        <label for="code">Code:</label><input type="text" name="code" id="code"/>
      </div>
      <div class='row'>
        <label for="description">Description:</label><input type="text" name="description" id="description"/>
      </div>
      <div class='row'>
        <label for="email">Email:</label><input type="text" name="email" id="email"/>
      </div>
      <div class='row'>
        <label for="photoUrl">PhotoUrl:</label><input type="text" name="photoUrl" id="photoUrl"/>
      </div>
      <button type='submit'>Create</button>
    </form>
  </div>

<#include "footer.ftl">