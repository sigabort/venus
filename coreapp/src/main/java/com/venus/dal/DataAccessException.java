package com.venus.dal;

public class DataAccessException extends Exception {

  public DataAccessException(String errStr, Throwable err) {
    super(errStr, err);
  }

  public DataAccessException(String errStr, Exception err) {
    super(errStr, err);
  }

  public DataAccessException(Throwable e) {
    super(e);
  }

  public DataAccessException(Exception e) {
    super(e);
  }

  public DataAccessException(String e) {
    super(e);
  }
}
