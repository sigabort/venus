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

import com.venus.controller.service.UserService;

@Controller
@Path(UserHandler.USERS_URL)
public class UserHandler
{
  public static final String USERS_URL = "/venus";
  @Autowired
  UserService service;

   @GET
   @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
   @Path("/data/{username}")
   public ModelAndView get(@PathParam("username") String userName)
   {
     System.out.println("I am in /data/username: " + userName);
     Object user = (Object)service.getUser(userName);
     List resp = new ArrayList<Object>();
     if (user != null) {
       resp.add(user);
     }
     return new ModelAndView("users", "users", resp);     
   }

   @GET
   @Produces(MediaType.TEXT_HTML)
   public ModelAndView viewAll()
   {
     System.out.println("I am in  req");
     Object user = (Object)service.getUser("ravi-ZDNbYrLqMlAyvvfswjSDNa3o6");
     List resp = new ArrayList<Object>();
     if (user != null) {
       resp.add(user);
     }
     return new ModelAndView("users", "users", resp);
   }
}
