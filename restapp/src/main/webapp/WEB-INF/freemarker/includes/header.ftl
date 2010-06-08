<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en"> <#-- Should be closed in footer.ftl -->

  <head>
    <#-- Include all Libs  -->
    <#include "security-lib.ftl">
    <#include "request-lib.ftl">

    <#-- set the context path and venus response (used by other FTL files) -->    
    <#assign ctxPath=rc.contextPath>
    <#assign venusResp=response>
    
    <#-- If the response is not proper, Send error -->
    <#if isProperResponse(venusResp)>
    <#else>
      <meta http-equiv="Refresh" content="5; url=${ctxPath}/error">
    </#if>
    
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" type="text/css" href="${ctxPath}/resources/styles/venus.css"></link>
    <title>${pageTitle! ''}</title>

  </head>

    <body> <#-- Should be closed in footer.ftl -->
      <div  id="container" class="container"> <#-- Container for all of the elements in the body.This should be closed in footer.ftl -->

        <#-- Header panel-->
        <div  id="header" class="header">

          <#-- logo goes here -->
          <div id="logo" class="logo">
            <img src="${ctxPath}/resources/images/header-bg.jpg"/>
            <p>I am title of institute</p>
          </div>

          <#-- User Status panel -->
          <div id="user-status-panel" class="user-status-panel">
            <#if isAuthorised()>
              <span>Signed in as <a href="${ctxPath}/users/<@username/>"><@username/></a></span>
              <span><a href="${ctxPath}/j_spring_security_logout">Logout</a></span>
            <#else>
              <a href="${ctxPath}/login">Login</a>
            </#if>      
          </div>

        </div> <#-- End of Header -->
        
        <#-- The following div is panel for navigation  -->
        <div  id="horiz-nav" class="horiz-nav">
          <ul class="horiz-nav-links" id="horiz-nav-links">
            <li><a href="${ctxPath}/index">Home</a></li>
            <li><a href="${ctxPath}/departments">Departments</a></li>
            <li><a href="${ctxPath}/programs">Programs</a></li>
            <li><a href="${ctxPath}/users">Staff</a></li>
          </ul>
        </div> <#-- End of Navigation -->

        <#-- All other content of the page should contain in the sub-container div. This should be closed in footer.ftl -->
        <div id="sub-container" class="sub-container">
        
