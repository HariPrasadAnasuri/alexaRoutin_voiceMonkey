package com.bvirtuoso.hari.model.jpa;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "healthInfo")
public class HealthInfoJpa {

  @Id
  @GeneratedValue(generator = "sequence-generator-health")
  @GenericGenerator(
          name = "sequence-generator-health",
          strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
          parameters = {
                  @Parameter(name = "sequence_name", value = "user_sequence_health"),
                  @Parameter(name = "initial_value", value = "331"),
                  @Parameter(name = "increment_size", value = "1")
          }
  )
  private long id;
  //org.hibernate.id.UUIDGenerator
  @Column(name = "local_date_time", columnDefinition = "TIMESTAMP")
  private LocalDateTime localDateTime;

  @Column(name = "local_date")
  private LocalDate localDate;

  @Column(name = "person")
  private String name;

  @Column(name = "weight")
  private BigDecimal weight;

  //  Number of times accessed the alexa service
  @Column(name = "note")
  private String note;

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

  public LocalDate getLocalDate() {
    return localDate;
  }

  public void setLocalDate(LocalDate localDate) {
    this.localDate = localDate;
  }
}
