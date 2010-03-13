package com.venus.dal;

import java.util.Date;
import java.util.List;

import com.venus.model.Department;
import com.venus.model.Program;
import com.venus.util.VenusSession;

public interface ProgramOperations {
  
  /**
   * Create / update program
   */
  public abstract Program createUpdateProgram(String name, Department dept, String code, String desc, String prerequisites, Integer duration, Date created, Date lastModified, VenusSession session) throws DataAccessException;
  
  public abstract Program findProgramByName(String name, Department dept, VenusSession session) throws DataAccessException;

  public abstract Program findProgramByCode(String code, VenusSession session) throws DataAccessException;

  public abstract void deleteProgram(Program program, VenusSession session) throws DataAccessException;

  public abstract List<Program> getPrograms(Department dept, int offset, int maxRet, VenusSession vs) throws DataAccessException;
  
}
