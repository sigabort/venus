package com.venus.restapp.controller;

import java.util.Map;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
import com.venus.restapp.request.BaseDepartmentRequest;
import com.venus.restapp.request.BaseRequest;
import com.venus.restapp.response.BaseResponse;
import com.venus.restapp.response.DepartmentResponse;
import com.venus.restapp.response.error.ResponseException;
import com.venus.restapp.util.RestUtil;

import org.apache.log4j.Logger;

/**
 * Controller class for handling all departments related requests.
 * Handles REST requests: create/update/delete/get
 * 
 */
@Controller("departmentController")
@RequestMapping(value="/departments")
public class DepartmentController {
  @Autowired
  private DepartmentService departmentService;
  private static final Logger log = Logger.getLogger(DepartmentController.class);

  /**
   * Get the Model object for creating the department
   * This is mostly used by the UI clients when they want to create a department
   * @param model        The model object for request
   * @return             The url location for view resolver to send the
   *                     proper view to the client with model object(DepartmentRequest) added to request
   */
  @RequestMapping(value="create", method=RequestMethod.GET)
  public String getCreateForm(Model model) {
    model.addAttribute("departmentRequest", new DepartmentRequest());
    return "departments/createDepartment";
  }

  /**
   * Create/Update the Department
   * @param departmentRequest   The DepartmentRequest object containing all parameters
   * @param result             The BindingResult object containing the errors if there
   *                            are any errors found while validating the request object
   * @return the ModelAndView object containing response of creation/updation of department.
   *             The response object is added as model object. This object contains information
   *             about the exceptions/errors(if any errors found) 
   */
  @RequestMapping(method=RequestMethod.POST)
  public ModelAndView create(@Valid DepartmentRequest departmentRequest, BindingResult result, HttpServletRequest req) {
    if (result.hasErrors()) {
      /* XXX: We need to populate the response with the actual errors. Need to check
       * how 'create' is populating the errors properly in case of invalid request.
       * We need to do same here too.
       */
      ResponseException re = new ResponseException(HttpStatus.BAD_REQUEST, "Bad request", null, null);
      return new ModelAndView("departments/createDepartment", "response", re.getResponse());
    }
    log.info("Adding/Updating department" + departmentRequest.getName());
    Object dept = null;
    try {
      dept = departmentService.createUpdateDepartment(departmentRequest, RestUtil.getVenusSession(req));
    }
    catch (ResponseException re) {
      return new ModelAndView("departments/createDepartment", "response", re.getResponse());
    }
    DepartmentResponse resp = DepartmentResponse.populateDepartment(dept);
    return new ModelAndView("departments/department", "response", resp);
  }

  /**
   * Send a particualr department details with given name in the institute.
   * @param name     The name of the department
   * @param request  The base request object containing all of the optional parameters
   * @param result   The binding result containing any errors if the request is bad
   * @return the ModelAndView object containing response with result of getting department information.
   *             The response object is added as model object. This object contains information
   *             about the exceptions/errors(if any errors found) 
   */
  @RequestMapping(value="{name}", method=RequestMethod.GET)
  public ModelAndView getDepartment(@PathVariable String name, @Valid BaseRequest request, BindingResult result, HttpServletRequest req) {
    if (result.hasErrors()) {
      /* XXX: We need to populate the response with the actual errors. Need to check
       * how 'create' is populating the errors properly in case of invalid request.
       * We need to do same here too.
       */
      ResponseException re = new ResponseException(HttpStatus.BAD_REQUEST, "Bad request", null, null);
      return new ModelAndView("departments/department", "response", re.getResponse());
    }
    log.info("Fetching department: " + name);
    Object dept = null;
    try {
      dept = departmentService.getDepartment(name, request, RestUtil.getVenusSession(req));
    }
    catch (ResponseException re) {
      log.info("Error while finding Department with name: " + name, re);
      return new ModelAndView("departments/department", "response", re.getResponse());
    }
    /* department not found? throw 404 */
    if (dept == null) {
      log.info("Department with name: " + name + " is not found...");
      ResponseException re = new ResponseException(HttpStatus.NOT_FOUND, "Department with name: " + name + ", not found", null, null);
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
   * @return the ModelAndView object containing response with result of getting departments.
   *             The response object is added as model object. This object contains information
   *             about the exceptions/errors(if any errors found) 
   */
  @RequestMapping(method=RequestMethod.GET)
  public ModelAndView getDepartments(@Valid BaseRequest request, BindingResult result, HttpServletRequest req) {
    if (result.hasErrors()) {
      /* XXX: We need to populate the response with the actual errors. Need to check
       * how 'create' is populating the errors properly in case of invalid request.
       * We need to do same here too.
       */
      ResponseException re = new ResponseException(HttpStatus.BAD_REQUEST, "Bad request", null, null);
      return new ModelAndView("departments/home", "response", re.getResponse());
    }

    log.info("Fetching all departments....");

    List depts = null;
    Integer totalCount = null;

    try {
      depts = departmentService.getDepartments(request, RestUtil.getVenusSession(req));
      /* get the total departments count */
      totalCount = departmentService.getDepartmentsCount(request, RestUtil.getVenusSession(req));
    }
    catch (ResponseException re) {
      return new ModelAndView("departments/home", "response", re.getResponse());
    }
    /* departments not found? send empty response. So, the client can take care of
     * what to do next
     */
    if (depts == null) {
      return new ModelAndView("departments/home", "response", new BaseResponse());
    }
    log.info("Got depts: " + depts.size());
    
    /* populate the response object */
    DepartmentResponse resp = DepartmentResponse.populateDepartments(depts, totalCount);
    return new ModelAndView("departments/home", "response", resp);
  }

}
