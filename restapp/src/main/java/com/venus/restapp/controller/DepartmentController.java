package com.venus.restapp.controller;

import java.util.Map;
import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.venus.restapp.service.DepartmentService;
import com.venus.restapp.request.DepartmentRequest;
import com.venus.restapp.request.BaseRequest;
import com.venus.restapp.response.BaseResponse;
import com.venus.restapp.response.DepartmentResponse;
import com.venus.restapp.response.error.ResponseException;

import org.apache.log4j.Logger;

/**
 * Controller class for handling all departments related requests.
 * 
 */
@Controller
@RequestMapping(value="/departments")
public class DepartmentController {
  @Autowired
  private DepartmentService departmentService;
  private static final Logger log = Logger.getLogger(DepartmentController.class);

  @RequestMapping(value="create", method=RequestMethod.GET)
  public String getCreateForm(Model model) {
    model.addAttribute("departmentRequest", new DepartmentRequest());
    return "departments/createDepartment";
  }


  @RequestMapping(method=RequestMethod.POST)
  public ModelAndView create(@Valid DepartmentRequest departmentRequest, BindingResult result) {
    if (result.hasErrors()) {
      return new ModelAndView("departments/createDepartment");
    }
    log.info("Adding department" + departmentRequest.getName());
    Object dept = null;
    try {
      dept = departmentService.createUpdateDepartment(departmentRequest);
    }
    catch (ResponseException re) {
      return new ModelAndView("departments/createDepartment", "response", re.getResponse());
    }
    DepartmentResponse resp = DepartmentResponse.populateDepartment(dept);
    return new ModelAndView("departments/department", "response", resp);
  }

  @RequestMapping(value="{name}", method=RequestMethod.GET)
  public ModelAndView getDepartment(@PathVariable String name, @Valid BaseRequest request, BindingResult result) {
    if (result.hasErrors()) {
      /* XXX: We need to populate the response with the actual errors. Need to check
       * how 'create' is populating the errors properly in case of invalid request.
       * We need to do same here too.
       */
      ResponseException re = new ResponseException(HttpStatus.BAD_REQUEST, "Bad request");
      return new ModelAndView("departments/department", "response", re.getResponse());
    }
    log.info("Fetching department: " + name);
    Object dept = null;
    try {
      dept = departmentService.getDepartment(name, request);
    }
    catch (ResponseException re) {
      return new ModelAndView("departments/department", "response", re.getResponse());
    }
    /* department not found? throw 404 */
    if (dept == null) {
      ResponseException re = new ResponseException(HttpStatus.NOT_FOUND, "Department with name: " + name + ", not found");
      return new ModelAndView("departments/department", "response", re.getResponse());
    }
    log.info("Got dept: " + name);
    /* populate the object */
    DepartmentResponse resp = DepartmentResponse.populateDepartment(dept);
    return new ModelAndView("departments/department", "response", resp);
  }

  /**
   * Send all of the department results in the institute. This is the default request
   * for departments page.
   * @param request  The base request object containing all of the optional parameters
   * @param result   The binding result containing any errors if the request is bad
   * @return The response object as Model object with List of departments or exception details
   *         if there are any errors
   */
  @RequestMapping(method=RequestMethod.GET)
  public ModelAndView getDepartments(@Valid BaseRequest request, BindingResult result) {
    if (result.hasErrors()) {
      /* XXX: We need to populate the response with the actual errors. Need to check
       * how 'create' is populating the errors properly in case of invalid request.
       * We need to do same here too.
       */
      ResponseException re = new ResponseException(HttpStatus.BAD_REQUEST, "Bad request");
      return new ModelAndView("departments/home", "response", re.getResponse());
    }
    log.info("Fetching all departments....");
    List depts = null;
    try {
      depts = departmentService.getDepartments(request);
    }
    catch (ResponseException re) {
      return new ModelAndView("departments/home", "response", re.getResponse());
    }
    /* departments not found? send empty response. So, the client can take care of
     * what to do next
     */
    if (depts == null) {
      return new ModelAndView("departments/department", "response", new BaseResponse());
    }
    log.info("Got depts: " + depts.size());
    /* populate the object */
    DepartmentResponse resp = DepartmentResponse.populateDepartments(depts);
    return new ModelAndView("departments/home", "response", resp);
  }

}
