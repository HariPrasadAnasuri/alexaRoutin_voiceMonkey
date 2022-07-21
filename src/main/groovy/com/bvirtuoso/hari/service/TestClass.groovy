package com.bvirtuoso.hari.service

import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class TestClass {
    public static void main(String [] args){
        /*Duration duration = Duration.between(LocalDateTime.now().minusHours(1), LocalDateTime.now())
        println(duration.getSeconds())*/
        LocalDateTime dateTime = LocalDateTime.now()
        LocalDateTime now = LocalDateTime.now()

        Duration duration = Duration.ofMinutes(120)
        print(duration.getSeconds())

        /*LocalDateTime truncatedDate = dateTime.truncatedTo(ChronoUnit.MINUTES)
        println(truncatedDate)
        LocalDateTime previousTime = truncatedDate.minusMinutes(1)
        LocalDateTime nextTime = truncatedDate.plusMinutes(1)
        if(truncatedDate.isAfter(previousTime) && truncatedDate.isBefore(nextTime)){
            print("The date is between")
        }
        List<Integer> scheduleTimeDurations = []*/
    }
}
