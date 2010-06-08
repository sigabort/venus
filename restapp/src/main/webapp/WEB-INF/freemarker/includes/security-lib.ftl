<#assign security=JspTaglibs["http://www.springframework.org/security/tags"]>

<#-- Check if the user is authenticated as admin -->
<#function isAuthorisedAsAdmin>
  <@security.authorize ifAnyGranted="ROLE_ADMIN">
    <#return true>
  </@security.authorize>
  <#return false>
</#function>

<#-- Check if the user is authenticated -->
<#function isAuthorised>
  <@security.authorize ifAnyGranted="ROLE_ADMIN,ROLE_USER">
    <#return true>
  </@security.authorize>
  <#return false>
</#function>

<#-- Macro for getting the logged in username -->
<#macro username>
  <@security.authentication property="principal.username"/>
</#macro>