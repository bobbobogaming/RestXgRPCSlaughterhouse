package com.example.springrestapi.exceptions;

public class AnimalNotFoundException extends RuntimeException {
  public AnimalNotFoundException(Long id) {
    super("Could not find animal with id: " + id);
  }
}
