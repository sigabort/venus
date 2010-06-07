<#assign pageTitle="Home Page">
<#include "includes/header.ftl">
<#assign institute=getResponseEntry(venusResp)>

<div>
  <h1> Welcome to ${institute.name}!</h1>
</div>

<#include "includes/footer.ftl">
