package com.venus.restapp.service;

import java.util.List;

import com.venus.model.Department;

import com.venus.restapp.request.DepartmentRequest;
import com.venus.restapp.request.BaseRequest;
import com.venus.restapp.response.error.ResponseException;

import org.springframework.security.access.prepost.PreAuthorize;

public interface DepartmentService {
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public abstract Department createUpdateDepartment(DepartmentRequest request) throws ResponseException;

  public abstract Department getDepartment(String name, BaseRequest request) throws ResponseException;

  public abstract List<Department> getDepartments(BaseRequest request) throws ResponseException;

  public abstract Integer getDepartmentsCount(BaseRequest request)  throws ResponseException;
}
