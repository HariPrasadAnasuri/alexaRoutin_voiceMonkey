package com.bvirtuoso.hari.service

import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Service

import java.time.LocalDateTime

//@Service
//@Scope(value="prototype", proxyMode= ScopedProxyMode.TARGET_CLASS)
class MotionBasedTask {

    Boolean isTaskEnabled;
    LocalDateTime localDateTime = LocalDateTime.now();

    Boolean getIsTaskEnabled() {
        return isTaskEnabled
    }

    void setIsTaskEnabled(Boolean isTaskEnabled) {
        this.isTaskEnabled = isTaskEnabled
    }

    LocalDateTime getLocalDateTime() {
        return localDateTime
    }

    void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime
    }
}
