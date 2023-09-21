package com.bvirtuoso.hari.restService

import com.bvirtuoso.hari.config.constants.AppConstants
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.apache.tools.ant.taskdefs.Local
import org.springframework.web.bind.annotation.*

import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/tv/controller")
class TvAppInterfaceController {
    protected static final Log log = LogFactory.getLog(TvAppInterfaceController.class)

    private AppConstants.TvControlType controlType;
    private LocalDateTime localDateTime = LocalDateTime.now();


    @GetMapping("/getControl")
    String getControl()
    {
        String controlString;
        if(controlType){
            controlString =  controlType.toString().toLowerCase()
        }else{
            controlString =  "";
        }
        controlType = null;
        return controlString
    }
    @GetMapping("/updateControl")
    void updateControl(@RequestParam String control)
    {
        if(AppConstants.TvControlType.NEXT.toString().toLowerCase() == control){
            controlType = AppConstants.TvControlType.NEXT
        }else if(AppConstants.TvControlType.PREVIOUS.toString().toLowerCase() == control){
            controlType = AppConstants.TvControlType.PREVIOUS
        }else if(AppConstants.TvControlType.PAUSE.toString().toLowerCase() == control){
            controlType = AppConstants.TvControlType.PAUSE
        }
    }
    @GetMapping("/setDate")
    void setDate(@RequestParam LocalDateTime date)
    {
        localDateTime = date;
        println("Date: "+ date)
    }
    @GetMapping("/getDate")
    String getDate()
    {
        String temp;
        //String temp = localDateTime.toString();
        if(localDateTime != null) {
            temp = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            //String temp = new Date(Timestamp.valueOf(localDateTime).getTime()).toString()
            localDateTime = null;
        }
        return temp;
    }
}

