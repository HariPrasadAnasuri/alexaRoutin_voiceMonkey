package com.bvirtuoso.hari.model.jpa;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;

@Entity
@Table(name = "member")
public class MemberEntity {

  @Id
  @GeneratedValue(generator = "sequence-generator-member")
  @GenericGenerator(
          name = "sequence-generator-member",
          strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
          parameters = {
                  @org.hibernate.annotations.Parameter(name = "sequence_name", value = "user_sequence_member"),
                  @org.hibernate.annotations.Parameter(name = "initial_value", value = "13"),
                  @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
          }
  )
  private long id;
  @Column(name = "person")
  private String person;

  @Column(name = "message")
  private String message;

//  Number of times accessed the alexa service
  @Column(name = "count")
  private Integer count;

  public MemberEntity() {
  }

  public MemberEntity(String person, String message, Integer count){
    this.person = person;
    this.message = message;
    this.count = count;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getPerson() {
    return person;
  }

  public void setPerson(String person) {
    this.person = person;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }
}
