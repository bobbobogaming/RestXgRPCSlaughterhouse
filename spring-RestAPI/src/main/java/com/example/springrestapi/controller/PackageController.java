package com.example.springrestapi.controller;

import com.example.grpc.GRPCObjectTranslatorService;
import com.example.grpc.Package;
import com.example.grpc.*;
import com.example.springrestapi.exceptions.PackageNotFoundException;
import com.example.springrestapi.modelAssembler.PackageModelAssembler;
import com.example.model.PackageEntity;
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
public class PackageController {
  @GrpcClient("GrpcConnection")
  private PackageServiceGrpc.PackageServiceBlockingStub packageStub;

  private final PackageModelAssembler assembler;

  public PackageController(PackageModelAssembler assembler) {
    this.assembler = assembler;
  }

  @PostMapping("/packages") ResponseEntity<?> newPackage(@RequestBody PackageEntity aPackage){
    Package packageToSend = GRPCObjectTranslatorService.toGRPCPackage(aPackage);
	Package packageReceived = packageStub.create(packageToSend);
	PackageEntity packageEntity = GRPCObjectTranslatorService.toPackageEntity(packageReceived);
	EntityModel<PackageEntity> entityModel = assembler.toModel(packageEntity);

    return  ResponseEntity
        .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(entityModel);
  }

  @GetMapping("/packages")
  public CollectionModel<EntityModel<PackageEntity>> all() {
    PackageAllRequest packageAllRequest = PackageAllRequest.newBuilder().build();

    PackageAllResponse packagesReceived = packageStub.getAll(packageAllRequest);
    List<PackageEntity> packageEntities = GRPCObjectTranslatorService.toPackageEntities(packagesReceived.getPackageList());
    List<EntityModel<PackageEntity>> packages = packageEntities.stream()
        .map(assembler::toModel)
        .collect(Collectors.toList());

    return CollectionModel.of(packages,
        linkTo(methodOn(TrayController.class).all()).withSelfRel());
  }

  @GetMapping("/packages/{id}")
  public EntityModel<PackageEntity> one(@PathVariable Long id) {
    PackageOneRequest packageOneRequest = PackageOneRequest.newBuilder()
        .setId(id)
        .build();
    Package aPackage = packageStub.getOne(packageOneRequest);

    if (aPackage.getPackageId() == 0L) throw new PackageNotFoundException(id);

    return assembler.toModel(GRPCObjectTranslatorService.toPackageEntity(aPackage));
  }

  @GetMapping("/packages/withanimal/{animalId}")
  public CollectionModel<EntityModel<PackageEntity>> allWithAnimal(@PathVariable Long animalId) {
    AllPackagesWithAnimalRequest allPackagesWithAnimalRequest = AllPackagesWithAnimalRequest.newBuilder()
        .setAnimalId(animalId)
        .build();

    AllPackagesWithAnimalResponse packagesReceived = packageStub.getAllWithAnimal(allPackagesWithAnimalRequest);
    List<PackageEntity> packageEntities = GRPCObjectTranslatorService.toPackageEntities(packagesReceived.getPackagesList());
    List<EntityModel<PackageEntity>> packages = packageEntities.stream()
        .map(assembler::toModel)
        .collect(Collectors.toList());

    return CollectionModel.of(packages,
        linkTo(methodOn(TrayController.class).all()).withSelfRel());
  }
}
