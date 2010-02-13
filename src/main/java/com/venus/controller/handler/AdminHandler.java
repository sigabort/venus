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

import org.jboss.resteasy.annotations.Form;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import org.apache.log4j.Logger;

import com.venus.controller.service.UserService;

@Controller
@Path(AdminHandler.ADMIN_URL)
public class AdminHandler
{
  public static final String ADMIN_URL = "/app/admin";
  @Autowired
  UserService service;
  private static final Logger log = Logger.getLogger(AdminHandler.class);

   @GET
   @Produces(MediaType.TEXT_HTML)
   public ModelAndView viewAll()
   {
     System.out.println("I am in admin  req");
     Object user = (Object)service.getUser("ravi-ZDNbYrLqMlAyvvfswjSDNa3o6");
     List resp = new ArrayList<Object>();
     if (user != null) {
       resp.add(user);
     }
     return new ModelAndView("users", "users", resp);
   }

   @GET
   @Produces(MediaType.TEXT_HTML)
   @Path("/{resource}")
   public ModelAndView defaultHandler(@PathParam("resource") String resource)
   {
     System.out.println("I am in default admin req: " + resource);
     return new ModelAndView(resource);
   }

}
