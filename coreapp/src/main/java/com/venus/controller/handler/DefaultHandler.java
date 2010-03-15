package com.venus.controller.handler;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import org.apache.log4j.Logger;

import com.venus.controller.util.ConfigParams;

@Controller
@Path(ConfigParams.DEFAULT_URL)
public class DefaultHandler
{
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
