package com.bvirtuoso.hari.restService

import com.bvirtuoso.hari.api.ApiInvoker
import com.bvirtuoso.hari.model.DishInfo
import com.bvirtuoso.hari.model.PersonInfo
import com.bvirtuoso.hari.repository.DishInfoRepository
import com.bvirtuoso.hari.repository.HealthInfoRepository
import com.bvirtuoso.hari.service.MotionBasedTask
import org.apache.juli.logging.Log
import org.apache.juli.logging.LogFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

import javax.servlet.http.HttpServletRequest
import java.time.LocalDateTime

@RestController
class RestApiEndpoint {
    private final static Log log = LogFactory.getLog(RestApiEndpoint.class)

    private String harshaAvailable = "unavailable";
    private List<DishInfo> dishInfos = new ArrayList<>();
    private List<PersonInfo> personInfos = new ArrayList<>();

    final private RestTemplate restTemplate
    private final ApiInvoker apiInvoker


    private boolean isPleaseWalkAnnounced = false

    public RestApiEndpoint(RestTemplate restTemplate, ApiInvoker apiInvoker){
        log.debug("Scheduler initialized")
        this.restTemplate = restTemplate
        this.apiInvoker = apiInvoker
    }

    @GetMapping("/setHarshaAvailability")
    @ResponseBody
    public void setHarshaAvailability(@RequestParam String availability){
        harshaAvailable = availability.toLowerCase();
    }

    @GetMapping("/getHarshaAvailability")
    @ResponseBody
    public String getHarshaAvailability(){
        return harshaAvailable;
    }

    @GetMapping("/addPeopleWeight")
    @ResponseBody
    public String peopleWeight(@RequestParam String name, @RequestParam BigDecimal weight) {
        log.debug("Wight - name: ${name}, weight: ${weight}")
        PersonInfo personInfo = new PersonInfo();
        if(name.equals("jothi") || name.equals("jyothi") || name.equals("jyoti")){
            name = "jyothi";
        }
        personInfo.setName(name);
        personInfo.setWeight(weight.setScale(2));
        personInfos.add(personInfo);
        return "success";
    }

    @GetMapping("/addPeopleNotes")
    @ResponseBody
    public String peopleNotes(@RequestParam String name, @RequestParam String note) {
        log.debug("Notes - name: ${name}, note: ${note}")
        PersonInfo personInfo = new PersonInfo();
        if(name.equals("jothi") || name.equals("jyothi") || name.equals("jyoti")){
            name = "jyothi"
        }
        personInfo.setName(name)
        personInfo.setNote(note)
        personInfos.add(personInfo)
        return "success"
    }

    @GetMapping("/addDishInfo")
    @ResponseBody
    public String dishInfo(@RequestParam String name, @RequestParam String timeOfDay) {
        log.debug("Dish - name: ${name}, timeOfDay: ${timeOfDay}")
        DishInfo dishInfo = new DishInfo();
        dishInfo.setName(name);
        dishInfo.setTimeOfDay(timeOfDay);
        dishInfos.add(dishInfo);
        return "success";
    }

    @GetMapping("/getDishInfo")
    @ResponseBody
    public List<DishInfo> getDishInfo() {
        return dishInfos;
    }

    @GetMapping("/getPeopleInfo")
    @ResponseBody
    public List<PersonInfo> getPeopleInfo() {
        return personInfos;
    }

    @GetMapping("/clearDishAndPeopleInfo")
    @ResponseBody
    public String clearDishAndPeopleInfo() {
        //log.debug("Clearing dish info")
        dishInfos = new ArrayList<>();
        personInfos = new ArrayList<>();
        return "success";
    }

}
