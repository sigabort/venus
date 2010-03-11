package com.venus.controller.error;


public enum HttpStatusCode {
  OK(200),
  BAD_REQUEST(400),
  UNAUTHORISED(401);

  private int bit;
  public int getBit() {
    return this.bit;
  }
 
  HttpStatusCode(int bit) {
    this.bit = bit;
  }

}
