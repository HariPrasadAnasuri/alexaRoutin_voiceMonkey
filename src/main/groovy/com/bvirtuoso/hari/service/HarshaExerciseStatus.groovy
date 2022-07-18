package com.bvirtuoso.hari.service

import org.apache.tomcat.jni.Local
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RestController

import java.time.LocalDateTime

@Service
class HarshaExerciseStatus {

    static Boolean isHarshaIn;
    static LocalDateTime localDateTime = LocalDateTime.now();

    Boolean getIsHarshaIn() {
        return isHarshaIn
    }

    void setIsHarshaIn(Boolean isHarshaIn) {
        this.isHarshaIn = isHarshaIn
    }

    LocalDateTime getLocalDateTime() {
        return localDateTime
    }

    void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime
    }
}
