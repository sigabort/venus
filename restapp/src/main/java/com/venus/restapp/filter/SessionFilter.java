package com.venus.restapp.filter;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;

import javax.servlet.ServletException;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.venus.restapp.service.InstituteService;
import com.venus.restapp.request.BaseRequest;
import com.venus.restapp.response.error.ResponseException;
import com.venus.restapp.auth.RestUserDetails;
import com.venus.restapp.util.RestParams;

import com.venus.model.Institute;
import com.venus.util.VenusSession;
import com.venus.util.VenusSessionFactory;

import java.util.List;


public class SessionFilter extends GenericFilterBean {
  @Autowired
  private InstituteService instituteService;

  private static final Logger log = Logger.getLogger(SessionFilter.class);
  private static ThreadLocal requestStore = new ThreadLocal();

  public void doFilter(ServletRequest request, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
    System.out.println("------In Session Filter-----"); 
    Institute institute = null;
    try {
      /* do the work this filter intends to do 
       * Get the institute depends on the request
       */
      institute = getInstitute(request, resp);
    }
    catch (ResponseException re) {
      throw new ServletException(re.getMessage());
    }
    /* Didn't get any institute :( stop the filter chain here. */
    if (institute == null) {
      log.error("Didnt get any institute for this request, stopping the request processing...");
      return;
    }
    VenusSession vs = VenusSessionFactory.getVenusSession(institute);
    request.setAttribute(RestParams.REST_REQUEST_SESSION_ATTR, vs);
    
    requestStore.set(request);
    
    /* delegate to next filter */
    chain.doFilter(request, resp);
  }
  
  public static ServletRequest getStoredRequest() {
    return (ServletRequest) requestStore.get();
  }
  public static void clearRequestStore() {
    requestStore.remove();
  }
  
  /**
   * Check if the user is logged in or not. If the user is logged in, the institute name
   * will be retrieved from the session. Even though, the institute id must be proper, 
   * check if the institute is already existing or not. If not, throw error. If not, use that.
   * 
   * If the user is not logged in, we need to fetch the institute in 2 ways:
   * (a) Institute id is set in the request parameters
   * (b) Institute id is not set.
   * <ul>
   *   <li>
   *     Institute Id is set: Check if the institute is existing or not. If exists, use it.
   *     If institute doesn't exist with the mentioned institute id, throw error. 
   *   </li>
   *   <li>
   *     Intitute Id is not set: For now, we check the URL of the request. Depends on the URL, we
   *     choose the institute. As, institute URL is set as code of the institute, check if the institute
   *     exists with the URL as code or not. If yes, use it. Otherwise, throw error.
   *   </li>
   * </ul>
   *
   * @param req
   * @param resp
   */
  private Institute getInstitute(ServletRequest req, ServletResponse resp) throws ResponseException {
    SecurityContext sc = SecurityContextHolder.getContext();
    Authentication auth = sc.getAuthentication();
    String instName = null;
    
    /* check if the user is authenticated or not. If yes, get the institute name
     * from the authentication object
     */
    if (auth != null && auth.isAuthenticated()) {
      RestUserDetails userDetails = (RestUserDetails) auth.getPrincipal();
      if (userDetails != null) {
        instName = userDetails.getInstitute();
        log.info("--------User[ " + userDetails.getUsername() + "] Inst[" + instName + "] is authenticated---------");
      }
    }
    /* Ok, as user is already logged-in, we have got the institute name to which the user belongs to */
    if (instName != null) {
      Institute institute = instituteService.getInstitute(instName, null);
      return institute;
    }
    
    
    /* see if the client sent request with institute code. If yes, try to fetch institute using
     * that code.
     */
    String code = req.getParameter(RestParams.REST_REQUEST_CODE_ATTR);
    if (StringUtils.isNotEmpty(code)) {
      BaseRequest br = new BaseRequest();
      br.setFilterBy("code");
      br.setFilterValue(code);
      br.setFilterOp("equals");
      
      List<Institute> institutes = instituteService.getInstitutes(br);
      if (institutes != null && institutes.size() > 0) {
        log.info("---------I have got institutes with code: " + code + " --------------[ " + institutes.size() + "]");
        return (Institute) institutes.get(0);
      }      
    }
    
    /* Request doesn't even have the code as parameter. So try mapping the 
     * request url with the code */
    String url = req.getServerName() + ":" + req.getServerPort();    
    log.info("---------Authentication hasn't happened yet, url[" + url + "]........---------");
    
    BaseRequest br = new BaseRequest();
    br.setFilterBy("code");
    br.setFilterValue(url);
    br.setFilterOp("equals");
    
    List<Institute> institutes = instituteService.getInstitutes(br);
    if (institutes != null && institutes.size() > 0) {
      log.info("---------I have got institutes with code: " + url + " --------------[ " + institutes.size() + "]");
      return (Institute) institutes.get(0);
    }
    log.info("No institutes with code: " + url + " --------------");      
    
    /* No institutes found with the given code. Now, get the default institute for this server */
    return getDefaultInstitute();
  }
  
  private Institute getDefaultInstitute() throws ResponseException {
    /* get only institutes which dont have parents, and sort by id in ascending order */
    BaseRequest br = new BaseRequest();
    br.setFilterBy("parent");
    br.setFilterValue(null);
    br.setFilterOp("equals");
    br.setSortBy("created");
    
    List<Institute> institutes = instituteService.getInstitutes(br);
    if (institutes != null && institutes.size() > 0) {
      log.info("---------I have got default institutes...----[ " + institutes.size() + "]");
      return (Institute) institutes.get(0);
    }
    return null;
  }
  
  
}