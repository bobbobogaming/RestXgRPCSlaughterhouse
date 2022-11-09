package com.example.springrestapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.format.DateTimeParseException;

@ControllerAdvice
public class DateTimeParseAdvice {
  @ResponseBody
  @ExceptionHandler(DateTimeParseException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public String dateTimeParseHandler(DateTimeParseException ex){
    return "invalid date format, format is 'yyyy-MM-dd HH:mm'";
  }

}
