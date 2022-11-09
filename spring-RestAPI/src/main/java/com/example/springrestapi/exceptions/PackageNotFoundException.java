package com.example.springrestapi.exceptions;

public class PackageNotFoundException extends RuntimeException{
  public PackageNotFoundException(Long id) {
    super("Could not find package with id: " + id);
  }
}
