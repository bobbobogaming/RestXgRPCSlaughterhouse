package com.example.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class AnimalPartEntity {
  private @Id @GeneratedValue Long id;

  @ManyToOne
  private AnimalEntity animalEntity;
  private AnimalPartTypeEnum animalPartTypeEnum;
  private int weight;

  public AnimalPartEntity() {
  }

  public AnimalPartEntity(AnimalEntity animalEntity, AnimalPartTypeEnum animalPartTypeEnum, int weight) {
    this.animalEntity = animalEntity;
    this.animalPartTypeEnum = animalPartTypeEnum;
    this.weight = weight;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public AnimalEntity getAnimal() {
    return animalEntity;
  }

  public void setAnimal(AnimalEntity animalEntity) {
    this.animalEntity = animalEntity;
  }

  public AnimalPartTypeEnum getAnimalPartType() {
    return animalPartTypeEnum;
  }

  public void setAnimalPartType(AnimalPartTypeEnum animalPartTypeEnum) {
    this.animalPartTypeEnum = animalPartTypeEnum;
  }

  public int getWeight() {
    return weight;
  }

  public void setWeight(int weight) {
    this.weight = weight;
  }

  @Override public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof AnimalPartEntity))
      return false;
    AnimalPartEntity that = (AnimalPartEntity) o;
    return weight == that.weight && Objects.equals(id, that.id)
        && Objects.equals(animalEntity, that.animalEntity) && Objects.equals(
        animalPartTypeEnum,
        that.animalPartTypeEnum);
  }

  @Override public int hashCode() {
    return Objects.hash(id, animalEntity, animalPartTypeEnum, weight);
  }

  @Override public String toString() {
    return "AnimalPart{" + "id=" + id + ", animal=" + animalEntity + ", partType='"
        + animalPartTypeEnum + '\'' + ", weight=" + weight + '}';
  }
}
