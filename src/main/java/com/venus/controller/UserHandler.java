package com.venus.controller;

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

@Controller
@Path(UserHandler.USERS_URL)
public class UserHandler
{
 
  public static final String USERS_URL = "/venus";

   @GET
   @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
   @Path("/data/{lastName}")
   public void get(@PathParam("lastName") String lastName)
   {
     System.out.println("I am in /data/lastname: " + lastName);
     return;
   }

   @GET
   @Produces(MediaType.TEXT_HTML)
   public ModelAndView viewAll()
   {
     System.out.println("I am in  req");
     List resp = new ArrayList<Map>();
     Map obj = new HashMap<String, String>();
     obj.put("firstName", "ravi");
     obj.put("lastName", "sandy");
     resp.add(obj);
     return new ModelAndView("users", "users", resp);
   }
}
