package com.venus.restapp.controller;

import java.util.Map;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import net.sf.oval.exception.ConstraintsViolatedException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
//import com.venus.restapp.response.CourseResponse;
import com.venus.restapp.response.error.ResponseException;
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
    model.addAttribute("courseRequest", new CourseRequest());
    return "courses/createCourse";
  }

  @RequestMapping(method=RequestMethod.POST)
  public ModelAndView create(CourseRequest courseRequest, HttpServletRequest request) throws ResponseException {
    validateRequest(courseRequest);
    VenusSession vs = RestUtil.getVenusSession(request);
    CourseImpl course = courseService.createUpdateCourse(courseRequest, vs);
//    CourseResponse resp = null;
//    if (course != null) {
//      resp = CourseResponse.createCourseResponse(course);
//    }
    return RestUtil.buildVenusResponse("courses/course", null);
  }
  
  private void validateRequest(Object request) {
    Validator validator = new Validator();
    List<ConstraintViolation> constraints = validator.validate(request);
    if (constraints != null && constraints.size() > 0) {
      throw new ConstraintsViolatedException(constraints);
    }
  }
  
  @ExceptionHandler(ConstraintsViolatedException.class)
  public ModelAndView handleException(ConstraintsViolatedException ex) {
    System.out.println("*******I got constraint violated exception*******");
    return null;
  }
  
  
}