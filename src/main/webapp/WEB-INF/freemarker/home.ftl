<#assign pageTitle="Users">
<#include "header.ftl">

  <h3> welcome users!</h3>
  <#if users?? && users?size gt 0>
    <div class="users">
      <#list users as user>
        <h2> FirstName: ${user.firstName} </h2>
        <h2> LastName: ${user.lastName} </h2>
      </#list>
    </div>
  </#if>

<#include "footer.ftl">