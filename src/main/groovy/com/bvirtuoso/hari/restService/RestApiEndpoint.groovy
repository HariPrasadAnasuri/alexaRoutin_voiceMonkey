package com.bvirtuoso.hari.restService

import com.bvirtuoso.hari.api.ApiInvoker
import com.bvirtuoso.hari.config.constants.AppConstants
import com.bvirtuoso.hari.model.DishInfo
import com.bvirtuoso.hari.model.GkQuestion
import com.bvirtuoso.hari.model.OnOrOffDuration
import com.bvirtuoso.hari.model.PersonInfo
import com.bvirtuoso.hari.model.jpa.*
import com.bvirtuoso.hari.repository.CapitalRepository
import com.bvirtuoso.hari.repository.CountryRepository
import com.bvirtuoso.hari.repository.GkQuestionRepository
import com.bvirtuoso.hari.repository.MemberRepository
import com.bvirtuoso.hari.repository.TvOnOffRepository
import org.apache.juli.logging.Log
import org.apache.juli.logging.LogFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import org.threeten.extra.AmountFormats

import jakarta.servlet.http.HttpServletRequest
import java.sql.Timestamp
import java.time.Duration
import java.time.LocalDateTime

@RestController
class RestApiEndpoint {
    private final static Log log = LogFactory.getLog(RestApiEndpoint.class)


    @Value("\${voiceMonkey.entertainment.turnOff}") String turnOffEntertainment;
    @Value("\${voiceMonkey.entertainment.turnOn}") String turnOnEntertainment;

    private String harshaAvailable = "unavailable"
    private List<DishInfo> dishInfos = new ArrayList<>()
    private List<PersonInfo> personInfos = new ArrayList<>()
    private List<Map> queryDataList = [];

    final private RestTemplate restTemplate
    private final ApiInvoker apiInvoker
    private final MemberRepository memberRepository
    private final GkQuestionRepository gkQuestionRepository
    private final TvOnOffRepository tvOnOffRepository
    private final CountryRepository countryRepository
    private final CapitalRepository capitalRepository

    private boolean isPleaseWalkAnnounced = false

    public RestApiEndpoint(RestTemplate restTemplate,
                           ApiInvoker apiInvoker,
                           MemberRepository memberRepository,
                           TvOnOffRepository tvOnOffRepository,
                           CountryRepository countryRepository,
                           CapitalRepository capitalRepository,
                           GkQuestionRepository gkQuestionRepository
    ){
        log.debug("Scheduler initialized")
        this.restTemplate = restTemplate
        this.apiInvoker = apiInvoker
        this.memberRepository = memberRepository
        this.tvOnOffRepository = tvOnOffRepository
        this.countryRepository = countryRepository
        this.capitalRepository = capitalRepository
        this.gkQuestionRepository = gkQuestionRepository
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
        personInfo.setName(name.toLowerCase());
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

    @GetMapping("/clearPeopleInfo")
    @ResponseBody
    public String clearPeopleInfo() {
        //log.debug("Clearing dish info")
        personInfos = new ArrayList<>();
        return "success";
    }
    @GetMapping("/clearDishInfo")
    @ResponseBody
    public String clearDishInfo() {
        //log.debug("Clearing dish info")
        dishInfos = new ArrayList<>();
        return "success";
    }

    @GetMapping("/member")
    @ResponseBody
    public MemberEntity getMemberInfo(HttpServletRequest request, @RequestParam String person) {
        List<MemberEntity> memberEntityList = memberRepository.findByPerson(person);
        return memberEntityList.get(0);
    }


    @GetMapping("/capital")
    @ResponseBody
    public CapitalEntity getCapitalByCountry(HttpServletRequest request, @RequestParam String country) {
        List<CountryEntity> countryList = countryRepository.findByCountry(country);
        List<CapitalEntity> capitalEntityList = capitalRepository.findByCountry(countryList.get(0));
        return capitalEntityList.get(0);
    }

    @GetMapping("/country")
    @ResponseBody
    public CountryEntity getCountryByCapital(HttpServletRequest request, @RequestParam String capital) {
        CapitalEntity capitalEntityList = capitalRepository.findByCapital(capital);
        return capitalEntityList.getCountry();
    }

    @GetMapping("/tvOn")
    @ResponseBody
    public void tvOn(HttpServletRequest request) {
        apiInvoker.invokeApi(turnOnEntertainment);
        //TvOnOff tvOnOff1 = tvOnOffRepository.getLastRow()
        TvOnOffEntity tvOnOffEntity = new TvOnOffEntity();
        tvOnOffEntity.setActivityTime(Timestamp.valueOf(LocalDateTime.now()));
        tvOnOffEntity.setTvStatus(true);
        deleteIfRowsExceeded();
        tvOnOffRepository.save(tvOnOffEntity);
    }

    @GetMapping("/tvOff")
    @ResponseBody
    public void tvOff(HttpServletRequest request) {
        apiInvoker.invokeApi(turnOffEntertainment);
        //TvOnOff tvOnOff1 = tvOnOffRepository.getLastRow()
        TvOnOffEntity tvOnOffEntity = new TvOnOffEntity();
        tvOnOffEntity.setActivityTime(Timestamp.valueOf(LocalDateTime.now()));
        tvOnOffEntity.setTvStatus(false);
        deleteIfRowsExceeded();
        tvOnOffRepository.save(tvOnOffEntity);
    }

    @GetMapping("/tvOnOrOffDuration")
    @ResponseBody
    public OnOrOffDuration getTvOnOrOff(HttpServletRequest request) {

        TvOnOffEntity tvOnOffEntity = tvOnOffRepository.getLastRow();
        Duration duration = Duration.between(tvOnOffEntity.getActivityTime().toLocalDateTime(), LocalDateTime.now());
        OnOrOffDuration onOrOffDuration = new OnOrOffDuration();
        onOrOffDuration.setTvOn(tvOnOffEntity.getTvStatus());
        String humanReadableDuration = AmountFormats.wordBased(duration, Locale.getDefault());
        System.out.println("humanReadableDuration: "+ humanReadableDuration);
        int minutesIndex = humanReadableDuration.indexOf("minute");
        if(minutesIndex > 0){
            humanReadableDuration = humanReadableDuration.substring(0, (minutesIndex + 7));
        }else{
            humanReadableDuration = "less than a minute";
        }
        onOrOffDuration.setDuration(duration);
        String message;
        if(tvOnOffEntity.getTvStatus()){
            message = "TV is ON, since "+ humanReadableDuration;
        }else{
            message = "TV is OFF, since "+ humanReadableDuration;
        }
        onOrOffDuration.setMessage(message);
        return onOrOffDuration;
    }
    @GetMapping("/getGkQuestion")
    @ResponseBody
    public GkQuestion getGkQuestion(){
        GkQuestion gkQuestion = new GkQuestion();
        GkQuestionEntity gkQuestionEntity = gkQuestionRepository.findTopByStatus(AppConstants.GK_CONSTANTS.UNTOUCHED.getCode());
        if(gkQuestionEntity == null){
            gkQuestionEntity = gkQuestionRepository.findTopByStatus(AppConstants.GK_CONSTANTS.WRONGANSWER.getCode());
        }
        if(gkQuestionEntity == null){
            gkQuestionEntity = gkQuestionRepository.findTopByStatus(AppConstants.GK_CONSTANTS.ASKED.getCode());
        }
        if(gkQuestionEntity != null){
            gkQuestion.setQuestion(gkQuestionEntity.getQuestion());
            gkQuestion.setId(gkQuestionEntity.getId());
            gkQuestionEntity.setStatus(AppConstants.GK_CONSTANTS.ASKED.getCode());
            gkQuestionRepository.save(gkQuestionEntity);
        }else{
            gkQuestion.setQuestion("Questions are over");
        }
        return gkQuestion;
    }

    @GetMapping("/validateGkAnswer")
    @ResponseBody
    public String getGkQuestion(@RequestParam String answer, @RequestParam Long questionId){
        GkQuestionEntity gkQuestionEntity = gkQuestionRepository.findTopById(questionId);
        String rightAnswer = gkQuestionEntity.getAnswer();
        String returnMessage = "";
        if(rightAnswer.equalsIgnoreCase(answer)){
            gkQuestionEntity.setStatus(AppConstants.GK_CONSTANTS.ANSWERED.getCode());
            returnMessage = "Right answer";
        }else{
            gkQuestionEntity.setStatus(AppConstants.GK_CONSTANTS.WRONGANSWER.getCode());
            returnMessage = "Wrong answer";
        }
        gkQuestionRepository.save(gkQuestionEntity);
        return returnMessage;
    }

    @GetMapping("/addQuestData")
    @ResponseBody
    public String addQuestData(@RequestParam def questDataString){
        String[] questDataArray = questDataString.split(",")
        queryDataList.clear()
        questDataArray.each {
            data ->
                def questData = [:]
                questData["tip"] = data
                queryDataList.add(questData)
        }
        log.debug("Quest data: ${questDataString}")
        return "Success"
    }

    @GetMapping("/getQuestData")
    @ResponseBody
    public List<Map> addQuestData(){
        return queryDataList
    }

    void deleteIfRowsExceeded(){
        long count = tvOnOffRepository.count();
        if(count > 100){
            tvOnOffRepository.delete(tvOnOffRepository.getFirstRow());
        }
    }

}
