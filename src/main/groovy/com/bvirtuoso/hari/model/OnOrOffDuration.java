package com.bvirtuoso.hari.model;

import java.time.Duration;

public class OnOrOffDuration {
  private boolean isTvOn;
  private Duration duration;
  private String message;

  public OnOrOffDuration(){

  }

  public boolean isTvOn() {
    return isTvOn;
  }

  public void setTvOn(boolean tvOn) {
    isTvOn = tvOn;
  }

  public Duration getDuration() {
    return duration;
  }

  public void setDuration(Duration duration) {
    this.duration = duration;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
