package com.bvirtuoso.hari.service

import java.time.Duration
import java.time.LocalDateTime

class TestClass {
    public static void main(String [] args){
        Duration duration = Duration.between(LocalDateTime.now().minusHours(1), LocalDateTime.now())
        println(duration.getSeconds())
    }
}
