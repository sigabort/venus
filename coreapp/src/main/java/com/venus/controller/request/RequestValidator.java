package com.venus.controller.request;

import java.util.List;

import net.sf.oval.Validator;
import net.sf.oval.ConstraintViolation;
import javax.ws.rs.core.Response;

import com.venus.controller.response.error.ResponseException;

public class RequestValidator {
  public static void validate(BaseRequest req) throws ResponseException {
    Validator validator = new Validator();
    // collect the constraint violations
    List<ConstraintViolation> violations = validator.validate(req);
    if (violations.size() > 0) {
      throw new ResponseException(Response.Status.BAD_REQUEST, violations, null);
    }
  }
}
