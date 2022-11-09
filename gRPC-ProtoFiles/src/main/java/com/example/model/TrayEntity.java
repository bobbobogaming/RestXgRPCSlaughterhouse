package com.example.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class TrayEntity {
  private @Id @GeneratedValue Long id;
  private int maxWeight;
  private AnimalPartTypeEnum animalPartTypeEnum;
  @OneToMany(fetch = FetchType.EAGER)
  private List<AnimalPartEntity> animalPartEntities;

  public TrayEntity() {
    animalPartEntities = new ArrayList<>();
  }

  public TrayEntity(int maxWeight, AnimalPartTypeEnum animalPartTypeEnum,
      List<AnimalPartEntity> animalPartEntities) {
    this.maxWeight = maxWeight;
    this.animalPartTypeEnum = animalPartTypeEnum;
    this.animalPartEntities = animalPartEntities;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getMaxWeight() {
    return maxWeight;
  }

  public void setMaxWeight(int maxWeight) {
    this.maxWeight = maxWeight;
  }

  public AnimalPartTypeEnum getAnimalPartType() {
    return animalPartTypeEnum;
  }

  public void setAnimalPartType(AnimalPartTypeEnum animalPartTypeEnum) {
    this.animalPartTypeEnum = animalPartTypeEnum;
  }

  public List<AnimalPartEntity> getAnimalParts() {
    return animalPartEntities;
  }

  public void setAnimalParts(List<AnimalPartEntity> animalPartEntities) {
    this.animalPartEntities = animalPartEntities;
  }

  @Override public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof TrayEntity))
      return false;
    TrayEntity trayEntity = (TrayEntity) o;
    return maxWeight == trayEntity.maxWeight && Objects.equals(id, trayEntity.id)
        && Objects.equals(animalPartTypeEnum, trayEntity.animalPartTypeEnum)
        && Objects.equals(animalPartEntities, trayEntity.animalPartEntities);
  }

  @Override public int hashCode() {
    return Objects.hash(id, maxWeight, animalPartTypeEnum, animalPartEntities);
  }

  @Override public String toString() {
    return "Tray{" + "id=" + id + ", maxWeight=" + maxWeight
        + ", animalPartType='" + animalPartTypeEnum + '\'' + ", animalParts="
        + animalPartEntities + '}';
  }
}
