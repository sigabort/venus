<#assign pageTitle="Departments">
<#include "header.ftl">

  <h3> Departments:</h3>
  <#if departments?? && departments?size gt 0>
    <div class="departments">
      <#list departments as dept>
        <div class="department">
	  <h4> <a href="/venus/app/departments/${dept.name}">${dept.name}</a> </h4>
	</div>
      </#list>
    </div>
  </#if>

<#include "footer.ftl">