package com.venus.restapp.controller;

import java.util.Map;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import net.sf.oval.exception.ConstraintsViolatedException;
import net.sf.oval.integration.spring.SpringValidator;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.venus.restapp.service.CourseService;
import com.venus.restapp.request.CourseRequest;
import com.venus.restapp.request.BaseCourseRequest;
import com.venus.restapp.request.BaseRequest;
import com.venus.restapp.response.BaseResponse;
import com.venus.restapp.response.RestResponse;
import com.venus.restapp.response.ResponseBuilder;
import com.venus.restapp.response.dto.CourseDTO;
import com.venus.restapp.response.error.ResponseException;
import com.venus.restapp.response.error.RestResponseException;
import com.venus.restapp.util.RestUtil;

import org.apache.log4j.Logger;

import com.venus.model.impl.CourseImpl;
import com.venus.util.VenusSession;

/**
 * Controller class for handling all courses related requests.
 * Handles REST requests: create/update/delete/get
 * 
 */
@Controller("courseController")
@RequestMapping(value="/courses")
public class CourseController {
  @Autowired
  private CourseService courseService;
  private static final Logger log = Logger.getLogger(CourseController.class);

  @RequestMapping(value="create", method=RequestMethod.GET)
  public String getCreateForm(Model model) {
    model.addAttribute("course", new CourseRequest());
    return "courses/create";
  }

  @RequestMapping(method=RequestMethod.POST)
  public ModelAndView create(@ModelAttribute("course") CourseRequest courseRequest, BindingResult result, HttpServletRequest request) {
    RestUtil.validateRequest(courseRequest, request, result);

    /* if there is any error, build the response and send over */
    if (result.hasErrors()) {
      BaseResponse resp = ResponseBuilder.createResponse(HttpStatus.BAD_REQUEST, result);
      return RestUtil.buildVenusResponse("courses/create", resp);
    }

    VenusSession vs = RestUtil.getVenusSession(request);
    CourseImpl course = null;
    /* create the course now */
    try {
      course = courseService.createUpdateCourse(courseRequest, vs);
    }
    catch (RestResponseException re) {
      result.rejectValue(re.getField(), re.getErrorCode().toString(), re.getMessage());
      BaseResponse resp = ResponseBuilder.createResponse(re.getErrorCode(), result);
      return RestUtil.buildVenusResponse("courses/create", resp);
    }
    RestResponse resp = ResponseBuilder.createResponse(course, new CourseDTO());
    return RestUtil.buildVenusResponse("courses/course", resp);
  }

}