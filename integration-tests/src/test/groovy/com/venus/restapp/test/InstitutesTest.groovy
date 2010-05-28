package com.venus.restapp.test;

import com.venus.restapp.VenusRestJSONClient;

import org.junit.Before;
import org.junit.Test;
import org.apache.http.HttpStatus;
import org.junit.Assert;

import java.util.Map;

/**
 * Institute related tests
 */
public class InstitutesTest extends AbstractTest {

  private VenusRestJSONClient client = null;
  
  @Before
  public void setUp() {
    client = new VenusRestJSONClient();
  }
  
  
  /* test create institute, fetch and check */
  @Test
  public void testCreateInstitute() {
    String name = "testCreateInstitute-" + getRandomString();
    def params = buildInstituteOptionalParams(name);

    /* create institute */
    def resp = client.createParentInstitute(name, params);
    testNoErrors(resp);
    def institute = resp?.entry;
    params['name'] = name;
    testInstituteDetails(institute, params);

    /* get the institute now and check */
    resp = client.getInstitute(name, null);
    testNoErrors(resp);
    institute = resp?.entry;
    testInstituteDetails(institute, params);
  }

  /**
   * Create one test institute and return that institute
   * This can be used by other tests to create a institute quickly
   */
  public static Object createTestInstitute(myClient, name) {
    def params = buildInstituteOptionalParams(name);

    /* create institute */
    def resp = myClient.createInstitute(name, params);
    testNoErrors(resp);
    def institute = resp?.entry;
    params['name'] = name;
    testInstituteDetails(institute, params);

    return institute;
  }

  /**
   * @param name  The name of the institute
   * @return      The map of the optional parameters
   */
  public static Map buildInstituteOptionalParams(name) {
    def code = name + "-code";
    def dispName = name + "-dispName";
    def desc = name + "-desc";
    def photoUrl = "http://venus.com/pics/121";
    def email = "sigabort@venus.com";
    def params = [code:code, displayName:dispName, description:desc, photoUrl:photoUrl, email:email];
    return params;
  }
  
  /**
   * Test the institute details
   * @param institute        The institute object to test
   * @param params      The parameters containing the expected results
   */
  public static void testInstituteDetails(institute, params) {
    Assert.assertEquals("The institute name", params['name'], institute?.name);
    Assert.assertEquals("The institute code", params['code'], institute?.code);
    Assert.assertEquals("The institute display name", params['displayName'], institute?.displayName);
    Assert.assertEquals("The institute description", params['description'], institute?.description);
    Assert.assertEquals("The institute photoUrl", params['photoUrl'], institute?.photoUrl);
    Assert.assertEquals("The institute email", params['email'], institute?.email);
  }
  
}
