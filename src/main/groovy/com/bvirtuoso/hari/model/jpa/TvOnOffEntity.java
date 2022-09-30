package com.bvirtuoso.hari.model.jpa;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "TvOnOff")
public class TvOnOffEntity {

  @Id
  @GeneratedValue(generator = "sequence-generator-tvonoff")
  @GenericGenerator(
          name = "sequence-generator-tvonoff",
          strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
          parameters = {
                  @org.hibernate.annotations.Parameter(name = "sequence_name", value = "user_sequence_tvonoff"),
                  @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                  @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
          }
  )
  private long id;

  private Boolean tvStatus;
  private Timestamp activityTime;

  public TvOnOffEntity() {

  }

  public TvOnOffEntity(Integer id, Boolean tvStatus, Timestamp activityTime) {
    this.id = id;
    this.tvStatus = tvStatus;
    this.activityTime = activityTime;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Boolean getTvStatus() {
    return tvStatus;
  }

  public void setTvStatus(Boolean tvStatus) {
    this.tvStatus = tvStatus;
  }

  public Timestamp getActivityTime() {
    return activityTime;
  }

  public void setActivityTime(Timestamp activityTime) {
    this.activityTime = activityTime;
  }
}
