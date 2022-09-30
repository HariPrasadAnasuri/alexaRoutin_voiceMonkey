package com.bvirtuoso.hari.model.jpa;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "gkQuestion")
public class GkQuestionEntity {

  @Id
  @GeneratedValue(generator = "sequence-generator-gk")
  @GenericGenerator(
          name = "sequence-generator-gk",
          strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
          parameters = {
                  @org.hibernate.annotations.Parameter(name = "sequence_name", value = "user_sequence_gk"),
                  @org.hibernate.annotations.Parameter(name = "initial_value", value = "60"),
                  @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
          }
  )
  private long id;

  @Column(name = "question")
  private String question;

  @Column(name = "answer")
  private String answer;

  @Column(name = "status", columnDefinition="Integer default 0")
  private Integer status;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }
}
