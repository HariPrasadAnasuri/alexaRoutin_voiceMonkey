package com.bvirtuoso.hari.model;

import java.math.BigDecimal;

public class PersonInfo {
  private String name;
  private BigDecimal weight;
  private String note;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getWeight() {
    return weight;
  }

  public void setWeight(BigDecimal weight) {
    this.weight = weight;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }
}
