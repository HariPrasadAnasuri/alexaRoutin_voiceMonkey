package com.bvirtuoso.hari.model.jpa;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "capital")
public class CapitalEntity {
  @Id
  @GeneratedValue(generator = "sequence-generator-capital")
  @GenericGenerator(
          name = "sequence-generator-capital",
          strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
          parameters = {
                  @org.hibernate.annotations.Parameter(name = "sequence_name", value = "user_sequence_capital"),
                  @org.hibernate.annotations.Parameter(name = "initial_value", value = "207"),
                  @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
          }
  )
  private long id;
  @Column(name = "capital")
  private String capital;

  @Column(name = "currency")
  private String currency;

  @Column(name = "countryAnsweredForCapital", columnDefinition="Boolean default false")
  private Boolean countryAnsweredForCapital = false;

  @Column(name = "countryAnsweredForCurrency", columnDefinition="Boolean default false")
  private Boolean countryAnsweredForCurrency = false;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "country_id")
  @JsonBackReference
  private CountryEntity country;

  public CapitalEntity() {
  }

  public CapitalEntity(long id, String capital, String currency, Boolean countryAnsweredForCapital, Boolean countryAnsweredForCurrency) {
    this.id = id;
    this.capital = capital;
    this.currency = currency;
    this.countryAnsweredForCapital = countryAnsweredForCapital;
    this.countryAnsweredForCurrency = countryAnsweredForCurrency;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getCapital() {
    return capital;
  }

  public void setCapital(String capital) {
    this.capital = capital;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public Boolean getCountryAnsweredForCapital() {
    return countryAnsweredForCapital;
  }

  public void setCountryAnsweredForCapital(Boolean countryAnsweredForCapital) {
    this.countryAnsweredForCapital = countryAnsweredForCapital;
  }

  public Boolean getCountryAnsweredForCurrency() {
    return countryAnsweredForCurrency;
  }

  public void setCountryAnsweredForCurrency(Boolean countryAnsweredForCurrency) {
    this.countryAnsweredForCurrency = countryAnsweredForCurrency;
  }

  public CountryEntity getCountry() {
    return country;
  }

  public void setCountry(CountryEntity countryEntity) {
    this.country = countryEntity;
  }
}
