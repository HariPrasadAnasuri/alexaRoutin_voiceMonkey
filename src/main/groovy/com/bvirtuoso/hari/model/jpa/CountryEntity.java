package com.bvirtuoso.hari.model.jpa;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "country")
public class CountryEntity {
  @Id
  @GeneratedValue(generator = "sequence-generator-country")
  @GenericGenerator(
          name = "sequence-generator-country",
          strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
          parameters = {
                  @org.hibernate.annotations.Parameter(name = "sequence_name", value = "user_sequence_country"),
                  @org.hibernate.annotations.Parameter(name = "initial_value", value = "207"),
                  @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
          }
  )
  private long id;

  @Column(name = "country")
  private String country;

  @Column(name = "capitalAnswered", columnDefinition="Boolean default false")
  private Boolean capitalAnswered = false;

  @Column(name = "currencyAnswered", columnDefinition="Boolean default false")
  private Boolean currencyAnswered = false;

  //  Number of times accessed the alexa service
  @Column(name = "count", columnDefinition="Integer default 0")
  private Integer count = 0;

  @OneToOne(cascade = CascadeType.ALL, mappedBy = "country")
  @JsonManagedReference
  private CapitalEntity capital;

  public CountryEntity() {

  }

  public CountryEntity(long id, String country, Boolean capitalAnswered, Boolean currencyAnswered) {
    this.id = id;
    this.country = country;
    this.capitalAnswered = capitalAnswered;
    this.currencyAnswered = currencyAnswered;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public Boolean getCapitalAnswered() {
    return capitalAnswered;
  }

  public void setCapitalAnswered(Boolean capitalAnswered) {
    this.capitalAnswered = capitalAnswered;
  }

  public Boolean getCurrencyAnswered() {
    return currencyAnswered;
  }

  public void setCurrencyAnswered(Boolean currencyAnswered) {
    this.currencyAnswered = currencyAnswered;
  }

  public CapitalEntity getCapital() {
    return capital;
  }

  public void setCapital(CapitalEntity capitalEntity) {
    this.capital = capitalEntity;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }
}
