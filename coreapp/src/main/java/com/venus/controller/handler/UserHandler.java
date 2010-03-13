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

import com.venus.controller.service.UserService;
import com.venus.controller.request.UserRequest;
import com.venus.controller.util.ConfigParams;

@Controller
@Path(ConfigParams.USERS_URL)
public class UserHandler
{
  @Autowired
  UserService service;

  private static final Logger log = Logger.getLogger(UserHandler.class);

   @POST
   @PUT
   @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
   public ModelAndView createUpdateUser(@Form UserRequest req, @Context HttpServletRequest servletReq)
   {
     log.info("I am going to create/update user: " + req.getUsername());
     Object user = service.createUpdateUser(req);
     return  new ModelAndView("redirect:" + ConfigParams.USERS_URL + "/" + req.getUsername());     
   }

   @GET
   @Produces(MediaType.TEXT_HTML)
   public ModelAndView viewAll(@Form UserRequest req, @Context HttpServletRequest servletReq)
   {
     log.info("I am in  req");
     List users = service.getUsers(req.getStartIndex(), req.getItemsPerPage());
     return new ModelAndView("users", "users", users);
   }

   @GET
   @Produces(MediaType.TEXT_HTML)
   @Path("/{username}")
   public ModelAndView defaultHandler(@PathParam("username") String username)
   {
     log.info("I am in default req: " + username);
     Object user = (Object) service.getUser(username);
     List resp = new ArrayList<Object>();
     if (user != null) {
       resp.add(user);
     }
     return new ModelAndView("users", "users", resp);
   }

}
