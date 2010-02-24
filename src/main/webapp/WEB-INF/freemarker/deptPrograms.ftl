<#assign pageTitle="All Programs">
<#include "header.ftl">

  <#if deptPrograms?? && deptPrograms?size gt 0>
    <div class="deptPrograms">
      <ul class="ul-pgms">
        <#list deptPrograms as dept>
	  <li class="li-depts">
            <div class="dept">
	      <h2>${dept.name}</h2>
		<#if dept.programs??>
		  <#list dept.programs as program>
		    <h3><a href="/venus/app/programs/${dept.name}/${program.name}">${program.name}</a></h3>
		  </#list>
		</#if>
	    </div>
          </li>
        </#list>
      </ul>
    </div>
  </#if>

<#include "footer.ftl">