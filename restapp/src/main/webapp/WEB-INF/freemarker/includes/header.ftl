<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en"> <#-- Should be closed in footer.ftl -->

  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link rel="stylesheet" type="text/css" href="resources/styles/venus.css"></link>
    <title>${pageTitle! ''}</title>
  </head>

    <body> <#-- Should be closed in footer.ftl -->
      <div  id="container" class="container"> <#-- Container for all of the elements in the body.This should be closed in footer.ftl -->

        <#-- Header panel-->
        <div  id="header" class="header">
        </div> <#-- End of Header -->
        
        <#-- The following div is panel for navigation  -->
        <div  id="horiz-nav" class="horiz-nav">
          <ul class="navlinks">
            <li><a href="home">Home</a>
            <li><a href="departments">Departments</a>
            <li><a href="programs">Programs</a>
            <li><a href="users">Staff</a>
          </ul>
        </div> <#-- End of Navigation -->

        <#-- All other content of the page should contain in the sub-container div. This should be closed in footer.ftl -->
        <div id="sub-container" class="sub-container">
        
