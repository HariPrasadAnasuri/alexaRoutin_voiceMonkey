package com.bvirtuoso.hari.scheduler

import com.bvirtuoso.hari.api.ApiInvoker
import com.bvirtuoso.hari.model.DishInfo
import com.bvirtuoso.hari.model.HariSchedule
import com.bvirtuoso.hari.model.PersonInfo
import com.bvirtuoso.hari.model.PersonalSchedule
import com.bvirtuoso.hari.model.jpa.DishInfoJpa
import com.bvirtuoso.hari.model.jpa.GkQuestionsWithImagesEntity
import com.bvirtuoso.hari.model.jpa.HealthInfoJpa
import com.bvirtuoso.hari.model.jpa.PersonAvailabilityEntity
import com.bvirtuoso.hari.model.jpa.TvOnOffEntity
import com.bvirtuoso.hari.repository.DishInfoRepository
import com.bvirtuoso.hari.repository.HealthInfoRepository
import com.bvirtuoso.hari.repository.PersonAvailabilityRepository
import com.bvirtuoso.hari.repository.TvOnOffRepository
import com.bvirtuoso.hari.restService.GkQuestionsWithImagesController
import com.bvirtuoso.hari.restService.RestApiEndpoint
import com.bvirtuoso.hari.service.MotionBasedTask
import com.bvirtuoso.hari.service.NgrokService
import org.apache.juli.logging.Log
import org.apache.juli.logging.LogFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

import java.sql.Timestamp
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
    private final TvOnOffRepository tvOnOffRepository
    private final PersonAvailabilityRepository personAvailabilityRepository
    private final NgrokService ngrokService

    final private RestApiEndpoint restApiEndpoint
    final private GkQuestionsWithImagesController gkQuestionsWithImagesController

    @Value("\${voiceMonkey.hostForImagesOrVideo}") String hostForImagesOrVideo
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
    @Value("\${voiceMonkey.turnOnAc}") String turnOnAc
    @Value("\${voiceMonkey.turnOffAc}") String turnOffAc
    @Value("\${voiceMonkey.turnOnPanasonicAc}") String turnOnPanasonicAc
    @Value("\${voiceMonkey.turnOffPanasonicAc}") String turnOffPanasonicAc
//    @Value("\${app.mobile.operateTermuxCharger}") String operateTermuxCharger

    private boolean alreadyAnnouncedPleaseWalk = false;
    private int reactTimeForAnnouncement = 0
    private boolean forReactTimeForAnnouncement = false
    private int reactTimeAfterPause = 0
    private boolean forReactTimeAfterPause = false
    private boolean notYetWarned = true;

    private final DishInfoRepository dishInfoRepository
    private final HealthInfoRepository healthInfoRepository

    private boolean isLightOn = false
    private boolean isPleaseWalkAnnounced = false
    private LocalDateTime walkingStartedTime = LocalDateTime.now()
    private Duration walkingInterval
    private boolean justAnnouncedAboutWalking = false
    private int warningCounterToSwitchOffTv = 0

    private boolean isPanasonicAcOn = false;

    public InvokeAlexaRoutineScheduler(RestTemplate restTemplate, ApiInvoker apiInvoker,
                                        DishInfoRepository dishInfoRepository,
                                        HealthInfoRepository healthInfoRepository,
                                       @Value("\${voiceMonkey.hariAnnouncement}") String hariAnnouncement,
                                       final RestApiEndpoint restApiEndpoint, final TvOnOffRepository tvOnOffRepository,
                                       final PersonAvailabilityRepository personAvailabilityRepository,
                                       final NgrokService ngrokService,
                                       final GkQuestionsWithImagesController gkQuestionsWithImagesController

    ){
        log.debug("Scheduler initialized")
        this.restTemplate = restTemplate
        this.apiInvoker = apiInvoker
        this.dishInfoRepository = dishInfoRepository
        this.healthInfoRepository = healthInfoRepository
        this.hariAnnouncement = hariAnnouncement
        this.apiInvoker.invokeVoiceMonkeyApi(hariAnnouncement + "Hey hari, application is deployed")
        this.restApiEndpoint = restApiEndpoint
        this.tvOnOffRepository = tvOnOffRepository
        this.personAvailabilityRepository = personAvailabilityRepository
        this.ngrokService = ngrokService
        this.gkQuestionsWithImagesController = gkQuestionsWithImagesController;

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
    @Scheduled(cron = "0 0/20 * * * *")
    public void MakeSureTurnoffAc(){
        log.debug("Turn off AC")
        apiInvoker.invokeVoiceMonkeyApi(turnOffAc)
    }

    @Scheduled(cron = "15 0 0/2 * * *")
    public void turnOnAc(){
        log.debug("Turning on AC")
        apiInvoker.invokeVoiceMonkeyApi(turnOnAc)

        Timer timer = new Timer();
        int delay = 300000;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                apiInvoker.invokeVoiceMonkeyApi(turnOffAc)
            }
        }, delay);
        Timer timer1 = new Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                apiInvoker.invokeVoiceMonkeyApi(turnOffAc)
            }
        }, delay*2);
        Timer timer2 = new Timer();
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                apiInvoker.invokeVoiceMonkeyApi(turnOffAc)
            }
        }, delay*3);
    }

    //@Scheduled(cron = "0 0/30 0-09 * * *")
    public void turnOnPanasonicAc(){
        log.debug("Turning on Panasonic AC")
        apiInvoker.invokeVoiceMonkeyApi(turnOnPanasonicAc)

        Timer timer = new Timer();
        int delay = 900000;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                apiInvoker.invokeVoiceMonkeyApi(turnOffPanasonicAc)
            }
        }, delay);
    }

    //Able to charge the mobile once the charge percentage is down.
    /*@Scheduled(cron = "1 0/15 * * * *")
    public void operateChargerForTermuxMobileMI(){
        try {
            apiInvoker.invokeVoiceMonkeyApi(operateTermuxCharger)
        }catch(Exception e){
            println("Termux API to operate charger is temporarily down")
        }
    }*/

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

    //@Scheduled(cron = "0/2 * * * * *")
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

    //@Scheduled(cron = "0 0/1 * * * *")
    public void tvOnOrOff(){
        TvOnOffEntity lastTimeTvStatus = tvOnOffRepository.getLastRow()
        String availability = restApiEndpoint.getHarshaAvailability()
        if(availability == "available"){
            if(lastTimeTvStatus.getTvStatus()){
                Duration durationFromLastTimeTvOn = Duration.between(lastTimeTvStatus.getActivityTime().toLocalDateTime(), LocalDateTime.now());
                Duration announceAfterThisDuration = Duration.between(lastTimeTvStatus.getActivityTime().toLocalDateTime(), lastTimeTvStatus.getActivityTime().toLocalDateTime().plusMinutes(15))
                Duration tvCanOnAfterThisDuration = Duration.between(lastTimeTvStatus.getActivityTime().toLocalDateTime(), lastTimeTvStatus.getActivityTime().toLocalDateTime().plusMinutes(30))
                boolean turnOffTv = (durationFromLastTimeTvOn <=> tvCanOnAfterThisDuration) > 0;
                boolean announce = (durationFromLastTimeTvOn <=> announceAfterThisDuration) > 0;
                if(announce && notYetWarned){
                    apiInvoker.invokeApi(hallAnnouncement+ "Harsha it is already 15 minutes since you on the TV")
                    notYetWarned = false;
                }

                if(turnOffTv){
                    apiInvoker.invokeApi(turnOffEntertainment);
                    notYetWarned = true;
                    TvOnOffEntity tvOnOffEntity = new TvOnOffEntity();
                    tvOnOffEntity.setActivityTime(Timestamp.valueOf(LocalDateTime.now()));
                    tvOnOffEntity.setTvStatus(false);
                    tvOnOffRepository.save(tvOnOffEntity);
                    log.debug("Turning off TV");

                }
            }
        }
    }

    //@Scheduled(cron = "0 0 2 * * *")
    void resetSchedules(){
        log.debug("resetSchedules() called")
        HariSchedule hariScheduleObj = new HariSchedule()
        LocalTime scheduledFrom = LocalTime.now()
        //LocalTime scheduledFrom = LocalTime.of(8, 00)
        hariSchedules =  hariScheduleObj.prepareScheduledTimes(scheduledFrom);
    }

    //@Scheduled(cron = "0 0 7 * * *")
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

    @Scheduled(cron = "15 0/5 * * * *")
    public void tryToKeepNgrokActive(){
        ngrokService.getNgrokUrl();
    }

    //@Scheduled(cron = "0/30 * * * * *")
    public void screenSaverForTv(){
        List<String> stringsList = new ArrayList<>();
        stringsList.add("0")

        Random random = new Random();

        // Generate a random index within the range of the list size
        int randomIndex = random.nextInt(stringsList.size());

        // Get the random string from the list
        String randomString = stringsList.get(randomIndex);
        log.debug("The image will show: https://api-v2.voicemonkey.io/announcement?token=aefdfde159807039f4c21fa0a5fe3680_811ba0df7e31c018095aa378843aceb5&device=monkey-conn-to-tv&image=https%3A%2F%2F16d4-2405-201-c018-4147-6025-a4e9-c5ca-ab95.ngrok-free.app%2Fmanage-photos%2Fphotos%2F"+ randomString)
        apiInvoker.invokeVoiceMonkeyApi("https://api-v2.voicemonkey.io/announcement?token=aefdfde159807039f4c21fa0a5fe3680_811ba0df7e31c018095aa378843aceb5&device=monkey-conn-to-tv&image=https%3A%2F%2F16d4-2405-201-c018-4147-6025-a4e9-c5ca-ab95.ngrok-free.app%2Fmanage-photos%2Fphotos%2F"+ randomString);
    }

    @Scheduled(cron = "0 0/1 * * * *")
    void askGkQuestionWithImage(){
        log.debug("askGkQuestionWithImage")
        TvOnOffEntity lastTimeTvStatus = tvOnOffRepository.getLastRow()
        PersonAvailabilityEntity personAvailability = personAvailabilityRepository.getLastRow()
        if((personAvailability != null) && (personAvailability.getAvailability().toLowerCase() == "available")) {
            if (lastTimeTvStatus.getTvStatus()) {
                GkQuestionsWithImagesEntity gkQuestionsWithImagesEntity = gkQuestionsWithImagesController.getNextQuestion();
                if(gkQuestionsWithImagesEntity) {
                    apiInvoker.invokeVoiceMonkeyApi("${hostForImagesOrVideo}&image=${gkQuestionsWithImagesEntity.getQuestionImageUrl()}");
                    Thread.sleep(3000)
                    apiInvoker.invokeVoiceMonkeyApi(hallAnnouncement + gkQuestionsWithImagesEntity.getQuestionText())
                    Thread.sleep((0.08 * 1000) * gkQuestionsWithImagesEntity.getQuestionText().length().toInteger() as long)
                    apiInvoker.invokeVoiceMonkeyApi(hallAnnouncement + gkQuestionsWithImagesEntity.getOptions())
                }
            }
        }
    }


}
