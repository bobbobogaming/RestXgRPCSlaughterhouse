package com.example.springrestapi.modelAssembler;

import com.example.model.TrayEntity;
import com.example.springrestapi.controller.AnimalPartController;
import com.example.springrestapi.controller.TrayController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class TrayModelAssembler implements RepresentationModelAssembler<TrayEntity, EntityModel<TrayEntity>> {
  @Override public EntityModel<TrayEntity> toModel(TrayEntity entity) {
    EntityModel<TrayEntity> entityModel = EntityModel.of(entity,
        linkTo(methodOn(TrayController.class).one(entity.getId())).withSelfRel(),
        linkTo(methodOn(TrayController.class).all()).withRel("trays"));

    if (!entity.getAnimalParts().isEmpty()){
      for (int i = 0; i < entity.getAnimalParts().size(); i++) {
        entityModel.add(linkTo(methodOn(AnimalPartController.class).one(entity.getAnimalParts().get(i).getId())).withRel("trayItem" + (i + 1)));
      }
    }

    return entityModel;
  }
}
