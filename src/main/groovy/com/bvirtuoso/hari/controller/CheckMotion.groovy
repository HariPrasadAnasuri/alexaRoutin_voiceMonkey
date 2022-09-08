package com.bvirtuoso.hari.controller

import com.bvirtuoso.hari.service.MotionBasedTask
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

import java.time.Duration
import java.time.LocalDateTime

@RestController
class CheckMotion {

    @Autowired
    final private MotionBasedTask harshaExerciseStatus

    public CheckMotion(final MotionBasedTask harshaExerciseStatus){
        this.harshaExerciseStatus = harshaExerciseStatus
    }

    @GetMapping("/getInActivityTime")
    public String getInActivityTime(){
        Duration duration = Duration.between(harshaExerciseStatus.getLocalDateTime(), LocalDateTime.now())
        return duration.getSeconds() + " Seconds"
    }
}