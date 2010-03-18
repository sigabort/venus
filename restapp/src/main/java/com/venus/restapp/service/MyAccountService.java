package com.venus.restapp.service;

import java.util.List;

import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

import com.venus.restapp.controller.MyAccount;

public interface MyAccountService {
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public List<MyAccount> findAll();
  @PreAuthorize("hasRole('ROLE_USER')")
  public void createAccount(MyAccount acc);
}
