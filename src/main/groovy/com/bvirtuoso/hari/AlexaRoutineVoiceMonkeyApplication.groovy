package com.bvirtuoso.hari

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class AlexaRoutineVoiceMonkeyApplication {

    static void main(String[] args) {
        SpringApplication.run(AlexaRoutineVoiceMonkeyApplication, args)
    }

}
