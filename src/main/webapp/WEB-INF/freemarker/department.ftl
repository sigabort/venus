<#assign pageTitle="Departments">
<#include "header.ftl">

  <h3> Department Details:</h3>
  <#if department??>
    <div class="department">
      <h4> Name: ${department.name} </h2>
      <h4> Description: ${department.description} </h2>
      <h4> Code: ${department.code} </h2>
    </div>
  </#if>

<#include "footer.ftl">