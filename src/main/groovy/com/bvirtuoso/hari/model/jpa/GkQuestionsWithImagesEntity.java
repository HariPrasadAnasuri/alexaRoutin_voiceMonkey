package com.bvirtuoso.hari.model.jpa;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "gkQuestionsWithImagesEntity")
public class GkQuestionsWithImagesEntity {

  @Id
  private long id;
  @Column
  private String answerOption;
  @Column(length = 2000)
  private String questionImageUrl;
  @Column
  private Boolean progress;
  @Column
  private Integer repeatedCount;
  @Column(length = 10000)
  private String questionText;
  @Column
  private String options;
  @Column
  private String answerText;
  @Column

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getAnswerOption() {
    return answerOption;
  }

  public void setAnswerOption(String answerOption) {
    this.answerOption = answerOption;
  }

  public String getQuestionImageUrl() {
    return questionImageUrl;
  }

  public void setQuestionImageUrl(String questionImageUrl) {
    this.questionImageUrl = questionImageUrl;
  }

  public Boolean getProgress() {
    return progress;
  }

  public void setProgress(Boolean progress) {
    this.progress = progress;
  }

  public Integer getRepeatedCount() {
    return repeatedCount;
  }

  public void setRepeatedCount(Integer repeatedCount) {
    this.repeatedCount = repeatedCount;
  }

  public String getQuestionText() {
    return questionText;
  }

  public void setQuestionText(String questionText) {
    this.questionText = questionText;
  }

  public String getOptions() {
    return options;
  }

  public void setOptions(String options) {
    this.options = options;
  }

  public String getAnswerText() {
    return answerText;
  }

  public void setAnswerText(String answerText) {
    this.answerText = answerText;
  }
}
