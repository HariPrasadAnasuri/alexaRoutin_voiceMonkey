package com.bvirtuoso.hari.scheduler

import com.bvirtuoso.hari.api.ApiInvoker
import com.bvirtuoso.hari.model.DishInfo
import com.bvirtuoso.hari.model.HariSchedule
import com.bvirtuoso.hari.model.OnOrOffDuration
import com.bvirtuoso.hari.model.PersonInfo
import com.bvirtuoso.hari.model.PersonalSchedule
import com.bvirtuoso.hari.model.jpa.DishInfoJpa
import com.bvirtuoso.hari.model.jpa.HealthInfoJpa
import com.bvirtuoso.hari.repository.DishInfoRepository
import com.bvirtuoso.hari.repository.HealthInfoRepository
import com.bvirtuoso.hari.restService.RestApiEndpoint
import com.bvirtuoso.hari.service.MotionBasedTask
import org.apache.juli.logging.Log
import org.apache.juli.logging.LogFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@RestController
class InvokeAlexaRoutineScheduler {
    private final static Log log = LogFactory.getLog(InvokeAlexaRoutineScheduler.class)

    MotionBasedTask harshaExerciseStatus = new MotionBasedTask()

    MotionBasedTask decoLightStatus = new MotionBasedTask()

    List<PersonalSchedule> hariSchedules


    final private RestTemplate restTemplate
    private final ApiInvoker apiInvoker

    final private RestApiEndpoint restApiEndpoint

    @Value("\${voiceMonkey.fan.turnOnOrOff}") String turnOnOrOffFanUrl
    @Value("\${voiceMonkey.entertainment.turnOff}") String turnOffEntertainment
    @Value("\${voiceMonkey.entertainment.turnOn}") String turnOnEntertainment
    @Value("\${voiceMonkey.drinkWater}") String drinkWater
    String hariAnnouncement
    @Value("\${app.bvirtuoso.turnOffTv}") String turnOffTv
    @Value("\${app.bvirtuoso.turnOnTv}") String turnOnTv
    @Value("\${voiceMonkey.tellTime}") String tellTime
    @Value("\${voiceMonkey.tellDate}") String tellDate
    @Value("\${app.bvirtuoso.onOffDuration}") String onOffDuration
    @Value("\${voiceMonkey.turnOnDecoLight}") String turnOnDecoLight
    @Value("\${voiceMonkey.turnOffDecoLight}") String turnOffDecoLight
    @Value("\${voiceMonkey.hallAnnouncement}") String hallAnnouncement
    @Value("\${voiceMonkey.entertainment.play}") String playTv
    @Value("\${voiceMonkey.entertainment.pause}") String pauseTv

    private boolean alreadyAnnouncedPleaseWalk = false;
    private int reactTimeForAnnouncement = 0
    private boolean forReactTimeForAnnouncement = false
    private int reactTimeAfterPause = 0
    private boolean forReactTimeAfterPause = false

    private final DishInfoRepository dishInfoRepository
    private final HealthInfoRepository healthInfoRepository

    private boolean isLightOn = false
    private boolean isPleaseWalkAnnounced = false
    private LocalDateTime walkingStartedTime = LocalDateTime.now()
    private Duration walkingInterval
    private boolean justAnnouncedAboutWalking = false
    private int warningCounterToSwitchOffTv = 0

    public InvokeAlexaRoutineScheduler(RestTemplate restTemplate, ApiInvoker apiInvoker,
                                        DishInfoRepository dishInfoRepository,
                                        HealthInfoRepository healthInfoRepository,
                                       @Value("\${voiceMonkey.hariAnnouncement}") String hariAnnouncement,
                                       final RestApiEndpoint restApiEndpoint){
        log.debug("Scheduler initialized")
        this.restTemplate = restTemplate
        this.apiInvoker = apiInvoker
        this.dishInfoRepository = dishInfoRepository
        this.healthInfoRepository = healthInfoRepository
        this.hariAnnouncement = hariAnnouncement
        this.apiInvoker.invokeVoiceMonkeyApi(hariAnnouncement + "Hey hari, application is deployed")
        this.restApiEndpoint = restApiEndpoint

    }

    @GetMapping("/motionDetected")
    public void checkMotion(){
        log.debug("Update the Motion info")
        harshaExerciseStatus.setLocalDateTime(LocalDateTime.now())
        decoLightStatus.setLocalDateTime(LocalDateTime.now())
        isPleaseWalkAnnounced = false
    }

    @Scheduled(cron = "10 0/20 10-23 * * *")
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
    @Scheduled(cron = "0/5 * * * * *")
    public void collectAllPersonalInformation(){
        //log.debug("collectAllPersonalInformation() called")
        updatePersonInfo()
        updateDishInfo()
    }

    public void updatePersonInfo(){
        LocalDate localDate = LocalDate.now()
        List<PersonInfo> personsInfo = restApiEndpoint.getPeopleInfo()
        personsInfo.each {personInfo ->
            HealthInfoJpa healthInfo = new HealthInfoJpa()
            healthInfo.setLocalDateTime(LocalDateTime.now())
            healthInfo.setLocalDate(LocalDate.now())
            healthInfo.setName(personInfo.getName())
            boolean isWeight = true;
            if((personInfo.getWeight() == null) || (personInfo.getWeight() == 0.0f)){
                healthInfo.setNote(personInfo.getNote())
                isWeight = false;
            }else{
                healthInfo.setWeight(personInfo.getWeight())
            }

            List<HealthInfoJpa> healthInfoList = healthInfoRepository.findByNameAndLocalDate(personInfo.getName(), localDate);
            if(healthInfoList.size() == 0){
                healthInfoRepository.save(healthInfo)
                restApiEndpoint.clearPeopleInfo()
            }else{
                HealthInfoJpa healthInfoJpa = healthInfoList.get(0)
                healthInfoJpa.setLocalDateTime(LocalDateTime.now())
                healthInfoJpa.setLocalDate(LocalDate.now())
                if(isWeight){
                    healthInfoJpa.setWeight(personInfo.getWeight())
                }else{
                    healthInfoJpa.setNote(personInfo.getNote())
                }

                HealthInfoJpa healthSaveResult = healthInfoRepository.save(healthInfoJpa)
                log.debug("healthSaveresult ${healthSaveResult}")
                restApiEndpoint.clearPeopleInfo()
            }

        }
    }

    public void updateDishInfo(){
        List<DishInfo> dishesInfo = restApiEndpoint.getDishInfo()
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
                restApiEndpoint.clearDishInfo()
            }
        }

    }

    @Scheduled(cron = "0/1 * * * * *")
    void counterForSeconds(){
        reactTimeForAnnouncement++
        reactTimeAfterPause++
    }

    @Scheduled(cron = "0/2 * * * * *")
    void harshaIdleCheck(){
        String availability = restApiEndpoint.getHarshaAvailability()
        if(availability.equals("available")){
            //Period period = Period.between(harshaExerciseStatus.getLocalDateTime(), LocalDateTime.now());
            Duration duration = Duration.between(harshaExerciseStatus.getLocalDateTime(), LocalDateTime.now())
            if(!isPleaseWalkAnnounced && (duration.getSeconds() > 600)){
                log.debug("Please walk announced")
                apiInvoker.invokeApi(hallAnnouncement+"Harsha please walk")
                reactTimeForAnnouncement = 0
                forReactTimeForAnnouncement = true
                isPleaseWalkAnnounced = true
                //apiInvoker.invokeApi(turnOnEntertainment)
                //apiInvoker.invokeApi(pauseTv)
                //apiInvoker.invokeApi(turnOffEntertainment)
            }else if(duration.getSeconds() < 600){
                forReactTimeForAnnouncement = false
                forReactTimeAfterPause = false
            }
            if(forReactTimeForAnnouncement && (reactTimeForAnnouncement > 15)){
                apiInvoker.invokeApi("${hallAnnouncement}I didn't see you that you are walking. If this repeat next time I'll turn off tv")
                log.debug("Pause TV")
                apiInvoker.invokeApi(pauseTv)
                reactTimeAfterPause = 0;
                forReactTimeAfterPause = true
                forReactTimeForAnnouncement = false
            }
            if(forReactTimeAfterPause && (reactTimeAfterPause > 30)){
                log.debug("Turn off TV")
                apiInvoker.invokeApi("${hallAnnouncement}Sorry to turn off TV, just try to walk")
                apiInvoker.invokeApi(turnOffEntertainment)
                forReactTimeAfterPause = false
            }
        }
    }

    @Scheduled(cron = "0/2 * * * * *")
    void decoLightOnOff(){
        Duration duration = Duration.between(decoLightStatus.getLocalDateTime(), LocalDateTime.now())
        /*Duration duration1 = Duration.between(harshaExerciseStatus.getLocalDateTime(), LocalDateTime.now())
        log.debug("Duration of deco: ${duration.getSeconds()}, Duration of harsha: ${duration1.getSeconds()}")*/
        if(duration.getSeconds() > 60){
            if(isLightOn){
                log.debug("Turn off deco light")
                apiInvoker.invokeApi(turnOffDecoLight)
                isLightOn = false
            }

            //decoLightStatus.setLocalDateTime(LocalDateTime.now())
        }else{
            if(!isLightOn) {
                log.debug("Turn on deco light")
                apiInvoker.invokeApi(turnOnDecoLight)
                isLightOn = true
            }
        }
    }

    //@Scheduled(cron = "0 0/1 15-17 * * *")
    public void tvOnOrOff(){
        //log.debug("Announcing tvOnOff")
        //println("Inside tvOnOrOff method")
        //After setting user agent only able to call the API
        // Always check the IDE pointing proper Java Installation or not, otherwise will get trust store keys issue.
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<>()<String>(headers);
        OnOrOffDuration onOrOffDuration = restTemplate.exchange(onOffDuration, HttpMethod.GET, entity, OnOrOffDuration.class).getBody()
        long minutes = onOrOffDuration.getDuration().toMinutes()

        if(onOrOffDuration.isTvOn()){
            if(minutes > 10){
                this.apiInvoker.invokeVoiceMonkeyApi(hariAnnouncement + "Arey yar please walk in between")
            }
        }

        /*if(onOrOffDuration.isTvOn()){
            if(minutes > 120){
                restTemplate.exchange(turnOffTv, HttpMethod.GET, entity, OnOrOffDuration.class).getBody()
            }
        }else{
            if(minutes > 30){
                restTemplate.exchange(turnOnTv, HttpMethod.GET, entity, OnOrOffDuration.class).getBody()
            }
        }*/
        println("onOrOffDuration ${onOrOffDuration}")
    }

    @Scheduled(cron = "0 0 2 * * *")
    void resetSchedules(){
        log.debug("resetSchedules() called")
        HariSchedule hariScheduleObj = new HariSchedule()
        LocalTime scheduledFrom = LocalTime.now()
        //LocalTime scheduledFrom = LocalTime.of(8, 00)
        hariSchedules =  hariScheduleObj.prepareScheduledTimes(scheduledFrom);
    }

    @Scheduled(cron = "0 0 7 * * *")
    void startHariScheduleForTheDay(){
        log.debug("startHariScheduleForTheDay() called")
        LocalTime scheduledFrom = LocalTime.now().plusHours(1)
        // For testing enable following
        //LocalTime scheduledFrom  = LocalTime.of(8, 00)
        LocalDate date = LocalDate.now()
        hariSchedules.each {
            hariSchedule ->
                hariSchedule.setLocalDateTime(LocalDateTime.of(date, scheduledFrom))
                hariSchedule.setAlreadyAdded(false)
                scheduledFrom = scheduledFrom.plusSeconds(hariSchedule.getDuration().getSeconds())
                log.debug("Time: ${hariSchedule.getLocalDateTime()} Duration: ${hariSchedule.duration.getSeconds()/60} taskDesc: ${hariSchedule.taskDesc}" )
        }
    }
    boolean onceOnly = true
    //@Scheduled(cron = "0 0/1 8-23 * * *")
    void hariSchedule(){
        // Following is useful when doing some testing
        /*if(onceOnly){
            resetSchedules()
            startHariScheduleForTheDay()
            onceOnly = false
        }*/
        //log.debug("hariSchedule() called")
        hariSchedules.each {
            schedule ->
            if (!schedule.getAlreadyAdded()) {
                if (schedule.getLocalDateTime().isBefore(LocalDateTime.now())) {
                    schedule.setAlreadyAdded(true)
                    log.debug("Alexa hari schedule call: "+schedule.getTaskDesc() + "about "+ (schedule.getDuration().getSeconds()/60) +"Minutes");
                    apiInvoker.invokeVoiceMonkeyApi(hariAnnouncement + schedule.getTaskDesc() + "about "+ (schedule.getDuration().getSeconds()/60) +"Minutes")
                }
            }
        }
    }
}
