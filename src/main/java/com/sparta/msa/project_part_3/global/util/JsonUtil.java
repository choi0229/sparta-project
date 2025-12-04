package com.sparta.msa.project_part_3.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.msa.project_part_3.global.exception.DomainException;
import com.sparta.msa.project_part_3.global.exception.DomainExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JsonUtil {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static <T> String toJson(T object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new DomainException(DomainExceptionCode.JSON_PROCESSING_ERROR);
    }
  }

  public static <T> T fromJson(String jsonString, Class<T> clazz) {
    try {
      return objectMapper.readValue(jsonString, clazz);
    } catch (JsonProcessingException e) {
      throw new DomainException(DomainExceptionCode.JSON_PROCESSING_ERROR);
    }
  }
}