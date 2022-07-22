/*
package com.bvirtuoso.hari.controller

import com.bvirtuoso.hari.service.HarshaExerciseStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

import java.time.Duration
import java.time.LocalDateTime

@RestController
class PersonaSchedule {

    @Autowired
    final private HarshaExerciseStatus harshaExerciseStatus

    public PersonaSchedule(final HarshaExerciseStatus harshaExerciseStatus){
        this.harshaExerciseStatus = harshaExerciseStatus
    }

    @GetMapping("/motionDetected")
    public void checkMotion(){
        harshaExerciseStatus.setLocalDateTime(LocalDateTime.now())
    }

    @GetMapping("/getInActivityTime")
    public String getInActivityTime(){
        Duration duration = Duration.between(harshaExerciseStatus.getLocalDateTime(), LocalDateTime.now())
        return duration.getSeconds() + " Seconds"
    }
}*/
