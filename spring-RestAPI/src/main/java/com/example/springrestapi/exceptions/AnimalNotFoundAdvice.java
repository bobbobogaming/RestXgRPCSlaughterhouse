package com.example.springrestapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class AnimalNotFoundAdvice {
  @ResponseBody
  @ExceptionHandler(AnimalNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String animalNotFoundHandler(AnimalNotFoundException ex){
    return ex.getMessage();
  }
}
