package com.venus.restapp.request.validation;

import net.sf.oval.Validator;
import net.sf.oval.configuration.annotation.AbstractAnnotationCheck;
import net.sf.oval.context.OValContext;

import com.venus.model.Status;

public class ValidStatusCheck extends AbstractAnnotationCheck<ValidStatus> {
  
  public boolean isSatisfied(Object validatedObject, Object valueToValidate, OValContext context, Validator validator) {
    if (valueToValidate == null) return true;

    String val = valueToValidate.toString();
    
    try {
      Status status = Status.valueOf(val);
      return true;
    }
    catch (Exception e) {
      return false;
    }
  }
}

