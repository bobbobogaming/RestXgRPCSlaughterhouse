package com.example.springrestapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class AnimalPartNotFoundAdvice {
  @ResponseBody
  @ExceptionHandler(AnimalPartNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String animalPartNotFoundHandler(AnimalPartNotFoundException ex){
    return ex.getMessage();
  }
}
