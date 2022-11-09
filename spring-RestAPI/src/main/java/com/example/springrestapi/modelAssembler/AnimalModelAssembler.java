package com.example.springrestapi.modelAssembler;

import com.example.model.AnimalEntity;
import com.example.springrestapi.controller.AnimalController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AnimalModelAssembler implements RepresentationModelAssembler<AnimalEntity,EntityModel<AnimalEntity>>{

  @Override public EntityModel<AnimalEntity> toModel(AnimalEntity entity) {
    return EntityModel.of(entity,
      linkTo(methodOn(AnimalController.class).one(entity.getRegistrationNo())).withSelfRel(),
      linkTo(methodOn(AnimalController.class).all(null, null)).withRel("animals").expand());
  }
}
