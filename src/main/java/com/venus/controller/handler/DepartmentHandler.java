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

import com.venus.controller.response.BaseResponse;
import com.venus.controller.response.ProgramResponse;

@Controller
@Path(DepartmentHandler.DEPT_HANDLER_URL)
public class DepartmentHandler
{
  public static final String DEPT_HANDLER_URL = "/app/departments";
  @Autowired
  DepartmentService deptService;
  @Autowired
  ProgramService progService;

  private static final Logger log = Logger.getLogger(DepartmentHandler.class);

   @POST
   @PUT
   @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
   public ModelAndView createUpdateDepartment(@Form DepartmentRequest req, @Context HttpServletRequest servletReq)
   {
     log.info("I am going to create/update dept: " + req.getName());
     Object dept = deptService.createUpdateDepartment(req);
     return  new ModelAndView("redirect:" + DEPT_HANDLER_URL + "/" + req.getName());     
   }


   @GET
   @Produces(MediaType.TEXT_HTML)
   public ModelAndView viewAll(@Form BaseRequest req, @Context HttpServletRequest servletReq)
   {
     log.info("I am in  departments home");
     List deptList = deptService.getDepartments(req.getOffset(), req.getMaxReturn());
     return new ModelAndView("departments", "departments", deptList);
   }

   @GET
   @Produces(MediaType.TEXT_HTML)
   @Path("/{name}")
   public ModelAndView defaultHandler(@PathParam("name") String name)
   {
     log.info("I am in get department req: " + name);
     Object dept = deptService.getDepartment(name);
     return new ModelAndView("department", "department", dept);
   }
  
   @GET
   @Produces("application/json")
   @Path("/{name}/programs")
   public ProgramResponse getPrograms(@PathParam("name") String name, @Form BaseRequest req) {
     log.info("I am in get programs for dept: " + name);
     List programs = progService.getPrograms(name, req.getOffset(), req.getMaxReturn());
     ProgramResponse resp = new ProgramResponse();
     resp.populatePrograms(programs);
     return resp;
   }

}
