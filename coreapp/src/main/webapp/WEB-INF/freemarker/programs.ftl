<#assign pageTitle="Programs">
<#include "header.ftl">

  <#if programs?? && programs?size gt 0>
    <div class="programs">
      <ul class="ul-pgms">
        <#list programs as pgm>
	  <li class="li-pgms">
            <div class="program">
	      <h4> <a href="${contextPath}/programs/${pgm.name}">${pgm.name}</a> </h4>
	    </div>
          </li>
        </#list>
      </ul>
    </div>
  </#if>

<#include "footer.ftl">