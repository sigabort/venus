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

import com.venus.controller.service.ProgramService;
import com.venus.controller.request.BaseRequest;
import com.venus.controller.request.ProgramRequest;

@Controller
@Path(ProgramHandler.PROGRAM_HANDLER_URL)
public class ProgramHandler {

  public static final String PROGRAM_HANDLER_URL = "/programs";
  @Autowired
  ProgramService programService;

  private static final Logger log = Logger.getLogger(ProgramHandler.class);

   @POST
   @PUT
   @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
   public ModelAndView createUpdateProgram(@Form ProgramRequest req, @Context HttpServletRequest servletReq) {
     log.info("I am going to create/update program: " + req.getName());
     Object program = programService.createUpdateProgram(req);
     return  new ModelAndView("redirect:" + PROGRAM_HANDLER_URL + "/" + req.getName());     
   }


   @GET
   @Produces(MediaType.TEXT_HTML)
   @Path("/{dept}")
     public ModelAndView viewAll(@PathParam("dept") String deptName,
				 @Form BaseRequest req, @Context HttpServletRequest servletReq) {
     log.info("I am in  programs home for dept: " + deptName + ", [" + req.getStartIndex() + ", " + req.getItemsPerPage() + "]");
     List programList = programService.getPrograms(deptName, req.getStartIndex(), req.getItemsPerPage());
     return new ModelAndView("programs", "programs", programList);
   }

   @GET
   @Produces(MediaType.TEXT_HTML)
   public ModelAndView viewAll(@Form BaseRequest req, @Context HttpServletRequest servletReq) {
     log.info("I am in  programs for all departments...[" + req.getStartIndex() + ", " + req.getItemsPerPage() + "]");
     List programList = programService.getPrograms(req.getStartIndex(), req.getItemsPerPage());
     return new ModelAndView("deptPrograms", "deptPrograms", programList);
   }

   @GET
   @Produces(MediaType.TEXT_HTML)
   @Path("/{dept}/{name}")
   public ModelAndView defaultHandler(@PathParam("name") String name, 
				      @PathParam("dept") String deptName) {
     log.info("I am in get program req: " + name + ", for dept: " + deptName);
     Object program = programService.getProgram(name, deptName);
     return new ModelAndView("program", "program", program);
   }

}
