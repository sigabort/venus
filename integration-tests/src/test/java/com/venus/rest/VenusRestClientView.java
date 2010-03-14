package com.venus.rest;

import org.wiztools.restclient.Request;
import org.wiztools.restclient.Response;
import org.wiztools.restclient.View;
import org.wiztools.restclient.ViewAdapter;

public class VenusRestClientView extends ViewAdapter {
  Response resp = null;
  Boolean cancelled = false;
  Boolean error = false;
  String errStr = null;
  Boolean started = false;
  Boolean finished = false;

  public static VenusRestClientView getView() {
    return new VenusRestClientView();
  }
  public void doStart(Request request) {
    this.started = true;
  }

  public void doResponse(Response response) {
    this.resp = response;
  }

  public void doCancelled() {
    this.cancelled = true;
  }

  public void doEnd() {
    this.finished = true;
  }

  public void doError(String error) {
    this.error = true;
    this.errStr = error;
  }

  public Response getResponse() {
    return this.resp;
  }

  public Boolean isStarted() {
    return this.started;
  }

  public Boolean isFinished() {
    return this.finished;
  }

  public Boolean isError() {
    return this.error;
  }

  public Boolean isCancelled() {
    return this.cancelled;
  }

  public String getError() {
    return this.errStr;
  }

}