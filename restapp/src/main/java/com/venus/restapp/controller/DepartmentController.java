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
import com.venus.restapp.response.DepartmentResponse;
import com.venus.restapp.response.error.ResponseException;



@Controller
@RequestMapping(value="/departments")
public class DepartmentController {
  @Autowired
  private DepartmentService departmentService;

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
    System.out.println("\n\n----------------Adding department: ---------------------\n" + departmentRequest.getName());
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
      return new ModelAndView("departments/home");
    }
    System.out.println("\n\n----------------Fetching department: ---------------------\n" + name);
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
    System.out.println("\n\n----------------Got dept: ---------------------\n" + name);
    /* populate the object */
    DepartmentResponse resp = DepartmentResponse.populateDepartment(dept);
    return new ModelAndView("departments/department", "response", resp);
  }

  @RequestMapping(method=RequestMethod.GET)
  public String getDepartments(Model model) {
    return "departments/home";
  }

}
