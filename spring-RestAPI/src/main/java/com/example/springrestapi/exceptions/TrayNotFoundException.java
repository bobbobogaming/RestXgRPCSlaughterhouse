package com.example.springrestapi.exceptions;

public class TrayNotFoundException extends RuntimeException {
  public TrayNotFoundException(Long id) {
    super("Could not find tray: " + id);
  }
}
