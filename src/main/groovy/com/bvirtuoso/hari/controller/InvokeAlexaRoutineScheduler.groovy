package com.bvirtuoso.hari.controller

import com.bvirtuoso.hari.api.ApiInvoker
import com.bvirtuoso.hari.model.DishInfo
import com.bvirtuoso.hari.model.PersonInfo
import com.bvirtuoso.hari.model.jpa.DishInfoJpa
import com.bvirtuoso.hari.model.jpa.HealthInfoJpa
import com.bvirtuoso.hari.repository.DishInfoRepository
import com.bvirtuoso.hari.repository.HealthInfoRepository
import com.bvirtuoso.hari.service.HarshaExerciseStatus
import org.apache.juli.logging.Log
import org.apache.juli.logging.LogFactory
import org.apache.tomcat.jni.Local
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Controller
import org.springframework.web.client.RestTemplate

import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period

@Controller
class InvokeAlexaRoutineScheduler {
    private final static Log log = LogFactory.getLog(InvokeAlexaRoutineScheduler.class)

    @Autowired
    final HarshaExerciseStatus harshaExerciseStatus

    final private RestTemplate restTemplate
    private final ApiInvoker apiInvoker

    @Value("\${voiceMonkey.fan.turnOnOrOff}") String turnOnOrOffFanUrl
    @Value("\${voiceMonkey.entertainment.turnOff}") String turnOffEntertainment
    @Value("\${voiceMonkey.entertainment.turnOn}") String turnOnEntertainment
    @Value("\${voiceMonkey.drinkWater}") String drinkWater
    @Value("\${app.bvirtuoso.turnOffTv}") String turnOffTv
    @Value("\${app.bvirtuoso.turnOnTv}") String turnOnTv
    @Value("\${voiceMonkey.tellTime}") String tellTime
    @Value("\${voiceMonkey.tellDate}") String tellDate
    @Value("\${voiceMonkey.pleaseWalk}") String pleaseWalk
    @Value("\${app.bvirtuoso.onOffDuration}") String onOffDuration
    @Value("\${app.bvirtuoso.getPeopleInfo}") String getPeopleInfo
    @Value("\${app.bvirtuoso.getDishInfo}") String getDishInfo
    @Value("\${app.bvirtuoso.clearDishAndPeopleInfo}") String clearDishAndPeopleInfo
    @Value("\${app.bvirtuoso.harshaAvailability}") String harshaAvailability


    private final DishInfoRepository dishInfoRepository
    private final HealthInfoRepository healthInfoRepository

    public InvokeAlexaRoutineScheduler(RestTemplate restTemplate, ApiInvoker apiInvoker,
                                        DishInfoRepository dishInfoRepository,
                                        HealthInfoRepository healthInfoRepository){
        log.debug("Scheduler initialized")
        this.restTemplate = restTemplate
        this.apiInvoker = apiInvoker
        this.dishInfoRepository = dishInfoRepository
        this.healthInfoRepository = healthInfoRepository
    }

    @Scheduled(cron = "0 0/20 10-23 * * *")
    public void drinkWater(){
        log.debug("Announcing drink water")
        apiInvoker.invokeVoiceMonkeyApi(drinkWater)
    }

    @Scheduled(cron = "0 0 10-23/1 * * *")
    public void tellTime(){
        log.debug("Announcing time")
        apiInvoker.invokeVoiceMonkeyApi(tellTime)

    }
    @Scheduled(cron = "0 15 10-23/3 * * *")
    public void tellDate(){
        log.debug("Announcing date")
        apiInvoker.invokeVoiceMonkeyApi(tellDate)
    }

    //@Scheduled(cron = "0 0/5 20-23 * * *")
    @Scheduled(cron = "0 0/5 * * * *")
    public void collectAllPersonalInformation(){
        log.debug("collectAllPersonalInformation() called")
        updatePersonInfo()
        updateDishInfo()
        deleteInformationFromBvirtuosoApp()
    }

    public void updatePersonInfo(){
        LocalDate localDate = LocalDate.now()
        List<PersonInfo> personsInfo = apiInvoker.getPersonInfo(getPeopleInfo)
        personsInfo.each {personInfo ->
            HealthInfoJpa healthInfo = new HealthInfoJpa()
            healthInfo.setLocalDateTime()
            healthInfo.setLocalDate(LocalDate.now())
            healthInfo.setName(personInfo.getName())
            boolean isWeight = true;
            if(personInfo.getWeight() == 0.0f){
                healthInfo.setNote(personInfo.getNote())
                isWeight = false;
            }else{
                healthInfo.setWeight(personInfo.getWeight())
            }

            List<HealthInfoJpa> healthInfoList = healthInfoRepository.findByNameAndLocalDate(personInfo.getName(), localDate);
            if(healthInfoList.size() == 0){
                healthInfoRepository.save(healthInfo)
            }else{
                HealthInfoJpa healthInfoJpa = healthInfoList.get(0)
                healthInfoJpa.setLocalDateTime(LocalDateTime.now())
                healthInfoJpa.setLocalDate(LocalDate.now())
                if(isWeight){
                    healthInfoJpa.setWeight(personInfo.getWeight())
                }else{
                    healthInfoJpa.setNote(personInfo.getNote())
                }

                healthInfoRepository.save(healthInfoJpa)
            }

        }
    }

    public void updateDishInfo(){
        List<DishInfo> dishesInfo = apiInvoker.getDishInfo(getDishInfo)
        LocalDate localDate = LocalDate.now()
        LocalDateTime localDateTime = LocalDateTime.now()
        dishesInfo.each {dishInfo ->
            List<DishInfoJpa> healthInfoList = dishInfoRepository.findByLocalDateAndTimeOfDay(
                                                    localDate, dishInfo.getTimeOfDay());
            if(healthInfoList.size() == 0){
                DishInfoJpa dishInfoJpa = new DishInfoJpa()
                dishInfoJpa.setLocalDate(localDate)
                dishInfoJpa.setLocalDateTime(localDateTime)
                dishInfoJpa.setName(dishInfo.getName())
                dishInfoJpa.setTimeOfDay(dishInfo.getTimeOfDay())
                dishInfoRepository.save(dishInfoJpa)
            }else{
                DishInfoJpa dishInfoJpa = healthInfoList.get(0)
                dishInfoJpa.setName(dishInfo.getName())
                dishInfoRepository.save(dishInfoJpa)
            }
        }

    }

    public void deleteInformationFromBvirtuosoApp(){
        apiInvoker.invokeApi(clearDishAndPeopleInfo)
        log.debug("deleteInformationFromBvirtuosoApp called")
    }



    @Scheduled(cron = "0 0/1 * * * *")
    void harshaIdleCheck(){
        String availability = apiInvoker.invokeApi(harshaAvailability)
        if(availability.equals("available")){
            //Period period = Period.between(harshaExerciseStatus.getLocalDateTime(), LocalDateTime.now());
            Duration duration = Duration.between(harshaExerciseStatus.getLocalDateTime(), LocalDateTime.now())
            if(duration.getSeconds() > 900){
                log.debug("Announcing - Please Walk")
                apiInvoker.invokeApi(pleaseWalk)
                harshaExerciseStatus.setLocalDateTime(LocalDateTime.now())
            }
        }
    }

    @Scheduled(cron = "0 0/1 15-17 * * *")
    public void tvOnOrOff(){
        log.debug("Announcing tvOnOff")
        //println("Inside tvOnOrOff method")
        //After setting user agent only able to call the API
        // Always check the IDE pointing proper Java Installation or not, otherwise will get trust store keys issue.
        /*HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        OnOrOffDuration onOrOffDuration = restTemplate.exchange(onOffDuration, HttpMethod.GET, entity, OnOrOffDuration.class).getBody()
        long minutes = onOrOffDuration.getDuration().toMinutes()
        if(onOrOffDuration.isTvOn()){
            if(minutes > 120){
                restTemplate.exchange(turnOffTv, HttpMethod.GET, entity, OnOrOffDuration.class).getBody()
            }
        }else{
            if(minutes > 30){
                restTemplate.exchange(turnOnTv, HttpMethod.GET, entity, OnOrOffDuration.class).getBody()
            }
        }
        println("onOrOffDuration ${onOrOffDuration}")*/
    }
}
