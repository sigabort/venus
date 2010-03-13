package com.venus.controller.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import com.venus.controller.util.ConfigParams;

/**
 * This is just to make sure we are setting proper context root
 *
 * @author sigabort
 */
public class VenusFreemarkerView extends FreeMarkerView {

  @Override
  protected void doRender(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
    /* set the customized context path - because we had to add extra context param other than
     * the base context root. This makes it easier at the view end to refer to that.
     */
    model.put("contextPath", request.getContextPath() + ConfigParams.DEFAULT_CTX_ROOT);

    /* we send the root context path too - for accessing absolute paths */
    model.put("rootContextPath", request.getContextPath());

    // Now we have what we want, render the correct view.
    super.doRender(model, request, response);
  }

}