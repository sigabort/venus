<#assign pageTitle="Departments">
<#include "header.ftl">

  <#if departments?? && departments?size gt 0>
    <div class="departments">
      <ul class="ul-depts">
        <#list departments as dept>
	  <li class="li-depts">
            <div class="department">
	      <h4> <a href="/venus/departments/${dept.name}">${dept.name}</a> </h4>
	    </div>
          </li>
        </#list>
      </ul>
    </div>
  </#if>

<#include "footer.ftl">