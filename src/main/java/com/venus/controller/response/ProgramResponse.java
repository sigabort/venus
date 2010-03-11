package com.venus.controller.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonWriteNullProperties;
import javax.xml.bind.annotation.XmlElement;

import com.venus.controller.response.dto.ProgramDTO;
import com.venus.model.Program;

import java.util.List;
import java.util.ArrayList;

@XmlAccessorType(XmlAccessType.PROPERTY)
@JsonWriteNullProperties(value=false)
public class ProgramResponse extends BaseResponse {
  
  @XmlElement(name = "entry")
  private ArrayList<ProgramDTO> entries;
  private ProgramDTO entry;

  public ProgramResponse() {
    super();
  }
  
  public void setEntries(ArrayList<ProgramDTO> entries) {
    this.entries = entries;
  }
  
  public ArrayList<ProgramDTO> getEntries() {
    return this.entries;
  }
  
  public void setEntry(ProgramDTO entry) {
    this.entry = entry;
  }
  
  public ProgramDTO getEntry() {
    return this.entry;
  }


  public void populatePrograms(List programs) {
    ArrayList<ProgramDTO> list = null;
    if (programs != null && programs.size() > 0) {
      list = new ArrayList<ProgramDTO>(programs.size());
    }
    for (Object p :  programs) {
      ProgramDTO dto = ProgramDTO.getProgramDTO((Program)p);
      if (dto != null) {
	list.add(dto);
      }
    }
    this.setEntries(list);
  }
  
}