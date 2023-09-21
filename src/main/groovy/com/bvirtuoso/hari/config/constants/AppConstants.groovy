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

    public enum GK_CONSTANTS  {
        UNTOUCHED(0, "untouched"),
        ASKED(1, "asked"),
        ANSWERED(2, "answered"),
        WRONGANSWER(3, "wronganswer");

        private Integer code;
        private String text;

        private GK_CONSTANTS(Integer code, String text) {
            this.code = code;
            this.text= text;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
    public enum TvControlType{
        NEXT("NEXT"),
        PREVIOUS("PREVIOUS"),
        PAUSE("PAUSE")

        private final String controlType

        TvControlType(String controlType){
            this.controlType = controlType
        }
        public int getControlType(){
            return controlType
        }
    }
}
