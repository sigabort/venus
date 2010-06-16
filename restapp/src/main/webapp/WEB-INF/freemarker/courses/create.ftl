<#assign pageTitle="Create Course">
<#include "../includes/header.ftl">
<#assign form=JspTaglibs["http://www.springframework.org/tags/form"]>


<div id="create-course-nav" class="create-course-nav">

      <@form.form modelAttribute="course" method="post">
        <fieldset>
        <legend>Course Fields</legend>
        <p>
          <@form.label for="name" path="name" cssErrorClass="error">Name</@form.label><br/>
          <@form.input path="name" /> <@form.errors path="name" />      
        </p>
        <p> 
          <@form.label for="code" path="code" cssErrorClass="error">Code</@form.label><br/>
          <@form.input path="code" /> <@form.errors path="code" />
        </p>
        <p> 
          <@form.label for="department" path="department" cssErrorClass="error">Department</@form.label><br/>
          <@form.input path="department" /> <@form.errors path="department" />
        </p>
        <p> 
          <@form.label for="admin" path="admin" cssErrorClass="error">Admin</@form.label><br/>
          <@form.input path="admin" /> <@form.errors path="admin" />
        </p>
        <p> 
          <@form.label for="instructor" path="instructor" cssErrorClass="error">Instructor</@form.label><br/>
          <@form.input path="instructor" /> <@form.errors path="instructor" />
        </p>
        <p> 
          <@form.label for="program" path="program" cssErrorClass="error">Program</@form.label><br/>
          <@form.input path="program" /> <@form.errors path="program" />
        </p>
        <p> 
          <input type="submit" />
        </p>
      </fieldset>
    </@form.form>

</div> <#-- End of create-course-nav div -->

<#include "../includes/footer.ftl">
