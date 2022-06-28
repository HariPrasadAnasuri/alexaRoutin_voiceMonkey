package com.bvirtuoso.hari.api

import com.bvirtuoso.hari.model.DishInfo
import com.bvirtuoso.hari.model.PersonInfo
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Component
public class ApiInvoker {
  final private RestTemplate restTemplate;
  public ApiInvoker(final RestTemplate restTemplate){
    this.restTemplate = restTemplate;
  }
  public def invokeVoiceMonkeyApi(String url){
    invokeApi(url);
  }

  void invokeApi(String url){
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    //After setting user agent only able to call the API
    // Always check the IDE pointing proper Java Installation or not, otherwise will get trust store keys issue.
    headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
    HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
    restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
  }

  public List<PersonInfo> getPersonInfo(String url){
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    //After setting user agent only able to call the API
    // Always check the IDE pointing proper Java Installation or not, otherwise will get trust store keys issue.
    headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
    HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
    return restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<PersonInfo>>() {}).getBody();
  }

  public List<DishInfo> getDishInfo(String url){
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    //After setting user agent only able to call the API
    // Always check the IDE pointing proper Java Installation or not, otherwise will get trust store keys issue.
    headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
    HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
    return restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<DishInfo>>() {}).getBody();
  }
}
