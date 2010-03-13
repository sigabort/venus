package com.venus.controller.adapters;

import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class VenusDateAdapter extends XmlAdapter<String, Date> {

  @Override
  public String marshal(Date date) throws Exception {
    return (date == null)? "" : date.toString();
  }

  @Override
  public Date unmarshal(String date) throws Exception {
    return (date == null)? null : new Date(date);
  }

}