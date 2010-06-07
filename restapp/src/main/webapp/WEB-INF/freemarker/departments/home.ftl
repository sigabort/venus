<#assign pageTitle="Departments Page">
<#include "../includes/header.ftl">

<#assign depts=getResponseEntries(venusResp)!>

<div id="departments-nav" class="departments-nav">

  <#-- Check if the departments exist or not. If not, and the user is admin suggest creating.
       Else, display a message -->
  <#if depts?? && depts?size gt 0>

    <ul id="departments" class="departments">
      <#list depts as dept>
        <li id="depatment" class="department">
          <a href="${ctxPath}/departments/${dept.name}">${dept.name}</a>
        </li>  
      </#list> <#-- End of the list depts -->
    </ul>

  <#-- Ok, no departments exist. Check if the user is admin. If yes, suggest to create one. Otherwise, show nothing --> 
  <#else> 

    <#if isAuthorisedAsAdmin()>
      <h3>Departments do not exist. Please click <a href="${ctxPath}/departments/create">here</a> to create one.</h3>
    <#else>
      <h2> Departments don't exist yet. Keep looking!</h2>
    </#if> <#-- End of isAuthorisedAsAdmin  -->

  </#if> <#-- End of If departments -->

</div> <#-- End of departments-nav div -->

<#include "../includes/footer.ftl">
