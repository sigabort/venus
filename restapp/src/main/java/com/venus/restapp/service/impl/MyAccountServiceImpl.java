package com.venus.restapp.service.impl;

import java.util.List;
import java.util.ArrayList;

import com.venus.restapp.service.MyAccountService;

import org.springframework.stereotype.Service;

import com.venus.restapp.controller.MyAccount;

import com.venus.dal.DepartmentOperations;
import com.venus.dal.impl.DepartmentOperationsImpl;
import com.venus.util.VenusSession;
import com.venus.util.VenusSessionFactory;
import com.venus.model.Department;
import com.venus.dal.DataAccessException;

import org.apache.log4j.Logger;

@Service("myAccountService")
public class MyAccountServiceImpl implements MyAccountService {

  private DepartmentOperations dol = new DepartmentOperationsImpl();
  private VenusSession vs = VenusSessionFactory.getVenusSession(null);
  private static final Logger log = Logger.getLogger(MyAccountServiceImpl.class);

  public List<MyAccount> findAll() {
    MyAccount acc = new MyAccount();
    List list = new ArrayList<MyAccount>();
    list.add(acc);
    return list;
    
  }
  public void createAccount(MyAccount acc) {
    try {
      List<Department> depts = dol.getDepartments(0, 100, null, vs);
      System.out.println("\n\n------------------------------------------\n\n");
      System.out.println("I have got the departments....." + depts.size());
    }
    catch (DataAccessException dae) {
      String errStr = "Error while getting departments";
      log.error(errStr, dae);
      throw new RuntimeException(errStr, dae);
    }
    //dummy to test the auth
  }

}
