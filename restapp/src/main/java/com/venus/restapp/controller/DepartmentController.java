package com.venus.restapp.controller;

import java.util.Map;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import com.venus.restapp.response.RestResponse;
import com.venus.restapp.response.ResponseBuilder;
import com.venus.restapp.response.error.RestResponseException;
import com.venus.restapp.response.dto.DepartmentDTO;
import com.venus.restapp.util.RestUtil;

import org.apache.log4j.Logger;

import com.venus.util.VenusSession;
import com.venus.model.Department;

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

  @RequestMapping(value="create", method=RequestMethod.GET)
  public String getCreateForm(Model model) {
    model.addAttribute("department", new DepartmentRequest());
    return "departments/create";
  }

  @RequestMapping(method=RequestMethod.POST)
  public ModelAndView create(@ModelAttribute("department") DepartmentRequest departmentRequest, BindingResult result, HttpServletRequest req) {
    RestUtil.validateRequest(departmentRequest, req, result);

    /* if there is any error, build the response and send over */
    if (result.hasErrors()) {
      BaseResponse resp = ResponseBuilder.createResponse(HttpStatus.BAD_REQUEST, result);
      return RestUtil.buildVenusResponse("departments/create", resp);
    }

    VenusSession vs = RestUtil.getVenusSession(req);
    Department dept = null;

    try {
      dept = departmentService.createUpdateDepartment(departmentRequest, vs);
    }
    catch (RestResponseException re) {
      return RestUtil.buildErrorResponse("departments/create", re, result);
    }
    RestResponse resp = ResponseBuilder.createResponse(dept, new DepartmentDTO());
    return RestUtil.buildVenusResponse("departments/department", resp);
  }

  @RequestMapping(value="{name}", method=RequestMethod.GET)
  public ModelAndView getDepartment(@PathVariable String name, BaseRequest request, BindingResult result, HttpServletRequest req) {
    RestUtil.validateRequest(request, req, result);

    /* if there is any error, build the response and send over */
    if (result.hasErrors()) {
      BaseResponse resp = ResponseBuilder.createResponse(HttpStatus.BAD_REQUEST, result);
      return RestUtil.buildVenusResponse("departments/department", resp);
    }

    VenusSession vs = RestUtil.getVenusSession(req);
    Department dept = null;

    try {
      dept = departmentService.getDepartment(name, request, vs);
    }
    catch (RestResponseException re) {
      return RestUtil.buildErrorResponse("departments/department", re, result);
    }
    if (dept == null) {
      RestResponseException re = new RestResponseException(null, HttpStatus.NOT_FOUND, "Department with name: " + name + ", not found");
      return RestUtil.buildErrorResponse("departments/department", re, result);
    }

    RestResponse resp = ResponseBuilder.createResponse(dept, new DepartmentDTO());
    return RestUtil.buildVenusResponse("departments/department", resp);
  }

  @RequestMapping(method=RequestMethod.GET)
  public ModelAndView getDepartments(BaseRequest request, BindingResult result, HttpServletRequest req) {
    RestUtil.validateRequest(request, req, result);

    /* if there is any error, build the response and send over */
    if (result.hasErrors()) {
      BaseResponse resp = ResponseBuilder.createResponse(HttpStatus.BAD_REQUEST, result);
      return RestUtil.buildVenusResponse("departments/department", resp);
    }

    VenusSession vs = RestUtil.getVenusSession(req);
    
    List depts = null;
    Integer totalCount = null;

    try {
      depts = departmentService.getDepartments(request, vs);
      /* get the total departments count */
      totalCount = departmentService.getDepartmentsCount(request, vs);
    }
    catch (RestResponseException re) {
      return RestUtil.buildErrorResponse("departments/home", re, result);
    }
    /* populate the response object */
    RestResponse resp = ResponseBuilder.createResponse(depts, totalCount, new DepartmentDTO());
    return RestUtil.buildVenusResponse("departments/home", resp);
  }

}
