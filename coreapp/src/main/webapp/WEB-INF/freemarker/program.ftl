<#assign pageTitle="Programs">
<#include "header.ftl">

  <h3> Program Details:</h3>
  <#if program??>
    <div class="program">
      <h4> Name: ${program.name} </h2>
      <h4> Description: ${program.description} </h2>
      <h4> Code: ${program.code} </h2>
    </div>
  </#if>

<#include "footer.ftl">