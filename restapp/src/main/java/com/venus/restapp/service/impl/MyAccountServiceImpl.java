package com.venus.restapp.service.impl;

import java.util.List;
import java.util.ArrayList;

import com.venus.restapp.service.MyAccountService;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.venus.restapp.controller.MyAccount;

@Service("myAccountService")
public class MyAccountServiceImpl implements MyAccountService {
  public List<MyAccount> findAll() {
    MyAccount acc = new MyAccount();
    List list = new ArrayList<MyAccount>();
    list.add(acc);
    return list;
  }
  public void createAccount(MyAccount acc) {
    //dummy to test the auth
  }

}
