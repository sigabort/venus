<#assign pageTitle="Create a Program">
<#include "header.ftl">

  <h3> Create a Program!</h3>

  <div id="create-program"> 
    <form action="/venus/programs" method="POST" name="create_program_form">
      <div class='row'>
        <label for="name">Name:</label><input type="text" name="name" id="name"/>
      </div>
      <div class='row'>
        <label for="code">Code:</label><input type="text" name="code" id="code"/>
      </div>
      <div class='row'>
        <label for="department">Department:</label>
	  <#if departments??>
	    <select id="departmentName" name="departmentName">
              <#list departments as dept>
	        <option value="${dept.name}">${dept.name}</option>
	      </#list>
	    </select>
          <#else>
            <div class='no-depts'>
	      <h4> No departments available. Click <a href="/venus/departments/createDepartment">here</a> to create one </h4>
            </div>
          </#if>
      </div>
      <div class='row'>
        <label for="description">Description:</label><input type="text" name="description" id="description"/>
      </div>
      <div class='row'>
        <label for="prerequisites">Prerequisites:</label><input type="text" name="prerequisites" id="prerequisites"/>
      </div>
      <div class='row'>
        <label for="duration">Duration:</label><input type="text" name="duration" id="duration"/>
      </div>
      <button type='submit'>Create Program</button>
    </form>
  </div>

<#include "footer.ftl">
