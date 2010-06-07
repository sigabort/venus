    <#-- See if the response contains the entries field -->
    <#function getResponseEntries resp>
      <#if resp?? && resp.entries??>
        <#return resp.entries>
      </#if>
    </#function>

    <#-- See if the response contains the entry field -->
    <#function getResponseEntry resp>
      <#if resp?? && resp.entry??>
        <#return resp.entry>
      </#if>
    </#function>

    <#-- See if the response is set and is proper or not -->
    <#function isProperResponse resp>
      <#if resp?? && !resp.error>
        <#return true>
      </#if>
      <#return false>
    </#function>
