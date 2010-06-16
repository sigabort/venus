package com.venus.restapp.request.validation;

import net.sf.oval.configuration.annotation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Constraint(checkWith = ValidStatusCheck.class)
public @interface ValidStatus {
  /**
  * message to be used for the ConstraintsViolatedException 
  *
  * @see ConstraintsViolatedException
  */
  String message() default "status is not valid";
}
