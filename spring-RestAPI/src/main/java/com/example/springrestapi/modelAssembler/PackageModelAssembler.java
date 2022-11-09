package com.example.springrestapi.modelAssembler;

import com.example.model.PackageEntity;
import com.example.springrestapi.controller.PackageController;
import com.example.springrestapi.controller.TrayController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PackageModelAssembler implements RepresentationModelAssembler<PackageEntity, EntityModel<PackageEntity>> {
  @Override public EntityModel<PackageEntity> toModel(PackageEntity entity) {
    EntityModel<PackageEntity> entityModel = EntityModel.of(entity,
        linkTo(methodOn(PackageController.class).one(entity.getPackageId())).withSelfRel(),
        linkTo(methodOn(PackageController.class).all()).withRel("packages"));


    if (!entity.getTrays().isEmpty()){
      for (int i = 0; i < entity.getTrays().size(); i++) {
        entityModel.add(linkTo(methodOn(TrayController.class).one(entity.getTrays().get(i).getId())).withRel("trayNo:" + (i + 1)));
      }
    }

    return entityModel;
  }
}
