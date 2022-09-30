package com.bvirtuoso.hari.model.jpa;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dishInfo")
public class DishInfoJpa {

  @Id
  @GeneratedValue(generator = "sequence-generator-dish")
  @GenericGenerator(
          name = "sequence-generator-dish",
          strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
          parameters = {
                  @org.hibernate.annotations.Parameter(name = "sequence_name", value = "user_sequence_dish"),
                  @org.hibernate.annotations.Parameter(name = "initial_value", value = "20"),
                  @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
          }
  )
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
