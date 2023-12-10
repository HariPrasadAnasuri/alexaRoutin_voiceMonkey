package com.bvirtuoso.hari.model.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Timestamp;

@Entity
@Table(name = "PersonAvailability")
public class PersonAvailabilityEntity {

  @Id
  @GeneratedValue(generator = "sequence-generator-personAvailability")
  @GenericGenerator(
          name = "sequence-generator-personAvailability",
          strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
          parameters = {
                  @org.hibernate.annotations.Parameter(name = "sequence_name", value = "user_sequence_personAvailability"),
                  @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                  @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
          }
  )
  private long id;

  private String availability;
  private Timestamp activityTime;

  public PersonAvailabilityEntity() {

  }

  public PersonAvailabilityEntity(Integer id, String availability, Timestamp activityTime) {
    this.id = id;
    this.availability = availability;
    this.activityTime = activityTime;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getAvailability() {
    return availability;
  }

  public void setAvailability(String availability) {
    this.availability = availability;
  }

  public Timestamp getActivityTime() {
    return activityTime;
  }

  public void setActivityTime(Timestamp activityTime) {
    this.activityTime = activityTime;
  }
}
