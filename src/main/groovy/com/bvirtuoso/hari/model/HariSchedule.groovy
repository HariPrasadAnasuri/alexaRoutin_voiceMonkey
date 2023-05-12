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
    HariSchedule(){
        LocalTime lunchTime = LocalTime.of(13, 15)
        LocalTime afterLunchNap = LocalTime.of(14, 15)
        LocalTime dinnerTime = LocalTime.of(20, 10)
        /*fixedSchedules = [
                new PersonalSchedule(taskDesc: "Lunch time", time: lunchTime, duration: Duration.ofMinutes(60)),
                new PersonalSchedule(taskDesc: "Take a quick nap", time: afterLunchNap, duration: Duration.ofMinutes(30)),
                new PersonalSchedule(taskDesc: "Dinner time", time: dinnerTime, duration: Duration.ofMinutes(60))
        ]*/
        variableSchedules = [
                new PersonalSchedule(taskDesc: "Do exercise", duration: Duration.ofMinutes(30)),
                new PersonalSchedule(taskDesc: "Do bath", duration: Duration.ofMinutes(25)),
                new PersonalSchedule(taskDesc: "Do Breakfast", duration: Duration.ofMinutes(20)),
                new PersonalSchedule(taskDesc: "Let's Read news", duration: Duration.ofMinutes(15)),
                new PersonalSchedule(taskDesc: "Take a quick nap", duration: Duration.ofMinutes(15)),
                new PersonalSchedule(taskDesc: "Treadmill", duration: Duration.ofMinutes(15)),
                new PersonalSchedule(taskDesc: "Time for office work", duration: Duration.ofMinutes(120)),
                new PersonalSchedule(taskDesc: "Do your favorite work", duration: Duration.ofMinutes(60)),
                new PersonalSchedule(taskDesc: "Lunch time", time: lunchTime, duration: Duration.ofMinutes(60)),
                new PersonalSchedule(taskDesc: "Take a quick nap", time: afterLunchNap, duration: Duration.ofMinutes(30)),
                new PersonalSchedule(taskDesc: "Time for office work", duration: Duration.ofMinutes(120)),
                new PersonalSchedule(taskDesc: "Do exercise", duration: Duration.ofMinutes(60)),
                new PersonalSchedule(taskDesc: "Have some snacks", duration: Duration.ofMinutes(55)),
                new PersonalSchedule(taskDesc: "Time for office work", duration: Duration.ofMinutes(120)),
                new PersonalSchedule(taskDesc: "Dinner time", time: dinnerTime, duration: Duration.ofMinutes(60)),
                new PersonalSchedule(taskDesc: "Time for office work", duration: Duration.ofMinutes(60)),
                new PersonalSchedule(taskDesc: "Treadmill", duration: Duration.ofMinutes(20)),
                new PersonalSchedule(taskDesc: "Time for office work", duration: Duration.ofMinutes(60)),
                new PersonalSchedule(taskDesc: "Go for bed if possible", duration: Duration.ofMinutes(30)),
                new PersonalSchedule(taskDesc: "You should be in bed", duration: Duration.ofMinutes(30))
        ]
    }
    List<PersonalSchedule> prepareScheduledTimes(LocalTime scheduledFrom){
        return variableSchedules
//        Following code will adjust the final schedules based on fixed schedules
        /*variableSchedules.each {schedule ->
            LocalTime scheduledUntil = scheduledFrom.plusSeconds(schedule.getDuration().getSeconds())
            scheduledUntil = checkIsItCrossingFixedTime(scheduledFrom, scheduledUntil, schedule)
            scheduledFrom = scheduledUntil
        }
        return finalAdjustedSchedules*/
    }
    /*private LocalTime checkIsItCrossingFixedTime(LocalTime scheduledFrom, LocalTime scheduledUntil, PersonalSchedule schedule){
        LocalTime untilTime = scheduledFrom
        if(fixedSchedules.any(scheduleObj -> (!scheduleObj.isAlreadyAdded))) {
            fixedSchedules.any { fixedSchedule ->
                if (!fixedSchedule.isAlreadyAdded) {
                    LocalTime fixedScheduleFrom = fixedSchedule.getTime()
                    LocalTime fixedScheduleUntil = fixedSchedule.getTime().plusSeconds(fixedSchedule.getDuration().getSeconds())
                    if (scheduledFrom.isBefore(fixedScheduleFrom)) {
                        if (scheduledUntil.isBefore(fixedScheduleFrom)) {
                            //If time slot is before the fixed time slot
                            finalAdjustedSchedules.add(schedule)
                            untilTime = untilTime.plusMinutes((Integer) (schedule.getDuration().getSeconds() / 60))
                            return true
                        } else if (scheduledUntil.isAfter(fixedScheduleFrom)) {
                            //If time slot is before and after of fixed time slot from
                            long calcDurationBetweenScheduleFromAndFixedFrom = scheduledFrom.until(fixedScheduleFrom, ChronoUnit.MINUTES)
                            PersonalSchedule separatedTask_begin = new PersonalSchedule(taskDesc: schedule.getTaskDesc(), duration: Duration.ofMinutes(calcDurationBetweenScheduleFromAndFixedFrom))
                            finalAdjustedSchedules.add(separatedTask_begin)
                            untilTime = untilTime.plusMinutes(calcDurationBetweenScheduleFromAndFixedFrom)

                            finalAdjustedSchedules.add(fixedSchedule)
                            untilTime = untilTime.plusMinutes((Integer) (fixedSchedule.getDuration().getSeconds() / 60))

                            long calcDurationBetweenFixedFromToScheduledUntil = fixedScheduleFrom.until(scheduledUntil, ChronoUnit.MINUTES)
                            PersonalSchedule separatedTask_end = new PersonalSchedule(taskDesc: schedule.getTaskDesc(), duration: Duration.ofMinutes(calcDurationBetweenFixedFromToScheduledUntil))
                            finalAdjustedSchedules.add(separatedTask_end)
                            untilTime = untilTime.plusMinutes(calcDurationBetweenFixedFromToScheduledUntil)

                            fixedSchedule.setAlreadyAdded(true)
                            return true
                        } else if (scheduledUntil.isAfter(fixedScheduleUntil)) {
                            //If time slot is beyond the fixed time slot
                            long calcDurationBetweenScheduleFromAndFixedFrom = scheduledFrom.until(fixedScheduleFrom, ChronoUnit.MINUTES)
                            PersonalSchedule separatedTask_begin = new PersonalSchedule(taskDesc: schedule.getTaskDesc(), duration: Duration.ofMinutes(calcDurationBetweenScheduleFromAndFixedFrom))
                            finalAdjustedSchedules.add(separatedTask_begin)
                            finalAdjustedSchedules.add(fixedSchedule)
                            untilTime = untilTime.plusMinutes(calcDurationBetweenScheduleFromAndFixedFrom)

                            untilTime = untilTime.plusMinutes((Integer) (fixedSchedule.getDuration().getSeconds() / 60))

                            long calcDurationBetweenFixedFromToScheduledUntil = fixedScheduleFrom.until(scheduledUntil, ChronoUnit.MINUTES)
                            PersonalSchedule separatedTask_end = new PersonalSchedule(taskDesc: schedule.getTaskDesc(), duration: Duration.ofMinutes(calcDurationBetweenFixedFromToScheduledUntil))
                            finalAdjustedSchedules.add(separatedTask_end)
                            untilTime = untilTime.plusMinutes(calcDurationBetweenFixedFromToScheduledUntil)

                            fixedSchedule.setAlreadyAdded(true)
                            return true
                        }
                    } else if (scheduledFrom.isAfter(fixedScheduleFrom)) {
                        if (scheduledUntil.isBefore(fixedScheduleUntil)) {
                            //Time slot is between the,  static from and until
                            finalAdjustedSchedules.add(fixedSchedule)
                            finalAdjustedSchedules.add(schedule)
                            untilTime = untilTime.plusMinutes((Integer) (fixedSchedule.getDuration().getSeconds() / 60))
                            untilTime = untilTime.plusMinutes((Integer) (schedule.getDuration().getSeconds() / 60))

                            fixedSchedule.setAlreadyAdded(true)
                            return true
                        } else if (scheduledUntil.isAfter(fixedScheduleUntil)) {
                            if (scheduledFrom.isAfter(fixedScheduleUntil)) {
                                //if time slot is after fixed until
                                long timeGap = fixedScheduleUntil.until(scheduledFrom, ChronoUnit.MINUTES)
                                finalAdjustedSchedules.add(fixedSchedule)
                                finalAdjustedSchedules.add(schedule)
                                untilTime = untilTime.plusMinutes((Integer) (fixedSchedule.getDuration().getSeconds() / 60))
                                *//*Todo: doubtful*//*
                                untilTime = untilTime.plusMinutes((Integer) (schedule.getDuration().getSeconds() / 60) - timeGap)

                                fixedSchedule.setAlreadyAdded(true)
                                return true
                            } else if (scheduledFrom.isBefore(fixedScheduleUntil)) {
                                //if time slot crossing fixed Until
                                finalAdjustedSchedules.add(fixedSchedule)
                                finalAdjustedSchedules.add(schedule)
                                untilTime = untilTime.plusMinutes((Integer) (fixedSchedule.getDuration().getSeconds() / 60))
                                untilTime = untilTime.plusMinutes((Integer) (schedule.getDuration().getSeconds() / 60))

                                fixedSchedule.setAlreadyAdded(true)
                                return true
                            }
                        }
                    }
                }
            }
        }else{
            finalAdjustedSchedules.add(schedule)
            untilTime = untilTime.plusMinutes((Integer) (schedule.getDuration().getSeconds() / 60))
        }
        return untilTime.plusMinutes(1)
    }*/

    /*Boolean getIsAlreadyChecked() {
        return isAlreadyChecked
    }

    void setIsAlreadyChecked(Boolean isAlreadyChecked) {
        this.isAlreadyChecked = isAlreadyChecked
    }*/
}
