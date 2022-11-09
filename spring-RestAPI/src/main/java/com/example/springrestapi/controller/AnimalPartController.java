package com.example.springrestapi.controller;

import com.example.grpc.*;
import com.example.model.AnimalPartEntity;
import com.example.springrestapi.exceptions.AnimalPartNotFoundException;
import com.example.springrestapi.modelAssembler.AnimalPartModelAssembler;
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
public class AnimalPartController {
  @GrpcClient("GrpcConnection")
  private AnimalPartServiceGrpc.AnimalPartServiceBlockingStub animalPartStub;
  private final AnimalPartModelAssembler assembler;

  public AnimalPartController(AnimalPartModelAssembler assembler) {
    this.assembler = assembler;
  }

  @PostMapping("/animalparts") ResponseEntity<?> newAnimalPart(@RequestBody AnimalPartEntity newAnimalPart){
    AnimalPart animalPartToSend = GRPCObjectTranslatorService.toGRPCAnimalPart(newAnimalPart);
	AnimalPart animalPartReceived = animalPartStub.create(animalPartToSend);
    AnimalPartEntity animalPartEntity = GRPCObjectTranslatorService.toAnimalPartEntity(animalPartReceived);
    EntityModel<AnimalPartEntity> entityModel = assembler.toModel(animalPartEntity);

    return  ResponseEntity
        .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(entityModel);
  }

  @GetMapping("/animalparts")
  public CollectionModel<EntityModel<AnimalPartEntity>> all() {
    AnimalPartAllRequest animalPartAllRequest = AnimalPartAllRequest.newBuilder().build();

    AnimalPartAllResponse animalPartsReceived = animalPartStub.getAll(animalPartAllRequest);
    List<AnimalPartEntity> animalPartEntities = GRPCObjectTranslatorService.toAnimalPartEntities(animalPartsReceived.getAnimalPartsList());
    List<EntityModel<AnimalPartEntity>> animalParts = animalPartEntities.stream()
        .map(assembler::toModel)
        .collect(Collectors.toList());

    return CollectionModel.of(animalParts,
        linkTo(methodOn(AnimalPartController.class).all()).withSelfRel());
  }

  @GetMapping("/animalparts/{id}")
  public EntityModel<AnimalPartEntity> one(@PathVariable Long id) {
    AnimalPartOneRequest animalPartOneRequest = AnimalPartOneRequest.newBuilder()
        .setId(id)
        .build();
    AnimalPart animalPart = animalPartStub.getOne(animalPartOneRequest);

    if (animalPart.getId() == 0L) throw new AnimalPartNotFoundException(id);

    return assembler.toModel(GRPCObjectTranslatorService.toAnimalPartEntity(animalPart));
  }


}
