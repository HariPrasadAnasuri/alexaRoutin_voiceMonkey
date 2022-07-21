package com.bvirtuoso.hari.model

import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

class HariSchedule extends PersonalSchedule{
    List<PersonalSchedule> fixedSchedules
    List<PersonalSchedule> variableSchedules
    List<PersonalSchedule> finalAdjustedSchedules = []
    Boolean isAlreadyAdded = false
    HariSchedule(){
        LocalTime lunchTime = LocalTime.of(13, 15)
        LocalTime dinnerTime = LocalTime.of(20, 10)
        fixedSchedules = [
                new PersonalSchedule(taskDesc: "Lunch time", time: lunchTime, duration: Duration.ofMinutes(60)),
                new PersonalSchedule(taskDesc: "Dinner time", time: dinnerTime, duration: Duration.ofMinutes(60))
        ]
        variableSchedules = [
                new PersonalSchedule(taskDesc: "Do exercise", duration: Duration.ofMinutes(30)),
                new PersonalSchedule(taskDesc: "Do bath", duration: Duration.ofMinutes(25)),
                new PersonalSchedule(taskDesc: "Do Breakfast", duration: Duration.ofMinutes(20)),
                new PersonalSchedule(taskDesc: "Let's Read news", duration: Duration.ofMinutes(15)),
                new PersonalSchedule(taskDesc: "Take a quick nap", duration: Duration.ofMinutes(15)),
                new PersonalSchedule(taskDesc: "Treadmill", duration: Duration.ofMinutes(15)),
                new PersonalSchedule(taskDesc: "Time for office work", duration: Duration.ofMinutes(120)),
                new PersonalSchedule(taskDesc: "Do your favorite work", duration: Duration.ofMinutes(60)),
                new PersonalSchedule(taskDesc: "Take a quick nap", duration: Duration.ofMinutes(30)),
                new PersonalSchedule(taskDesc: "Do your favorite work", duration: Duration.ofMinutes(120)),
                new PersonalSchedule(taskDesc: "Do exercise", duration: Duration.ofMinutes(120)),
                new PersonalSchedule(taskDesc: "Have some snacks", duration: Duration.ofMinutes(55)),
                new PersonalSchedule(taskDesc: "Time for office work", duration: Duration.ofMinutes(120)),
                new PersonalSchedule(taskDesc: "Time for office work", duration: Duration.ofMinutes(60)),
                new PersonalSchedule(taskDesc: "Go for bed if possible", duration: Duration.ofMinutes(30)),
                new PersonalSchedule(taskDesc: "You should be in bed", duration: Duration.ofMinutes(30))
        ]
    }
    prepareScheduledTimes(){
        LocalTime scheduledFrom = LocalTime.now()
        variableSchedules.each {schedule ->
            LocalTime scheduledUntil = scheduledFrom.plusSeconds(schedule.getDuration().getSeconds())
            scheduledUntil = checkIsItCrossingFixedTime(scheduledFrom, scheduledUntil, schedule)
            scheduledFrom = scheduledUntil
        }
    }
    private LocalTime checkIsItCrossingFixedTime(LocalTime scheduledFrom, LocalTime scheduledUntil, PersonalSchedule schedule){
        LocalTime untilTime
        fixedSchedules.each {fixedSchedule ->
            LocalTime fixedScheduleFrom = fixedSchedule.getTime()
            LocalTime fixedScheduleUntil = fixedSchedule.getTime().plusSeconds(fixedSchedule.getDuration().getSeconds())
            if(scheduledFrom.isBefore(fixedScheduleFrom)) {
                if(scheduledUntil.isBefore(fixedScheduleUntil)){
                    finalAdjustedSchedules.add(schedule)
                    untilTime = scheduledUntil
                }else if(scheduledUntil.isAfter(fixedScheduleFrom)){
                    long calcDurationBetweenScheduleFromAndFixedFrom = fixedScheduleFrom.until(scheduledFrom, ChronoUnit.MINUTES)
                    PersonalSchedule separatedTask_begin = new PersonalSchedule(taskDesc: schedule.getTaskDesc(), duration: Duration.ofMinutes(calcDurationBetweenScheduleFromAndFixedFrom))
                    finalAdjustedSchedules.add(separatedTask_begin)
                    finalAdjustedSchedules.add(fixedSchedule)
                    long calcDurationBetweenScheduleUntilAndFixedUntil = fixedScheduleUntil.until(scheduledUntil, ChronoUnit.MINUTES)
                    PersonalSchedule separatedTask_end = new PersonalSchedule(taskDesc: schedule.getTaskDesc(), duration: Duration.ofMinutes(calcDurationBetweenScheduleUntilAndFixedUntil))
                    finalAdjustedSchedules.add(separatedTask_end)
                }
            }else
        }
        return null
    }

    Boolean getIsAlreadyChecked() {
        return isAlreadyChecked
    }

    void setIsAlreadyChecked(Boolean isAlreadyChecked) {
        this.isAlreadyChecked = isAlreadyChecked
    }
}
