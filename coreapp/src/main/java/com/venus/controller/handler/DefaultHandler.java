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

@Controller
@Path(DefaultHandler.DEFAULT_URL)
public class DefaultHandler
{
  public static final String DEFAULT_URL = "/";

  private static final Logger log = Logger.getLogger(DefaultHandler.class);

   @GET
   @Produces(MediaType.TEXT_HTML)
   public ModelAndView viewAll()
   {
     log.info("I am in  req");
     return new ModelAndView("home");
   }

   @GET
   @Produces(MediaType.TEXT_HTML)
   @Path("/{resource}")
   public ModelAndView defaultHandler(@PathParam("resource") String resource)
   {
     log.info("I am in default req: " + resource);
     return new ModelAndView(resource);
   }

}
