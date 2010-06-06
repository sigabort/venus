<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en"> <#-- Should be closed in footer.ftl -->

  <head>

    <#-- See if the response is set and is proper or not -->
    <#function isProperResponse resp>
      <#if resp?? && !resp.error>
        <#return true>
      </#if>
      <#return false>
    </#function>
    
    <#-- If the response is not proper, Send error -->
    <#if isProperResponse(response) && response.entry??>
      <#assign institute=response.entry/>
      <#assign ctxPath=rc.contextPath/>
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
        </div> <#-- End of Header -->
        
        <#-- The following div is panel for navigation  -->
        <div  id="horiz-nav" class="horiz-nav">
          <ul class="horiz-nav-links" id="horiz-nav-links">
            <li><a href="${ctxPath}/home">Home</a></li>
            <li><a href="${ctxPath}/departments">Departments</a></li>
            <li><a href="${ctxPath}/programs">Programs</a></li>
            <li><a href="${ctxPath}/users">Staff</a></li>
          </ul>
        </div> <#-- End of Navigation -->

        <#-- All other content of the page should contain in the sub-container div. This should be closed in footer.ftl -->
        <div id="sub-container" class="sub-container">
        
