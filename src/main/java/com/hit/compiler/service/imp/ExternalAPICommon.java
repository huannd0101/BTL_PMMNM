package com.hit.compiler.service.imp;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class ExternalAPICommon {
  private static final HttpHeaders fileHeaders = new HttpHeaders();
  private static final HttpHeaders jsonHeaders = new HttpHeaders();
  private static final RestTemplate restTemplate = new RestTemplate();

  static {
    fileHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
    jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
  }

  public static Object post(String url, Map<String, Object> elements, Class<?> output, boolean isFormData) {
    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(buildBody(elements),
        getHeader(isFormData));
    return restTemplate.postForEntity(url, requestEntity, output).getBody();
  }

  private static MultiValueMap<String, Object> buildBody(Map<String, Object> elements) {
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    for (String key : elements.keySet()) {
      body.add(key, elements.get(key));
    }
    return body;
  }

  private static HttpHeaders getHeader(boolean isFormData) {
    return isFormData ? fileHeaders : jsonHeaders;
  }

}
