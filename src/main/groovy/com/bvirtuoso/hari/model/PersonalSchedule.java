package com.bvirtuoso.hari.model;

import com.bvirtuoso.hari.config.constants.AppConstants;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class PersonalSchedule {

  LocalDateTime localDateTime;
  LocalTime time;
  String taskDesc;
  AppConstants.ScheduleStatus scheduleStatus;
  Duration duration;
  Boolean isAlreadyAdded = false;

  public LocalDateTime getLocalDateTime() {
    return localDateTime;
  }

  public void setLocalDateTime(LocalDateTime localDateTime) {
    this.localDateTime = localDateTime;
  }

  public LocalTime getTime() {
    return time;
  }

  public void setTime(LocalTime time) {
    this.time = time;
  }

  public String getTaskDesc() {
    return taskDesc;
  }

  public void setTaskDesc(String taskDesc) {
    this.taskDesc = taskDesc;
  }

  public AppConstants.ScheduleStatus getScheduleStatus() {
    return scheduleStatus;
  }

  public void setScheduleStatus(AppConstants.ScheduleStatus scheduleStatus) {
    this.scheduleStatus = scheduleStatus;
  }

  public Duration getDuration() {
    return duration;
  }

  public void setDuration(Duration duration) {
    this.duration = duration;
  }

  public Boolean getAlreadyAdded() {
    return isAlreadyAdded;
  }

  public void setAlreadyAdded(Boolean alreadyAdded) {
    isAlreadyAdded = alreadyAdded;
  }
}
