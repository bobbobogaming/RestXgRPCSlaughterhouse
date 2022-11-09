package com.example.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
public class AnimalEntity {
  private @Id @GeneratedValue Long registrationNo;
  private AnimalTypeEnum animalTypeEnum;
  private int weight;
  private String origin;
  private LocalDateTime arrival;

  public AnimalEntity() { }

  public AnimalEntity( AnimalTypeEnum animalTypeEnum, int weight, String origin) {
    this.animalTypeEnum = animalTypeEnum;
    this.weight = weight;
    this.origin = origin;
    this.arrival = LocalDateTime.now();
  }

  public Long getRegistrationNo() {
    return registrationNo;
  }

  public void setRegistrationNo(Long registrationNo) {
    this.registrationNo = registrationNo;
  }

  public AnimalTypeEnum getAnimalType() {
    return animalTypeEnum;
  }

  public void setAnimalType(AnimalTypeEnum animalTypeEnum) {
    this.animalTypeEnum = animalTypeEnum;
  }

  public int getWeight() {
    return weight;
  }

  public void setWeight(int weight) {
    this.weight = weight;
  }

  public String getOrigin() {
    return origin;
  }

  public void setOrigin(String origin) {
    this.origin = origin;
  }

  public LocalDateTime getArrival() {
    return arrival;
  }

  public void setArrival(String arrival) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    this.arrival = LocalDateTime.parse(arrival, formatter);
  }

  public void longSetArrival(long arrivalInEpochSecond) {
    this.arrival = LocalDateTime.ofEpochSecond(arrivalInEpochSecond,0,
        ZoneOffset.UTC);
  }

  @Override public int hashCode() {
    return Objects.hash(registrationNo, animalTypeEnum,weight,origin,
        animalTypeEnum);
  }

  @Override public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!(obj instanceof AnimalEntity))
      return false;
    AnimalEntity other = (AnimalEntity) obj;
    return registrationNo.equals(other.registrationNo)
        && animalTypeEnum == other.animalTypeEnum
        && weight == other.weight
        && origin.equals(other.origin)
        && arrival.equals(other.arrival);
  }

  @Override public String toString() {
    return "Animal{" + "registrationNo=" + registrationNo + ", animalType="
        + animalTypeEnum + ", weight=" + weight + ", origin='" + origin + '\''
        + ", arrival=" + arrival + '}';
  }
}
