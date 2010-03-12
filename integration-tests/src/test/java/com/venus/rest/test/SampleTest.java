package com.venus.rest.test;

import org.wiztools.restclient.Request;
import org.wiztools.restclient.Response;
import org.wiztools.restclient.RequestBean;
import org.wiztools.restclient.HTTPMethod;
import org.wiztools.restclient.View;
import org.wiztools.restclient.Implementation;
import org.wiztools.restclient.RequestExecuter;
import org.wiztools.restclient.RESTTestCase;

import org.junit.Assert;

public class SampleTest extends RESTTestCase {

  public void testRequestContainsWizTools() throws Exception {
    // Step 1: Create the request:
    RequestBean requestBean = new RequestBean();
    requestBean.setUrl(new java.net.URL("http://localhost:8080/venus/"));
    requestBean.setMethod(HTTPMethod.GET);
    Request request = requestBean;

    // Step 2: Write the handler
    View view = new View() {
	@Override
	  public void doStart(Request request){
          // do nothing!
	}

	@Override
	public void doResponse(Response response){
	  Assert.assertEquals("Response Code", 200, response.getStatusCode());
          System.out.println(response.getResponseBody());
	}
	@Override
	  public void doCancelled(){
          // do nothing!
	}

	@Override
	  public void doEnd(){
          // do nothing!
	}

	@Override
	  public void doError(final String error){
          System.err.println(error);
	}
      };
    
    // Step 3: Execute:
    RequestExecuter executer = Implementation.of(RequestExecuter.class);
    executer.execute(request, view);
    
  }
  
}
