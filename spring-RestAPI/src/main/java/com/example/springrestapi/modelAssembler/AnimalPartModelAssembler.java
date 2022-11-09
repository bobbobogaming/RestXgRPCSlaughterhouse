package com.example.springrestapi.modelAssembler;

import com.example.model.AnimalPartEntity;
import com.example.springrestapi.controller.AnimalController;
import com.example.springrestapi.controller.AnimalPartController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class AnimalPartModelAssembler implements RepresentationModelAssembler<AnimalPartEntity, EntityModel<AnimalPartEntity>> {
  @Override public EntityModel<AnimalPartEntity> toModel(AnimalPartEntity entity) {
    EntityModel<AnimalPartEntity> entityModel = EntityModel.of(entity,
        linkTo(methodOn(AnimalPartController.class).one(entity.getId())).withSelfRel(),
        linkTo(methodOn(AnimalPartController.class).all()).withRel("animalparts")

    );

    if (entity.getAnimal() != null){
      entityModel.add(linkTo(methodOn(AnimalController.class).one(entity.getAnimal().getRegistrationNo())).withRel("animal"));
    }
    return entityModel;
  }
}
