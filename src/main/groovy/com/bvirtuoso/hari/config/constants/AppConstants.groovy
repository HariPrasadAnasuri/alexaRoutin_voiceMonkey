package com.bvirtuoso.hari.config.constants;

import com.bvirtuoso.hari.model.PersonalSchedule;

import java.util.LinkedHashMap;
import java.util.List;

public class AppConstants {
    public enum ScheduleStatus{
        DONE(1, "Done"),
        IN_PROGRESS(2, "InProgress"),
        NEW(3, "New");

        private Integer code;
        private String text;
        ScheduleStatus(Integer code, String text) {
            this.code = code;
            this.text = text;
        }

        public Integer getCode() {
            return code;
        }

        public String getText() {
            return text;
        }
    }

    static List<PersonalSchedule> schedulesDuration =
            [new PersonalSchedule(duration: "")]
}
