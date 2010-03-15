package com.venus.controller.handler;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.servlet.http.HttpServletRequest;

import org.jboss.resteasy.annotations.Form;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import org.apache.log4j.Logger;

import com.venus.controller.service.DepartmentService;
import com.venus.controller.service.ProgramService;
import com.venus.controller.request.BaseRequest;
import com.venus.controller.request.DepartmentRequest;
import com.venus.controller.request.RequestValidator;

import com.venus.controller.response.BaseResponse;
import com.venus.controller.response.ProgramResponse;
import com.venus.controller.response.error.ResponseException;
import com.venus.controller.util.ConfigParams;

@Controller
@Path(ConfigParams.DEPT_HANDLER_URL)
public class DepartmentHandler
{
  @Autowired
  DepartmentService deptService;
  @Autowired
  ProgramService progService;

  private static final Logger log = Logger.getLogger(DepartmentHandler.class);

   @POST
   @PUT
   @Produces(MediaType.TEXT_HTML)
   public ModelAndView createUpdateDepartment(@Form DepartmentRequest req, @Context HttpServletRequest servletReq) throws Exception {
     try {
       RequestValidator.validate(req);
     }
     catch (ResponseException re) {
       log.error("createUpdateDept: request validation failed");
       return new ModelAndView("createDepartment", "response", re.getResponse());
     }
     log.info("I am going to create/update dept: " + req.getName());
     Object dept = deptService.createUpdateDepartment(req);
     return  new ModelAndView("redirect:" + ConfigParams.DEPT_HANDLER_URL + "/" + req.getName());     
   }


   @GET
   @Produces(MediaType.TEXT_HTML)
   public ModelAndView viewAll(@Form BaseRequest req, @Context HttpServletRequest servletReq) throws Exception {
     log.info("I am in  departments home");
     List deptList = deptService.getDepartments(req.getStartIndex(), req.getItemsPerPage());
     return new ModelAndView("departments", "departments", deptList);
   }

   @GET
   @Produces(MediaType.TEXT_HTML)
   @Path("/{name}")
   public ModelAndView defaultHandler(@PathParam("name") String name) throws Exception {
     log.info("I am in get department req: " + name);
     Object dept = deptService.getDepartment(name);
     return new ModelAndView("department", "department", dept);
   }
  
   @GET
   @Produces("application/json")
   @Path("/{name}/programs")
   public BaseResponse getPrograms(@PathParam("name") String name, @Form BaseRequest req) throws Exception {
     log.info("I am in get programs for dept: " + name);
     ProgramResponse resp = new ProgramResponse();
     List programs = null;
     try {
       programs = progService.getPrograms(name, req.getStartIndex(), req.getItemsPerPage());
     }
     catch (ResponseException re) {
       return re.getResponse();
     }
     resp.populatePrograms(programs);
     return resp;
   }

}
