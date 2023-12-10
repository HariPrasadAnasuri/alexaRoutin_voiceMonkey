package com.bvirtuoso.hari.restService

import com.bvirtuoso.hari.api.ApiInvoker
import com.bvirtuoso.hari.config.constants.AppConstants
import com.bvirtuoso.hari.model.jpa.GkQuestionEntity
import com.bvirtuoso.hari.model.jpa.GkQuestionsWithImagesEntity
import com.bvirtuoso.hari.repository.GkQuestionRepository
import com.bvirtuoso.hari.repository.GkQuestionsWithImagesRepository
import jakarta.ws.rs.QueryParam
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/gk/questions")
class GkQuestionsWithImagesController {
    protected static final Log log = LogFactory.getLog(GkQuestionsWithImagesController.class)

    private AppConstants.TvControlType controlType;
    private LocalDateTime localDateTime = LocalDateTime.now();
    private final ApiInvoker apiInvoker
    @Value("\${voiceMonkey.hallAnnouncement}") String hallAnnouncement
    private LocalDateTime lastTimeAnswered;

    private final GkQuestionsWithImagesRepository gkQuestionsWithImagesRepository;

    public GkQuestionsWithImagesController(final GkQuestionsWithImagesRepository gkQuestionsWithImagesRepository,
                                           final ApiInvoker apiInvoker){
        this.gkQuestionsWithImagesRepository = gkQuestionsWithImagesRepository;
        this.apiInvoker = apiInvoker
        this.lastTimeAnswered = LocalDateTime.now()
    }

    @GetMapping("/getNextQuestion")
    GkQuestionsWithImagesEntity getNextQuestion()
    {
        GkQuestionsWithImagesEntity questionEntity = gkQuestionsWithImagesRepository.findTop1ByProgress(false)
        return questionEntity
    }

    @GetMapping("/answerTheQuestion")
    GkQuestionsWithImagesEntity answerTheQuestion(@QueryParam("") String option)
    {
        log.debug("Option value is: "+ option)
        Duration duration = Duration.between(lastTimeAnswered, LocalDateTime.now())
        if(duration.getSeconds() > 30){
            GkQuestionsWithImagesEntity questionEntity = gkQuestionsWithImagesRepository.findTop1ByProgress(false)
            if(questionEntity) {
                if (questionEntity.getAnswerOption().toLowerCase() == option.toLowerCase()) {
                    questionEntity.setProgress(true)
                    apiInvoker.invokeApi(hallAnnouncement + "Hey great your answer is right.")
                } else {
                    questionEntity.setProgress(false)
                    apiInvoker.invokeApi(hallAnnouncement + "Sorry wrong answer")
                }
                questionEntity.setRepeatedCount(questionEntity.getRepeatedCount()+1)
                gkQuestionsWithImagesRepository.save(questionEntity)
            }
            log.debug("Option value is: "+ option)
            lastTimeAnswered = LocalDateTime.now()
            return questionEntity
        }
    }

    /*@GetMapping("/setDate")
    void setDate(@RequestParam LocalDateTime date)
    {
        localDateTime = date;
        println("Date: "+ date)
    }*/
}

