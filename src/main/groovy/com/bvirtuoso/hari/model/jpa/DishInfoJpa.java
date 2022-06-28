package com.bvirtuoso.hari.model.jpa;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dishInfo")
public class DishInfoJpa {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column(name = "local_date_time", columnDefinition = "TIMESTAMP")
  private LocalDateTime localDateTime;

  @Column(name = "local_date")
  private LocalDate localDate;

  @Column(name = "name")
  private String name;

  @Column(name = "timeOfDay")
  private String timeOfDay;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public LocalDateTime getLocalDateTime() {
    return localDateTime;
  }

  public void setLocalDateTime(LocalDateTime localDateTime) {
    this.localDateTime = localDateTime;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTimeOfDay() {
    return timeOfDay;
  }

  public void setTimeOfDay(String timeOfDay) {
    this.timeOfDay = timeOfDay;
  }

  public LocalDate getLocalDate() {
    return localDate;
  }

  public void setLocalDate(LocalDate localDate) {
    this.localDate = localDate;
  }
}
