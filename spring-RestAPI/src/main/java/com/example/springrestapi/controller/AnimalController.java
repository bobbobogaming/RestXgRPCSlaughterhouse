package com.example.springrestapi.controller;

import com.example.grpc.*;
import com.example.springrestapi.exceptions.AnimalNotFoundException;
import com.example.springrestapi.modelAssembler.AnimalModelAssembler;
import com.example.model.AnimalEntity;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class AnimalController {
@GrpcClient("GrpcConnection")
  private AnimalServiceGrpc.AnimalServiceBlockingStub animalStub;

  private final AnimalModelAssembler assembler;

  public AnimalController( AnimalModelAssembler assembler) {
    this.assembler = assembler;
  }

  @PostMapping("/animals") ResponseEntity<?> newAnimal(@RequestBody AnimalEntity newAnimal){
    Animal animalToSend = GRPCObjectTranslatorService.toGRPCAnimal(newAnimal);
    Animal animalReceived = animalStub.create(animalToSend);
    AnimalEntity animalEntity = GRPCObjectTranslatorService.toAnimalEntity(animalReceived);
    EntityModel<AnimalEntity> entityModel = assembler.toModel(animalEntity);

    return  ResponseEntity
        .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(entityModel);
  }

  @GetMapping("/animals")
  public CollectionModel<EntityModel<AnimalEntity>> all(@RequestParam(required = false, defaultValue = "") String origin, @RequestParam(required = false, defaultValue = "") String date) {
    AnimalAllRequest animalAllRequest = AnimalAllRequest.newBuilder()
        .setDate(date)
        .setOrigin(origin)
        .build();

    AnimalAllResponse animalsReceived = animalStub.getAll(animalAllRequest);
    List<AnimalEntity> animalEntities = GRPCObjectTranslatorService.toAnimalEntities(animalsReceived.getAnimalsList());
    List<EntityModel<AnimalEntity>> animals = animalEntities.stream()
        .map(assembler::toModel)
        .collect(Collectors.toList());

    return CollectionModel.of(animals,
        linkTo(methodOn(AnimalController.class).all(null,null)).withSelfRel().expand());
  }

  @GetMapping("/animals/{id}")
  public EntityModel<AnimalEntity> one(@PathVariable Long id) {
    AnimalOneRequest animalOneRequest = AnimalOneRequest.newBuilder()
        .setId(id)
        .build();
    Animal animal = animalStub.getOne(animalOneRequest);

    if (animal.getRegistrationNo() == 0L) throw new AnimalNotFoundException(id);

    return assembler.toModel(GRPCObjectTranslatorService.toAnimalEntity(animal));
  }

  @GetMapping("/animals/frompackage/{packageId}")
  public CollectionModel<EntityModel<AnimalEntity>> allFrompackage(@PathVariable Long packageId) {
    AllAnimalsFromPackageRequest allAnimalsFromPackageRequest = AllAnimalsFromPackageRequest.newBuilder()
        .setPackageId(packageId)
        .build();

    AllAnimalsFromPackageResponse animalsReceived = animalStub.getAllFromPackage(allAnimalsFromPackageRequest);
    List<AnimalEntity> animalEntities = GRPCObjectTranslatorService.toAnimalEntities(animalsReceived.getAnimalsList());
    List<EntityModel<AnimalEntity>> animals = animalEntities.stream()
        .map(assembler::toModel)
        .collect(Collectors.toList());

    return CollectionModel.of(animals,
        linkTo(methodOn(AnimalController.class).all(null,null)).withSelfRel().expand());
  }
}
